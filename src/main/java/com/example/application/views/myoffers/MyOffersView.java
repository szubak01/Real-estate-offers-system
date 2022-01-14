package com.example.application.views.myoffers;

import com.example.application.data.entity.Images;
import com.example.application.data.entity.Location;
import com.example.application.data.entity.Offer;
import com.example.application.data.enums.OfferType;
import com.example.application.data.service.LocationService;
import com.example.application.data.service.OfferService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.Setter;


@PageTitle("My offers")
@Route(value = "myOffers", layout = MainLayout.class)
@RolesAllowed("user")
@Getter
@Setter
public class MyOffersView extends Div {

  private final OfferService offerService;
  private final LocationService locationService;

  private final Tab listOfOffersTab;
  private final Tab addNewOfferTab;
  private final VerticalLayout content;
  private VerticalLayout listOfOffersContent;
  private VerticalLayout addNewOfferContent;

  private HorizontalLayout card;

  Button deleteButton;
  Button updateButton;
  VerticalLayout leftTab;

  // Update mode layout
  VerticalLayout updateContent;

  // Update Mode Buttons
  private final Button saveChangesButton = new Button("Save changes");
  private final Button cancelUpdateButton = new Button("Cancel");

  // Basic info fields
  private final Select<OfferType> offerTypeSelect = new Select<>();
  private final TextField offerTitle = new TextField("Offer title");
  private final NumberField pricePerMonth = new NumberField("Price per month");
  private final NumberField rent = new NumberField("Rent (additional)");
  private final NumberField deposit = new NumberField("Deposit");
  private final NumberField livingArea = new NumberField("Living area");
  private final NumberField numberOfRooms = new NumberField("Number of rooms in apartment");
  private final Select<String> typeOfRoom = new Select<>();
  private final RichTextEditor description = new RichTextEditor();

  // Location fields
  private final TextField city = new TextField("City");
  private final TextField voivodeship = new TextField("Voivodeship");
  private final TextField streetNumber = new TextField("Street & number");
  private final TextField postalCode = new TextField("Postal code");

  private final Button addOfferButton = new Button("Add offer");
  MultiUploadForm multiUpload;
  private final Span errorValidationMessage = new Span();

  public MyOffersView(OfferService offerService,
      LocationService locationService) {
    this.offerService = offerService;
    this.locationService = locationService;

    OfferFormValidator validator = new OfferFormValidator(this, offerService);
    validator.formValidation();

    listOfOffersTab = new Tab("List of offers");
    addNewOfferTab = new Tab("Add new offer");

    Tabs tabs = new Tabs(listOfOffersTab, addNewOfferTab);
    tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);
    tabs.addSelectedChangeListener(event ->
        setContent(event.getSelectedTab())
    );

    content = new VerticalLayout();
    content.setSpacing(false);
    setContent(tabs.getSelectedTab());

    setRequiredIndicatorVisible(
        offerTypeSelect, offerTitle, pricePerMonth, rent, deposit,
        livingArea, numberOfRooms, typeOfRoom, city, voivodeship,
        streetNumber, postalCode
    );

