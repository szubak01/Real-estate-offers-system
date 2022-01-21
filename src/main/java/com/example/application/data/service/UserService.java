package com.example.application.data.service;

import com.example.application.data.enums.Role;
import com.example.application.data.entity.User;
import com.example.application.data.repository.UserRepository;
import com.example.application.security.SecurityUtils;
import com.example.application.views.profile.ProfileEditForm;
import com.example.application.views.signup.SignUpForm;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import org.jfairy.Fairy;
import org.jfairy.producer.person.Person;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class UserService extends CrudService<User, Integer> {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public UserService(PasswordEncoder passwordEncoder,
        UserRepository userRepository, SecurityUtils securityUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    protected UserRepository getRepository() {
        return userRepository;
    }

    public void signUp(SignUpForm signUpForm) throws IOException {
        User user = new User();
        user.setUsername(signUpForm.getUsername().getValue());
        user.setPassword(passwordEncoder.encode(signUpForm.getPassword().getValue()));
        user.setEmail(signUpForm.getEmail().getValue());
        user.setPhoneNumber(signUpForm.getPhoneNumber().getValue());
        user.setProfilePictureUrl(signUpForm.getUpload().getBuffer().getInputStream().readAllBytes());
        user.setRoles(Collections.singleton(Role.USER));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }

    public void updateUser(ProfileEditForm profileEditForm) throws IOException {
        User user = securityUtils.getCurrentUser().get();

        user.setUsername(profileEditForm.getUsername().getValue());
        if(!(profileEditForm.getPassword().getValue() == null)) {
            user.setPassword(passwordEncoder.encode(profileEditForm.getPassword().getValue()));
        }
        user.setEmail(profileEditForm.getEmail().getValue());
        user.setPhoneNumber(profileEditForm.getPhoneNumber().getValue());
        if(profileEditForm.getUpload().getBuffer().getInputStream().readAllBytes() != null
            && profileEditForm.getUpload().getBuffer().getInputStream().readAllBytes().length > 0) {
            user.setProfilePictureUrl(
                profileEditForm.getUpload().getBuffer().getInputStream().readAllBytes());
        }
        user.setFirstName(profileEditForm.getFirstName().getValue());
        user.setLastName(profileEditForm.getLastName().getValue());
        user.setDateOfBirth(profileEditForm.getDateOfBirth().getValue());
        user.setCity(profileEditForm.getCity().getValue());
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

//    public void populateDB(){
//        LocalDate start = LocalDate.of(1970, Month.JANUARY, 1);
//        long days = ChronoUnit.DAYS.between(start, LocalDate.now());
//        LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));
//
//
//
//        Fairy fairy = Fairy.create(Locale.forLanguageTag("en"));
//        Person person = fairy.person();
//
//        User fairyUser = new User();
//        fairyUser.setUsername(person.username());
//        fairyUser.setPassword(passwordEncoder.encode("user"));
//        fairyUser.setEmail(person.email());
//        fairyUser.setPhoneNumber(person.telephoneNumber().replace("-", ""));
//        fairyUser.setRoles(Collections.singleton(Role.USER));
//        fairyUser.setCreatedAt(Instant.now());
//
//        fairyUser.setFirstName(person.firstName());
//        fairyUser.setLastName(person.lastName());
//        fairyUser.setDateOfBirth(randomDate);
//        fairyUser.setCity(person.getAddress().city());
//
//        userRepository.save(fairyUser);
//    }
}
