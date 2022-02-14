package com.example.application.views.studentactivity;

import com.example.application.data.entity.Location;
import com.example.application.data.entity.Offer;
import com.example.application.data.entity.Rate;
import com.example.application.data.entity.Reservation;
import com.example.application.data.entity.User;
import com.example.application.data.enums.OfferState;
import com.example.application.data.service.OfferService;
import com.example.application.data.service.RateService;
import com.example.application.data.service.ReservationService;
import com.example.application.data.service.UserService;
import com.example.application.security.SecurityUtils;
import com.example.application.views.MainLayout;
import com.example.application.views.mainview.SingleOfferView;
import com.example.application.views.profile.userprofile.UserProfileView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParameters;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.annotation.security.RolesAllowed;

@PageTitle("Activity")
@RolesAllowed("student")
@Route(value = "activity", layout = MainLayout.class)
public class StudentActivityView extends VerticalLayout {

  private final UserService userService;
  private final OfferService offerService;
  private final ReservationService reservationService;
  private final RateService rateService;
  private final SecurityUtils securityUtils;

  private final HorizontalLayout content = new HorizontalLayout();
  private VerticalLayout activeRent;
  private VerticalLayout myReservations;

  private Dialog finishRentDialog;


  public StudentActivityView(UserService userService, OfferService offerService,
      ReservationService reservationService, SecurityUtils securityUtils, RateService rateService) {
    this.userService = userService;
    this.offerService = offerService;
    this.reservationService = reservationService;
    this.securityUtils = securityUtils;
    this.rateService = rateService;
    addClassNames(
        "flex", "flex-grow", "max-w-screen-lg", "mx-auto", "pb-l", "justify-center",
        "box-border"
    );
    setWidth("1024px");
    User currentStudent = securityUtils.getCurrentUser().get();

    content.add(activeRent(currentStudent), myReservations(currentStudent));
    add(content);
  }

  public VerticalLayout activeRent(User currentStudent) {
    Offer rentedOffer = currentStudent.getRentedOffer();

    activeRent = new VerticalLayout();
    activeRent.setMinWidth("564px");
    activeRent.addClassNames("border", "border-primary");

    H3 activeRentHeader = new H3("Active rent");
    activeRentHeader.addClassNames("self-center");
    Hr separator = new Hr();
    separator.addClassNames("w-full", "flex-0", "bg-primary");

    H3 myReservationHeader = new H3("Reservations");
    myReservationHeader.addClassNames("self-center");
    Hr separator2 = new Hr();
    separator2.addClassNames("w-full", "flex-0", "bg-primary");



    Button checkOwnerButton = new Button("OWNER PROFILE");
    checkOwnerButton.addClassNames("w-full");
    checkOwnerButton.addClickListener(event -> {
      String userID = rentedOffer.getUser().getId().toString();
      String url = RouteConfiguration.forSessionScope()
          .getUrl(UserProfileView.class, new RouteParameters("userID", userID));
      UI.getCurrent().getPage().setLocation(url);
    });

    Button checkOfferButton = new Button("OFFER LINK");
    checkOfferButton.addClassNames("w-full");
    checkOfferButton.addClickListener(event -> {
      String offerID = rentedOffer.getId().toString();
      String url = RouteConfiguration.forSessionScope()
          .getUrl(SingleOfferView.class, new RouteParameters("offerID", offerID));
      UI.getCurrent().getPage().setLocation(url);
    });

    activeRent.add(activeRentHeader, separator);


    if (rentedOffer == null) {
      H3 nullHeaderOffer = new H3("You don't have any active rent");
      nullHeaderOffer.addClassNames("self-center");
      activeRent.add(nullHeaderOffer);
    } else if (rentedOffer.getOfferState().equals(OfferState.RENTED_OUT)) {

      H3 info = new H3("Your rent is active since");
      info.addClassNames("self-center");
      HorizontalLayout rentSince = new HorizontalLayout();
      Icon sinceIcon = new Icon(VaadinIcon.TIME_FORWARD);
      Span startedAt = new Span(rentedOffer.getRentStart().truncatedTo(ChronoUnit.MINUTES).toString()
          .replaceAll("[TZ]", " ").substring(0, 16));
      rentSince.add(sinceIcon, startedAt);
      rentSince.addClassNames("text-l", "mb-s", "pt-s", "self-center");
      startedAt.addClassNames("m-0", "pl-m", "font-medium");
      sinceIcon.setColor("dodgerblue");

      activeRent.add(info, rentSince, checkOwnerButton, checkOfferButton);

    } else if (rentedOffer.getOfferState().equals(OfferState.RENT_FINISHED) && !rentedOffer.isRenterRated()) {
      createDialog(rentedOffer);
      H3 rateOwnerHeader = new H3("Your last rent is finished, please rate an owner");
      rateOwnerHeader.addClassNames("self-center");
      Button rateButton = new Button("RATE OWNER");
      rateButton.addClassNames("w-full");
      rateButton.addClickListener(event -> finishRentDialog.open());
      activeRent.add(rateOwnerHeader, rateButton);
    }
    activeRent.add();
    return activeRent;
  }

