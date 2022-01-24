package com.example.application.views.reservations;

import com.example.application.data.entity.Offer;
import com.example.application.data.entity.Rate;
import com.example.application.data.entity.Reservation;
import com.example.application.data.entity.User;
import com.example.application.data.enums.OfferState;
import com.example.application.data.service.OfferService;
import com.example.application.data.service.RateService;
import com.example.application.data.service.ReservationService;
import com.example.application.security.SecurityUtils;
import com.example.application.views.profile.userprofile.UserProfileView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class OfferReservations extends VerticalLayout {

  private final SecurityUtils securityUtils;
  private final OfferService offerService;
  private final ReservationService reservationService;
  private final RateService rateService;


  // header
  private HorizontalLayout header;
  private Icon locationIcon;
  private Span location;
  private Icon createdAtIcon;
  private Span createdAt;
  private Span offerType;
  private Icon offerTypeIcon;
  private Span counter;
  private Icon counterIcon;
  private Span openBadge;
  private Span closedBadge;
  private Span rentedBadge;

  // user card
  private List<User> userList = new ArrayList<>();
  private HorizontalLayout userCard;

  private HorizontalLayout infoL;
  private Image userImage;
  private Span firstName;
  private Span lastName;
  private Span studentSpan;

  private HorizontalLayout buttonLayout;
  private Button checkUserButton;
  private Button rentOutButton;
  private Button endRentButton;

  private Dialog finishRentDialog;


  private Hr separator;

  public OfferReservations(OfferService offerService, Integer offerID,
      SecurityUtils securityUtils, ReservationService reservationService,
      RateService rateService
  ) {
    this.offerService = offerService;
    this.securityUtils = securityUtils;
    this.reservationService = reservationService;
    this.rateService = rateService;
    addClassNames("flex", "flex-grow", "max-w-screen-lg", "mx-auto", "pb-l", "items-center");
    separator = new Hr();
    Offer offer = offerService.getOfferById(offerID);
    List<Reservation> reservationsForOffer = reservationService.getAllReservationsForOffer(offer);

    add(createOfferHeader(offer),
        separator);

    for (Reservation reservation : reservationsForOffer) {
      if(offer.getOfferState().equals(OfferState.RENTED_OUT)){
        reservationService.deleteById(reservation.getId());
      }
      userList.add(reservation.getUser());
    }

    if(userList.isEmpty() && offer.getOfferState().equals(OfferState.RENTED_OUT)){
      User renter = offer.getRenter();
      add(new H2("Rented out since: " + offer.getRentStart().truncatedTo(ChronoUnit.SECONDS).toString().replaceAll("[TZ]", " ").substring(0,11)));
      add(createUserCard(renter, offer));
    } else if(userList.isEmpty() && offer.getOfferState().equals(OfferState.RENT_FINISHED)){
      add(new H2("Rent for this offer is no longer active"));
    }

    for (User user : userList) {
      add(createUserCard(user, offer));
    }

    if(reservationsForOffer.isEmpty() && !offer.getOfferState().equals(OfferState.RENTED_OUT)){
      H2 noRes = new H2("There is no reservations done for this offer.");
      add(noRes);
    }

    createDialog(offer);
  }

  private HorizontalLayout createOfferHeader(Offer offer){
    String locationValue = offer.getLocation().getCity() + ", " + offer.getLocation().getVoivodeship() + ", " + offer.getLocation().getStreetNumber();
    header = new HorizontalLayout();
    location = new Span(locationValue);
    locationIcon = new Icon(VaadinIcon.LOCATION_ARROW_CIRCLE_O);

    String date = offer.getCreatedAt().truncatedTo(ChronoUnit.SECONDS).toString().replaceAll("[TZ]", " ");//.substring(0,11)
    createdAtIcon = new Icon(VaadinIcon.CALENDAR_CLOCK);
    createdAt = new Span(date);

    offerType = new Span(offer.getOfferTypeSelect().getOfferType());
    offerTypeIcon = new Icon(VaadinIcon.INFO_CIRCLE_O);

    int rSize = reservationService.getAllReservationsForOffer(offer).size();
    counter = new Span(String.valueOf(rSize));
    counterIcon = new Icon(VaadinIcon.MALE);

    // badge
    OfferState offerState = offer.getOfferState();
    openBadge = new Span("OPEN");
    closedBadge = new Span("CLOSED");
    rentedBadge = new Span("RENTED OUT");

    header.add
        (
            locationIcon, location,
            createdAtIcon, createdAt,
            offerTypeIcon, offerType,
            counterIcon, counter
        );

    if(offerState.equals(OfferState.OPEN)){
      header.add(openBadge);
    } else if (offerState.equals(OfferState.CLOSED)){
      header.add(closedBadge);
    } else if (offerState.equals(OfferState.RENTED_OUT)){
      header.add(rentedBadge);
    } else {
      rentedBadge.setText("RENT FINISHED");
      header.add(rentedBadge);
    }
    cssForHeader();
    return header;
  }

  private HorizontalLayout createUserCard(User user, Offer offer){
    userCard = new HorizontalLayout();
    userCard.addClassNames("border", "border-primary", "rounded-l", "p-xs", "box-border", "flex-grow");
    userCard.setWidth("80%");

    userImage = new Image();

    infoL = new HorizontalLayout();
    infoL.addClassNames("self-center", "justify-start");
    studentSpan = new Span("Student: ");
    firstName = new Span(user.getFirstName());
    lastName = new Span(user.getLastName());
    infoL.add(studentSpan, firstName, lastName);

    buttonLayout = new HorizontalLayout();
    checkUserButton = new Button("CHECK USER");
    rentOutButton = new Button("RENT OUT");
    endRentButton = new Button("END RENT");
    buttonLayout.add(checkUserButton, rentOutButton);

    buttonHandlers(user, offer);
    cssForUserCard(user);
    userCard.add(userImage, infoL, buttonLayout);
    return userCard;
  }

  private void buttonHandlers(User user, Offer offer) {
    if(offer.getRenter() == null){
      rentOutButton.setEnabled(true);
      rentOutButton.setText("RENT OUT");
    } else {
      buttonLayout.remove(rentOutButton);
      buttonLayout.add(endRentButton);
    }
    rentOutButton.addClickListener(event -> {
      Offer updateOffer = offerService.getOfferById(offer.getId());
      // offer update {state, start date}
      updateOffer.setOfferState(OfferState.RENTED_OUT);
      updateOffer.setRentStart(Instant.now());
      // setting renter
      updateOffer.setRenter(user);
      offerService.save(updateOffer);
      UI.getCurrent().getPage().reload();
    });

    checkUserButton.addClickListener(event -> navigateToUserProfile(user));

    endRentButton.addClickListener(event -> finishRentDialog.open());

  }

  private VerticalLayout dialogContent(Offer offer){
    VerticalLayout content = new VerticalLayout();
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
    Button cancelButton = new Button("CANCEL");
    cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    buttons.add(endRentButton, cancelButton);
    buttons.setSizeFull();

    cancelButton.addClickListener(event -> finishRentDialog.close());
    endRentButton.addClickListener(event -> {

      offer.setRentEnd(Instant.now());
      offer.setOfferState(OfferState.RENT_FINISHED);
      offer.setOwnerRated(true);
      offerService.save(offer);

      Rate rate = new Rate();
      rate.setOffer(offer);
      rate.setRateNumber(select.getValue());
      rate.setComment(comment.getValue());
      rate.setCreatedAt(Instant.now());
      rate.setRatedBy(offer.getUser().getId());
      rate.setPersonRated(offer.getRenter().getId());
      rate.setRenterRate(false);
      rateService.save(rate);

      finishRentDialog.close();
      UI.getCurrent().getPage().reload();
    });

    content.add(header, select, comment, buttons);
    return content;
  }

  private void createDialog(Offer offer){
    finishRentDialog = new Dialog();
    finishRentDialog.add(dialogContent(offer));
    finishRentDialog.setWidth("500px");
  }

  private void cssForHeader(){

    //header
    header.addClassNames("flex");
    location.addClassNames("m-0", "pl-s", "pr-l", "font-medium");
    locationIcon.setColor("blue");

    createdAt.addClassNames("m-0", "pl-s", "pr-l", "font-medium");
    createdAtIcon.setColor("blue");

    offerType.addClassNames("m-0", "pl-s", "pr-l", "font-medium");
    offerTypeIcon.setColor("blue");

    counter.addClassNames("m-0", "pl-s", "pr-l", "font-medium");
    counterIcon.setColor("blue");

    openBadge.getElement().getThemeList().add("badge success");
    openBadge.addClassNames("pl-l");

    closedBadge.getElement().getThemeList().add("badge error");
    closedBadge.addClassNames("pr-l");

    rentedBadge.getElement().getThemeList().add("badge contrast primary");
    rentedBadge.addClassNames("pr-l");

    separator.addClassNames("bg-primary", "flex-grow", "max-w-full");
  }

  private void cssForUserCard(User user) {

    // image
    userImage.addClassNames("w-full", "rounded-l", "box-border");
    userImage.setWidth("10%");

    if (user.getProfilePictureUrl() == null || user.getProfilePictureUrl().length <= 0) {
      Avatar avatar = new Avatar(user.getUsername());
      avatar.getStyle().set("border-radius", "12px");

      userImage.add(avatar);
    } else {
      userImage.getElement().setAttribute("src",
          new StreamResource(" ",
              () -> new ByteArrayInputStream(user.getProfilePictureUrl())));
    }

    infoL.setWidth("50%");
    infoL.addClassNames("text-m", "font-medium");

    // buttons
    buttonLayout.setWidth("40%");
    buttonLayout.addClassNames("pb-s");
    checkUserButton.setHeightFull();
    checkUserButton.setWidthFull();
    rentOutButton.setHeightFull();
    rentOutButton.setWidthFull();
    rentOutButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
    endRentButton.setWidthFull();
    endRentButton.setHeightFull();
    endRentButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

  }

  public void navigateToUserProfile(User user){
    String userID = user.getId().toString();
    String url = RouteConfiguration.forSessionScope()
        .getUrl(UserProfileView.class, new RouteParameters("userID", userID));
    UI.getCurrent().getPage().setLocation(url);
  }

}
