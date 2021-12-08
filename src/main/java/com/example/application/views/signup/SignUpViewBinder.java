package com.example.application.views.signup;

import com.example.application.data.entity.User;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;

public class SignUpViewBinder {

  private SignUpForm signUpForm;

  //Flag for disabling first run for password validation
  private boolean enablePasswordValidation;

  public SignUpViewBinder(SignUpForm signUpForm) {
    this.signUpForm = signUpForm;
  }

  //Method to add the data binding and validation logics
  //to the signUp form
  public void addBindingAndValidation() {
    BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);
    binder.bindInstanceFields(signUpForm);

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

        //todo: save bean to database

        showSuccess(user);
      } catch (ValidationException validationException) {
        // validators exceptions are already visible for each field,
        // and bean-level errors are shown in the status label
        validationException.printStackTrace();
      }
    });
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

  private void showSuccess(User user) {
    Notification notification =
        Notification.show("Successful registration, welcome " + user.getUsername());
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    //todo: redirect user to MainLayout
  }
}
