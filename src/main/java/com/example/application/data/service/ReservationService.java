package com.example.application.data.service;

import com.example.application.data.entity.Offer;
import com.example.application.data.entity.Reservation;
import com.example.application.data.entity.User;
import com.example.application.data.repository.ReservationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationService {

  private final ReservationRepository reservationRepository;

  public void saveReservation(Offer offer, User user){
    Reservation reservation = new Reservation();
    reservation.setOffer(offer);
    reservation.setUser(user);

    reservationRepository.save(reservation);
    log.info("Reservation saved to database. "
        + "ReservationID: [" + reservation.getId() + "] ,"
        + "OfferID: [" + offer.getId() + "] ,"
        + "UserID: [" + user.getId() + "] "
    );
  }

  public List<Reservation> getAllReservations(){
    return reservationRepository.findAll();
  }

  public Optional<Reservation> getOfferReservationForUser(Offer offer, User user){
    List<Predicate<Reservation>> predicates = new ArrayList<>();
    predicates.add(reservation -> reservation.getOffer().getId().equals(offer.getId()));
    predicates.add(reservation -> reservation.getUser().getId().equals(user.getId()));

    Optional<Reservation> mReservation = getAllReservations()
        .stream()
        .filter(predicates.stream().reduce(reservation -> true, Predicate::and))
        .findFirst();

    return mReservation;
  }

  public void deleteReservation(Offer offer, User currentUser) {
    Optional<Reservation> offerReservationForUser = getOfferReservationForUser(offer, currentUser);
    reservationRepository.delete(offerReservationForUser.get());
    log.info("Reservation for user with ID: [" + currentUser.getId() + "] deleted." );
  }

  public List<Reservation> getAllReservationsForUser(User user){
    return reservationRepository
        .findAll()
        .stream()
        .filter(reservation -> reservation.getUser().getId().equals(user.getId()))
        .collect(Collectors.toList());
  }

  public List<Reservation> getAllReservationsForOffer(Offer offer){
    return reservationRepository
        .findAll()
        .stream()
        .filter(reservation -> reservation.getOffer().getId().equals(offer.getId()))
        .collect(Collectors.toList());
  }

  public boolean isAlreadyReservedByUser(Offer offer, User user){
    List<Reservation> userRes = getAllReservationsForUser(user);
    Optional<Reservation> first = userRes.stream()
        .filter(reservation -> reservation.getOffer().getId().equals(offer.getId())).findFirst();
    return first.isPresent();
  }

}
