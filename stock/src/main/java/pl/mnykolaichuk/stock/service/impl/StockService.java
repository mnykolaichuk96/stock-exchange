package pl.mnykolaichuk.stock.service.impl;

import org.springframework.stereotype.Service;
import pl.mnykolaichuk.stock.dto.StockDto;
import pl.mnykolaichuk.stock.entity.Company;
import pl.mnykolaichuk.stock.entity.Stock;
import pl.mnykolaichuk.stock.mapper.StockMapper;
import pl.mnykolaichuk.stock.repository.CompanyRepository;
import pl.mnykolaichuk.stock.repository.StockRepository;
import pl.mnykolaichuk.stock.service.IStockService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StockService implements IStockService {

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
                .collect(Collectors.toSet());

        company.setStocks(stocks);

        companyRepository.save(company);
    }
}
