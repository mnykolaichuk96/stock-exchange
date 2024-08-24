package pl.mnykolaichuk.gatewayserver.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter @Setter @ToString @AllArgsConstructor @Builder
public class SellOfferDto {
    private Long stockId;
    private Long userId;
    private Integer startAmount;
    private Integer amount;
    private Double minPrice;
    private Long timestamp;
    private Boolean actual;

    public SellOfferDto() {
        this.timestamp = System.currentTimeMillis();  // Inicjowanie timestamp w konstruktorze
        this.actual = true;
    }

    public SellOfferDto(Long stockId, Long userId, Integer startAmount, Integer amount, Double minPrice) {
        this.stockId = stockId;
        this.userId = userId;
        this.startAmount = startAmount;
        this.amount = amount;
        this.minPrice = minPrice;
        this.timestamp = System.currentTimeMillis();  // Inicjowanie timestamp w konstruktorze
        this.actual = true;
    }

}
