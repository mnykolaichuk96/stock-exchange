package pl.mnykolaichuk.buyOffer.service;

import pl.mnykolaichuk.buyOffer.dto.BuyOfferDto;

import java.time.LocalDateTime;

public interface IBuyOfferService {

    void addBuyOffer(BuyOfferDto buyOfferDto);

    public LocalDateTime getTimestampAsLocalDateTime(Long sellOfferId);
}
