package pl.mnykolaichuk.users.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "user_detail")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class UserDetail extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // Id generowane przez bd
    private Long userDetailsId;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // TODO ForeignKey to keycloak user_id
    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "userDetail", cascade = CascadeType.ALL)
    private List<UserStock> userStockList;

}
