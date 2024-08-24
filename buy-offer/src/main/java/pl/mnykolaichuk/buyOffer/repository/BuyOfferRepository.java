package pl.mnykolaichuk.buyOffer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mnykolaichuk.buyOffer.entity.BuyOffer;

public interface BuyOfferRepository extends JpaRepository<BuyOffer, Long> {
}
