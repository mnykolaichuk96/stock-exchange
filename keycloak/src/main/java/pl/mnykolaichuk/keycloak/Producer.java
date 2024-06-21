package pl.mnykolaichuk.keycloak;

import io.strimzi.kafka.oauth.client.ClientConfig;
import io.strimzi.kafka.oauth.common.ConfigProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;

public class Producer {

    private static final Logger log = Logger.getLogger(CustomEventListenerProvider.class);

    public static void publishEvent(String topic, String value) {
        // Tworzymy obiekt properties poniewaz nie jest to Spring Boot App i musimy recznie
        Properties properties = new Properties();

        try {
            properties.load(Producer.class.getClassLoader().getResourceAsStream("application.yml"));
        } catch (IOException e) {
            log.error("Error loading properties from application.yml", e);
        }

        final String OAUTH_CLIENT_ID = System.getenv("OAUTH_CLIENT_ID");
        final String OAUTH_CLIENT_SECRET = System.getenv("OAUTH_CLIENT_SECRET");
        final String BOOTSTRAP_SERVER = System.getenv("bootstrap");
        final String SASL_MECHANISM = "OAUTHBEARER";
        final String OAUTH_TOKEN_ENDPOINT_URI = System.getenv("OAUTH_TOKEN_ENDPOINT_URI");
        final String SECURITY_PROTOCOL = "SSL";
        final String SECURITY_PROTOCOL_VERSION = "TLSv1.3";
        final String SSL_TRUSTSTORE_LOCATION = System.getenv("SSL_TRUSTSTORE_LOCATION");
        final String SSL_TRUSTSTORE_PASSWORD = System.getenv("SSL_TRUSTSTORE_PASSWORD");
        final String SSL_KEYSTORE_LOCATION = System.getenv("SSL_KEYSTORE_LOCATION");
        final String SSL_KEYSTORE_PASSWORD = System.getenv("SSL_KEYSTORE_PASSWORD");
        final String SSL_KEY_PASSWORD = System.getenv("SSL_KEY_PASSWORD");

        // Logowanie wartości zmiennych środowiskowych
        log.debug("OAUTH_CLIENT_ID: " + OAUTH_CLIENT_ID);
        log.debug("OAUTH_CLIENT_SECRET: " + OAUTH_CLIENT_SECRET);
        log.debug("BOOTSTRAP_SERVER: " + BOOTSTRAP_SERVER);
        log.debug("OAUTH_TOKEN_ENDPOINT_URI: " + OAUTH_TOKEN_ENDPOINT_URI);
        log.debug("SSL_TRUSTSTORE_LOCATION: " + SSL_TRUSTSTORE_LOCATION);
        log.debug("SSL_TRUSTSTORE_PASSWORD: " + SSL_TRUSTSTORE_PASSWORD);
        log.debug("SSL_KEYSTORE_LOCATION: " + SSL_KEYSTORE_LOCATION);
        log.debug("SSL_KEYSTORE_PASSWORD: " + SSL_KEYSTORE_PASSWORD);
        log.debug("SSL_KEY_PASSWORD: " + SSL_KEY_PASSWORD);

        // Korzystamy z zmiennych srodowiskowych, to nie jest Spring Boot App i environment nie zwiazany z properties
        properties.setProperty("OAUTH_CLIENT_ID", OAUTH_CLIENT_ID);
        properties.setProperty("OAUTH_CLIENT_SECRET", OAUTH_CLIENT_SECRET);
        properties.setProperty("bootstrap", BOOTSTRAP_SERVER);
        properties.setProperty("sasl.mechanism", SASL_MECHANISM);
        properties.setProperty("OAUTH_TOKEN_ENDPOINT_URI", OAUTH_TOKEN_ENDPOINT_URI);

        // Laczenie sie z Kafka
        properties.setProperty("security.protocol", SECURITY_PROTOCOL);
        properties.setProperty("ssl.truststore.location", SSL_TRUSTSTORE_LOCATION);
        properties.setProperty("ssl.truststore.password", SSL_TRUSTSTORE_PASSWORD);
        properties.setProperty("ssl.keystore.location", SSL_KEYSTORE_LOCATION);
        properties.setProperty("ssl.keystore.password", SSL_KEYSTORE_PASSWORD);
        properties.setProperty("ssl.key.password", SSL_KEY_PASSWORD);
        properties.setProperty("ssl.endpoint.identification.algorithm", "");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // Налаштування підтверджень від всіх реплік
        properties.setProperty("sasl.mechanism", SASL_MECHANISM); // Використання механізму SASL/OAUTHBEARER
        properties.setProperty("ssl.protocol", SECURITY_PROTOCOL_VERSION); // Використання протоколу SSL версії TLSv1.3

        // Konfiguracja JAAS dla uwierzytelniania OAuth
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required;");

        // Konfiguracja klasy obsługi wywołań zwrotnych logowania klienta dla OAuth
        properties.setProperty("sasl.login.callback.handler.class", "io.strimzi.kafka.oauth.client.JaasClientOauthLoginCallbackHandler");

        // Налаштування серіалізації ключів та значень
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ClientConfig.OAUTH_TOKEN_ENDPOINT_URI, OAUTH_TOKEN_ENDPOINT_URI); // Налаштування URL для отримання токену OAuth
        properties.setProperty(ClientConfig.OAUTH_CLIENT_ID, OAUTH_CLIENT_ID); // Налаштування ідентифікатора клієнта OAuth
        properties.setProperty(ClientConfig.OAUTH_CLIENT_SECRET, OAUTH_CLIENT_SECRET); // Налаштування секрету клієнта OAuth
        properties.setProperty(ClientConfig.OAUTH_USERNAME_CLAIM, "preferred_username"); // Налаштування вимоги імені користувача

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER); // Вказівка адреси сервера Kafka
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Налаштування серіалізації ключів
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Налаштування серіалізації значень

        // Дозволяє вирішувати і експортувати властивості системи, якщо це необхідно
        ConfigProperties.resolveAndExportToSystemProperties(properties);

        // Створення продюсера Kafka з налаштованими властивостями
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Створення запису для відправки до Kafka
        ProducerRecord<String, String> eventRecord = new ProducerRecord<>(topic, value);

        // Відправка запису - асинхронно
        Future<RecordMetadata> res = producer.send(eventRecord);
        log.info("producer called");

        // Очищення буферів
        producer.flush();

        // Очищення буферів і закриття продюсера
        producer.close();
    }
}

