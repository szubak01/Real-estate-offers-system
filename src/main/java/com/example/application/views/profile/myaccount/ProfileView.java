package com.example.application.views.profile.myaccount;

import com.example.application.data.service.UserService;
import com.example.application.security.SecurityUtils;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;
import lombok.Getter;

@PageTitle("My account")
@Route(value = "account", layout = MainLayout.class)
@RolesAllowed({"landlord", "student"})
@Getter
public class ProfileView extends VerticalLayout {

  private final SecurityUtils securityUtils;
  private final UserService userService;

  public ProfileView(UserService userService, SecurityUtils securityUtils) {
    this.userService = userService;
    this.securityUtils = securityUtils;

    ProfileInfo profileInfo = new ProfileInfo(userService, securityUtils);

    add(profileInfo);

    ProfileEditFormBinder binder = new ProfileEditFormBinder(profileInfo.getProfileEditForm(), userService,
        securityUtils);
    binder.addBindingAndValidation();
  }


}
