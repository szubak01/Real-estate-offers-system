package com.example.application.views.mainview;


import com.example.application.data.entity.Offer;
import com.example.application.data.entity.OfferImage;
import com.example.application.data.entity.Reservation;
import com.example.application.data.entity.User;
import com.example.application.data.enums.OfferState;
import com.example.application.data.enums.OfferType;
import com.example.application.data.enums.Role;
import com.example.application.data.service.OfferService;
import com.example.application.data.service.ReservationService;
import com.example.application.security.SecurityUtils;
import com.example.application.views.profile.userprofile.UserProfileView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;


public class SingleOffer extends VerticalLayout {

  private final SecurityUtils securityUtils;
  private final OfferService offerService;
  private final ReservationService reservationService;

  private final HorizontalLayout horizontalLayout;
  private Image image;

  public SingleOffer(OfferService offerService, Integer offerID, SecurityUtils securityUtils,
      ReservationService reservationService) {
    this.offerService = offerService;
    this.securityUtils = securityUtils;
    this.reservationService = reservationService;

    addClassNames("flex", "flex-grow", "max-w-screen-xl", "mx-auto", "pb-l");

    Optional<User> maybeUser = securityUtils.getCurrentUser();

    Offer offer = offerService.getOfferById(offerID);
    List<OfferImage> offerImages = offerService.getOfferImages(offer);
    User owner = offer.getUser();

    String offerTitle = offer.getOfferTitle();
    H2 mainTitle = new H2(offerTitle);
    mainTitle.addClassNames("mb-0");

    HorizontalLayout offerLocationLayout = new HorizontalLayout();
    String loc =
        offer.getLocation().getCity() + ", " + offer.getLocation().getVoivodeship() + ", " + offer
            .getLocation().getStreetNumber();
    Span location = new Span(loc);
    location.addClassNames("m-0", "pl-m", "font-medium");
    Icon locationIcon = new Icon(VaadinIcon.LOCATION_ARROW_CIRCLE_O);
    locationIcon.setColor("blue");
    offerLocationLayout.add(locationIcon, location);

    Hr separator = new Hr();
    separator.addClassNames("bg-primary", "flex-grow", "max-w-full");

    // images and info about user
    horizontalLayout = new HorizontalLayout();
    horizontalLayout.addClassNames("w-full");
    horizontalLayout.setSizeFull();

    OrderedList imagesView = new OrderedList();
    imagesView.addClassNames("grid", "grid-cols-3", "gap-m", "box-border", "px-s");
    imagesView.setMinWidth("70%");

    if (!offerImages.isEmpty()) {

      for (OfferImage img : offerImages) {
        String imageName = img.getImageName();
        byte[] imageBytes = img.getImage();
        image = new Image();
        image.setWidth("265px");
        image.setHeight("220px");
        image.addClassNames("w-full", "rounded-l", "p-xd", "box-border", "overflow-hidden");
        StreamResource resource = new StreamResource(imageName,
            () -> new ByteArrayInputStream(imageBytes));
        image.setSrc(resource);
        imagesView.add(image);

        image.addClickListener(imageClickEvent -> {
          Dialog dialog = new Dialog();
          Image bigImage = new Image();

          bigImage.setSrc(resource);
          dialog.add(bigImage);
          dialog.setMaxWidth("1200px");
          dialog.setMaxHeight("700px");

          if (dialog.isOpened()) {
            bigImage.setMaxWidth("1200px");
            bigImage.setMaxHeight("700px");
          }
          dialog.open();
        });
      }

    } else {
      imagesView.add(new H2("No photos available."));
    }

    VerticalLayout userInfo = new VerticalLayout();
    userInfo.addClassNames("bg-contrast-5", "rounded-l");

    HorizontalLayout ownerImageName = new HorizontalLayout();
    ownerImageName.addClassNames("w-full");

    Image userImage = new Image();
    userImage.addClassNames("rounded-l");

    Span name = new Span(owner.getUsername());
    name.addClassNames("font-medium", "text-2xl", "text-secondary", "text-center", "pt-s", "pl-l");

    Button checkOwnerButton = new Button("CHECK");
    checkOwnerButton.addClassNames("self-center", "ms-auto");
    checkOwnerButton.addClickListener(event -> navigateToOwnerProfile(owner));

    Hr separator2 = new Hr();
    separator2.addClassNames("bg-primary", "flex-grow-0", "max-w-full");

    if (owner.getProfilePictureUrl() == null || owner.getProfilePictureUrl().length <= 0) {
      Avatar avatar = new Avatar(owner.getUsername());
      avatar.getStyle().set("border-radius", "12px");
      avatar.setWidth("65px");
      avatar.setHeight("65px");

      ownerImageName.add(avatar, name, checkOwnerButton);
    } else {
      userImage.getElement().setAttribute("src",
          new StreamResource(" ",
              () -> new ByteArrayInputStream(owner.getProfilePictureUrl())));

      userImage.setWidth("65px");
      userImage.setHeight("65px");

      ownerImageName.add(userImage, name, checkOwnerButton);
    }

    // creation date
    String createdAt = offer.getCreatedAt().truncatedTo(ChronoUnit.SECONDS).toString()
        .replaceAll("[TZ]", " ").substring(0, 11);
    Span creationDate = new Span("Creation date:" + "  " + createdAt);
    creationDate.addClassNames("font-medium");

    // phone number
    Span phoneNumber = new Span("Phone:" + "  " + owner.getPhoneNumber());
    phoneNumber.addClassNames("font-medium");

    // email
    Span email = new Span("E-mail:" + "  " + owner.getEmail());
    email.addClassNames("font-medium");

    // offer reservations counter
    Hr resSeparator = new Hr();
    resSeparator.addClassNames("bg-primary", "flex-0");

    HorizontalLayout counterLayout = new HorizontalLayout();
    Span counterLabel = new Span("In queue for this offer: ");
    counterLabel.addClassNames("font-medium");
    int rSize = reservationService.getAllReservationsForOffer(offer).size();
    Span counter = new Span(String.valueOf(rSize));
    counter.addClassNames("font-medium");
    Icon counterIcon = new Icon(VaadinIcon.MALE);
    counterIcon.setColor("blue");
    counterLayout.add(counterIcon, counterLabel, counter);

    userInfo.add(ownerImageName, separator2,
        creationDate,
        phoneNumber,
        email,
        resSeparator,
        counterLayout);

    // reservation button
    Button cancelReservationButton = new Button("CANCEL RESERVATION");
    Button reservationButton = new Button("GET INTO QUEUE FOR THIS OFFER");

    reservationButton.addClassNames("w-full");
    reservationButton.addClickListener(event -> {
      if (maybeUser.isPresent()) {
        User currentUser = maybeUser.get();
        userInfo.remove(reservationButton);
        userInfo.add(cancelReservationButton);
        reservationService.saveReservation(offer, currentUser);
        reservationService.getOfferReservationForUser(offer, currentUser);
      }
    });

    cancelReservationButton.addClassNames("w-full");
    cancelReservationButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    cancelReservationButton.addClickListener(event -> {
      User currentUser = maybeUser.get();
      userInfo.remove(cancelReservationButton);
      userInfo.add(reservationButton);
      reservationService.deleteReservation(offer, currentUser);
    });

    if (maybeUser.isPresent()) {
      User currentUser = maybeUser.get();
      Optional<Role> userRole = currentUser.getRoles().stream()
          .filter(role -> role.equals(Role.USER))
          .findFirst();

      if (userRole.isPresent()) {
        reservationButton.setText("LOG IN AS A STUDENT");
        reservationButton.setEnabled(false);
        cancelReservationButton.setText("LOG IN AS A STUDENT");
        cancelReservationButton.setEnabled(false);
      }
    }

    if (maybeUser.isPresent()) {
      User currentUser = maybeUser.get();
      boolean alreadyReservedByUser = reservationService
          .isAlreadyReservedByUser(offer, currentUser);
      if (alreadyReservedByUser) {
        userInfo.remove(reservationButton);
        userInfo.add(cancelReservationButton);
      } else {
        userInfo.remove(cancelReservationButton);
        userInfo.add(reservationButton);
      }
    }

    if (offer.getOfferState().equals(OfferState.CLOSED)) {
      reservationButton.setText("OFFER CLOSED");
      reservationButton.setEnabled(false);
    }

    if (offer.getOfferState().equals(OfferState.RENTED_OUT)) {
      reservationButton.setText("OFFER RENTED OUT");
      reservationButton.setEnabled(false);
    }

    if (maybeUser.isEmpty()) {
      Button navigateToSignUp = new Button("LOGIN TO JOIN THE QUEUE");
      navigateToSignUp.setWidthFull();
      navigateToSignUp.addThemeVariants(ButtonVariant.LUMO_ERROR);
      navigateToSignUp.addClickListener(event -> UI.getCurrent().getPage().setLocation("/login"));
      userInfo.remove(reservationButton);
      userInfo.add(navigateToSignUp);
    }

    // Offer details
    VerticalLayout offerDetails = new VerticalLayout();
    offerDetails
        .addClassNames("flex-grow", "mx-auto", "p-0");

    H2 detailsTitle = new H2("Details");
    detailsTitle.addClassNames("mt-m", "mb-0");
    Hr detailsSeparator = new Hr();
    detailsSeparator.addClassNames("bg-primary", "flex-grow");
    detailsSeparator.setMaxWidth("70%");

    FormLayout detailsLayout = new FormLayout();
    detailsLayout.setMaxWidth("70%");

    // offerTypeSelect
    HorizontalLayout offerTypeLayout = new HorizontalLayout();
    Span offerTypeLabel = new Span("Offer type:");
    Span offerType = new Span(offer.getOfferTypeSelect().getOfferType());
    Icon offerTypeIcon = new Icon(VaadinIcon.INFO_CIRCLE_O);
    offerTypeLayout.add(offerTypeIcon, offerTypeLabel, offerType);
    cssForFields(offerTypeLayout, offerTypeLabel, offerType, offerTypeIcon);

    // pricePerMonth
    HorizontalLayout pricePerMonthLayout = new HorizontalLayout();
    Span priceLabel = new Span("Monthly:");
    Span pricePerMonth = new Span(offer.getPricePerMonth().toString() + " PLN");
    Icon pricePerMonthIcon = new Icon(VaadinIcon.MONEY);
    pricePerMonthLayout.add(pricePerMonthIcon, priceLabel, pricePerMonth);
    cssForFields(pricePerMonthLayout, priceLabel, pricePerMonth, pricePerMonthIcon);

    // rent
    HorizontalLayout rentLayout = new HorizontalLayout();
    Span rentLabel = new Span("Rent:");
    Span rent = new Span(offer.getRent().toString() + " PLN");
    Icon rentIcon = new Icon(VaadinIcon.MONEY_WITHDRAW);
    rentLayout.add(rentIcon, rentLabel, rent);
    cssForFields(rentLayout, rentLabel, rent, rentIcon);

    // deposit
    HorizontalLayout depositLayout = new HorizontalLayout();
    Span depositLabel = new Span("Deposit:");
    Span deposit = new Span(offer.getDeposit().toString() + " PLN");
    Icon depositIcon = new Icon(VaadinIcon.MONEY_DEPOSIT);
    depositLayout.add(depositIcon, depositLabel, deposit);
    cssForFields(depositLayout, depositLabel, deposit, depositIcon);

    // livingArea
    HorizontalLayout livingAreaLayout = new HorizontalLayout();
    Span livingAreaLabel = new Span("Living area:");
    Span livingArea = new Span(offer.getLivingArea().toString() + " m²");
    Icon livingAreaIcon = new Icon(VaadinIcon.WORKPLACE);
    livingAreaLayout.add(livingAreaIcon, livingAreaLabel, livingArea);
    cssForFields(livingAreaLayout, livingAreaLabel, livingArea, livingAreaIcon);

    // numberOfRooms
    HorizontalLayout numberOfRoomsLayout = new HorizontalLayout();
    Span numberOfRoomsLabel = new Span("Rooms:");
    Span numberOfRooms = new Span(offer.getNumberOfRooms().toString().substring(0, 1));
    Icon numberOfRoomsIcon = new Icon(VaadinIcon.GRID_BIG_O);
    numberOfRoomsLayout.add(numberOfRoomsIcon, numberOfRoomsLabel, numberOfRooms);
    cssForFields(numberOfRoomsLayout, numberOfRoomsLabel, numberOfRooms, numberOfRoomsIcon);

    // typeOfRoom
    HorizontalLayout typeOfRoomLayout = new HorizontalLayout();
    Span typeOfRoomLabel = new Span("Room type:");
    Span typeOfRoom = new Span(offer.getTypeOfRoom());
    Icon typeOfRoomIcon = new Icon(VaadinIcon.DOT_CIRCLE);
    typeOfRoomLayout.add(typeOfRoomIcon, typeOfRoomLabel, typeOfRoom);
    cssForFields(typeOfRoomLayout, typeOfRoomLabel, typeOfRoom, typeOfRoomIcon);

    offerDetails.add(detailsTitle, detailsSeparator);

    detailsLayout.add(offerTypeLayout, livingAreaLayout,
        pricePerMonthLayout, rentLayout,
        depositLayout, numberOfRoomsLayout);

    if (offer.getOfferTypeSelect().equals(OfferType.Room)) {
      detailsLayout.add(typeOfRoomLayout);
    }

    VerticalLayout descriptionLayout = new VerticalLayout();
    descriptionLayout.addClassNames("flex-grow", "mx-auto", "p-0");

    H2 descriptionTitle = new H2("Description");
    descriptionTitle.addClassNames("mt-m", "mb-0");

    Hr descriptionSeparator = new Hr();
    descriptionSeparator.addClassNames("bg-primary", "flex-grow");
    descriptionSeparator.setMaxWidth("70%");

    Paragraph description = new Paragraph();
    description.addClassNames("mt-m", "mb-0", "overflow-ellipsis", "font-medium");
    description.setMaxWidth("70%");
    description.setText(offer.getDescription());

    descriptionLayout.add(descriptionTitle, descriptionSeparator, description);

    horizontalLayout.add(imagesView, userInfo);

    add(mainTitle,
        offerLocationLayout,
        separator,
        horizontalLayout,
        offerDetails,
        detailsLayout,
        descriptionLayout);
  }

  private void navigateToOwnerProfile(User owner) {
    String userID = owner.getId().toString();
    String url = RouteConfiguration.forSessionScope()
        .getUrl(UserProfileView.class, new RouteParameters("userID", userID));
    UI.getCurrent().getPage().setLocation(url);
  }

  private void cssForFields(HorizontalLayout layout, Span label, Span value, Icon icon) {
    layout.addClassNames("text-l", "icon-l", "mb-m");
    label.addClassNames("pl-xs", "font-bold");
    value.addClassNames("m-0", "pl-m", "font-medium");
    icon.setColor("blue");
  }
}
