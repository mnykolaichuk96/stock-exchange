package pl.mnykolaichuk.sellOffer.config;

import jakarta.persistence.EntityManagerFactory;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import pl.mnykolaichuk.sellOffer.dto.SellOfferDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

//    @Value("${spring.kafka.consumer.group-id}")
//    private String groupId;

    @Value("${sell-offer.kafka.topic}")
    private String sellOfferTopic;

    /**
     * Creates a new Kafka topic for sell offers.
     * <p>
     * This bean definition creates a new topic in Kafka with the specified name, number of partitions,
     * and replication factor. The topic is intended to be used for processing buy offer messages.
     * </p>
     * sellOfferTopic: The name of the topic.
     * 20: The number of partitions for the topic, which determines the level of parallelism.
     * (short) 1: The replication factor, which indicates how many replicas of the topic's data should be maintained across the Kafka cluster.
     *
     * @return NewTopic - A new instance of {@link NewTopic} configured with the specified parameters.
     */
    @Bean
    public NewTopic sellOffersTopic() {
        return new NewTopic(sellOfferTopic, 20, (short) 3);
    }

    @Bean
    public ProducerFactory<Long, SellOfferDto> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "sell-offer-transactional-id");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Long, SellOfferDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTransactionManager<Long, SellOfferDto> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }
}

