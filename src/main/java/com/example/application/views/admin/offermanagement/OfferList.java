package com.example.application.views.admin.offermanagement;

import com.example.application.data.entity.Offer;
import com.example.application.data.enums.OfferState;
import com.example.application.data.enums.OfferType;
import com.example.application.data.service.OfferService;
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
import javax.annotation.security.RolesAllowed;

@PageTitle("Offers management")
@RolesAllowed("admin")
@Route(value = "admin/offers", layout = MainLayout.class)
public class OfferList extends VerticalLayout {

  Grid<Offer> grid = new Grid<>(Offer.class);
  TextField filterText = new TextField();
  OfferEditForm form;
  OfferService offerService;

  public OfferList(OfferService offerService) {
    this.offerService = offerService;
    addClassName("list-view");
    setSizeFull();
    configureGrid();

    form = new OfferEditForm(offerService, Arrays.asList(OfferType.values().clone()), Arrays.asList(OfferState.values().clone()));
    form.setWidth("25em");
    form.addListener(OfferEditForm.SaveEvent.class, this::saveOffer);
    form.addListener(OfferEditForm.DeleteEvent.class, this::deleteOffer);
    form.addListener(OfferEditForm.CloseEvent.class, e -> closeEditor());

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
        editOffer(event.getValue()));
  }


  private void configureGrid() {
    grid.addClassNames("contact-grid");
    grid.setSizeFull();
    grid.setColumns(
        "id",
        "offerTitle",
        "pricePerMonth",
        "rent",
        "deposit",
        "livingArea",
        "numberOfRooms",
        "typeOfRoom",
        "description",
        "offerTypeSelect",
        "offerState"
        );
//    grid.addColumn(offer -> offer.getOfferTypeSelect().getName()).setHeader("Offer ");
//    grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar() {
    filterText.setPlaceholder("Filter by offer title...");
    filterText.setClearButtonVisible(true);
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    filterText.addValueChangeListener(e -> updateList());

    Button addOfferButton = new Button("Add offer");
    addOfferButton.addClickListener(click -> addOffer());

    HorizontalLayout toolbar = new HorizontalLayout(filterText, addOfferButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  private void saveOffer(OfferEditForm.SaveEvent event) {
    offerService.save(event.getOffer());
    updateList();
    closeEditor();
  }

  private void deleteOffer(OfferEditForm.DeleteEvent event) {
    offerService.deleteOfferById(event.getOffer().getId());
    updateList();
    closeEditor();
  }

  public void editOffer(Offer offer) {
    if (offer == null) {
      closeEditor();
    } else {
      form.setOffer(offer);
      form.setVisible(true);
      addClassName("editing");
    }
  }

  void addOffer() {
    grid.asSingleSelect().clear();
    editOffer(new Offer());
  }

  private void closeEditor() {
    form.setOffer(null);
    form.setVisible(false);
    removeClassName("editing");
  }

  private void updateList() {
    grid.setItems(offerService.findAllOffers(filterText.getValue()));
  }

}
