package com.example.application.views.profile.userprofile;


import com.example.application.data.service.UserService;
import com.example.application.security.SecurityUtils;
import com.example.application.views.MainLayout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;
import lombok.SneakyThrows;

@PageTitle("Profile")
@RolesAllowed({"user", "student"})
@Route(value = "user/profile/:userID", layout = MainLayout.class)
public class UserProfileView extends VerticalLayout implements BeforeEnterObserver {

  private final SecurityUtils securityUtils;
  private final UserService userService;

  public UserProfileView(SecurityUtils securityUtils, UserService userService){
    this.securityUtils = securityUtils;
    this.userService = userService;

    add();
  }

  @SneakyThrows
  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Integer userID = Integer.parseInt(event.getRouteParameters().get("userID").get());

    UserProfile userProfile = new UserProfile(userID, securityUtils, userService);
    add(userProfile);
  }
}
