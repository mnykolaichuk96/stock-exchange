package pl.mnykolaichuk.stock.service.impl;

import org.springframework.stereotype.Service;
import pl.mnykolaichuk.stock.dto.StockDto;
import pl.mnykolaichuk.stock.entity.Company;
import pl.mnykolaichuk.stock.entity.Stock;
import pl.mnykolaichuk.stock.mapper.StockMapper;
import pl.mnykolaichuk.stock.repository.CompanyRepository;
import pl.mnykolaichuk.stock.service.ICompanyService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompanyService implements ICompanyService {
    private CompanyRepository companyRepository;

    /**
     * Create company without assigned stocks.
     *
     * @param companyName - String type company name.
     */
    @Override
    public void createCompany(String companyName) {
        Company company = Company.builder()
                .name(companyName)
                .stocks(null)
                .build();

        companyRepository.save(company);
    }

    /**
     * Create company with assigned stocks.
     *
     * @param companyName - String type company name.
     * @param stockDtos   - Set of assigned to company stocks.
     */
    @Override
    public void createCompanyWithStocks(String companyName, Set<StockDto> stockDtos) {

        Set<Stock> stocks = stockDtos.stream()
                .map(stockDto -> StockMapper.mapToStock(stockDto, new Stock()))
                .collect(Collectors.toSet());

        Company company = Company.builder()
                .name(companyName)
                .stocks(stocks)
                .build();

        companyRepository.save(company);
    }

}
