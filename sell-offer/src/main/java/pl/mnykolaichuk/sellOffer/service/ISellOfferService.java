package pl.mnykolaichuk.sellOffer.service;

import pl.mnykolaichuk.sellOffer.dto.SellOfferDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public interface ISellOfferService {

    void addSellOffer(SellOfferDto sellOfferDto);

    public LocalDateTime getTimestampAsLocalDateTime(Long sellOfferId);
}
