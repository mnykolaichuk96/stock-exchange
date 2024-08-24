package pl.mnykolaichuk.trade.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class TransactionDto {

    private Long buyOfferId;

    private Long sellOfferId;

    private Integer amount;

    private Double price;

    private Long timestamp;
}
