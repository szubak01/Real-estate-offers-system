package com.example.application.data.generator;

import com.example.application.data.Role;
import com.example.application.data.entity.Location;
import com.example.application.data.entity.Offer;
import com.example.application.data.entity.User;
import com.example.application.data.enums.OfferType;
import com.example.application.data.repository.LocationRepository;
import com.example.application.data.repository.OfferRepository;
import com.example.application.data.repository.UserRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
        OfferRepository offerRepository
        ) {

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


            logger.info("... generating location example entity...");

            Location l = new Location();
            l.setCity("Warsaw");
            l.setVoivodeship("Mazowieckie");
            l.setStreetNumber("Krakowska 21A");
            l.setPostalCode("00-420");

            locationRepository.save(l);

            Location l2 = new Location();
            l2.setCity("Cracow");
            l2.setVoivodeship("Mazowieckie2");
            l2.setStreetNumber("Krakowska 221A");
            l2.setPostalCode("20-420");

            locationRepository.save(l2);

            Location l3 = new Location();
            l3.setCity("Rzeszow");
            l3.setVoivodeship("Mazowieckie3");
            l3.setStreetNumber("Krakowska 231A");
            l3.setPostalCode("30-420");

            locationRepository.save(l3);

            logger.info("... generating offer example entity...");

            Offer o = new Offer();
            o.setOfferTypeSelect(OfferType.Apartment);
            o.setOfferTitle("Offer title example example ........");
            o.setPricePerMonth(600.0);
            o.setRent(350.0);
            o.setDeposit(1000.0);
            o.setLivingArea(54.0);
            o.setNumberOfRooms(3.0);
            o.setTypeOfRoom(" ");
            o.setDescription("Example Description for offer entity with all blahblalalalaalala");
            //o.setOfferImages(imgs);
            o.setCreatedAt(Instant.now());
            o.setUser(user);
            o.setLocation(l);

            offerRepository.save(o);

            Offer o2 = new Offer();
            o2.setOfferTypeSelect(OfferType.Apartment);
            o2.setOfferTitle("Offer title example example ........");
            o2.setPricePerMonth(600.0);
            o2.setRent(350.0);
            o2.setDeposit(1000.0);
            o2.setLivingArea(54.0);
            o2.setNumberOfRooms(3.0);
            o2.setTypeOfRoom(" ");
            o2.setDescription("Example Description for offer entity with all blahblalalalaalala");
            //o.setOfferImages(imgs);
            o2.setCreatedAt(Instant.now());
            o2.setUser(user);
            o2.setLocation(l2);

            offerRepository.save(o2);

            Offer o3 = new Offer();
            o3.setOfferTypeSelect(OfferType.Apartment);
            o3.setOfferTitle("Offer title example example ........");
            o3.setPricePerMonth(600.0);
            o3.setRent(350.0);
            o3.setDeposit(1000.0);
            o3.setLivingArea(54.0);
            o3.setNumberOfRooms(3.0);
            o3.setTypeOfRoom(" ");
            o3.setDescription("Example Description for offer entity with all blahblalalalaalala");
            //o.setOfferImages(imgs);
            o3.setCreatedAt(Instant.now());
            o3.setUser(user);
            o3.setLocation(l3);

            offerRepository.save(o3);

            logger.info("Generated demo data");
        };
    }

}