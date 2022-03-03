## Rent&Rate
Web application(server-side) with real estate rental offers. </br> 
Rent&Rate allows users, depending on their role, to view and post real estate offers, apartments or rooms,
reserve and rent given offer.</br> 
The application also includes the option of issuing ratings about the property owner and tenant.

## Functionalities
There are four roles in the application: admin, landlord, student and unregistered guest.</br>
There are three main modules in the application.</br>
Users have access to given modules depending on the roles assigned to them.
- `Admin module`, which is responsible for managing all resources in the system.
- `Student module`, which provides functionalities responsible for searching, booking and renting offers.
- `Landlord module`, which provides functionalities responsible for posting and managing offers.

For all these roles, the basic functionalities are common, i.e. listing offers in the system, displaying individual offers or checking profiles of individual users.

The most important functionalities of the application include:
- Login & Registration
- Management of the owned offers
- Viewing the list of users who have booked a given offer
- Renting a flat / room to a given user
- Management of the active rent
- Booking & Canceling an offer
- Viewing a list of reservations made
- Profile editing
- Viewing active rent
- Issuing user ratings

## Technologies

- Vaadin Flow
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Apache Maven

## Project structure

- `MainLayout.java` in `src/main/java` contains the navigation setup (i.e., the
  side/top bar and the main menu). 
- `views` package in `src/main/java` contains the server-side Java views of application.
- `data` package in `src/main/java` contains business logic and domain model of application.
- `security` package in `src/main/java` contains security configuration classes of application.
- `views` folder in `frontend/` contains the client-side JavaScript views of application.
- `themes` folder in `frontend/` contains the custom CSS styles.


## Running the application

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

## Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/rrr-1.0-SNAPSHOT.jar`



