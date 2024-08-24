package pl.mnykolaichuk.buyOffer.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class BuyOfferDto {
    private Long buyOfferId;
    private Long stockId;
    private Long userId;
    private Integer startAmount;
    private Integer amount;
    private Double maxPrice;
    private Long timestamp;
    private Boolean actual;

}
