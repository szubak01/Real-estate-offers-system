package com.example.application.data.service;

import com.example.application.data.entity.Location;
import com.example.application.data.entity.Offer;
import com.example.application.data.entity.User;
import com.example.application.data.repository.ImagesRepository;
import com.example.application.data.repository.LocationRepository;
import com.example.application.data.repository.OfferRepository;
import com.example.application.security.SecurityUtils;
import com.example.application.views.myoffers.MyOffersView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class OfferService {

  private final OfferRepository offerRepository;
  private final ImagesRepository imagesRepository;
  private final LocationRepository locationRepository;
  private final SecurityUtils securityUtils;

  public void saveOffer(MyOffersView view) {
    User currentUser = securityUtils.getCurrentUser().get();

    Location location = mapLocationValuesFromView(view, new Location());
    locationRepository.save(location);
    log.info("New location saved to database with ID: [" + location.getId() + "]");

    Offer offer = mapOfferValuesFromView(view, new Offer());
    offer.setCreatedAt(Instant.now());
    offer.setUser(currentUser);
    offer.setLocation(location);

    offerRepository.save(offer);
    log.info("New offer saved to database with ID: [" + offer.getId() + "] . Added by user with ID: " + offer.getUser().getId());
  }

  public void updateOffer(MyOffersView view, Offer offer) {

    Offer updatedOffer = mapOfferValuesFromView(view, offer);
    updatedOffer.setUpdatedAt(Instant.now());
    offerRepository.save(updatedOffer);
    log.info("Offer with ID: [" + updatedOffer.getId() + "] updated.");

    Location location = updatedOffer.getLocation();
    Location updatedLocation = mapLocationValuesFromView(view, location);
    locationRepository.save(updatedLocation);
    log.info("Location updated for offer with ID: [" + updatedOffer.getId() + "] |  Location ID: [" + updatedLocation.getId() + "]");
  }

  public void deleteOfferById(Integer offerId) {
    offerRepository.deleteById(offerId);
    log.info("Offer with ID: [" + offerId + "] deleted.");
  }


  public List<Offer> getOffersOwnedByCurrentUser(){
    User currentUser = securityUtils.getCurrentUser().get();
    log.info("Offers for user with ID: [" + currentUser.getId() + "] retrieved from database.");

    return offerRepository
        .findAll()
        .stream()
        .filter(offer -> offer.getUser().getId().equals(currentUser.getId()))
        .collect(Collectors.toList());
  }

  private Offer mapOfferValuesFromView(MyOffersView view, Offer offer){
    offer.setOfferTypeSelect(view.getOfferTypeSelect().getValue());
    offer.setOfferTitle(view.getOfferTitle().getValue());
    offer.setPricePerMonth(view.getPricePerMonth().getValue());
    offer.setRent(view.getRent().getValue());
    offer.setDeposit(view.getDeposit().getValue());
    offer.setLivingArea(view.getLivingArea().getValue());
    offer.setNumberOfRooms(view.getNumberOfRooms().getValue());
    offer.setTypeOfRoom(view.getTypeOfRoom().getValue());
    offer.setDescription(view.getDescription().getValue());

    return offer;
  }

  private Location mapLocationValuesFromView(MyOffersView view, Location location){
    location.setCity(view.getCity().getValue());
    location.setVoivodeship(view.getVoivodeship().getValue());
    location.setStreetNumber(view.getStreetNumber().getValue());
    location.setPostalCode(view.getPostalCode().getValue());

    return location;
  }

}
