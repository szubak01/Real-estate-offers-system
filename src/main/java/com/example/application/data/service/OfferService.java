package com.example.application.data.service;

import com.example.application.data.entity.Location;
import com.example.application.data.entity.Offer;
import com.example.application.data.repository.LocationRepository;
import com.example.application.data.repository.OfferRepository;
import com.example.application.security.SecurityUtils;
import com.example.application.views.myoffers.MyOffersView;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OfferService {

  private final OfferRepository offerRepository;
  private final LocationRepository locationRepository;
  private final SecurityUtils securityUtils;

  public void saveOffer(MyOffersView myOffersView) {
    Location location = new Location();
    location.setCity(myOffersView.getCity().getValue());
    location.setVoivodeship(myOffersView.getVoivodeship().getValue());
    location.setStreetNumber(myOffersView.getStreetNumber().getValue());
    location.setPostalCode(myOffersView.getPostalCode().getValue());

    locationRepository.save(location);
    log.info("New location record save to db with ID: " + location.getId());

    Offer offer = new Offer();
    offer.setOfferTypeSelect(myOffersView.getOfferTypeSelect().getValue());
    offer.setOfferTitle(myOffersView.getOfferTitle().getValue());
    offer.setPricePerMonth(myOffersView.getPricePerMonth().getValue());
    offer.setRent(myOffersView.getRent().getValue());
    offer.setDeposit(myOffersView.getDeposit().getValue());
    offer.setLivingArea(myOffersView.getLivingArea().getValue());
    offer.setNumberOfRooms(myOffersView.getNumberOfRooms().getValue());
    offer.setTypeOfRoom(myOffersView.getTypeOfRoom().getValue());
    offer.setDescription(myOffersView.getDescription().getValue());
    offer.setOfferImages(myOffersView.getMultiUpload().getMultiFileMemoryBuffer().getFiles());
    offer.setCreatedAt(Instant.now());
    offer.setUser(securityUtils.getCurrentUser().get());
    offer.setLocation(location);

    offerRepository.save(offer);
    log.info("New offer added with ID: " + offer.getId() + ". Added by user with ID: " + offer.getUser().getId());
  }
}
