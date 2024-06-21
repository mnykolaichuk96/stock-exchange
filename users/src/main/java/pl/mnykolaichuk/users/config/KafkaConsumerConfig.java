package pl.mnykolaichuk.users.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${spring.kafka.security.protocol}")
    private String securityProtocol;

    @Value("${spring.kafka.ssl.trust-store-password}")
    private String trustStorePassword;

    @Value("${spring.kafka.ssl.trust-store-location}")
    private String trustStoreLocation;

    @Value("${consumer.ssl.keystore-location}")
    private String keyStoreLocation;

    @Value("${consumer.ssl.keystore-password}")
    private String keyStorePassword;

    @Autowired
    private ConsumerFactory<Integer, String> consumerFactory;

    private Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>
                (consumerFactory.getConfigurationProperties());

        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keyStoreLocation);
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keyStorePassword);
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustStoreLocation);
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword);
        props.put(SslConfigs.DEFAULT_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM, "https");
        props.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, securityProtocol);

        return props;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));

        return factory;
    }
}
