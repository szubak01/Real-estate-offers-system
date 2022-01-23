package com.example.application.views.mainview;

import com.example.application.data.service.OfferService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Rental offers")
@Route(value = "rentaloffers", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class MainView extends VerticalLayout {

  private final OfferService offerService;

  // search layout
  private final VerticalLayout searchLayout = new VerticalLayout();
  private final H2 mainTitle = new H2("All offers in one place");
  private final Paragraph paragraph = new Paragraph(
      "All fields are required unless otherwise noted");
  private final Hr separator = new Hr();
  private final TextField searchBar = new TextField();

  public MainView(OfferService offerService) {
    this.offerService = offerService;
    addClassNames("max-w-screen-xl", "mx-auto", "pb-l", "px-l");
    setSizeFull();
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);

    // search layout
    searchLayoutCss();
    searchLayout.add(mainTitle, paragraph, separator, searchBar);

    // offers layout
    CardLayout offersLayout = new CardLayout(offerService);

    add(searchLayout, offersLayout);
  }

  private void searchLayoutCss() {
    searchLayout.addClassNames("justify-between", "box-border");
    mainTitle.addClassNames("mt-s", "justify-center");
    paragraph.addClassNames("mb-s", "mt-0", "text-secondary", "justify-center");
    separator.addClassNames("bg-primary", "flex-grow", "max-w-full");
    searchBar.addClassNames("flex-grow", "min-w-full");
    searchBar.setPlaceholder("Search by..");
  }

}
