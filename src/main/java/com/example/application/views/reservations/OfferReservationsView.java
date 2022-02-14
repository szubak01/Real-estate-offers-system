package com.example.application.views.reservations;

import com.example.application.data.service.OfferService;
import com.example.application.data.service.RateService;
import com.example.application.data.service.ReservationService;
import com.example.application.security.SecurityUtils;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;
import lombok.SneakyThrows;

@PageTitle("Reservations")
@RolesAllowed("landlord")
@Route(value = "offer/reservations/:offerID", layout = MainLayout.class)
public class OfferReservationsView extends VerticalLayout implements BeforeEnterObserver {

  private final OfferService offerService;
  private final SecurityUtils securityUtils;
  private final ReservationService reservationService;
  private final RateService rateService;
  private Integer offerID;

  public OfferReservationsView(OfferService offerService, SecurityUtils securityUtils,
      ReservationService reservationService, RateService rateService) {
    this.offerService = offerService;
    this.securityUtils = securityUtils;
    this.reservationService = reservationService;
    this.rateService = rateService;

    add();
  }

  @SneakyThrows
  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    offerID = Integer.parseInt(event.getRouteParameters().get("offerID").get());

    OfferReservations offerReservations = new OfferReservations(offerService, offerID,
        securityUtils, reservationService, rateService);
    add(offerReservations);
  }
}
