package pl.mnykolaichuk.stock.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mnykolaichuk.stock.dto.StockDto;
import pl.mnykolaichuk.stock.entity.Company;
import pl.mnykolaichuk.stock.entity.Stock;
import pl.mnykolaichuk.stock.exception.CompanyAlreadyExistException;
import pl.mnykolaichuk.stock.exception.CompanyNotFoundException;
import pl.mnykolaichuk.stock.mapper.StockMapper;
import pl.mnykolaichuk.stock.repository.CompanyRepository;
import pl.mnykolaichuk.stock.repository.StockRepository;
import pl.mnykolaichuk.stock.service.ICompanyService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService implements ICompanyService {
    private CompanyRepository companyRepository;
    private StockRepository stockRepository;

    /**
     * Create company without assigned stocks.
     *
     * @param companyName - String type company name.
     */
    @Override
    public void createCompany(String companyName) {
        if (companyRepository.getCompanyByName(companyName) != null) {
            throw new CompanyAlreadyExistException("Company", "companyName", companyName);
        }

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

        if (companyRepository.getCompanyByName(companyName) != null) {
            throw new CompanyAlreadyExistException("Company", "companyName", companyName);
        }

        Company company = Company.builder()
                .name(companyName)
                .build();

        Set<Stock> stocks = stockDtos.stream()
                .map(stockDto -> StockMapper.mapToStock(stockDto, new Stock()))
                .peek(stock -> stock.setCompany(company))
                .collect(Collectors.toSet());

        company.setStocks(stocks);

        companyRepository.save(company);
    }

    /**
     * Getting existing company by its name.
     *
     * @param companyName - name of searching company.
     * @return Company object
     */
    @Override
    public Company getCompanyByName(String companyName) {
        Company company = companyRepository.getCompanyByName(companyName);
        if (company == null) {
            throw new CompanyNotFoundException("Company", "companyName", companyName);
        }
        return company;
    }
}
