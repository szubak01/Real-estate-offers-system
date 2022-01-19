package com.example.application.views.mainview;


import com.example.application.data.entity.Offer;
import com.example.application.data.service.OfferService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Span;
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
    card.addClassNames("rounded-l", "p-0", "border", "border-primary-50", "overflow-hidden", "w-full", "overflow-ellipsis"); // "bg-contrast-5",
    card.getStyle().set("border-width", "2px");
    card.setSizeFull();

    VerticalLayout info = new VerticalLayout();
    info.addClassNames("font-medium", "text-primary");

    image = new Image();
    image.addClassNames("w-full", "rounded-l", "p-xd", "box-border");
    image.setMaxWidth("30%");

    mainTitle = new H3();
    mainTitle.addClassNames("mt-m");
    location = new Span();
    pricePerMonth = new Span();
    offerType = new Span();
    livingArea = new Span();

    VerticalLayout buttonLayout = new VerticalLayout();
    checkButton = new Button("CHECK OFFER");
    checkButton.addClassNames("w-full", "pr-l", "justify-center");
    checkButton.addClickListener(event -> {
      navigateToSingleOffer(offer);
    });

    buttonLayout.setWidth("20%");
    buttonLayout.add(checkButton);


    mainTitle.setText(offer.getOfferTitle());
    location.setText(offer.getLocation().getCity() + ", " + offer.getLocation().getVoivodeship());
    pricePerMonth.setText(offer.getPricePerMonth().toString() + " PLN" + "/month");
    offerType.setText(offer.getOfferTypeSelect().getOfferType());
    livingArea.setText(offer.getLivingArea().toString() + " m²");
    if (offerService.offerHasImage(offer)) {
      image.getElement().setAttribute("src",
          new StreamResource(" ",
              () -> new ByteArrayInputStream(offerService.getOfferImages(offer).get(0).getImage())));
    } else {
      image.setSrc("images/default_image.png");
    }
    info.add(mainTitle, location, pricePerMonth, offerType, livingArea);
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
