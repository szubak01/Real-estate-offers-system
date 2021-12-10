package com.example.application.views.signup;

import com.example.application.data.service.UserService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.Getter;

@PageTitle("Sign up")
@Route(value = "signup")
@AnonymousAllowed
@Getter
public class SignUpView extends VerticalLayout {

  private final UserService userService;

  public SignUpView(UserService userService) {
    this.userService = userService;

    SignUpForm signUpForm = new SignUpForm(this.userService);

    setSizeFull();
    setHorizontalComponentAlignment(Alignment.CENTER, signUpForm);
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    add(signUpForm);

    SignUpViewBinder signUpViewBinder = new SignUpViewBinder(signUpForm, userService);
    signUpViewBinder.addBindingAndValidation();
  }

}
