package com.example.application.views.profile.userprofile;


import com.example.application.data.service.UserService;
import com.example.application.security.SecurityUtils;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UserProfile extends VerticalLayout {

  private final SecurityUtils securityUtils;
  private final UserService userService;


  public UserProfile(Integer userID, SecurityUtils securityUtils, UserService userService) {
    this.securityUtils = securityUtils;
    this.userService = userService;

    add(new H2("USER ID: " + userID));
  }
}
