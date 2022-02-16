package com.example.application.views.admin.ratemanagement;

import com.example.application.data.entity.Rate;
import com.example.application.data.entity.User;
import com.example.application.data.service.RateService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import java.util.List;

public class RateEditForm extends FormLayout {

  private RateService rateService;
  private Rate rate;

  private IntegerField rateNumber = new IntegerField("Rate number");
  private TextField comment = new TextField("Comment");
  private IntegerField ratedBy = new IntegerField("Rated by (ID)");
  private IntegerField personRated = new IntegerField("Person rated (ID)");

  Binder<Rate> binder = new BeanValidationBinder<>(Rate.class);

  Button save = new Button("Save");
  Button delete = new Button("Delete");
  Button close = new Button("Cancel");

  public RateEditForm(RateService rateService, List<User> users){

    binder.bindInstanceFields(this);

    add(
        rateNumber,
        comment,
        ratedBy,
        personRated,
        createButtonsLayout()
    );
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(event -> fireEvent(new RateEditForm.DeleteEvent(this, rate)));
    close.addClickListener(event -> fireEvent(new RateEditForm.CloseEvent(this)));

    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, delete, close);
  }

  public void setRate(Rate rate) {
    this.rate = rate;
    binder.readBean(rate);
  }

  private void validateAndSave() {
    try {
      binder.writeBean(rate);
      fireEvent(new SaveEvent(this, rate));
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // Events
  public static abstract class RateEditFormEvent extends ComponentEvent<RateEditForm> {
    private Rate rate;

    protected RateEditFormEvent(RateEditForm source, Rate rate) {
      super(source, false);
      this.rate = rate;
    }

    public Rate getRate() {
      return rate;
    }
  }

  public static class SaveEvent extends RateEditFormEvent {
    SaveEvent(RateEditForm source, Rate rate) {
      super(source, rate);
    }
  }

  public static class DeleteEvent extends RateEditFormEvent {
    DeleteEvent(RateEditForm source, Rate rate) {
      super(source, rate);
    }

  }

  public static class CloseEvent extends RateEditFormEvent {
    CloseEvent(RateEditForm source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }


}
