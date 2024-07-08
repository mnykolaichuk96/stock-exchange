package pl.mnykolaichuk.stock.dto;

import lombok.*;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class CompanyDto {
    private String name;

    private Set<StockDto> stockDtos;
}
