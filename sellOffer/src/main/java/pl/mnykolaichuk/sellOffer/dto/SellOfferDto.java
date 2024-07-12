package pl.mnykolaichuk.sellOffer.dto;

import lombok.*;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class SellOfferDto {
    private Long stockId;
    private Long userId;
    private Integer startAmount;
    private Integer amount;
    private Double minPrice;
}
