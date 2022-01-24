package com.example.application.views.profile.userprofile;


import com.example.application.data.service.RateService;
import com.example.application.data.service.UserService;
import com.example.application.security.SecurityUtils;
import com.example.application.views.MainLayout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import javax.annotation.security.RolesAllowed;
import lombok.SneakyThrows;

@PageTitle("Profile")
@AnonymousAllowed
@Route(value = "user/profile/:userID", layout = MainLayout.class)
public class UserProfileView extends VerticalLayout implements BeforeEnterObserver {

  private final SecurityUtils securityUtils;
  private final UserService userService;
  private final RateService rateService;

  public UserProfileView(SecurityUtils securityUtils, UserService userService, RateService rateService){
    this.securityUtils = securityUtils;
    this.userService = userService;
    this.rateService = rateService;


    add();
  }

  @SneakyThrows
  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Integer userID = Integer.parseInt(event.getRouteParameters().get("userID").get());

    UserProfile userProfile = new UserProfile(userID, securityUtils, userService, rateService);
    add(userProfile);
  }
}
