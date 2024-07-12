package pl.mnykolaichuk.sellOffer.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mnykolaichuk.sellOffer.dto.SellOfferDto;
import pl.mnykolaichuk.sellOffer.entity.OutboxEvent;
import pl.mnykolaichuk.sellOffer.entity.SellOffer;
import pl.mnykolaichuk.sellOffer.mapper.SellOfferMapper;
import pl.mnykolaichuk.sellOffer.repository.OutboxEventRepository;
import pl.mnykolaichuk.sellOffer.repository.SellOfferRepository;
import pl.mnykolaichuk.sellOffer.service.ISellOfferService;

@Service
@AllArgsConstructor
public class SellOfferService implements ISellOfferService {

    private SellOfferRepository sellOfferRepository;
    private OutboxEventRepository outboxEventRepository;

    @Override
    @Transactional
    public void addSellOffer(SellOfferDto sellOfferDto) {
        String sellOfferTopic = "sell-offers";

        SellOffer sellOffer = SellOfferMapper.mapToSellOffer(sellOfferDto, new SellOffer());
        sellOfferRepository.save(sellOffer);

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .payload(sellOffer.toString())
                .topic(sellOfferTopic)
                .build();
        outboxEventRepository.save(outboxEvent);
    }
}
