package pl.mnykolaichuk.trade.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "transaction")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(name = "buy_offer_id")
    private Long buyOfferId;

    @Column(name = "sell_offer_id")
    private Long sellOfferId;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "price")
    private Double price;

    @Column(name = "timestamp")
    private Long timestamp;

    public LocalDateTime getTimestampAsLocalDateTime() {
        return Instant.ofEpochMilli(this.timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
