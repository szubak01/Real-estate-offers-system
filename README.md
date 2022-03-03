## Rent&Rate
Web application with real estate rental offers. 
Rent&Rate allows users, depending on their role, to view and post real estate offers, apartments or rooms,
reserve and rent given offer.The application also includes the option of issuing opinions about the property owner and tenant.


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

## Project structure

- `MainLayout.java` in `src/main/java` contains the navigation setup (i.e., the
  side/top bar and the main menu). 
- `views` package in `src/main/java` contains the server-side Java views of application.
- `data` package in `src/main/java` contains business logic and domain model of application.
- `security` package in `src/main/java` contains security configuration classes of application.
- `views` folder in `frontend/` contains the client-side JavaScript views of application.
- `themes` folder in `frontend/` contains the custom CSS styles.


