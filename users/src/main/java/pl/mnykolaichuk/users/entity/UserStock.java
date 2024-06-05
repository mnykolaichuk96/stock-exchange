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

    // TODO ForeignKey to stock service stock_id
    @Column(name = "stock_id")
    private Long stockId;

    @ManyToOne
    @JoinColumn(name = "user_detail_id")
    private UserDetail userDetail;

}
