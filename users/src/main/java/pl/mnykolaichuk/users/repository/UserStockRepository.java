package pl.mnykolaichuk.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mnykolaichuk.users.entity.UserStock;

public interface UserStockRepository extends JpaRepository<UserStock, Long> {
}
