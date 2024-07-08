package pl.mnykolaichuk.stock.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // Id generowane przez bd
    private Long stockId;

    @Column(name = "name")
    private String name;

    @Column(name = "price_id")
    private Long priceId;

    // ile zostało dostępnych akcji
    @Column(name = "avoid_amount")
    private Integer avoidAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

}
