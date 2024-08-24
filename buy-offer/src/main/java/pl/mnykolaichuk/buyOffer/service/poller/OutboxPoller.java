package pl.mnykolaichuk.buyOffer.service.poller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.mnykolaichuk.buyOffer.dto.BuyOfferDto;
import pl.mnykolaichuk.buyOffer.entity.OutboxEvent;
import pl.mnykolaichuk.buyOffer.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@AllArgsConstructor
public class OutboxPoller {
    private static final Logger logger = LoggerFactory.getLogger(OutboxPoller.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private OutboxEventRepository outboxEventRepository;
    private KafkaTemplate<Long, BuyOfferDto> kafkaTemplate;

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
                    BuyOfferDto buyOfferDto = objectMapper.readValue(event.getPayload(), BuyOfferDto.class);
                    logger.info("BuyOffer inside payload of outbox_event table:\n\t" + buyOfferDto);

                    kafkaTemplate.executeInTransaction(operations -> {
                        operations.send(event.getTopic(), buyOfferDto.getStockId(), buyOfferDto);
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
