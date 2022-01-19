package com.example.application.views.mainview;


import com.example.application.data.entity.Offer;
import com.example.application.data.entity.OfferImage;
import com.example.application.data.entity.User;
import com.example.application.data.service.OfferService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class SingleOffer extends VerticalLayout {

  private final OfferService offerService;
  private final HorizontalLayout horizontalLayout;
  private Image image;

  public SingleOffer(OfferService offerService, Integer offerID) {
    this.offerService = offerService;
    addClassNames("flex", "flex-grow", "max-w-screen-xl", "mx-auto", "pb-l");

    Offer offer = offerService.getOfferById(offerID);
    List<OfferImage> offerImages = offerService.getOfferImages(offer);
    User owner = offer.getUser();

    H2 mainTitle = new H2();
    String offerTitle = offer.getOfferTitle();
    mainTitle.setText(offerTitle);
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

      for (OfferImage img : offerImages){
        String imageName = img.getImageName();
        byte[] imageBytes = img.getImage();
        image = new Image();
        image.setWidth("250px");
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
          dialog.setMaxWidth("1024px");
          dialog.setMaxHeight("768px");

          if(dialog.isOpened()){
            bigImage.setMaxWidth("1024px");
            bigImage.setMaxHeight("768px");
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

    Hr separator2 = new Hr();
    separator2.addClassNames("bg-primary", "flex-grow-0", "max-w-full");

    if (owner.getProfilePictureUrl() == null || owner.getProfilePictureUrl().length <= 0) {
      Avatar avatar = new Avatar(owner.getUsername());
      avatar.getStyle().set("border-radius", "12px");
      avatar.setWidth("65px");
      avatar.setHeight("65px");

      ownerImageName.add(avatar, name);
    } else {
      userImage.getElement().setAttribute("src",
          new StreamResource(" ",
              () -> new ByteArrayInputStream(owner.getProfilePictureUrl())));

      userImage.setWidth("65px");
      userImage.setHeight("65px");

      ownerImageName.add(userImage, name);
    }

    // creation date
    String createdAt = offer.getCreatedAt().truncatedTo(ChronoUnit.SECONDS).toString().replaceAll("[TZ]", " ").substring(0,11);
    Span creationDate = new Span("Creation date:   " + createdAt);
    creationDate.addClassNames("font-medium");

    // phone number
    Span phoneNumber = new Span("Phone:   " + owner.getPhoneNumber());
    phoneNumber.addClassNames("font-medium");

    // email
    Span email = new Span("E-mail:   " + owner.getEmail());
    email.addClassNames("font-medium");

    // reservation button
    Button reservationButton = new Button("GET INTO QUEUE FOR THIS OFFER");
    reservationButton.addClassNames("w-full");



    userInfo.add(ownerImageName, separator2,
        creationDate,
        phoneNumber,
        email,
        reservationButton);

    horizontalLayout.add(imagesView, userInfo);

    add(mainTitle,
        separator,
        horizontalLayout);
  }
}
