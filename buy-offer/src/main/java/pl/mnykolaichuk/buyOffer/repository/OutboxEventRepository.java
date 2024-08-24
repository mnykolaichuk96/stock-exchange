package pl.mnykolaichuk.buyOffer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mnykolaichuk.buyOffer.entity.OutboxEvent;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
}
