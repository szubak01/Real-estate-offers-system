package com.example.application.views.profile.myaccount;

import com.example.application.data.entity.User;
import com.example.application.data.service.UserService;
import com.example.application.security.SecurityUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import java.io.IOException;
import java.util.Optional;


public class ProfileEditFormBinder {

  private final ProfileEditForm profileEditForm;
  private final UserService userService;
  private final SecurityUtils securityUtils;

  public ProfileEditFormBinder(
      ProfileEditForm profileEditForm,
      UserService userService,
      SecurityUtils securityUtils) {
    this.profileEditForm = profileEditForm;
    this.userService = userService;
    this.securityUtils = securityUtils;
  }

  public void addBindingAndValidation() {
    User currentUser = securityUtils.getCurrentUser().get();
    BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

    binder.bindInstanceFields(profileEditForm);
    binder.forField(profileEditForm.getUsername()).withValidator(this::usernameValidator)
        .bind("username");
    binder.forField(profileEditForm.getPassword()).withValidator(this::passwordValidator)
        .bind("password");
    binder.setStatusLabel(profileEditForm.getErrorMessageFailed());

    binder.readBean(currentUser);

    profileEditForm.getSaveButton().addClickListener(event -> {
      try {
        binder.writeBean(currentUser);
        userService.updateUser(profileEditForm);
        showUpdateSuccess();
      } catch (ValidationException | IOException e) {
        e.printStackTrace();
      }
    });
  }

  private ValidationResult usernameValidator(String username, ValueContext valueContext) {

    String providedUsername = profileEditForm.getUsername().getValue();
    User user = userService.findByUsername(providedUsername);
    Optional<User> maybeUser = securityUtils.getCurrentUser();
    User currentUser = maybeUser.get();

    if (currentUser.getUsername().equals(providedUsername)) {
      return ValidationResult.ok();
    } else if (user != null) {
      return ValidationResult.error("This username is already taken.");
    } else {
      return ValidationResult.ok();
    }
  }

  private ValidationResult passwordValidator(String password, ValueContext valueContext) {

    if (password == null || password.length() < 8) {
      return ValidationResult.error("Password should be at least 8 characters long.");
    }
    return ValidationResult.ok();
  }

  private void showUpdateSuccess() {
    UI.getCurrent().getPage().reload();

    Notification notification = Notification.show(
        "Details updated.",
        3000,
        Notification.Position.MIDDLE
    );
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

  }

}
