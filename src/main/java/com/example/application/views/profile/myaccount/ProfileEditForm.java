package com.example.application.views.profile.myaccount;

import com.example.application.data.service.UserService;
import com.example.application.views.signup.UploadImageForm;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProfileEditForm extends FormLayout {

  private final UserService userService;

  private final H2 title;
  private final Hr separator;
  private final H3 title2;
  private final Hr separator2;

  private final TextField username;
  //private final PasswordField password;
  private final EmailField email;
  private final TextField phoneNumber;
  private final Button imageButton;

  //Additional info
  private final TextField firstName;
  private final TextField lastName;
  private final DatePicker dateOfBirth;
  private final TextField city;

  private final Button saveButton;
  private final Button cancelButton;

  private final UploadImageForm upload = new UploadImageForm();
  private final Span errorMessageFailed;

  public ProfileEditForm(UserService userService) {
    this.userService = userService;

    title = new H2("Personal information");
    separator = new Hr();

    username = new TextField("Username");
    //password = new PasswordField("Password");
    email = new EmailField("Email");
    phoneNumber = new TextField("Phone");
    imageButton = new Button("Change profile picture", new Icon(VaadinIcon.PLUS));

    title2 = new H3("Additional information");
    separator2 = new Hr();

    //Additional info
    firstName = new TextField("First name");
    lastName = new TextField("Last name");
    dateOfBirth = new DatePicker("Date of birth");
    dateOfBirth.setMax(LocalDate.now());
    city = new TextField("City");

    saveButton = new Button("Save");
    cancelButton = new Button("Cancel");

    errorMessageFailed = new Span();

    responsiveFormLayout();
    styleFormLayoutElements();
    styleFormLayout();
    listenersHandler();

    add(
        title, separator,
        username, email,
        //password,
        phoneNumber, imageButton, upload,
        title2, separator2,
        firstName, lastName, dateOfBirth, city,
        createButtonsLayout(),
        errorMessageFailed
    );
  }

  private void listenersHandler() {
    // opens or closes upload area
    imageButton.addClickListener(click -> {
      boolean state = upload.isVisible();
      upload.setVisible(!state);
    });
  }

  private HorizontalLayout createButtonsLayout() {
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    saveButton.getStyle().set("margin-top", "20px");
    cancelButton.getStyle().set("margin-top", "20px");
    saveButton.addClickShortcut(Key.ENTER);
    cancelButton.addClickShortcut(Key.ESCAPE);

    return new HorizontalLayout(saveButton, cancelButton);
  }

  private void responsiveFormLayout() {
    // Allow the form layout to be responsive
    // On device widths 0-490px we have one column, otherwise there are two columns
    setResponsiveSteps(
        new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
        new ResponsiveStep("500px", 2, ResponsiveStep.LabelsPosition.TOP)
    );

    setColspan(title, 2);
    setColspan(title2, 2);
    setColspan(separator, 2);
    setColspan(separator2, 2);
  }

  private void styleFormLayoutElements() {
    separator.getStyle().set("background-color", "var(--lumo-primary-color)");
    separator.getStyle().set("flex-grow", "1");
    separator2.getStyle().set("background-color", "var(--lumo-primary-color)");
    separator2.getStyle().set("flex-grow", "1");
    imageButton.getStyle().set("margin-top", "20px");
  }

  private void styleFormLayout() {
    getStyle().set("border", "1px var(--lumo-primary-color), var(--lumo-shade)");
    getStyle().set("border-radius", "var(--lumo-border-radius-l)");
    getStyle().set("padding", "var(--lumo-space-l)");
    getStyle().set("margin", "10px");
    getStyle().set("spacing", "10px");
  }

}
