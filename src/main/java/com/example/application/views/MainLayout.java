package com.example.application.views;

import com.example.application.data.entity.User;
import com.example.application.security.SecurityUtils;
import com.example.application.security.SecurityConfiguration;
import com.example.application.views.admin.AdminView;
import com.example.application.views.rentaloffers.RentaloffersView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
public class MainLayout extends AppLayout {

  public static class MenuItemInfo {

    private String text;
    private String iconClass;
    private Class<? extends Component> view;

    public MenuItemInfo(String text, String iconClass, Class<? extends Component> view) {
      this.text = text;
      this.iconClass = iconClass;
      this.view = view;
    }

    public String getText() {
      return text;
    }

    public String getIconClass() {
      return iconClass;
    }

    public Class<? extends Component> getView() {
      return view;
    }

  }

  private H1 viewTitle;

  private SecurityUtils securityUtils;
  private AccessAnnotationChecker accessChecker;

  public MainLayout(SecurityUtils securityUtils, AccessAnnotationChecker accessChecker) {
    this.securityUtils = securityUtils;
    this.accessChecker = accessChecker;

    setPrimarySection(Section.DRAWER);
    addToNavbar(true, createHeaderContent());
    addToDrawer(createDrawerContent());
  }

  private Component createHeaderContent() {
    DrawerToggle toggle = new DrawerToggle();
    toggle.addClassName("text-secondary");
    toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
    toggle.getElement().setAttribute("aria-label", "Menu toggle");

    viewTitle = new H1();
    viewTitle.addClassNames("m-0", "text-l");

    Header header = new Header(toggle, viewTitle);
    header.addClassNames(
        "bg-base",
        "border-b",
        "border-contrast-10",
        "box-border",
        "flex",
        "h-xl",
        "items-center",
        "w-full");

    return header;
  }

  private Component createDrawerContent() {
    H2 appName = new H2("Select view");
    appName.addClassNames("flex", "items-center", "h-xl", "m-0", "px-m", "text-xl");

    com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(
        appName,
        createNavigation(), createFooter());
    section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full");
    return section;
  }

  private Nav createNavigation() {
    Nav nav = new Nav();
    nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto");
    nav.getElement().setAttribute("aria-labelledby", "views");

    // Wrap the links in a list; improves accessibility
    UnorderedList list = new UnorderedList();
    list.addClassNames("list-none", "m-0", "p-0");
    nav.add(list);

    for (RouterLink link : createLinks()) {
      ListItem item = new ListItem(link);
      list.add(item);
    }
    return nav;
  }

  private List<RouterLink> createLinks() {
    MenuItemInfo[] menuItems = new MenuItemInfo[]{ //
        new MenuItemInfo("Rental offers", "la la-arrow-circle-right", RentaloffersView.class), //

        new MenuItemInfo("Admin", "la la-columns", AdminView.class), //

    };

    List<RouterLink> links = new ArrayList<>();
    for (MenuItemInfo menuItemInfo : menuItems) {
      if (accessChecker.hasAccess(menuItemInfo.getView())) {
        links.add(createLink(menuItemInfo));
      }

    }
    return links;
  }

  private static RouterLink createLink(MenuItemInfo menuItemInfo) {
    RouterLink link = new RouterLink();
    link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
    link.setRoute(menuItemInfo.getView());

    Span icon = new Span();
    icon.addClassNames("me-s", "text-xl");
    if (!menuItemInfo.getIconClass().isEmpty()) {
      icon.addClassNames(menuItemInfo.getIconClass());
    }

    Span text = new Span(menuItemInfo.getText());
    text.addClassNames("font-medium", "text-m");

    link.add(icon, text);
    return link;
  }

  private Footer createFooter() {
    Footer layout = new Footer();
    layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs", "overflow-hidden");

    Optional<User> maybeUser = securityUtils.getCurrentUser();
    if (maybeUser.isPresent()) {
      User user = maybeUser.get();

      Image image = new Image();
      image.setMaxHeight("35px");
      image.setMaxWidth("40px");
      image.getStyle().set("border-radius", "12px");
      image.getStyle().set("margin-right", "10px");

      image.getElement().setAttribute("src",
          new StreamResource(" ",
              ()-> new ByteArrayInputStream(user.getProfilePictureUrl())));

      ContextMenu userMenu = new ContextMenu(image);
      userMenu.setOpenOnClick(true);
      userMenu.addItem("Logout", e -> securityUtils.logout());

      Span name = new Span(user.getUsername());
      name.addClassNames("font-medium", "text-m", "text-secondary");
      name.setMaxWidth("80px");

      Button logoutButton = new Button("Logout", event -> securityUtils.logout());
      logoutButton.addClassNames("box-content", "ml-m", "mx-s");

      layout.add(image, name, logoutButton);

    } else {
      Button loginButton = new Button("Login", event -> navigateToLoginView());
      loginButton.addClassNames("box-content", "w-full", "border-primary");

      Button signUpButton = new Button("Sing up", event -> navigateToSignUpView());
      signUpButton.addClassNames("box-content", "w-full", "border-primary");

      layout.addClassNames("flex-col");
      layout.add(loginButton, signUpButton);
    }

    return layout;
  }

  public void navigateToLoginView() {
    UI.getCurrent().getPage().setLocation(SecurityConfiguration.LOGIN_URL);
  }

  private void navigateToSignUpView() {
    UI.getCurrent().getPage().setLocation("/signup");

  }

  @Override
  protected void afterNavigation() {
    super.afterNavigation();
    viewTitle.setText(getCurrentPageTitle());
  }

  private String getCurrentPageTitle() {
    PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
    return title == null ? "" : title.value();
  }
}
