package pl.mnykolaichuk.sellOffer.service.poller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.mnykolaichuk.sellOffer.entity.OutboxEvent;
import pl.mnykolaichuk.sellOffer.repository.OutboxEventRepository;

import java.util.List;

@Component
@AllArgsConstructor
public class OutboxPoller {
    private static final Logger logger = LoggerFactory.getLogger(OutboxPoller.class);

    private OutboxEventRepository outboxEventRepository;
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Polls the outbox table every 5 seconds, sends events to Kafka, and removes them from the database.
     * This method ensures that the operation of sending events to Kafka and deleting them from the database
     * occurs within a single transactional context, thus maintaining data consistency.
     */
    @Scheduled(fixedRate = 5000) // Poll every 5 sec
    @Transactional("kafkaTransactionManager")
    public void pollOutbox() {
        List<OutboxEvent> events = outboxEventRepository.findAll();

        if (events.isEmpty()) {
            logger.info("No events found in Outbox");
        } else {
            for (OutboxEvent event: events) {
                try {
                    kafkaTemplate.executeInTransaction(operations -> {
                        operations.send(event.getTopic(), event.getPayload());
                        outboxEventRepository.delete(event); // Usunięcie zdarzenia po wysłaniu
                        return true;
                    });
                    logger.info("Sent and deleted event from Outbox: " + event.getPayload());
                } catch (Exception e) {
                    logger.error("Failed to send event to Kafka: " + event.getPayload(), e);
                }
            }
        }
    }

}
