package pl.mnykolaichuk.stock.service;

import pl.mnykolaichuk.stock.dto.StockDto;
import pl.mnykolaichuk.stock.entity.Stock;

import java.util.Set;

public interface IStockService {

    /**
     * Adding stocks to existing company.
     *
     * @param companyId - ID of existing company
     * @param stockDtos - Set of stocks for assigned to existing company
     */
    void addStocks(Long companyId, Set<StockDto> stockDtos);

    /**
     * Getting available amount of stock by its name
     *
     * @param stockId - ID of stock
     * @return amount od stock
     */
    Integer getStockAvailableAmount(Long stockId);

    /**
     * Update existing Stock using Stock Data Transfer Object with:
     * name, priceId, availableAmount
     * All of this fields could be updated.
     *
     * @param stockDto - Stock Data Transfer Object
     */
    void updateStock(StockDto stockDto);

    /**
     * Get Stock By ID
     *
     * @param id Stock ID
     * @return Stock
     */
    Stock getStockById(Long id);
}
