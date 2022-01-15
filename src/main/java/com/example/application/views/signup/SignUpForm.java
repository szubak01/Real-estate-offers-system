package com.example.application.views.signup;

import com.example.application.data.service.UserService;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public class SignUpForm extends FormLayout {

  private final UserService userService;

  private final H2 title;
  private final Hr separator;

  private final TextField username;
  private final EmailField email;
  private final PasswordField password;
  private final PasswordField passwordConfirm;
  private final TextField phoneNumber;

  private final Button signUpButton;
  private final Button profilePictureButton;

  private final Span errorMessageFailed;

  UploadImageForm upload = new UploadImageForm();

  public SignUpForm(UserService userService) {
    this.userService = userService;

    separator = new Hr();
    title = new H2("Sign up");

    username = new TextField("Username");
    email = new EmailField("Email");
    password = new PasswordField("Password");
    passwordConfirm = new PasswordField("Confirm password");
    phoneNumber = new TextField("Phone");

    signUpButton = new Button("Sign Up");
    profilePictureButton = new Button("Add profile picture", new Icon(VaadinIcon.PLUS));
    errorMessageFailed = new Span();

    listenersHandler();
    styleFormLayout();
    styleFormLayoutElements();
    responsiveFormLayout();
    setRequiredIndicatorVisible(username, email, password, passwordConfirm, phoneNumber);

    add(
        title, separator,
        username, email,
        password, passwordConfirm,
        phoneNumber, profilePictureButton,
        upload,
        errorMessageFailed,
        signUpButton
    );
  }

  private void listenersHandler(){
    // opens or closes upload area
    profilePictureButton.addClickListener(click -> {
      boolean state = upload.isVisible();
      upload.setVisible(!state);
    });
  }

  private void styleFormLayout() {
    getStyle().set("border", "1px var(--lumo-primary-color), var(--lumo-shade)");
    getStyle().set("border-radius", "var(--lumo-border-radius-l)");
    getStyle().set("box-shadow", "0 0 0 1px var(--lumo-shade-5pct), var(--lumo-box-shadow-m)");
    getStyle().set("padding", "var(--lumo-space-l)");
    getStyle().set("margin", "10px");
    getStyle().set("spacing", "10px");
  }

  private void styleFormLayoutElements() {

    title.getStyle().set("text-align", "center");

    separator.getStyle().set("background-color", "var(--lumo-primary-color)");
    separator.getStyle().set("flex-grow", "1");

    signUpButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    signUpButton.getStyle().set("margin", "20px");
  }

  private void responsiveFormLayout() {
    setMaxWidth("500px");

    // Allow the form layout to be responsive
    // On device widths 0-490px we have one column, otherwise there are two columns
    setResponsiveSteps(
        new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
        new ResponsiveStep("490px", 2, ResponsiveStep.LabelsPosition.TOP)
    );

    setColspan(title, 2);
    setColspan(separator, 2);
    setColspan(username, 1);
    setColspan(email, 1);
    setColspan(password, 1);
    setColspan(passwordConfirm, 1);
    setColspan(phoneNumber, 1);
    setColspan(profilePictureButton, 1);
    setColspan(upload, 2);
    setColspan(signUpButton, 2);
  }

  private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
    Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
  }

}
