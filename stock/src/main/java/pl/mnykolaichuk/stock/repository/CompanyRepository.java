package pl.mnykolaichuk.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mnykolaichuk.stock.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    public Company getCompanyByCompanyId(Long companyId);
    public Company getCompanyByName(String name);
}
