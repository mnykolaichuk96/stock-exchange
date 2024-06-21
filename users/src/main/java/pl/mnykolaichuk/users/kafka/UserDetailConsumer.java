package pl.mnykolaichuk.users.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserDetailConsumer {

    private static final Logger log = LoggerFactory.getLogger(UserDetailConsumer.class);


    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received message:");
        log.info("Key: " + record.key());
        log.info("Value: " + record.value());
        log.info("Offset: " + record.offset());
        log.info("Partition: " + record.partition());
        log.info("Timestamp: " + record.timestamp());
    }
}

