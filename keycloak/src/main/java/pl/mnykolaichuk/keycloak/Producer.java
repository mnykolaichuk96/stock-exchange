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

    @Value("${OAUTH_CLIENT_SECRET}")
    private static String oauthClientSecret;

    public static void publishEvent(String topic, String value) {
        Properties properties = new Properties();
        properties.setProperty("OAUTH_CLIENT_ID", "stock-exchange-auth-code");
        properties.setProperty("bootstrap", "http://kafka:9093");
        properties.setProperty("sasl.mechanism", "OAUTHBEARER");
        properties.setProperty("OAUTH_TOKEN_ENDPOINT_URI", "http://keycloak:8080/realms/master/protocol/openid-connect/token");

        properties.setProperty("OAUTH_CLIENT_SECRET", oauthClientSecret);

//        try {
//            // Завантаження властивостей з файлу application.yml
//            properties.load(Producer.class.getClassLoader().getResourceAsStream("application.yml"));
//        } catch (IOException e) {
//            // Обробка виключення у випадку, якщо файл не знайдено або виникла інша помилка введення/виведення
//            e.printStackTrace();
//        }

        // Отримання значення властивості "bootstrap" з файлу властивостей
        String BOOTSTRAP_SERVER = properties.getProperty("bootstrap");
        log.info("----producer----");
        log.info(properties.getProperty("OAUTH_CLIENT_ID"));
        log.info(properties.getProperty("OAUTH_CLIENT_SECRET"));

        // Налаштування властивостей для підключення до Kafka
        properties.setProperty("security.protocol", "SSL"); // Використання безпечного протоколу SASL_SSL
        properties.setProperty("ssl.truststore.location", "/etc/kafka/secrets/kafka.server.truststore");
        properties.setProperty("ssl.truststore.password", "temp_password");
        properties.setProperty("ssl.keystore.location", "/etc/kafka/secrets/kafka.client.keystore");
        properties.setProperty("ssl.keystore.password", "temp_password");
        properties.setProperty("ssl.key.password", "temp_password");
        properties.setProperty("ssl.endpoint.identification.algorithm", "");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // Налаштування підтверджень від всіх реплік
        properties.setProperty("sasl.mechanism", "OAUTHBEARER"); // Використання механізму SASL/OAUTHBEARER
        properties.setProperty("ssl.protocol", "TLSv1.3"); // Використання протоколу SSL версії TLSv1.3
        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required;"); // Налаштування JAAS для аутентифікації OAuth
        properties.setProperty("sasl.login.callback.handler.class", "io.strimzi.kafka.oauth.client.JaasClientOauthLoginCallbackHandler"); // Налаштування класу обробника зворотного виклику для логіну

        // Налаштування серіалізації ключів та значень
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ClientConfig.OAUTH_TOKEN_ENDPOINT_URI, properties.getProperty("OAUTH_TOKEN_ENDPOINT_URI")); // Налаштування URL для отримання токену OAuth
        properties.setProperty(ClientConfig.OAUTH_CLIENT_ID, properties.getProperty("OAUTH_CLIENT_ID")); // Налаштування ідентифікатора клієнта OAuth
        properties.setProperty(ClientConfig.OAUTH_CLIENT_SECRET, properties.getProperty("OAUTH_CLIENT_SECRET")); // Налаштування секрету клієнта OAuth
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

