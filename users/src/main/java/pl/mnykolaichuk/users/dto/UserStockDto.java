package pl.mnykolaichuk.users.dto;


import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserStockDto {
    Long userDetailId;
    Long stockId;
    Integer stockAmount;
}
