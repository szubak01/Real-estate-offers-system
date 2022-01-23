package com.example.application.views.profile.userprofile;


import com.example.application.data.entity.User;
import com.example.application.data.service.UserService;
import com.example.application.security.SecurityUtils;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.time.temporal.ChronoUnit;

public class UserProfile extends HorizontalLayout {

  private final SecurityUtils securityUtils;
  private final UserService userService;

  //private final HorizontalLayout content = new HorizontalLayout();
  private final VerticalLayout userInfo = new VerticalLayout();
  private final VerticalLayout userOpinions = new VerticalLayout();
  private final FormLayout detailsInfo = new FormLayout();

  private Image image;
  private Span name;
  private Span email;
  private Span phoneNumber;
  private Span role;
  private Span joinedAt;
  private Span dateOfBirth;
  private Span city;
  private Span overall;

  public UserProfile(Integer userID, SecurityUtils securityUtils, UserService userService) {
    this.securityUtils = securityUtils;
    this.userService = userService;
    addClassNames("flex", "flex-grow", "max-w-screen-lg", "mx-auto", "pb-l");
    setWidth("1024");

    User user = userService.findById(userID);
    Hr separator = new Hr();
    separator.addClassNames("bg-primary", "flex-grow", "max-w-full");

    // overall
    HorizontalLayout overallLayout = new HorizontalLayout();
    Icon starIcon = new Icon(VaadinIcon.STAR);
    overall = new Span("Rating: TBA");
    overallLayout.add(starIcon, overall);
    cssForSpans(overallLayout, overall, starIcon);
    starIcon.setColor("yellow");

    // image
    image = new Image();
    if (user.getProfilePictureUrl() == null || user.getProfilePictureUrl().length <= 0){
      Avatar avatar = new Avatar(user.getUsername());
      avatar.getStyle().set("border-radius", "12px");
      avatar.getStyle().set("margin-right", "10px");
      avatar.setMinHeight("240px");
      avatar.setMinWidth("240px");
      image.add(avatar);
    } else {
      image.getElement().setAttribute("src",
          new StreamResource(" ",
              () -> new ByteArrayInputStream(user.getProfilePictureUrl())));
    }

    // name
    HorizontalLayout nameLayout = new HorizontalLayout();
    Icon nameIcon = new Icon(VaadinIcon.MALE);
    name = new Span(user.getFirstName() + " " + user.getLastName());
    nameLayout.add(nameIcon, name);
    cssForSpans(nameLayout, name, nameIcon);

    //email
    HorizontalLayout emailLayout = new HorizontalLayout();
    Icon emailIcon = new Icon(VaadinIcon.AT);
    email = new Span(user.getEmail());
    emailLayout.add(emailIcon, email);
    cssForSpans(emailLayout, email, emailIcon);

    // phone
    HorizontalLayout phoneLayout = new HorizontalLayout();
    Icon phoneIcon = new Icon(VaadinIcon.PHONE);
    phoneNumber = new Span(user.getPhoneNumber());
    phoneLayout.add(phoneIcon, phoneNumber);
    cssForSpans(phoneLayout, phoneNumber, phoneIcon);

    // role
    HorizontalLayout roleLayout = new HorizontalLayout();
    Icon roleIcon = new Icon(VaadinIcon.USER);
    role = new Span(String.valueOf(user.getRoles().stream().findFirst().get()));
    roleLayout.add(roleIcon, role);
    cssForSpans(roleLayout, role, roleIcon);

    // city
    HorizontalLayout cityLayout = new HorizontalLayout();
    Icon cityIcon = new Icon(VaadinIcon.WORKPLACE);
    city = new Span(user.getCity());
    cityLayout.add(cityIcon, city);
    cssForSpans(cityLayout, city, cityIcon);

    // joined at
    HorizontalLayout joinedAtLayout = new HorizontalLayout();
    Icon sinceIcon = new Icon(VaadinIcon.TIME_FORWARD);
    joinedAt = new Span(user.getCreatedAt().truncatedTo(ChronoUnit.DAYS).toString().replaceAll("[TZ]", " ").substring(0, 11));
    joinedAtLayout.add(sinceIcon, joinedAt);
    cssForSpans(joinedAtLayout, joinedAt, sinceIcon);

    // date of birth
    HorizontalLayout birthLayout = new HorizontalLayout();
    Icon birthIcon = new Icon(VaadinIcon.CALENDAR_USER);
    dateOfBirth = new Span(String.valueOf(user.getDateOfBirth()));
    birthLayout.add(birthIcon, dateOfBirth);
    cssForSpans(birthLayout, dateOfBirth, birthIcon);

    detailsInfo.add(overallLayout, nameLayout, emailLayout, phoneLayout, roleLayout, cityLayout, joinedAtLayout, birthLayout);
    userInfo.add(image, separator, detailsInfo);
    add(userInfo, userOpinions);
    css();
  }

  private HorizontalLayout createRateCard(User user){
    HorizontalLayout card = new HorizontalLayout();
    card.addClassName("card");
    card.setSpacing(false);
    card.getThemeList().add("spacing-s");

    Image image = new Image();
    //image.setSrc(user.getProfilePictureUrl());
    VerticalLayout description = new VerticalLayout();
    description.addClassName("description");
    description.setSpacing(false);
    description.setPadding(false);

    HorizontalLayout header = new HorizontalLayout();
    header.addClassName("header");
    header.setSpacing(false);
    header.getThemeList().add("spacing-s");

//    Span name = new Span(user.getFirstName() + " " + user.getLastName());
//    name.addClassName("name");
//    Span date = new Span(rate.getCreatedAt());
//    date.addClassName("date");
//    header.add(name, date);
//
//    Span comment = new Span(rate.getComment());
//    comment.addClassName("post");
//
//    HorizontalLayout actions = new HorizontalLayout();
//    actions.addClassName("actions");
//    actions.setSpacing(false);
//    actions.getThemeList().add("spacing-s");
//
//    description.add(header, comment);
//    card.add(image, description);
    return card;
  }

  private void css(){
    userInfo.setWidth("30%");
    userInfo.addClassNames("w-full", "border", "box-border", "border-primary", "shadow-s", "rounded-l");

    userOpinions.addClassNames("w-full", "border", "box-border", "border-primary", "shadow-s", "rounded-l");
    userOpinions.setWidth("70%");
    image.addClassNames("rounded-l", "self-center");
    image.getStyle().set("border-radius", "12px");
    //image.setWidthFull();
    image.setWidth("230px");
  }

  private void cssForSpans(HorizontalLayout layout, Span value, Icon icon){
    layout.addClassNames("text-l", "mb-s", "pt-s");
    value.addClassNames("m-0", "pl-m", "font-medium");
    icon.setColor("dodgerblue");
  }

}
