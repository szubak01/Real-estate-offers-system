package com.example.application.views.studentactivity;

import com.example.application.data.service.OfferService;
import com.example.application.data.service.ReservationService;
import com.example.application.data.service.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;

@PageTitle("Activity")
@RolesAllowed("student")
@Route(value = "activity", layout = MainLayout.class)
public class StudentActivityView extends VerticalLayout {

  private final UserService userService;
  private final OfferService offerService;
  private final ReservationService reservationService;

  private final HorizontalLayout content = new HorizontalLayout();
  private final VerticalLayout activeRent = new VerticalLayout();
  private final VerticalLayout myReservations = new VerticalLayout();


  public StudentActivityView(UserService userService, OfferService offerService, ReservationService reservationService){
    this.userService = userService;
    this.offerService = offerService;
    this.reservationService = reservationService;
    addClassNames("flex", "flex-grow", "max-w-screen-lg", "mx-auto", "pb-l", "justify-center");




    activeRent.setWidth("60%");
    myReservations.setWidth("40%");

    activeRent.add();
    myReservations.add();
    content.add(activeRent, myReservations);
    add(content);
  }
}
