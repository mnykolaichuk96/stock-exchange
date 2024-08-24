package pl.mnykolaichuk.sellOffer.service.kafka;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.mnykolaichuk.sellOffer.converter.KafkaMsgConverter;
import pl.mnykolaichuk.sellOffer.dto.SellOfferDto;
import pl.mnykolaichuk.sellOffer.service.impl.SellOfferService;

@Service
@AllArgsConstructor
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final SellOfferService sellOfferService;

    @KafkaListener(topics = "gateway-to-sell-offer", groupId = "gateway-to-sell-offer")
    public void consume(SellOfferDto sellOfferDto) {
        logger.info("SellOfferDto inside sell-offer-ms after consuming from 'gateway-to-sell-offer':\n###\t" +
                sellOfferDto);

        sellOfferService.addSellOffer(sellOfferDto);
    }
}
