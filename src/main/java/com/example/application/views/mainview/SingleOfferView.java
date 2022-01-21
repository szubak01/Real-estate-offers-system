package com.example.application.views.mainview;

import com.example.application.data.service.OfferService;
import com.example.application.data.service.ReservationService;
import com.example.application.security.SecurityUtils;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
@Route(value = "offer/:offerID", layout = MainLayout.class)
@AnonymousAllowed
public class SingleOfferView extends VerticalLayout implements BeforeEnterObserver {

  private final OfferService offerService;
  private final SecurityUtils securityUtils;
  private final ReservationService reservationService;
  private Integer offerID;

  public SingleOfferView(OfferService offerService, SecurityUtils securityUtils, ReservationService reservationService){
    this.offerService = offerService;
    this.securityUtils = securityUtils;
    this.reservationService = reservationService;

    addClassNames("mb-xl");
  }

  @SneakyThrows
  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    offerID = Integer.parseInt(event.getRouteParameters().get("offerID").get());

    SingleOffer singleOffer = new SingleOffer(offerService, offerID, securityUtils, reservationService);
    add(singleOffer);
  }

}
