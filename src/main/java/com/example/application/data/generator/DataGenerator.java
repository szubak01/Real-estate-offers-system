package com.example.application.data.generator;

import com.example.application.data.enums.Role;
import com.example.application.data.entity.User;
import com.example.application.data.repository.LocationRepository;
import com.example.application.data.repository.OfferImageRepository;
import com.example.application.data.repository.OfferRepository;
import com.example.application.data.repository.UserRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import org.jfairy.Fairy;
import org.jfairy.producer.person.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

  @Bean
  public CommandLineRunner loadData(
      PasswordEncoder passwordEncoder,
      UserRepository userRepository,
      LocationRepository locationRepository,
      OfferRepository offerRepository,
      OfferImageRepository offerImageRepository
  ) {

    return args -> {
      Logger logger = LoggerFactory.getLogger(getClass());
      if (userRepository.count() != 0L) {
        logger.info("Using existing database");
        return;
      }
      int seed = 123;

      LocalDate start = LocalDate.of(1970, Month.JANUARY, 1);
      long days = ChronoUnit.DAYS.between(start, LocalDate.now());
      LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));

      logger.info("Generating demo data");


      Fairy fairy = Fairy.create(Locale.forLanguageTag("en"));
      Person person = fairy.person();

      User fairyUser = new User();
      fairyUser.setUsername("student");
      fairyUser.setPassword(passwordEncoder.encode("student"));
      fairyUser.setEmail(person.email());
      fairyUser.setPhoneNumber("678123678");
      fairyUser.setRoles(Collections.singleton(Role.STUDENT));
      fairyUser.setCreatedAt(Instant.now());

      fairyUser.setFirstName(person.firstName());
      fairyUser.setLastName(person.lastName());
      fairyUser.setDateOfBirth(randomDate);
      fairyUser.setCity(person.getAddress().city());

      userRepository.save(fairyUser);


      logger.info("... generating 2 User entities...");
      User user = new User();
      user.setUsername("user");
      user.setPassword(passwordEncoder.encode("user"));
      user.setEmail("user@example.com");
      user.setPhoneNumber("123123123");
      user.setCreatedAt(Instant.now());
      user.setFirstName("FirstName");
      user.setLastName("LastName");
      user.setCity("userCity");
      user.setRoles(Collections.singleton(Role.LANDLORD));
      userRepository.save(user);

      User admin = new User();
      admin.setUsername("admin");
      admin.setPassword(passwordEncoder.encode("admin"));
      admin.setEmail("admin@example.com");
      admin.setPhoneNumber("123123123");
      admin.setRoles(Collections.singleton(Role.ADMIN));
      userRepository.save(admin);

      logger.info("... generating location example entity...");

      logger.info("... generating offer example entity...");

      logger.info("Generated demo data");
    };
  }

}