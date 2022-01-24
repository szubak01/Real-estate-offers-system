package com.example.application.data.repository;

import com.example.application.data.entity.Offer;
import com.example.application.data.enums.OfferState;
import com.example.application.data.enums.OfferType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {


  List<Offer> findOffersByLocation_CityContaining(String value);

  List<Offer> findOffersByOfferTypeSelect(OfferType offerTypeSelect);

  List<Offer> findOffersByOfferState(OfferState value);

  List<Offer> findOffersByPricePerMonthIsLessThan(Double value);
}
