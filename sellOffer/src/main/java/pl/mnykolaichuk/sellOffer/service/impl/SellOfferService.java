package pl.mnykolaichuk.sellOffer.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    @Value("${sell-offer.kafka.topic}")
    private String sellOfferTopic;

    private SellOfferRepository sellOfferRepository;
    private OutboxEventRepository outboxEventRepository;

    @Override
    @Transactional
    public void addSellOffer(SellOfferDto sellOfferDto) {

        SellOffer sellOffer = SellOfferMapper.mapToSellOffer(sellOfferDto, new SellOffer());
        sellOfferRepository.save(sellOffer);

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .payload(sellOffer.toString())
                .topic(sellOfferTopic)
                .build();
        outboxEventRepository.save(outboxEvent);
    }
}