    add(tabs, content);
  }

  private void setContent(Tab tab) {
    content.removeAll();

    if (tab.equals(listOfOffersTab)) {
      content.add(offersListContent());
    } else {
      content.add(createAddNewOfferContent());
    }
  }

  private VerticalLayout createAddNewOfferContent() {
    addNewOfferContent = new VerticalLayout();
    addNewOfferContent.addClassNames(
        "grid",  // display
        "justify-center",
        "max-w-screen-lg", // sets the maximum width of an element to 1024px
        "mx-auto", // horizontal margin
        "pb-l",  // bottom margin
        "px-l"  // horizontal padding
    );

    Section mainSection = new Section();
    mainSection.addClassNames("flex", "flex-grow", "flex-col");

    H2 mainTitle = new H2("Provide basic information");
    mainTitle.addClassNames("mt-0");

    Paragraph paragraph = new Paragraph("All fields are required unless otherwise noted");
    paragraph.addClassNames("mb-s", "mt-0", "text-secondary");

    Hr separator = new Hr();
    separator.addClassNames("bg-primary", "flex-grow", "max-w-full");

    H2 locationTitle = new H2("Location");
    locationTitle.addClassNames("mt-l");

    Hr separator2 = new Hr();
    separator2.addClassNames("bg-primary", "flex-grow", "max-w-full");

    H2 descriptionTitle = new H2("Description");
    descriptionTitle.addClassNames("mt-l");

    Hr separator3 = new Hr();
    separator3.addClassNames("bg-primary", "flex-grow", "max-w-full");

    description.addClassNames("flex-grow", "pl-0", "m-0", "mt-s");

    H2 uploadTitle = new H2("Upload images");
    locationTitle.addClassNames("mt-l");

    Hr separator4 = new Hr();
    separator4.addClassNames("bg-primary", "flex-grow", "max-w-full");

    addOfferButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addOfferButton.addClassNames("mt-xl");

    multiUpload = new MultiUploadForm();

    //Basic info form
    FormLayout basicInfoLayout = new FormLayout();

    Div plnSuffix = new Div();
    plnSuffix.setText("PLN");

    Div plnSuffix2 = new Div();
    plnSuffix2.setText("PLN");

    Div plnSuffix3 = new Div();
    plnSuffix3.setText("PLN");

    Div squareMeter = new Div();
    squareMeter.setText("m²");

    offerTypeSelect.setLabel("Select type of an offer");
    offerTypeSelect.setItems(OfferType.values());
    offerTypeSelect.setValue(OfferType.Apartment);
    offerTypeSelect.addValueChangeListener(
        (ValueChangeListener<ComponentValueChangeEvent<Select<OfferType>, OfferType>>) valueChange -> {
          try {
            if (valueChange.getValue() == null) {
              offerTypeSelect.setValue(OfferType.Apartment);
            } else if (valueChange.getValue().equals(OfferType.Room)) {
              basicInfoLayout.add(typeOfRoom);
            } else {
              basicInfoLayout.remove(typeOfRoom);
            }
          } catch (IllegalArgumentException exc) {
            UI.getCurrent().getPage().reload();
            exc.printStackTrace();
          }
        });

    pricePerMonth.setSuffixComponent(plnSuffix2);
    pricePerMonth.setMin(0);

    rent.setSuffixComponent(plnSuffix3);
    rent.setMin(0);

    deposit.setSuffixComponent(plnSuffix);
    deposit.setMin(0);

    livingArea.setSuffixComponent(squareMeter);
    livingArea.setMin(1);

    numberOfRooms.setMin(1);

    typeOfRoom.setLabel("Type of room");
    typeOfRoom.setItems("Single", "Double", "Triple");

    basicInfoLayout.setColspan(offerTitle, 3);
    basicInfoLayout.setResponsiveSteps(
        new ResponsiveStep("0", 1),
        new ResponsiveStep("500px", 3)
    );

    basicInfoLayout.add(
        offerTypeSelect,
        offerTitle,
        pricePerMonth, rent, deposit,
        livingArea, numberOfRooms
    );

    // Location form
    FormLayout locationFormLayout = new FormLayout();
    locationFormLayout.addClassNames("pt-s", "flex-grow", "max-w-full");

    locationFormLayout.setResponsiveSteps(
        new ResponsiveStep("0", 1),
        new ResponsiveStep("500px", 4)
    );

    locationFormLayout.add(
        city, voivodeship, streetNumber, postalCode
    );

    addNewOfferContent.add(
        mainTitle, paragraph, separator,
        basicInfoLayout,
        descriptionTitle, separator3,
        description,
        locationTitle, separator2,
        locationFormLayout,
        uploadTitle, separator4,
        multiUpload,
        addOfferButton,
        errorValidationMessage
    );

    return addNewOfferContent;
  }

  private Component offersListContent() {
    leftTab = new VerticalLayout();

    List<Offer> offers = offerService.getOffersOwnedByCurrentUser();

    leftTab.setSizeFull();
    leftTab.setAlignItems(Alignment.CENTER);
    leftTab.setAlignSelf(Alignment.CENTER);
    leftTab.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    leftTab.addClassNames("grid", "gap-s", "justify-center", "mx-auto", "pb-l", "px-l");

    if (offers.isEmpty()) {

      leftTab.add(new H2("You don't have any offers."));
      leftTab.add(new Paragraph("If you decide to add an offer, it will be displayed here."));
      getStyle().set("text-align", "center");

    } else {

      for (Offer offer : offers) {
        leftTab.add(createCard(offer));
      }
    }

    leftTab.add();
    return leftTab;
  }

  private HorizontalLayout createCard(Offer offer) {
    card = new HorizontalLayout();
    card.addClassNames("bg-base", "rounded-l", "p-s", "justify-center", "mr-l", "ml-l", "border", "border-primary");
    card.getStyle().set("border-width", "2px");

    Optional<Images> maybeImage = offer.getImages().stream().findFirst();

    Image image = new Image();
    image.setMaxWidth("15%");
    image.addClassNames("rounded-l");

    if (maybeImage.isPresent()) {

      // todo: retrieve image from database and display it in a card

    } else {
      image.setSrc("images/default_image.png");
    }

    FormLayout description = new FormLayout();
    description.setMaxWidth("70%");
    description.addClassNames("overflow-hidden");

    Span title = new Span(offer.getOfferTitle());
    description.addFormItem(title, "Title:");

    Span offerType = new Span(offer.getOfferTypeSelect().getOfferType());
    description.addFormItem(offerType, "Offer type:");

    Span pricePerMonth = new Span(offer.getPricePerMonth().toString() + " PLN");
    description.addFormItem(pricePerMonth, "Price per month:");

    Span livingArea = new Span(offer.getLivingArea().toString() + " m²");
    description.addFormItem(livingArea, "Living area:");

    String city = offer.getLocation().getCity();
    String voivodeship = offer.getLocation().getVoivodeship();
    String streetNumber = offer.getLocation().getStreetNumber();
    String postalCode = offer.getLocation().getPostalCode();
    Span address = new Span(city + " " + voivodeship + " " + streetNumber + " " + postalCode);
    description.addFormItem(address, "Address:");

    Span createdAt = new Span(offer.getCreatedAt().toString());
    description.addFormItem(createdAt, "Date: ");

    description.setResponsiveSteps(
        new ResponsiveStep("0", 1),
        new ResponsiveStep("500px", 2)
    );

    VerticalLayout buttons = new VerticalLayout();
    buttons.addClassNames("m-l");
    buttons.setMaxWidth("15%");

    deleteButton = new Button("DELETE");
    deleteButton.setWidthFull();
    deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

    updateButton = new Button("UPDATE");
    updateButton.setWidthFull();

    buttons.add(updateButton, deleteButton);

    deleteButton.addClickListener(event -> {
      Integer offerId = offer.getId();
      offerService.deleteOfferById(offerId);
      leftTab.remove(this.card);
      UI.getCurrent().getPage().reload();
    });

    updateButton.addClickListener(event -> {
      switchToUpdateMode(offer);
    });

    card.add(image, description, buttons);
    return card;
  }

  private void switchToUpdateMode(Offer offer) {
    content.removeAll();

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.add(saveChangesButton, cancelUpdateButton);

    saveChangesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveChangesButton.addClickListener(event -> {
      offerService.updateOffer(this, offer);
      UI.getCurrent().getPage().reload();
    });

    cancelUpdateButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    cancelUpdateButton.addClickListener(event -> UI.getCurrent().getPage().reload());

    fillUpdateFields(offer);

    updateContent = createAddNewOfferContent();
    updateContent.remove(addOfferButton);
    updateContent.add(buttons);

    content.add(updateContent);
  }

  private void fillUpdateFields(Offer offer) {

    offerTypeSelect.setValue(offer.getOfferTypeSelect());
    offerTitle.setValue(offer.getOfferTitle());
    pricePerMonth.setValue(offer.getPricePerMonth());
    rent.setValue(offer.getRent());
    deposit.setValue(offer.getDeposit());
    livingArea.setValue(offer.getLivingArea());
    numberOfRooms.setValue(offer.getNumberOfRooms());
    typeOfRoom.setValue(offer.getTypeOfRoom());
    description.setValue(offer.getDescription());

    Location location = offer.getLocation();

    city.setValue(location.getCity());
    voivodeship.setValue(location.getVoivodeship());
    streetNumber.setValue(location.getStreetNumber());
    postalCode.setValue(location.getPostalCode());
  }

  private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
    Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
  }

}
