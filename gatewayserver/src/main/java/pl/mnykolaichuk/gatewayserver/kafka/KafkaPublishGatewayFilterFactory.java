package pl.mnykolaichuk.gatewayserver.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaPublishGatewayFilterFactory extends AbstractGatewayFilterFactory<KafkaPublishGatewayFilterFactory.Config> {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaPublishGatewayFilterFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        super(Config.class);
        this.kafkaTemplate = new KafkaTemplate<>(producerFactory(bootstrapServers));
    }

    private ProducerFactory<String, String> producerFactory(String bootstrapServers) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> exchange.getRequest().getBody().collectList().flatMap(dataBufferList -> {
            String payload = dataBufferList.stream()
                    .map(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        return new String(bytes);
                    })
                    .reduce((a, b) -> a + b)
                    .orElse("");
            kafkaTemplate.send(config.getTopic(), payload);
            return chain.filter(exchange);
        });
    }

    public static class Config {
        private String topic;

        public Config(String topic) {
            this.topic = topic;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }
}
