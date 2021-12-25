package com.example.application.views.myoffers;

import com.example.application.data.enums.OfferType;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.stream.Stream;
import javax.annotation.security.RolesAllowed;
import lombok.Getter;


@PageTitle("My offers")
@Route(value = "myOffers", layout = MainLayout.class)
@RolesAllowed("user")
@Getter
public class MyOffersView extends Div {

  private final Tab listOfOffersTab;
  private final Tab addNewOfferTab;
  private final VerticalLayout content;
  private VerticalLayout listOfOffersContent;
  private VerticalLayout addNewOfferContent;

  // Basic info fields
  private Select<OfferType> offerTypeSelect;
  private TextField offerTitle;
  private NumberField pricePerMonth;
  private NumberField rent;
  private NumberField deposit;
  private NumberField livingArea;
  private NumberField numberOfRooms;
  private Select<String> typeOfRoom;
  private RichTextEditor description;

  // Location fields
  private TextField city;
  private TextField voivodeship;
  private TextField streetAndNumber;
  private TextField postalCode;

  // Upload area
  MultiFileMemoryBuffer multiFileMemoryBuffer;
  Upload multiFileUpload;

  public MyOffersView() {

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

    add(tabs, content);
  }

  private void setContent(Tab tab) {
    content.removeAll();

    if (tab.equals(listOfOffersTab)) {
      content.add(createListOfOffersContent());
    } else {
      content.add(createAddNewOfferContent());
    }
  }

  private VerticalLayout createListOfOffersContent() {
    listOfOffersContent = new VerticalLayout();

    listOfOffersContent.add();

    return listOfOffersContent;
  }

  private VerticalLayout createAddNewOfferContent() {
    addNewOfferContent = new VerticalLayout();
    addNewOfferContent.addClassNames(
        "grid",  // display
        "gap-l", // space between items in a flexbox or grid layout
        //"items-start",
        "justify-center",
        "max-w-screen-lg", // sets the maximum width of an element to 1024px
        "mx-auto", // horizontal margin
        "pb-l",  // bottom margin
        "px-l"  // horizontal padding
    );


    addNewOfferContent.add(createMainSection());
    return addNewOfferContent;
  }

  private Component createMainSection() {
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

    description = new RichTextEditor();
    description.addClassNames("flex-grow", "pl-0", "m-0", "mt-s");

    H2 uploadTitle = new H2("Upload images");
    locationTitle.addClassNames("mt-l");

    Hr separator4 = new Hr();
    separator4.addClassNames("bg-primary", "flex-grow", "max-w-full");


    mainSection.add(
        mainTitle, paragraph, separator,
        createBasicInfoForm(),
        locationTitle, separator2,
        createLocationForm(),
        descriptionTitle, separator3,
        description,
        uploadTitle, separator4,
        createImageUploadArea()
        );
    return mainSection;
  }

  private Component createBasicInfoForm() {
    FormLayout layout = new FormLayout();

    Div plnSuffix = new Div();
    plnSuffix.setText("PLN");

    Div plnSuffix2 = new Div();
    plnSuffix2.setText("PLN");

    Div plnSuffix3 = new Div();
    plnSuffix3.setText("PLN");

    Div squareMeter = new Div();
    squareMeter.setText("m²");

    offerTypeSelect = new Select<>();
    offerTypeSelect.setLabel("Select type of an offer");
    offerTypeSelect.setItems(OfferType.values());
    offerTypeSelect.setValue(OfferType.Apartment);
    offerTypeSelect.addValueChangeListener(
        (ValueChangeListener<ComponentValueChangeEvent<Select<OfferType>, OfferType>>) valueChange -> {
          if (valueChange.getValue().equals(OfferType.Room)) {
            layout.add(typeOfRoom);
          } else {
            layout.remove(typeOfRoom);
          }
        });

    offerTitle = new TextField("Offer title");

    pricePerMonth = new NumberField("Price per month");
    pricePerMonth.setSuffixComponent(plnSuffix2);
    pricePerMonth.setMin(0);

    rent = new NumberField("Rent (additional)");
    rent.setSuffixComponent(plnSuffix3);
    rent.setMin(0);

    deposit = new NumberField("Deposit");
    deposit.setSuffixComponent(plnSuffix);
    deposit.setMin(0);

    livingArea = new NumberField("Living area");
    livingArea.setSuffixComponent(squareMeter);
    livingArea.setMin(1);

    numberOfRooms = new NumberField("Number of rooms in apartment");
    numberOfRooms.setMin(1);

    typeOfRoom = new Select<>();
    typeOfRoom.setLabel("Type of room");
    typeOfRoom.setItems("Single", "Double", "Triple");

    setRequiredIndicatorVisible(
        offerTypeSelect, offerTitle, pricePerMonth, rent, deposit,
        livingArea, numberOfRooms, typeOfRoom
    );

    layout.setColspan(offerTitle, 3);
    layout.setResponsiveSteps(
        new ResponsiveStep("0", 1),
        new ResponsiveStep("500px", 3)
    );

    layout.add(
        offerTypeSelect,
        offerTitle,
        pricePerMonth, rent, deposit,
        livingArea, numberOfRooms
    );

    return layout;
  }

  private Component createLocationForm() {
    FormLayout layout = new FormLayout();
    layout.addClassNames("pt-s", "flex-grow", "max-w-full");

    city = new TextField("City");
    voivodeship = new TextField("Voivodeship");
    streetAndNumber = new TextField("Street & number");
    postalCode = new TextField("Postal code");


    layout.setResponsiveSteps(
        new ResponsiveStep("0", 1),
        new ResponsiveStep("500px", 4)
    );

    layout.add(
        city, voivodeship, streetAndNumber, postalCode
    );
    return layout;
  }

  private Component createImageUploadArea(){
    FormLayout layout = new FormLayout();

    multiFileMemoryBuffer = new MultiFileMemoryBuffer();
    multiFileUpload = new Upload(multiFileMemoryBuffer);
    multiFileUpload.addClassNames("box-border");
    multiFileUpload.setAcceptedFileTypes("image/*");
    multiFileUpload.addFileRejectedListener(event -> {
          String errorMessage = event.getErrorMessage();

          Notification notification = Notification.show(
              errorMessage,
              3500,
              Notification.Position.MIDDLE
          );
          notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });


    Paragraph hint = new Paragraph("Accepted file formats: (.png), (.jpg/.jpeg)");
    hint.getStyle().set("color", "var(--lumo-secondary-text-color)");
    hint.getStyle().set("margin-top", "15px");


    layout.setColspan(multiFileUpload, 4);
    layout.setColspan(hint, 4);
    layout.setResponsiveSteps(
        new ResponsiveStep("0", 1),
        new ResponsiveStep("500px", 4)
    );

    layout.add(hint, multiFileUpload);
    return layout;
  }

  private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
    Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
  }

}
