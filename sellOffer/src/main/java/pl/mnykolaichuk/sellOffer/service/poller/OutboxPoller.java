package pl.mnykolaichuk.sellOffer.service.poller;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.mnykolaichuk.sellOffer.entity.OutboxEvent;
import pl.mnykolaichuk.sellOffer.repository.OutboxEventRepository;

import java.util.List;

@Component
@AllArgsConstructor
public class OutboxPoller {
    private OutboxEventRepository outboxEventRepository;

    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 5000) // Poll every 5 sec
    public void pollOutbox() {
        List<OutboxEvent> events = outboxEventRepository.findAll();

        for (OutboxEvent event: events) {
            kafkaTemplate.send(event.getTopic(), event.getPayload());
        }
    }

}
