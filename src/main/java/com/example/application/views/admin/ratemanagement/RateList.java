package com.example.application.views.admin.ratemanagement;

import com.example.application.data.entity.Rate;
import com.example.application.data.service.RateService;
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
import javax.annotation.security.RolesAllowed;

@PageTitle("Rates management")
@RolesAllowed("admin")
@Route(value = "admin/rates", layout = MainLayout.class)
public class RateList extends VerticalLayout {

  private final RateService rateService;
  private final UserService userService;
  Grid<Rate> grid = new Grid<>(Rate.class);
  TextField filterText = new TextField();
  RateEditForm form;


  public RateList(RateService rateService, UserService userService){
    this.rateService = rateService;
    this.userService = userService;

    setSizeFull();
    configureGrid();

    form = new RateEditForm(rateService, userService.findAll());
    form.setWidth("25em");
    form.addListener(RateEditForm.SaveEvent.class, this::saveRate);
    form.addListener(RateEditForm.DeleteEvent.class, this::deleteRate);
    form.addListener(RateEditForm.CloseEvent.class, e -> closeEditor());

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
        editRate(event.getValue()));

  }

  private void configureGrid() {
    grid.setSizeFull();
    grid.setColumns(
        "id",
        "rateNumber",
        "comment",
        "ratedBy",
        "personRated",
        "renterRate",
        "createdAt"
    );
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar() {
    filterText.setPlaceholder("Filter rates...");
    filterText.setClearButtonVisible(true);
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    filterText.addValueChangeListener(e -> updateList());

    Button addUserButton = new Button("Add rate");
    addUserButton.addClickListener(click -> addRate());

    HorizontalLayout toolbar = new HorizontalLayout(filterText, addUserButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  private void saveRate(RateEditForm.SaveEvent event) {
    rateService.save(event.getRate());
    updateList();
    closeEditor();
  }

  private void deleteRate(RateEditForm.DeleteEvent event) {
    rateService.delete(event.getRate().getId());
    updateList();
    closeEditor();
  }

  public void editRate(Rate rate) {
    if (rate == null) {
      closeEditor();
    } else {
      form.setRate(rate);
      form.setVisible(true);
      addClassName("editing");
    }
  }

  void addRate() {
    grid.asSingleSelect().clear();
    editRate(new Rate());
  }

  private void closeEditor() {
    form.setRate(null);
    form.setVisible(false);
    removeClassName("editing");
  }

  private void updateList() {
    grid.setItems(rateService.findAllRates(filterText.getValue()));
  }



}

