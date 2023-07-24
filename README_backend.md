# To build
cd project_root
mvn clean install

# To run

## IntelliJ
Define a Run Configuration with:
- main class = com.example.airqualitylimitedjs.AirqualitypocApplication
- environment variables:
    - spring.profiles.active=localwithh2
    - OS_MAPPING_KEY = THE_KEY

## Command line

### With Maven
mvn clean spring-boot:run -Dspring-boot.run.profiles=localwithh2 -DOS_MAPPING_KEY=THE_KEY

### With Java
java -jar -Dspring.profiles.active=localwithh2 -DOS_MAPPING_KEY=THE_KEY target/airqualitylimitedjs-0.0.1-SNAPSHOT.jar