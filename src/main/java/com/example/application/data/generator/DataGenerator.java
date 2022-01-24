//package com.example.application.data.generator;
//
//import com.example.application.data.enums.OfferState;
//import com.example.application.data.enums.Role;
//import com.example.application.data.entity.Location;
//import com.example.application.data.entity.Offer;
//import com.example.application.data.entity.OfferImage;
//import com.example.application.data.entity.User;
//import com.example.application.data.enums.OfferType;
//import com.example.application.data.repository.LocationRepository;
//import com.example.application.data.repository.OfferImageRepository;
//import com.example.application.data.repository.OfferRepository;
//import com.example.application.data.repository.UserRepository;
//import com.vaadin.flow.spring.annotation.SpringComponent;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.Month;
//import java.time.temporal.ChronoUnit;
//import java.util.Collections;
//import java.util.Locale;
//import java.util.Random;
//import javax.imageio.ImageIO;
//import org.jfairy.Fairy;
//import org.jfairy.producer.person.Person;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@SpringComponent
//public class DataGenerator {
//
//  @Bean
//  public CommandLineRunner loadData(
//      PasswordEncoder passwordEncoder,
//      UserRepository userRepository,
//      LocationRepository locationRepository,
//      OfferRepository offerRepository,
//      OfferImageRepository offerImageRepository
//  ) {
//
//    return args -> {
//      Logger logger = LoggerFactory.getLogger(getClass());
//      if (userRepository.count() != 0L) {
//        logger.info("Using existing database");
//        return;
//      }
//      int seed = 123;
//
//      LocalDate start = LocalDate.of(1970, Month.JANUARY, 1);
//      long days = ChronoUnit.DAYS.between(start, LocalDate.now());
//      LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));
//
//      logger.info("Generating demo data");
//
//
//      Fairy fairy = Fairy.create(Locale.forLanguageTag("en"));
//      Person person = fairy.person();
//
//      User fairyUser = new User();
//      fairyUser.setUsername("student");
//      fairyUser.setPassword(passwordEncoder.encode("student"));
//      fairyUser.setEmail(person.email());
//      fairyUser.setPhoneNumber("678123678");
//      fairyUser.setRoles(Collections.singleton(Role.STUDENT));
//      fairyUser.setCreatedAt(Instant.now());
//
//      fairyUser.setFirstName(person.firstName());
//      fairyUser.setLastName(person.lastName());
//      fairyUser.setDateOfBirth(randomDate);
//      fairyUser.setCity(person.getAddress().city());
//
//      userRepository.save(fairyUser);
//
//
//      logger.info("... generating 2 User entities...");
//      User user = new User();
//      user.setUsername("user");
//      user.setPassword(passwordEncoder.encode("user"));
//      user.setEmail("user@example.com");
//      user.setPhoneNumber("123123123");
//      user.setCreatedAt(Instant.now());
//      user.setFirstName("FirstName");
//      user.setLastName("LastName");
//      user.setCity("userCity");
//      user.setRoles(Collections.singleton(Role.USER));
//      userRepository.save(user);
//
////      User student = new User();
////      user.setUsername("student");
////      user.setPassword(passwordEncoder.encode("user"));
////      user.setEmail("studen112t@example.com");
////      user.setPhoneNumber("321321321");
////      user.setCreatedAt(Instant.now());
////      user.setFirstName("StudentName");
////      user.setLastName("StudentLast");
////      user.setCity("Student city");
////      user.setDateOfBirth(LocalDate.now());
////      user.setRoles(Collections.singleton(Role.STUDENT));
////      userRepository.save(student);
//
//      User admin = new User();
//      admin.setUsername("admin");
//      admin.setPassword(passwordEncoder.encode("admin"));
//      admin.setEmail("admin@example.com");
//      admin.setPhoneNumber("123123123");
//      admin.setRoles(Collections.singleton(Role.ADMIN));
//      userRepository.save(admin);
//
//      logger.info("... generating location example entity...");
//
//      Location l = new Location();
//      l.setCity("Warsaw");
//      l.setVoivodeship("Mazowieckie");
//      l.setStreetNumber("Krakowska 21A");
//      l.setPostalCode("00-420");
//
//      locationRepository.save(l);
//
//      Location l2 = new Location();
//      l2.setCity("Cracow");
//      l2.setVoivodeship("Mazowieckie2");
//      l2.setStreetNumber("Krakowska 221A");
//      l2.setPostalCode("20-420");
//
//      locationRepository.save(l2);
//
//      Location l3 = new Location();
//      l3.setCity("Rzeszow");
//      l3.setVoivodeship("Mazowieckie3");
//      l3.setStreetNumber("Krakowska 231A");
//      l3.setPostalCode("30-420");
//
//      locationRepository.save(l3);
//
//      logger.info("... generating offer example entity...");
//
//      Offer o = new Offer();
//      o.setOfferTypeSelect(OfferType.Apartment);
//      o.setOfferTitle("Offer title example example ........");
//      o.setPricePerMonth(600.0);
//      o.setRent(350.0);
//      o.setDeposit(1000.0);
//      o.setLivingArea(54.0);
//      o.setNumberOfRooms(3.0);
//      o.setTypeOfRoom(" ");
//      o.setDescription("Example Description for offer entity with all blahblalalalaalala");
//      o.setOfferState(OfferState.OPEN);
//      o.setCreatedAt(Instant.now());
//      o.setUser(user);
//      o.setLocation(l);
//
//      offerRepository.save(o);
//
//      BufferedImage bImage = ImageIO.read(
//          new File("src/main/resources/META-INF/resources/images/example_image.jpg"));
//      ByteArrayOutputStream bos = new ByteArrayOutputStream();
//      ImageIO.write(bImage, "jpg", bos);
//      byte[] data = bos.toByteArray();
//
//      OfferImage offerImage = new OfferImage();
//      offerImage.setImageName("example_image.jpg");
//      offerImage.setImage(data);
//      offerImage.setOffer(o);
//      offerImageRepository.save(offerImage);
//
//      Offer o2 = new Offer();
//      o2.setOfferTypeSelect(OfferType.Apartment);
//      o2.setOfferTitle("Offer title example example ........");
//      o2.setPricePerMonth(600.0);
//      o2.setRent(350.0);
//      o2.setDeposit(1000.0);
//      o2.setLivingArea(54.0);
//      o2.setNumberOfRooms(3.0);
//      o2.setTypeOfRoom(" ");
//      o2.setDescription("Example Description for offer entity with all blahblalalalaalala");
//      //o.setOfferImages(imgs);
//      o2.setOfferState(OfferState.OPEN);
//      o2.setCreatedAt(Instant.now());
//      o2.setUser(user);
//      o2.setLocation(l2);
//
//      offerRepository.save(o2);
//
//      Offer o3 = new Offer();
//      o3.setOfferTypeSelect(OfferType.Apartment);
//      o3.setOfferTitle("Offer title example example ........");
//      o3.setPricePerMonth(600.0);
//      o3.setRent(350.0);
//      o3.setDeposit(1000.0);
//      o3.setLivingArea(54.0);
//      o3.setNumberOfRooms(3.0);
//      o3.setTypeOfRoom(" ");
//      o3.setDescription("Example Description for offer entity with all blahblalalalaalala");
//      //o.setOfferImages(imgs);
//      o3.setOfferState(OfferState.OPEN);
//      o3.setCreatedAt(Instant.now());
//      o3.setUser(user);
//      o3.setLocation(l3);
//
//      offerRepository.save(o3);
//
//      logger.info("Generated demo data");
//    };
//  }
//
//}