package com.example.application.views.admin.usermanagement;

import com.example.application.data.entity.User;
import com.example.application.data.enums.Role;
import com.example.application.data.service.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;

@PageTitle("Users management")
@RolesAllowed("admin")
@Route(value = "admin/users", layout = MainLayout.class)
public class UserList extends VerticalLayout {

  private final UserService userService;
  Grid<User> grid = new Grid<>(User.class);
  TextField filterText = new TextField();
  UserEditForm form;




  public UserList(UserService userService){
    this.userService = userService;

    setSizeFull();
    configureGrid();

    form = new UserEditForm(userService, Arrays.stream(Role.values()).collect(Collectors.toSet()));
    form.setWidth("25em");
    form.addListener(UserEditForm.SaveEvent.class, this::saveUser);
    form.addListener(UserEditForm.DeleteEvent.class, this::deleteUser);
    form.addListener(UserEditForm.CloseEvent.class, e -> closeEditor());

    FlexLayout content = new FlexLayout(grid, form);
    content.setFlexGrow(2, grid);
    content.setFlexGrow(1, form);
    content.setFlexShrink(0, form);
    content.addClassNames("content", "gap-m");
    content.setSizeFull();

    add(getToolbar(), content);
    updateList();
    closeEditor();
    grid.asSingleSelect().addValueChangeListener(event ->
        editUser(event.getValue()));

  }

  private void configureGrid() {
    grid.setSizeFull();
    grid.setColumns(
        "id",
        "username",
        "email",
        "phoneNumber",
        "roles",
        "firstName",
        "lastName",
        "dateOfBirth",
        "city",
        "createdAt"
    );
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar() {
    filterText.setPlaceholder("Filter users...");
    filterText.setClearButtonVisible(true);
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    filterText.addValueChangeListener(e -> updateList());

    Button addUserButton = new Button("Add user");
    addUserButton.addClickListener(click -> addUser());

    HorizontalLayout toolbar = new HorizontalLayout(filterText, addUserButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  private void saveUser(UserEditForm.SaveEvent event) {
    userService.save(event.getUser());
    updateList();
    closeEditor();
  }

  private void deleteUser(UserEditForm.DeleteEvent event) {
    userService.delete(event.getUser().getId());
    updateList();
    closeEditor();
  }

  public void editUser(User user) {
    if (user == null) {
      closeEditor();
    } else {
      form.setUser(user);
      form.setVisible(true);
      addClassName("editing");
    }
  }

  void addUser() {
    grid.asSingleSelect().clear();
    editUser(new User());
  }

  private void closeEditor() {
    form.setUser(null);
    form.setVisible(false);
    removeClassName("editing");
  }

  private void updateList() {
    grid.setItems(userService.findAllUsers(filterText.getValue()));
  }

}
