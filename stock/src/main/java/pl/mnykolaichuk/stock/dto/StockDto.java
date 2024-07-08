package pl.mnykolaichuk.stock.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class StockDto {
    private String name;
    private Long priceId;
    private Integer avoidAmount;

    private CompanyDto companyDto;
}
