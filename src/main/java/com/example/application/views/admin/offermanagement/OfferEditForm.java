package com.example.application.views.admin.offermanagement;

import com.example.application.data.entity.Offer;
import com.example.application.data.entity.OfferImage;
import com.example.application.data.enums.OfferState;
import com.example.application.data.enums.OfferType;
import com.example.application.data.service.OfferService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import java.util.List;

public class OfferEditForm extends FormLayout {

  private OfferService offerService;
  private Offer offer;

  private TextField offerTitle = new TextField("Offer title");
  private NumberField pricePerMonth = new NumberField("Price per month");
  private NumberField rent = new NumberField("Rent (additional)");
  private NumberField deposit = new NumberField("Deposit");
  private NumberField livingArea = new NumberField("Living area");
  private NumberField numberOfRooms = new NumberField("Number of rooms in apartment");
  private Select<String> typeOfRoom = new Select<>("SINGLE", "DOUBLE", "TRIPLE");
  private TextArea description = new TextArea("Description");
  private ComboBox<OfferType> offerTypeSelect = new ComboBox<>("Offer type");
  private ComboBox<OfferState> offerState = new ComboBox<>("Offer state");

  Binder<Offer> binder = new BeanValidationBinder<>(Offer.class);

  Button save = new Button("Save");
  Button delete = new Button("Delete");
  Button close = new Button("Cancel");

  public OfferEditForm(OfferService offerService, List<OfferType> offerTypes, List<OfferState> offerStates){

    binder.bindInstanceFields(this);

    offerTypeSelect.setItems(offerTypes);
    offerTypeSelect.setItemLabelGenerator(OfferType::getOfferType);

    offerState.setItems(offerStates);
    offerState.setItemLabelGenerator(OfferState::getOfferState);

    typeOfRoom.setLabel("Type of room");

    add(
        offerTitle,
        pricePerMonth,
        rent,
        deposit,
        livingArea,
        numberOfRooms,
        typeOfRoom,
        description,
        offerTypeSelect,
        offerState,
        createButtonsLayout(offerService)
    );
  }

  private HorizontalLayout createButtonsLayout(OfferService offerService) {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(event -> {
      if (offerService.offerHasImage(offer)) {
        List<OfferImage> offerImages = offerService.getOfferImages(offer);
        for (OfferImage img : offerImages) {
          offerService.deleteOfferImageById(img.getId());
        }
      }
      fireEvent(new DeleteEvent(this, offer));
    });
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));


    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, delete, close);
  }

  public void setOffer(Offer offer) {
    this.offer = offer;
    binder.readBean(offer);
  }

  private void validateAndSave() {
    try {
      binder.writeBean(offer);
      fireEvent(new SaveEvent(this, offer));
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // Events
  public static abstract class OfferEditFormEvent extends ComponentEvent<OfferEditForm> {
    private Offer offer;

    protected OfferEditFormEvent(OfferEditForm source, Offer offer) {
      super(source, false);
      this.offer = offer;
    }

    public Offer getOffer() {
      return offer;
    }
  }

  public static class SaveEvent extends OfferEditFormEvent {
    SaveEvent(OfferEditForm source, Offer offer) {
      super(source, offer);
    }
  }

  public static class DeleteEvent extends OfferEditFormEvent {
    DeleteEvent(OfferEditForm source, Offer offer) {
      super(source, offer);
    }

  }

  public static class CloseEvent extends OfferEditFormEvent {
    CloseEvent(OfferEditForm source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

}
