package pl.mnykolaichuk.stock.service;

import pl.mnykolaichuk.stock.dto.StockDto;

import java.util.Set;

public interface IStockService {

    /**
     * Adding stocks to existing company.
     *
     * @param companyId - ID of existing company
     * @param stockDtos - Set of stocks for assigned to existing company
     */
    void addStocks(Long companyId, Set<StockDto> stockDtos);
}
