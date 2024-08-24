package pl.mnykolaichuk.sellOffer.mapper;

import pl.mnykolaichuk.sellOffer.dto.SellOfferDto;
import pl.mnykolaichuk.sellOffer.entity.SellOffer;

public class SellOfferMapper {

    /**
     *
     * @param sellOfferDto from sellOfferDto
     * @param sellOffer to sellOffer Entity
     * @return sellOffer Entity
     */
    public static SellOffer mapToSellOffer(SellOfferDto sellOfferDto, SellOffer sellOffer) {

        sellOffer.setStockId(sellOfferDto.getStockId());
        sellOffer.setUserId(sellOfferDto.getUserId());
        sellOffer.setStartAmount(sellOfferDto.getStartAmount());
        sellOffer.setAmount(sellOfferDto.getAmount());
        sellOffer.setMinPrice(sellOfferDto.getMinPrice());
        sellOffer.setActual(sellOfferDto.getActual());
        sellOffer.setTimestamp(sellOfferDto.getTimestamp());

        return sellOffer;
    }

    /**
     *
     * @param sellOffer from sellOffer Entity
     * @param sellOfferDto to sellOfferDto
     * @return sellOfferDto
     */
    public static SellOfferDto mapToSellOfferDto(SellOffer sellOffer, SellOfferDto sellOfferDto) {
        sellOfferDto.setSellOfferId(sellOffer.getSellOfferId());
        sellOfferDto.setStockId(sellOffer.getStockId());
        sellOfferDto.setUserId(sellOffer.getUserId());
        sellOfferDto.setStartAmount(sellOffer.getStartAmount());
        sellOfferDto.setAmount(sellOffer.getAmount());
        sellOfferDto.setMinPrice(sellOffer.getMinPrice());
        sellOfferDto.setActual(sellOffer.getActual());
        sellOfferDto.setTimestamp(sellOffer.getTimestamp());

        return sellOfferDto;
    }
}
