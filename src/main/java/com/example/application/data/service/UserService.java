package com.example.application.data.service;

import com.example.application.data.Role;
import com.example.application.data.entity.User;
import com.example.application.data.repository.UserRepository;
import com.example.application.security.SecurityUtils;
import com.example.application.views.profile.ProfileEditForm;
import com.example.application.views.signup.SignUpForm;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
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
        user.setPassword(passwordEncoder.encode(profileEditForm.getPassword().getValue()));
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
}
