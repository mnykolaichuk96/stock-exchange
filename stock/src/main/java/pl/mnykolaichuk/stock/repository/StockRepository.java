package pl.mnykolaichuk.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mnykolaichuk.stock.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock getStockByStockId(Long stockId);
    Stock getStockByName(String name);
}
