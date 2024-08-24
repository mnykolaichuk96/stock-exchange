package pl.mnykolaichuk.gatewayserver.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ServerWebExchange;
import pl.mnykolaichuk.gatewayserver.config.KeycloakRoleConverter;
import pl.mnykolaichuk.gatewayserver.dto.BuyOfferDto;
import pl.mnykolaichuk.gatewayserver.dto.SellOfferDto;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaPublishGatewayFilterFactory extends AbstractGatewayFilterFactory<KafkaPublishGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaPublishGatewayFilterFactory.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * KafkaTemplate - високорівневий клас який забезпечеє API для роботи producer в Kafka
     * */
    public KafkaPublishGatewayFilterFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        super(Config.class);
        this.kafkaTemplate = new KafkaTemplate<>(producerFactory(bootstrapServers));
    }

    private ProducerFactory<String, Object> producerFactory(String bootstrapServers) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Optional: Disable adding type info headers
//        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * apply - перехоплює запити які йдуть через щлюз (gateway servers) і перед тим як
     * request піде далі публікуємо його в Kafka
     * getBody() повертає об'єкт типу Flux<DataBuffer>,
     * Flux - це реактивний тип, який представляє асинхронний потік даних
     * DataBuffer представляє частину тіла запиту у вигляді масиву байтів
     *
     * Flux<DataBuffer> є реактивним потоком даних, обробка тіла здійснюється через операції такі як:
     *  collectList(), flatMap(), map()
     * */
    @Override
    public GatewayFilter apply(Config config) {
        // collectList() збирає все в один список,
        /*
        *   .map() перетворює DataBuffer на масив байтів.
        *   .reduce() приймає два масиви байтів і об'єднує їх в один більший масив.
        * Ця логіка використовується для того,
        * щоб зібрати всі байти з різних буферів в один єдиний масив, який представляє повне тіло запиту.
        * */
        return (exchange, chain) -> exchange.getRequest().getBody().collectList().flatMap(dataBufferList -> {
            byte[] totalBytes = dataBufferList.stream()
                    .map(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        return bytes;
                    })
                    .reduce((a, b) -> {     // a - поточне агреговане значення, b - новий елемент
                        byte[] combined = new byte[a.length + b.length];
                        System.arraycopy(a, 0, combined, 0, a.length);
                        System.arraycopy(b, 0, combined, a.length, b.length);
                        return combined;
                    })
                    .orElse(new byte[0]);

            Object offer;

            // Визначаємо тип об'єкта на основі URL
            if (isBuyOfferRequest(exchange)) {
                try {
                    offer = objectMapper.readValue(totalBytes, BuyOfferDto.class);
                    logger.info("BuyOfferDto object inside gateway before publish in topic:\n" + offer);
                } catch (IOException e) {
                    return Mono.error(new RuntimeException(e));
                }
            } else if (isSellOfferRequest(exchange)) {
                try {
                    offer = objectMapper.readValue(totalBytes, SellOfferDto.class);
                    logger.info("SellOfferDto object inside gateway before publish in topic:\n" + offer);
                } catch (IOException e) {
                    return Mono.error(new RuntimeException(e));
                }
            } else {
                return Mono.error(new IllegalArgumentException("Unknown request type"));
            }

            // Відправляємо об'єкт в Kafka
            kafkaTemplate.send(config.getTopic(), offer);

            return chain.filter(exchange);
        });
    }

    private boolean isBuyOfferRequest(ServerWebExchange exchange) {
        return exchange.getRequest().getURI().getPath().contains("/buy-offer");
    }

    private boolean isSellOfferRequest(ServerWebExchange exchange) {
        return exchange.getRequest().getURI().getPath().contains("/sell-offer");
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
