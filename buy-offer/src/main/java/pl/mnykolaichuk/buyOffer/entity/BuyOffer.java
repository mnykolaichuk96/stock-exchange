package pl.mnykolaichuk.buyOffer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "buy_offer")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class BuyOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buyOfferId;

    @Column(name = "stockId")
    private Long stockId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "start_amount")
    private Integer startAmount;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "max_price")
    private Double maxPrice;

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "actual")
    private Boolean actual;

//    public BuyOffer() {
//        this.timestamp = System.currentTimeMillis();  // Inicjowanie timestamp w konstruktorze
//        this.actual = true;
//    }

}
