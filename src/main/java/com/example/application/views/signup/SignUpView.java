package com.example.application.views.signup;


import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
public class SignUpView extends VerticalLayout {

  private H3 title;
  private TextField username;
  private EmailField email;
  private PasswordField password;
  private TextField phoneNumber;
  private Button signUpButton;

  public SignUpView() {

    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
    //setHorizontalComponentAlignment(Alignment.CENTER);


    getStyle().set("display", "flex");
    add(signUpForm());
  }

  private FormLayout signUpForm() {

    FormLayout layout = new FormLayout();
    addClassNames(
        "flex",
        "bg-base",
        "border-b",
        "border-contrast-10",
        "box-border");

    title = new H3("Sign up");
    username = new TextField("Username");
    email = new EmailField("Email");
    password = new PasswordField("Password");
    phoneNumber = new TextField("Phone number");

    signUpButton = new Button("Sign Up");
    signUpButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    setRequiredIndicatorVisible(username, email, password, phoneNumber);

    layout.add(
        title,
        username,
        email,
        password,
        phoneNumber,
        signUpButton
    );

    // max width of the form
    setMaxWidth("360px");

    // Allow the form layout to be responsive
    // On device widths 0-380px we have one column, otherwise there are two columns
    layout.setResponsiveSteps(
        new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
        new ResponsiveStep("350px", 2, ResponsiveStep.LabelsPosition.TOP)
    );

    // These components always take full width
    layout.setColspan(title, 2);
    layout.setColspan(username, 2);
    layout.setColspan(email, 2);
    layout.setColspan(password, 2);
    layout.setColspan(phoneNumber, 2);
    layout.setColspan(signUpButton, 2);

    return layout;
  }

  private void setRequiredIndicatorVisible(HasValueAndElement<?, ?>... components) {
    Stream.of(components).forEach(comp -> comp.setRequiredIndicatorVisible(true));
  }

}
