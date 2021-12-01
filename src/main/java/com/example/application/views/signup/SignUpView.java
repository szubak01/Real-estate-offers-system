package com.example.application.views.signup;

import com.example.application.data.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.PermitAll;

@PageTitle("signup")
@Route(value = "signup")
@Uses(Icon.class)
@PermitAll
public class SignUpView extends Div {

  private TextField username = new TextField("Username");
  private TextField password = new TextField("Password");
  private EmailField email = new EmailField("Email address");
  private DatePicker dateOfBirth = new DatePicker("Birthday");
  private TextField phone = new TextField("Phone number");

  private Button save = new Button("Save");
  private Button cancel = new Button("Cancel");


  public SignUpView(UserService userService){

    add(
        //createTitle(),
        //createFormLayout(),
        //createButtonLayout()
    );
  }


}
