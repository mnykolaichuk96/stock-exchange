package pl.mnykolaichuk.stock.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.mnykolaichuk.stock.dto.StockDto;
import pl.mnykolaichuk.stock.entity.Company;
import pl.mnykolaichuk.stock.entity.Stock;
import pl.mnykolaichuk.stock.exception.StockNotFoundException;
import pl.mnykolaichuk.stock.mapper.StockMapper;
import pl.mnykolaichuk.stock.repository.CompanyRepository;
import pl.mnykolaichuk.stock.repository.StockRepository;
import pl.mnykolaichuk.stock.service.IStockService;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StockService implements IStockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    private StockRepository stockRepository;
    private CompanyRepository companyRepository;

    /**
     * Adding stocks to existing company.
     *
     * @param companyId - ID of existing company
     * @param stockDtos - Set of stocks for assigned to existing company
     */
    @Override
    public void addStocks(Long companyId, Set<StockDto> stockDtos) {

        Company company = companyRepository.getCompanyByCompanyId(companyId);

        Set<Stock> stocks = stockDtos.stream()
                .map(stockDto -> StockMapper.mapToStock(stockDto, new Stock()))
                .peek(stock -> stock.setCompany(company))
                .collect(Collectors.toSet());

        // Створюємо мапу існуючих акцій за ім'ям для швидкого доступу
        Map<String, Stock> existingStocksMap = company.getStocks().stream()
                .collect(Collectors.toMap(Stock::getName, stock -> stock));

        for (Stock newStock : stocks) {
            if (existingStocksMap.containsKey(newStock.getName())) {
                // Якщо акція вже існує, оновлюємо її avoidAmount
                Stock existingStock = existingStocksMap.get(newStock.getName());
                existingStock.setAvailableAmount(existingStock.getAvailableAmount() + newStock.getAvailableAmount());

                stockRepository.save(existingStock);
            } else {
                // Якщо акція нова, додаємо її до компанії
                company.getStocks().add(newStock);
            }
        }

        logger.info("CompanyId: " + String.valueOf(company.getCompanyId()));

        companyRepository.save(company);
    }

    /**
     * Getting avoid amount of stock by its name
     *
     * @param stockId - stock ID
     * @return amount od stock
     */
    @Override
    public Integer getStockAvailableAmount(Long stockId) {
        Optional<Stock> optionalStock = Optional.ofNullable(stockRepository.getStockByStockId(stockId));

        Stock stock =
                optionalStock.orElseThrow(() -> new StockNotFoundException("Stock", "stockName", stockId.toString()));

        return stock.getAvailableAmount();
    }

    /**
     * Update existing Stock using Stock Data Transfer Object with:
     * name, priceId, availableAmount
     * All of this fields could be updated.
     *
     * @param stockDto - Stock Data Transfer Object
     */
    @Override
    public void updateStock(StockDto stockDto) {

        Optional<Stock> optionalStock = Optional.ofNullable(stockRepository.getStockByName(stockDto.getName()));

        Stock stock = optionalStock.orElseThrow(
                () -> new StockNotFoundException("Stock", "stockName", stockDto.getName())
        );

        Stock updatedStock = StockMapper.mapToStock(stockDto, stock);

        stockRepository.save(updatedStock);
    }

    /**
     * Get Stock By ID
     *
     * @param id Stock ID
     * @return Stock
     */
    @Override
    public Stock getStockById(Long id) {
        return stockRepository.getStockByStockId(id);
    }
}
