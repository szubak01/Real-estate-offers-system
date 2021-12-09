package com.example.application.views.signup;

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

  public SignUpView() {

    SignUpForm signUpForm = new SignUpForm();

    setSizeFull();
    setHorizontalComponentAlignment(Alignment.CENTER, signUpForm);
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    add(signUpForm);

    SignUpViewBinder signUpViewBinder = new SignUpViewBinder(signUpForm);
    signUpViewBinder.addBindingAndValidation();
  }

}
