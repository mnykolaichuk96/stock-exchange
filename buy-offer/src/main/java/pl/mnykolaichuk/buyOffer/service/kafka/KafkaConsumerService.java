package pl.mnykolaichuk.buyOffer.service.kafka;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.mnykolaichuk.buyOffer.converter.KafkaMsgConverter;
import pl.mnykolaichuk.buyOffer.dto.BuyOfferDto;
import pl.mnykolaichuk.buyOffer.service.impl.BuyOfferService;

@Service
@AllArgsConstructor
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final BuyOfferService buyOfferService;

    @KafkaListener(topics = "gateway-to-buy-offer", groupId = "gateway-to-buy-offer")
    public void consume(BuyOfferDto buyOfferDto) {
        logger.info("BuyOfferDto inside buy-offer-ms after consuming from 'gateway-to-sell-offer':\n###\t" +
                buyOfferDto);

        buyOfferService.addBuyOffer(buyOfferDto);
    }
}
