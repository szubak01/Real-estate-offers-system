package com.example.application.data.service;

import com.example.application.data.entity.OfferImage;
import com.example.application.data.entity.Location;
import com.example.application.data.entity.Offer;
import com.example.application.data.entity.User;
import com.example.application.data.enums.OfferState;
import com.example.application.data.enums.OfferType;
import com.example.application.data.repository.OfferImageRepository;
import com.example.application.data.repository.LocationRepository;
import com.example.application.data.repository.OfferRepository;
import com.example.application.security.SecurityUtils;
import com.example.application.views.myoffers.MyOffersView;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OfferService {

  private final OfferRepository offerRepository;
  private final OfferImageRepository offerImageRepository;
  private final LocationRepository locationRepository;
  private final SecurityUtils securityUtils;

  public void save(Offer offer){
    offerRepository.save(offer);
  }

  public void saveOffer(MyOffersView view) throws IOException {
    User currentUser = securityUtils.getCurrentUser().get();

    Location location = mapLocationValuesFromView(view, new Location());
    locationRepository.save(location);
    log.info("New location saved to database with ID: [" + location.getId() + "]");

    Offer offer = mapOfferValuesFromView(view, new Offer());
    offer.setOfferState(OfferState.OPEN);
    offer.setCreatedAt(Instant.now());
    offer.setUser(currentUser);
    offer.setLocation(location);

    offerRepository.save(offer);
    log.info(
        "New offer saved to database with ID: [" + offer.getId() + "] . Added by user with ID: "
            + offer.getUser().getId());

    MultiFileMemoryBuffer buffer = view.getMultiUpload().getMultiFileMemoryBuffer();
    Set<String> filesName = buffer.getFiles();

    if (!filesName.isEmpty()) {
      for (String name : filesName) {
        byte[] imageBytes = buffer.getInputStream(name).readAllBytes();
        OfferImage img = new OfferImage();
        img.setOffer(offer);
        img.setImage(imageBytes);
        img.setImageName(name);
        offerImageRepository.save(img);
        log.info("Image for offer with ID: [" + offer.getId() + "] saved to database.");
      }
    }

  }

  public void updateOffer(MyOffersView view, Offer offer) {

    Offer updatedOffer = mapOfferValuesFromView(view, offer);
    updatedOffer.setUpdatedAt(Instant.now());
    offerRepository.save(updatedOffer);
    log.info("Offer with ID: [" + updatedOffer.getId() + "] updated.");

    Location location = updatedOffer.getLocation();
    Location updatedLocation = mapLocationValuesFromView(view, location);
    locationRepository.save(updatedLocation);
    log.info("Location updated for offer with ID: [" + updatedOffer.getId() + "] |  Location ID: ["
        + updatedLocation.getId() + "]");
  }

  public void deleteOfferImageById(Integer imageId) {
    offerImageRepository.deleteById(imageId);
    log.info("Image with ID: [" + imageId + "] has been deleted.");
  }

  public void deleteOfferById(Integer offerId) {
    offerRepository.deleteById(offerId);
    log.info("Offer with ID: [" + offerId + "] deleted.");
  }

  public List<Offer> getOffersOwnedByCurrentUser() {
    User currentUser = securityUtils.getCurrentUser().get();
    log.info("Offers for user with ID: [" + currentUser.getId() + "] retrieved from database.");

    return offerRepository
        .findAll()
        .stream()
        .filter(offer -> offer.getUser().getId().equals(currentUser.getId()))
        .collect(Collectors.toList());
  }

  public List<OfferImage> getOfferImages(Offer offer) {
    return offerImageRepository
        .findAll()
        .stream()
        .filter(image -> image.getOffer().getId().equals(offer.getId()))
        .collect(Collectors.toList());

  }

  private Offer mapOfferValuesFromView(MyOffersView view, Offer offer) {
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

  private Location mapLocationValuesFromView(MyOffersView view, Location location) {
    location.setCity(view.getCity().getValue());
    location.setVoivodeship(view.getVoivodeship().getValue());
    location.setStreetNumber(view.getStreetNumber().getValue());
    location.setPostalCode(view.getPostalCode().getValue());

    return location;
  }

  public boolean offerHasImage(Offer offer) {
    return getOfferImages(offer).size() != 0;
  }

  public List<Offer> getAllOffers() {
    return offerRepository.findAll();
  }

  public Offer getOfferById(Integer offerID) {
    return offerRepository.getById(offerID);
  }

  public List<Offer> getOffersByCity(String value) {
    return offerRepository.findOffersByLocation_CityContaining(value);
  }

  public List<Offer> getOffersByType(OfferType offerType) {
    return offerRepository.findOffersByOfferTypeSelect(offerType);
  }

  public List<Offer> getOffersByState(OfferState value) {
    return offerRepository.findOffersByOfferState(value);
  }

  public List<Offer> getOffersByPrice(Double value) {
    return offerRepository.findOffersByPricePerMonthIsLessThan(value);
  }
}
