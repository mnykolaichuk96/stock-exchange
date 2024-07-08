package pl.mnykolaichuk.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mnykolaichuk.stock.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    public Company getCompanyByCompanyId(Long companyId);
}
