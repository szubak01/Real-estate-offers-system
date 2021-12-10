package com.example.application.data.service;

import com.example.application.data.Role;
import com.example.application.data.entity.User;
import com.example.application.data.repository.UserRepository;
import com.example.application.views.signup.SignUpForm;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vaadin.artur.helpers.CrudService;

@Service
public class UserService extends CrudService<User, Integer> {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder,
        UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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


}
