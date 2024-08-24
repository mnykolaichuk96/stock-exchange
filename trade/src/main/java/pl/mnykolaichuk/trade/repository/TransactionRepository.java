package pl.mnykolaichuk.trade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mnykolaichuk.trade.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
