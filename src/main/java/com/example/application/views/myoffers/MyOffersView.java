package com.example.application.views.myoffers;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
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

  //Basic info fields
  RadioButtonGroup<String> radioGroup;
  private TextField offerTitle;
  private NumberField pricePerMonth;
  private NumberField deposit;
  private NumberField livingArea;
  private NumberField rooms;


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

    mainSection.add(mainTitle, paragraph, separator, createBasicInfoForm());
    return mainSection;
  }

  private FormLayout createBasicInfoForm() {
    FormLayout layout = new FormLayout();

    Div plnSuffix = new Div();
    plnSuffix.setText("PLN");

    Div squareMeter = new Div();
    squareMeter.setText("m²");

    radioGroup = new RadioButtonGroup<>();
    radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
    radioGroup.setLabel("Choose type of an offer:");
    radioGroup.setItems("Rent an apartment", "Rent a room");

    offerTitle = new TextField("Offer title");

    pricePerMonth = new NumberField("Price per month");
    pricePerMonth.setSuffixComponent(plnSuffix);
    pricePerMonth.setMin(0);

    deposit = new NumberField("Deposit");
    deposit.setSuffixComponent(plnSuffix);
    deposit.setMin(0);

    livingArea = new NumberField("Living area");
    livingArea.setSuffixComponent(squareMeter);
    livingArea.setMin(1);

    rooms = new NumberField("Number of rooms");

    layout.setColspan(offerTitle, 3);
    layout.setResponsiveSteps(
        new ResponsiveStep("0", 1),
        new ResponsiveStep("500px", 3)
    );


    layout.add(radioGroup, offerTitle, pricePerMonth, deposit);
    return layout;
  }

}