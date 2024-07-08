package pl.mnykolaichuk.users.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_stock")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class UserStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // Id generowane przez bd
    private Long userStockId;

    @Column(name = "stock_amount")
    private Long stockAmount;

    @Column(name = "stock_id")
    private Long stockId;       //Foreign key to Stock in Stock-MS. Get actual info by FeignClient

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_detail_id")
    private UserDetail userDetail;

}
