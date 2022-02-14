package com.example.application.views.admin.usermanagement;

import com.example.application.data.entity.User;
import com.example.application.data.enums.Role;
import com.example.application.data.service.UserService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import java.util.Set;

public class UserEditForm extends FormLayout {

  private UserService userService;
  private User user;

  private TextField username = new TextField("Username");
  private TextField email = new TextField("Email");
  private TextField phoneNumber = new TextField("Phone number");
  private TextField firstName = new TextField("First name");
  private TextField lastName = new TextField("Last name");
  private DatePicker dateOfBirth = new DatePicker("Date of birth");
  private TextField city = new TextField("City");
  private ComboBox<Set<Role>> roles = new ComboBox<>("User role");

  Binder<User> binder = new BeanValidationBinder<>(User.class);

  Button save = new Button("Save");
  Button delete = new Button("Delete");
  Button close = new Button("Cancel");

  public UserEditForm(UserService userService, Set<Role> roleList){

    binder.bindInstanceFields(this);

    roles.setItems(roleList);

    add(
        username,
        email,
        phoneNumber,
        firstName,
        lastName,
        dateOfBirth,
        city,
        roles,
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
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, user)));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));

    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, delete, close);
  }

  public void setUser(User user) {
    this.user = user;
    binder.readBean(user);
  }

  private void validateAndSave() {
    try {
      binder.writeBean(user);
      fireEvent(new SaveEvent(this, user));
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }

  // Events
  public static abstract class UserEditFormEvent extends ComponentEvent<UserEditForm> {
    private User user;

    protected UserEditFormEvent(UserEditForm source, User user) {
      super(source, false);
      this.user = user;
    }

    public User getUser() {
      return user;
    }
  }

  public static class SaveEvent extends UserEditFormEvent {
    SaveEvent(UserEditForm source, User user) {
      super(source, user);
    }
  }

  public static class DeleteEvent extends UserEditFormEvent {
    DeleteEvent(UserEditForm source, User user) {
      super(source, user);
    }

  }

  public static class CloseEvent extends UserEditFormEvent {
    CloseEvent(UserEditForm source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }


}
