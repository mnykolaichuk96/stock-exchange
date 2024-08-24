package pl.mnykolaichuk.buyOffer.mapper;

import pl.mnykolaichuk.buyOffer.dto.BuyOfferDto;
import pl.mnykolaichuk.buyOffer.entity.BuyOffer;

public class BuyOfferMapper {

    /**
     *
     * @param buyOfferDto from buyOfferDto
     * @param buyOffer to buyOffer Entity
     * @return buyOffer Entity
     */
    public static BuyOffer mapToBuyOffer(BuyOfferDto buyOfferDto, BuyOffer buyOffer) {

        buyOffer.setStockId(buyOfferDto.getStockId());
        buyOffer.setUserId(buyOfferDto.getUserId());
        buyOffer.setStartAmount(buyOfferDto.getStartAmount());
        buyOffer.setAmount(buyOfferDto.getAmount());
        buyOffer.setMaxPrice(buyOfferDto.getMaxPrice());
        buyOffer.setActual(buyOfferDto.getActual());
        buyOffer.setTimestamp(buyOfferDto.getTimestamp());

        return buyOffer;
    }

    /**
     *
     * @param buyOffer from buyOffer Entity
     * @param buyOffer from buyOffer Entity
     * @param buyOfferDto to buyOfferDto
     * @return buyOfferDto
     */
    public static BuyOfferDto mapToSellOfferDto(BuyOffer buyOffer, BuyOfferDto buyOfferDto) {
        buyOfferDto.setBuyOfferId(buyOfferDto.getBuyOfferId());
        buyOfferDto.setStockId(buyOffer.getStockId());
        buyOfferDto.setUserId(buyOffer.getUserId());
        buyOfferDto.setStartAmount(buyOffer.getStartAmount());
        buyOfferDto.setAmount(buyOffer.getAmount());
        buyOfferDto.setMaxPrice(buyOffer.getMaxPrice());
        buyOfferDto.setActual(buyOffer.getActual());
        buyOfferDto.setTimestamp(buyOffer.getTimestamp());

        return buyOfferDto;
    }
}
