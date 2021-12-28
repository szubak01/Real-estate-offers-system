package com.example.application.views.myoffers;

import com.example.application.data.entity.Location;
import com.example.application.data.entity.Offer;
import com.example.application.data.service.LocationService;
import com.example.application.data.service.OfferService;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class OfferFormValidator {

  private final MyOffersView myOffersView;
  private final OfferService offerService;

  BeanValidationBinder<Offer> offerBinder = new BeanValidationBinder<>(Offer.class);
  BeanValidationBinder<Location> locationBinder = new BeanValidationBinder<>(Location.class);

  public OfferFormValidator(MyOffersView myOffersView,
      OfferService offerService){
    this.myOffersView = myOffersView;
    this.offerService = offerService;
  }

  public void formValidation(){
    offerBinder.bindInstanceFields(myOffersView);
    offerBinder.setStatusLabel(myOffersView.getErrorValidationMessage());

    locationBinder.bindInstanceFields(myOffersView);
    locationBinder.setStatusLabel(myOffersView.getErrorValidationMessage());

    myOffersView.getAddOfferButton().addClickListener(event -> {
      try {
        Offer offer = new Offer();
        offerBinder.writeBean(offer);

        Location location = new Location();
        locationBinder.writeBean(location);

        offerService.saveOffer(myOffersView);
        showSuccessNotification();
      } catch (ValidationException e) {
        e.printStackTrace();
      }
    });
  }

  private void showSuccessNotification() {

    Notification notification = Notification.show(
        "Offer added successfully.",
        3500,
        Notification.Position.MIDDLE
    );
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    UI.getCurrent().getPage().reload();
  }

}
