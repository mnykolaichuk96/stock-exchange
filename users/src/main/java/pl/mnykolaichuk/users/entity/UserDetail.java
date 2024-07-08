package pl.mnykolaichuk.users.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "email")
    private String email;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(mappedBy = "userDetail", cascade = CascadeType.ALL)
    private Set<UserStock> userStocks = new HashSet<>();

}
