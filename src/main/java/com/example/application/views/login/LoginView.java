package com.example.application.views.login;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

  public LoginView() {
    setAction("login");
    LoginI18n loginForm = LoginI18n.createDefault();

    loginForm.setHeader(new LoginI18n.Header());
    loginForm.getHeader().setTitle("Rent&Rate");
    loginForm.setAdditionalInformation(null);

    setForgotPasswordButtonVisible(false);
    setI18n(loginForm);
    setOpened(true);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

    if (beforeEnterEvent.getLocation()
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
      setError(true);
    }

  }

}
