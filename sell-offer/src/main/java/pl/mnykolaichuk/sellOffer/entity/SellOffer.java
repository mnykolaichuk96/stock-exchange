package pl.mnykolaichuk.sellOffer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "sell_offer")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class SellOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellOfferId;

    @Column(name = "stockId")
    private Long stockId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "start_amount")
    private Integer startAmount;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "min_price")
    private Double minPrice;

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "actual")
    private Boolean actual;

//    public SellOffer() {
//        this.timestamp = System.currentTimeMillis();  // Inicjowanie timestamp w konstruktorze
//        this.actual = true;
//    }

}
