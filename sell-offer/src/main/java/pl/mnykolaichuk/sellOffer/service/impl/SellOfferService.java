package pl.mnykolaichuk.sellOffer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mnykolaichuk.sellOffer.dto.SellOfferDto;
import pl.mnykolaichuk.sellOffer.entity.OutboxEvent;import pl.mnykolaichuk.sellOffer.entity.SellOffer;
import pl.mnykolaichuk.sellOffer.mapper.SellOfferMapper;
import pl.mnykolaichuk.sellOffer.repository.OutboxEventRepository;
import pl.mnykolaichuk.sellOffer.repository.SellOfferRepository;
import pl.mnykolaichuk.sellOffer.service.ISellOfferService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class SellOfferService implements ISellOfferService {

    private SellOfferRepository sellOfferRepository;
    private OutboxEventRepository outboxEventRepository;

    @Override
    @Transactional
    public void addSellOffer(SellOfferDto sellOfferDto) {
        String sellOfferTopic = "sell-offers";
        ObjectMapper objectMapper = new ObjectMapper();

        SellOffer sellOffer = SellOfferMapper.mapToSellOffer(sellOfferDto, new SellOffer());
        sellOfferRepository.save(sellOffer);

        try {
            String payload = objectMapper.writeValueAsString(sellOffer);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .payload(payload)
                    .topic(sellOfferTopic)
                    .build();
            outboxEventRepository.save(outboxEvent);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LocalDateTime getTimestampAsLocalDateTime(Long sellOfferId) {
        return Instant.ofEpochMilli(getTimestamp(sellOfferId)).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Long getTimestamp(Long sellOfferId) {
        SellOffer sellOffer = sellOfferRepository.findById(sellOfferId)
                .orElseThrow(() -> new RuntimeException("SellOffer not found with ID: " + sellOfferId));

        return sellOffer.getTimestamp();
    }
}
