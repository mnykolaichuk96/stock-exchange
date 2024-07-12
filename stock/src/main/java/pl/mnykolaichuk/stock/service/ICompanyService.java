package pl.mnykolaichuk.stock.service;

import pl.mnykolaichuk.stock.dto.StockDto;
import pl.mnykolaichuk.stock.entity.Company;

import java.util.Set;

public interface ICompanyService {
    /**
     * Create company without assigned stocks.
     *
     * @param companyName - String type company name.
     */
    void createCompany(String companyName);

    /**
     * Create company with assigned stocks.
     *
     * @param companyName - String type company name.
     * @param stockDtos - Set of assigned to company stocks.
     */
    void createCompanyWithStocks(String companyName, Set<StockDto> stockDtos);
    /**
     * Getting existing company by its name.
     *
     * @param companyName - name of searching company.
     * @return Company object
     */
    Company getCompanyByName(String companyName);

}
