package pl.mnykolaichuk.sellOffer.entity;

import jakarta.persistence.*;
import lombok.*;

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

}
