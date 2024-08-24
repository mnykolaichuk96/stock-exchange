package pl.mnykolaichuk.gatewayserver.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter @Setter @ToString @AllArgsConstructor @Builder
public class BuyOfferDto {
    private Long stockId;
    private Long userId;
    private Integer startAmount;
    private Integer amount;
    private Double maxPrice;
    private Long timestamp;
    private Boolean actual;

    public BuyOfferDto() {
        this.timestamp = System.currentTimeMillis();  // Inicjowanie timestamp w konstruktorze
        this.actual = true;
    }

    public BuyOfferDto(Long stockId, Long userId, Integer startAmount, Integer amount, Double maxPrice) {
        this.stockId = stockId;
        this.userId = userId;
        this.startAmount = startAmount;
        this.amount = amount;
        this.maxPrice = maxPrice;
        this.timestamp = System.currentTimeMillis();  // Inicjowanie timestamp w konstruktorze
        this.actual = true;
    }

}
