package com.example.application.views.signup;

import com.example.application.data.entity.User;
import com.example.application.data.service.UserService;
import com.example.application.security.SecurityConfiguration;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import java.io.IOException;

public class SignUpViewBinder {

  public SignUpViewBinder(SignUpForm signUpForm,
      UserService userService) {
    this.signUpForm = signUpForm;
    this.userService = userService;
  }

  private final SignUpForm signUpForm;
  private final UserService userService;

  //Flag for disabling first run for password validation
  private boolean enablePasswordValidation;

  //Method to add the data binding and validation logics
  //to the signUp form
  public void addBindingAndValidation() {
    BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);
    binder.bindInstanceFields(signUpForm);

    binder.forField(signUpForm.getUsername())
        .withValidator(this::usernameValidator).bind("username");

    // Custom validator for password fields
    binder.forField(signUpForm.getPassword())
        .withValidator(this::passwordValidator).bind("password");

    // The passwordConfirm field is not connected to binder, but binder has to re-check the password validator when the field value changes
    signUpForm.getPasswordConfirm().addValueChangeListener(e -> {
      // user modified the second field, now we can validate/show errors
      enablePasswordValidation = true;

      binder.validate();
    });

    binder.setStatusLabel(signUpForm.getErrorMessageFailed());

    signUpForm.getSignUpButton().addClickListener(e -> {
      try{
        // Empty bean to store provided details
        User user = new User();
        //Run validators and write the values to the bean
        binder.writeBean(user);

        userService.signUp(signUpForm);
        showSuccess();
      } catch (ValidationException | IOException validationException) {
        // validators exceptions are already visible for each field,
        // and bean-level errors are shown in the status label
        validationException.printStackTrace();
      }
    });
  }

  // Checks if provided username is already in db
  private ValidationResult usernameValidator(String username, ValueContext valueContext) {

    String providedUsername = signUpForm.getUsername().getValue();
    User user = userService.findByUsername(providedUsername);

    if (user != null){
      return ValidationResult.error("This username is already taken.");
    } else {
      return ValidationResult.ok();
    }
  }

  // Method to validate should check
  // 1 - Password is at least 8 characters long
  // 2 - Values in both fields match each other
  private ValidationResult passwordValidator(String password, ValueContext valueContext) {

    if (password == null || password.length() < 8) {
      return ValidationResult.error("Password should be at least 8 characters long.");
    }

    if (!enablePasswordValidation) {
      // user hasn't visited the field yet, don't validate yet, next time
      enablePasswordValidation = true;
      return ValidationResult.ok();
    }

    String passwordConfirm = signUpForm.getPasswordConfirm().getValue();

    if (password != null && password.equals(passwordConfirm)) {
      return ValidationResult.ok();
    }

    return ValidationResult.error("Passwords doesn't match.");
  }

  private void showSuccess(){
    Notification notification = Notification.show(
        "Successful registration, now u can log in.",
        3500,
        Notification.Position.MIDDLE
    );
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    UI.getCurrent().getPage().setLocation(SecurityConfiguration.LOGIN_URL);
  }

}