  public VerticalLayout myReservations(User currentStudent) {
    List<Reservation> reservationList = reservationService
        .getAllReservationsForUser(currentStudent);

    myReservations = new VerticalLayout();
    myReservations.setMinWidth("460px");
    myReservations.addClassNames("border", "border-primary");

    H3 activeRentHeader = new H3("My reservations");
    activeRentHeader.addClassNames("self-center");
    Hr separator = new Hr();
    separator.addClassNames("w-full", "flex-0", "bg-primary");

    myReservations.add(activeRentHeader, separator);

    if (reservationList.isEmpty()) {
      H3 emptyResHeader = new H3("You don't have any reservations");
      emptyResHeader.addClassNames("self-center");
      myReservations.add(emptyResHeader);
    } else {
      for (Reservation res : reservationList) {
        HorizontalLayout resLayout = new HorizontalLayout();
        resLayout.setWidthFull();

        Location location = res.getOffer().getLocation();
        Span city = new Span(location.getCity() + ",");
        Span streetNumber = new Span(location.getStreetNumber());
        Icon lIcon = new Icon(VaadinIcon.LOCATION_ARROW_CIRCLE_O);
        resLayout.addClassNames("text-l", "mb-s", "pt-s");
        city.addClassNames("m-0", "pl-m", "font-medium", "self-center");
        streetNumber.addClassNames("m-0", "pl-m", "font-medium", "self-center");
        lIcon.addClassNames("self-center");
        lIcon.setColor("dodgerblue");

        Button offerButton = new Button("OFFER");
        offerButton.addClassNames("ms-auto");
        offerButton.addClickListener(event -> {
          String offerID = res.getOffer().getId().toString();
          String url = RouteConfiguration.forSessionScope()
              .getUrl(SingleOfferView.class, new RouteParameters("offerID", offerID));
          UI.getCurrent().getPage().setLocation(url);
        });

        resLayout.add(lIcon, city, streetNumber, offerButton);
        myReservations.add(resLayout);
      }
    }

    return myReservations;
  }

  private VerticalLayout dialogContent(Offer offer){
    VerticalLayout dialogContent = new VerticalLayout();
    content.setSizeFull();
    H3 header = new H3("Rate user");
    header.addClassNames("self-center");
    Select<Integer> select = new Select<>(1, 2, 3, 4, 5);
    select.setSizeFull();
    select.setValue(5);
    select.setLabel("Rate student (1-lowest | 5-highest)");
    TextArea comment = new TextArea("Comment");
    comment.setSizeFull();

    HorizontalLayout buttons = new HorizontalLayout();
    Button endRentButton = new Button("END RENT");
    buttons.add(endRentButton);
    buttons.setSizeFull();

    endRentButton.addClickListener(event -> {

      offer.setRenterRated(true);
      offerService.save(offer);

      Rate rate = new Rate();
      rate.setOffer(offer);
      rate.setRateNumber(select.getValue());
      rate.setComment(comment.getValue());
      rate.setCreatedAt(Instant.now());
      rate.setRatedBy(offer.getRenter().getId());
      rate.setPersonRated(offer.getUser().getId());
      rate.setRenterRate(true);
      rateService.save(rate);

      finishRentDialog.close();
      UI.getCurrent().getPage().reload();
    });

    dialogContent.add(header, select, comment, buttons);
    return dialogContent;
  }

  private void createDialog(Offer offer){
    finishRentDialog = new Dialog();
    finishRentDialog.add(dialogContent(offer));
    finishRentDialog.setWidth("500px");
  }
}
