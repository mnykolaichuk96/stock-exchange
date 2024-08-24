package pl.mnykolaichuk.buyOffer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mnykolaichuk.buyOffer.dto.BuyOfferDto;
import pl.mnykolaichuk.buyOffer.entity.BuyOffer;
import pl.mnykolaichuk.buyOffer.entity.OutboxEvent;
import pl.mnykolaichuk.buyOffer.mapper.BuyOfferMapper;
import pl.mnykolaichuk.buyOffer.repository.BuyOfferRepository;
import pl.mnykolaichuk.buyOffer.repository.OutboxEventRepository;
import pl.mnykolaichuk.buyOffer.service.IBuyOfferService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class BuyOfferService implements IBuyOfferService {

    private BuyOfferRepository buyOfferRepository;
    private OutboxEventRepository outboxEventRepository;

    @Override
    @Transactional
    public void addBuyOffer(BuyOfferDto buyOfferDto) {
        String buyOfferTopic = "buy-offers";
        ObjectMapper objectMapper = new ObjectMapper();

        BuyOffer buyOffer = BuyOfferMapper.mapToBuyOffer(buyOfferDto, new BuyOffer());
        buyOfferRepository.save(buyOffer);

        try {
            String payload = objectMapper.writeValueAsString(buyOffer);
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .payload(payload)
                    .topic(buyOfferTopic)
                    .build();
            outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LocalDateTime getTimestampAsLocalDateTime(Long buyOfferId) {
        return Instant.ofEpochMilli(getTimestamp(buyOfferId)).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Long getTimestamp(Long buyOfferId) {
        BuyOffer buyOffer = buyOfferRepository.findById(buyOfferId)
                .orElseThrow(() -> new RuntimeException("SellOffer not found with ID: " + buyOfferId));

        return buyOffer.getTimestamp();
    }
}
