package com.example.application.views.mainview;


import com.example.application.data.entity.Offer;
import com.example.application.data.enums.OfferState;
import com.example.application.data.service.OfferService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.util.List;
import lombok.Getter;

@Getter
public class CardLayout extends OrderedList {

  private final OfferService offerService;

  private HorizontalLayout card;
  private final List<Offer> offers;

  // card details
  private Image image;
  private Hr separator;
  private H3 mainTitle;
  private Span location;
  private Span pricePerMonth;
  private Span offerType;
  private Span livingArea;
  private Button checkButton;


  public CardLayout(OfferService offerService) {
    this.offerService = offerService;

    addClassNames("grid", "grid-cols-1", "gap-l", "box-border", "px-l");

    offers = offerService.getAllOffers();

    for (Offer offer : offers){
      add(createCard(offer));
    }
  }

  private HorizontalLayout createCard(Offer offer) {

    card = new HorizontalLayout();
    card.addClassNames("rounded-l", "p-0", "border", "border-primary", "overflow-hidden", "w-full", "overflow-ellipsis");
    card.getStyle().set("border-width", "2px");
    card.setSizeFull();

    VerticalLayout info = new VerticalLayout();
    info.addClassNames("font-medium");

    image = new Image();
    image.addClassNames("w-full", "rounded-l", "box-border", "m-0");
    image.setMaxWidth("40%");

    //mainTitle
    mainTitle = new H3(offer.getOfferTitle());
    mainTitle.addClassNames("mt-m", "overflow-ellipsis");

    // location
    HorizontalLayout offerLocationLayout = new HorizontalLayout();
    Icon locationIcon = new Icon(VaadinIcon.LOCATION_ARROW_CIRCLE_O);
    locationIcon.setColor("blue");
    location = new Span(offer.getLocation().getCity() + ", " + offer.getLocation().getVoivodeship());
    offerLocationLayout.add(locationIcon, location);

    // price
    HorizontalLayout pricePerMonthLayout = new HorizontalLayout();
    pricePerMonth = new Span(offer.getPricePerMonth().toString() + " PLN" + "/month");
    pricePerMonth.addClassNames("m-0", "pl-m", "font-medium");

    Icon pricePerMonthIcon = new Icon(VaadinIcon.MONEY);
    pricePerMonthIcon.setColor("blue");
    pricePerMonthLayout.add(pricePerMonthIcon, pricePerMonth);

    // offer type
    HorizontalLayout offerTypeLayout = new HorizontalLayout();
    offerType = new Span(offer.getOfferTypeSelect().getOfferType());
    offerType.addClassNames("m-0", "pl-m", "font-medium");

    Icon offerTypeIcon = new Icon(VaadinIcon.INFO_CIRCLE_O);
    offerTypeIcon.setColor("blue");
    offerTypeLayout.add(offerTypeIcon, offerType);

    // living area
    HorizontalLayout livingAreaLayout = new HorizontalLayout();
    livingArea = new Span(offer.getLivingArea().toString() + " m²");
    livingArea.addClassNames("m-0", "pl-m", "font-medium");

    Icon livingAreaIcon = new Icon(VaadinIcon.WORKPLACE);
    livingAreaIcon.setColor("blue");
    livingAreaLayout.add(livingAreaIcon, livingArea);

    VerticalLayout buttonLayout = new VerticalLayout();
    buttonLayout.addClassNames("m-0");

    checkButton = new Button("CHECK OFFER");
    checkButton.addClassNames("w-full", "pr-l", "border", "border-primary");
    checkButton.addClickListener(event -> navigateToSingleOffer(offer));

    // badge
    Span open = new Span("OPEN");
    open.getElement().getThemeList().add("badge success primary");
    open.addClassNames("w-full", "pr-l");

    Span closed = new Span("CLOSED");
    closed.getElement().getThemeList().add("badge error primary");
    closed.addClassNames("w-full", "pr-l");

    Span rentedOut = new Span("RENTED OUT");
    rentedOut.getElement().getThemeList().add("badge contrast primary");
    rentedOut.addClassNames("w-full", "pr-l");

    buttonLayout.setWidth("20%");
    buttonLayout.add(checkButton);
    if(offer.getOfferState().equals(OfferState.OPEN)){
      buttonLayout.add(open);
    } else if (offer.getOfferState().equals(OfferState.CLOSED)){
      buttonLayout.add(rentedOut);
    } else {
      buttonLayout.add(rentedOut);

    }

    if (offerService.offerHasImage(offer)) {
      image.getElement().setAttribute("src",
          new StreamResource(" ",
              () -> new ByteArrayInputStream(offerService.getOfferImages(offer).get(0).getImage())));
    } else {
      image.setSrc("images/default_image.png");
    }

    info.add(mainTitle, offerLocationLayout, pricePerMonthLayout, offerTypeLayout, livingAreaLayout);
    card.add(image, info, buttonLayout);

    return card;
  }

  public void navigateToSingleOffer(Offer offer){
    String offerID = offer.getId().toString();
    String url = RouteConfiguration.forSessionScope()
        .getUrl(SingleOfferView.class, new RouteParameters("offerID", offerID));
    UI.getCurrent().getPage().setLocation(url);
  }

}
