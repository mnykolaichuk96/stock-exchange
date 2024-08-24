package pl.mnykolaichuk.sellOffer.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class SellOfferDto {
    private Long sellOfferId;
    private Long stockId;
    private Long userId;
    private Integer startAmount;
    private Integer amount;
    private Double minPrice;
    private Long timestamp;
    private Boolean actual;

}
