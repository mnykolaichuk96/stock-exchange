package pl.mnykolaichuk.users.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class DecrementStockAmountDto {
    private Long stockId;
    private Integer stockAmountToDecrement;
}
