package com.example.application.data.generator;

import com.example.application.data.Role;
import com.example.application.data.entity.User;
import com.example.application.data.entity.Users;
import com.example.application.data.repository.UserRepository;
import com.example.application.data.repository.UsersRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository,
            UsersRepository usersRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 2 User entities...");
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setEmail("user@example.com");
            user.setPhoneNumber("123123123");
            user.setRoles(Collections.singleton(Role.USER));
            userRepository.save(user);


            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@example.com");
            admin.setPhoneNumber("123123123");
            admin.setRoles(Collections.singleton(Role.ADMIN));
            userRepository.save(admin);


            logger.info("... generating 100 Users entities...");
            ExampleDataGenerator<Users> usersRepositoryGenerator = new ExampleDataGenerator<>(Users.class,
                    LocalDateTime.of(2021, 11, 29, 0, 0, 0));
            usersRepositoryGenerator.setData(Users::setId, DataType.ID);
            usersRepositoryGenerator.setData(Users::setUsername, DataType.FIRST_NAME);
            usersRepositoryGenerator.setData(Users::setPassword, DataType.WORD);
            usersRepositoryGenerator.setData(Users::setEmail, DataType.EMAIL);
            usersRepositoryGenerator.setData(Users::setPhone, DataType.PHONE_NUMBER);
            usersRepositoryGenerator.setData(Users::setDateOfBirth, DataType.DATE_OF_BIRTH);
            usersRepository.saveAll(usersRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}