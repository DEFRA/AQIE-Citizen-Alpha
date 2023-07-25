# AQIE-Citizen-Alpha
AQIE = Air Quality and Industrial Emissions. This is the alpha prototype for our Citizen service.

## Backend
Controllers returning a View have a name prefixed with Display.

## Frontend
The frontend code does not use a JavaScript framework. This is done on purpose to follow progressive enhancement recommended by GDS. See https://www.gov.uk/service-manual/technology/using-progressive-enhancement.

## To dockerize the app
To build AQIE-Citizen-Alpha/Dockerfile, I followed instructions at https://www.baeldung.com/dockerizing-spring-boot-application.

To create an image from the Dockerfile:
- cd AQIE-Citizen-Alpha
- mvn clean install
- docker build --tag=airqualitylimitedjs:latest .

To verify a new image has been built:
- docker image list
- To delete the redundant image image_id: docker image rm image_id

To run the container from our image:
- H2 profile: docker run -e spring.profiles.active=localwithh2 -e OS_MAPPING_KEY=THE_KEY -p8080:8080 airqualitylimitedjs:latest
- PostgreSQL profile: docker run -e spring.profiles.active=localdockerwithpostgre -e OS_MAPPING_KEY=THE_KEY -p8080:8080 airqualitylimitedjs:latest
    - Prerequisite = Start the PostgreSQL container.

## References
https://www.thymeleaf.org/

## TODO
Add html pages: 
    - Page 4 = Nearest air quality to a postcode. 1 text field = Postcode and 1 Submit button.
        - Once submitted, add a table listing air qualities. Cols = AirQualityIndex, Area.

Add a reCAPTCHA.

Unit tests for inputValidation.js
