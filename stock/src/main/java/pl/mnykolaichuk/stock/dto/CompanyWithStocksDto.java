package pl.mnykolaichuk.stock.dto;

import lombok.*;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class CompanyWithStocksDto {
    private String companyName;
    private Set<StockDto> stockDtos;
}
