package com.example.application.security;

import com.example.application.data.entity.User;
import com.example.application.data.repository.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.jaas.SecurityContextLoginModule;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

  @Autowired
  private UserRepository userRepository;

  private Optional<Authentication> getAuthentication() {
    SecurityContext context = SecurityContextHolder.getContext();
    return Optional.ofNullable(context.getAuthentication())
        .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken));
  }

  public Optional<User> getCurrentUser() {
    return getAuthentication()
        .map(authentication -> userRepository.findByUsername(authentication.getName()));
  }

  static boolean isUserLoggedIn() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null
        && !(authentication instanceof AnonymousAuthenticationToken)
        && authentication.isAuthenticated();
  }

  public void logout() {
    UI.getCurrent().getPage().setLocation(SecurityConfiguration.LOGOUT_URL);
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
  }

}
