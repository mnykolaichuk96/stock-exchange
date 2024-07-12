package pl.mnykolaichuk.sellOffer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mnykolaichuk.sellOffer.entity.SellOffer;

public interface SellOfferRepository extends JpaRepository<SellOffer, Long> {
}
