package com.example.application.views.profile.myaccount;

import com.example.application.data.entity.User;
import com.example.application.data.service.UserService;
import com.example.application.security.SecurityUtils;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import lombok.Getter;

@Getter
public class ProfileInfo extends HorizontalLayout {

  private final SecurityUtils securityUtils;
  private final UserService userService;
  private final ProfileEditForm profileEditForm;
  private final ProfileEditForm profileInfoView;
  private Dialog editDialog;
  private final Button openEditorButton;


  private final Image profileImage;

  public ProfileInfo(UserService userService, SecurityUtils securityUtils) {
    this.userService = userService;
    this.securityUtils = securityUtils;

    profileEditForm = new ProfileEditForm(userService);
    profileInfoView = new ProfileEditForm(userService);

    VerticalLayout leftSideLayout = new VerticalLayout();
    VerticalLayout rightSideLayout = new VerticalLayout();

    User user = securityUtils.getCurrentUser().get();

    // LEFT SIDE VL1
    openEditorButton = new Button("Edit profile");
    openEditorButton.setWidth("240px");
    openEditorButton.addClickListener(event -> editDialog.open());

    createDialog();

    profileImage = new Image();
    profileImage.setMaxWidth("240px");
    profileImage.setMaxHeight("240px");
    profileImage.setMinWidth("240px");
    profileImage.setMinHeight("240px");
    profileImage.getStyle().set("border-radius", "12px");

    if (user.getProfilePictureUrl() == null || user.getProfilePictureUrl().length <= 0){
      Avatar avatar = new Avatar(user.getUsername());
      avatar.getStyle().set("border-radius", "12px");
      avatar.getStyle().set("margin-right", "10px");
      avatar.setMinHeight("240px");
      avatar.setMinWidth("240px");

      leftSideLayout.add(avatar);
    } else {
      openEditorButton.setWidth("240px");
      profileImage.getElement().setAttribute("src",
          new StreamResource(" ",
              () -> new ByteArrayInputStream(user.getProfilePictureUrl())));
      leftSideLayout.add(profileImage);
    }

    leftSideLayout.setMaxWidth("260px");
    leftSideLayout.add(openEditorButton);

    // RIGHT SIDE VL2
    rightSideLayout.add(customInfoViewLayout());

    add(leftSideLayout, rightSideLayout);
  }

  private void createDialog(){
    editDialog = new Dialog();
    editDialog.add(profileEditForm);
    editDialog.setMaxWidth("800px");
    profileEditForm.getCancelButton().addClickListener(e -> editDialog.close());
  }

  private ProfileEditForm customInfoViewLayout(){
    User user = securityUtils.getCurrentUser().get();

    profileInfoView.getSaveButton().setVisible(false);
    profileInfoView.getCancelButton().setVisible(false);
    profileInfoView.getImageButton().setVisible(false);
    profileInfoView.getPassword().setVisible(false);
    profileInfoView.getTitle().getStyle().set("margin", "0px 0px 0px 0px");
    profileInfoView.getStyle().set("padding", "8px 24px 24px 8px");

    profileInfoView.getUsername().setPlaceholder(user.getUsername());
    profileInfoView.getEmail().setPlaceholder(user.getEmail());
    //profileInfoView.getPassword().setPlaceholder(user.getPassword());
    profileInfoView.getPhoneNumber().setPlaceholder(user.getPhoneNumber());

    //Additional info
    profileInfoView.getFirstName().setPlaceholder(user.getFirstName());
    profileInfoView.getLastName().setPlaceholder(user.getLastName());
    profileInfoView.getCity().setPlaceholder(user.getCity());
    profileInfoView.getDateOfBirth().setPlaceholder(String.valueOf(user.getDateOfBirth()));

    return profileInfoView;
  }
}
