package com.example.application.views.mainview;

import com.example.application.data.entity.Offer;
import com.example.application.data.enums.OfferState;
import com.example.application.data.enums.OfferType;
import com.example.application.data.service.OfferService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.io.ByteArrayInputStream;
import java.util.List;
import lombok.Getter;

@PageTitle("Rental offers")
@Route(value = "rentaloffers", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
@Getter
public class MainView extends VerticalLayout {

  private final OfferService offerService;
  private OrderedList orderedList = new OrderedList();

  private HorizontalLayout card;
  private List<Offer> offers;

  // card details
  private Image image;
  private H3 mainTitle;
  private Span location;
  private Span pricePerMonth;
  private Span offerType;
  private Span livingArea;
  private Button checkButton;

  private final VerticalLayout header = new VerticalLayout();

  // search layout
  private final FormLayout searchLayout = new FormLayout();
  private final H2 mainTitle2 = new H2("Search from all available offers");
  private final Hr separator = new Hr();
  private final Hr separator2 = new Hr();

  private final TextField filterByCity = new TextField();
  private final ComboBox<OfferType> filterByOfferType = new ComboBox<>();
  private final ComboBox<OfferState> filterByOfferState = new ComboBox<>();
  private final ComboBox<Double> filterByPrice = new ComboBox<>();

  public MainView(OfferService offerService) {
    this.offerService = offerService;
    addClassNames("max-w-screen-xl", "mx-auto", "pb-l", "px-l");
    setSizeFull();
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    orderedList.addClassNames("grid", "grid-cols-1", "gap-l", "box-border", "px-l");

    updateListByCity();

    // filters
    filterByCity.setLabel("City");
    filterByCity.setPlaceholder("Search by city..");
    filterByCity.setValueChangeMode(ValueChangeMode.EAGER);
    filterByCity.addValueChangeListener(e -> updateListByCity());

    filterByOfferType.setPlaceholder("Select..");
    filterByOfferType.setLabel("Offer type");
    filterByOfferType.setItems(OfferType.Room, OfferType.Apartment);
    filterByOfferType.addValueChangeListener(e -> updateListByOfferType());

    filterByOfferState.setPlaceholder("Select..");
    filterByOfferState.setLabel("Offer state");
    filterByOfferState.setItems(OfferState.OPEN, OfferState.CLOSED, OfferState.RENTED_OUT);
    filterByOfferState.addValueChangeListener(e -> updateListByOfferState());

    filterByPrice.setPlaceholder("Less than..");
    filterByPrice.setLabel("Price lower than");
    filterByPrice.setItems(500.0 , 1000.0, 1500.0, 2000.0, 2500.0, 3000.0, 3500.0, 4000.0, 4500.0, 5000.0);
    filterByPrice.addValueChangeListener(e -> updateListByPrice());

    searchLayout.addClassNames("pl-xl", "max-w-screen-lg");
    searchLayout.setResponsiveSteps(
        new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
        new ResponsiveStep("490px", 4, ResponsiveStep.LabelsPosition.TOP)
    );
    searchLayout.setColspan(separator2, 4);
    searchLayout.add(filterByCity, filterByOfferType, filterByOfferState, filterByPrice, separator2);
    header.add(mainTitle2, separator);
    searchLayoutCss();
    add(header, searchLayout, orderedList);
  }

  private void updateListByCity(){
    orderedList.removeAll();
    offers = offerService.getOffersByCity(filterByCity.getValue());
    for (Offer offer : offers){
      orderedList.add(createCard(offer));
    }
  }

  private void updateListByOfferType(){
    orderedList.removeAll();
    offers = offerService.getOffersByType(filterByOfferType.getValue());
    for (Offer offer : offers){
      orderedList.add(createCard(offer));
    }
  }

  private void updateListByOfferState(){
    orderedList.removeAll();
    offers = offerService.getOffersByState(filterByOfferState.getValue());
    for (Offer offer : offers){
      orderedList.add(createCard(offer));
    }
  }

  private void updateListByPrice(){
    orderedList.removeAll();
    offers = offerService.getOffersByPrice(filterByPrice.getValue());
    for (Offer offer : offers){
      orderedList.add(createCard(offer));
    }
  }

  private void searchLayoutCss() {
    searchLayout.addClassNames("justify-between", "box-border");
    mainTitle.addClassNames("mt-s", "justify-center");
    separator.addClassNames("bg-primary", "flex-grow", "max-w-full");
    separator2.addClassNames("bg-primary", "flex-grow", "max-w-full");
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
    mainTitle = new H3("Offer title");
    mainTitle.setText(offer.getOfferTitle());
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

    Span rentFinished = new Span("RENT FINISHED");
    rentFinished.getElement().getThemeList().add("badge contrast primary");
    rentFinished.addClassNames("w-full", "pr-l");

    buttonLayout.setWidth("20%");
    buttonLayout.add(checkButton);
    if(offer.getOfferState().equals(OfferState.OPEN)){
      buttonLayout.add(open);
    } else if (offer.getOfferState().equals(OfferState.CLOSED)){
      buttonLayout.add(rentedOut);
    } else if (offer.getOfferState().equals(OfferState.RENTED_OUT)){
      buttonLayout.add(rentedOut);
    } else {
      buttonLayout.add(rentFinished);
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
