package com.example.application.views.signup;


import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.stream.Stream;
import lombok.Getter;


@PageTitle("Sign up")
@Route(value = "signup")
@AnonymousAllowed
@Getter
public class SignUpView extends VerticalLayout{

  private final FormLayout formLayout = new FormLayout();
  private final H2 title;
  private final Hr separator;

  private final TextField username;
  private final EmailField email;
  private final PasswordField hashedPassword;
  private final PasswordField passwordConfirm;
  private final TextField phoneNumber;
  private final Button signUpButton;

  private final Span errorMessageFailed;

  public SignUpView() {

    separator = new Hr();
    title = new H2("Sign up");

    username = new TextField("Username");
    email = new EmailField("Email");
    hashedPassword = new PasswordField("Password");
    passwordConfirm = new PasswordField("Confirm password");
    phoneNumber = new TextField("Phone");

    signUpButton = new Button("Sign Up");
    errorMessageFailed = new Span();

    formLayout.add(
        title, separator,
        username, email,
        hashedPassword, passwordConfirm,
        phoneNumber,
        errorMessageFailed,
        signUpButton
    );

    styleVerticalLayout();
    styleFormLayout();
    styleFormLayoutElements();
    responsiveFormLayout();
    setRequiredIndicatorVisible(username, email, hashedPassword, passwordConfirm, phoneNumber);

    add(formLayout);

    SignUpViewBinder signUpViewBinder = new SignUpViewBinder(this);
    signUpViewBinder.addBindingAndValidation();
  }

  private void styleVerticalLayout(){
    setSizeFull();
    setHorizontalComponentAlignment(Alignment.CENTER, formLayout);
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
  }

  private void styleFormLayout(){
    formLayout.getStyle().set("border", "1px var(--lumo-primary-color), var(--lumo-shade)");
    formLayout.getStyle().set("border-radius", "var(--lumo-border-radius-l)");
    formLayout.getStyle().set("box-shadow", "0 0 0 1px var(--lumo-shade-5pct), var(--lumo-box-shadow-m)");
    formLayout.getStyle().set("padding", "var(--lumo-space-l)");
    formLayout.getStyle().set("margin", "10px");
    formLayout.getStyle().set("spacing", "10px");
  }

  private void styleFormLayoutElements(){

    title.getStyle().set("text-align", "center");

    separator.getStyle().set("background-color", "var(--lumo-primary-color)");
    separator.getStyle().set("flex-grow", "1");

    signUpButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    signUpButton.getStyle().set("margin", "20px");
  }

  private void responsiveFormLayout(){
    // max width of the form
    formLayout.setMaxWidth("500px");

    // Allow the form layout to be responsive
    // On device widths 0-490px we have one column, otherwise there are two columns
    formLayout.setResponsiveSteps(
        new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
        new ResponsiveStep("490px", 2, ResponsiveStep.LabelsPosition.TOP)
    );

    // These components always take full width
    formLayout.setColspan(title, 2);
    formLayout.setColspan(separator, 2);
    formLayout.setColspan(username, 1);
    formLayout.setColspan(email, 1);
    formLayout.setColspan(hashedPassword, 1);
    formLayout.setColspan(passwordConfirm, 1);
    formLayout.setColspan(phoneNumber, 1);
    formLayout.setColspan(signUpButton, 2);
  }

  private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
    Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
  }
}
