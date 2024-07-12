package pl.mnykolaichuk.sellOffer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mnykolaichuk.sellOffer.entity.OutboxEvent;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
}
