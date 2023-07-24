# To build
cd /Users/philippebrossier/code/airqualitylimitedjs
mvn clean install

# To run

## IntelliJ
Define a Run Configuration with:
- main class = com.example.airqualitylimitedjs.AirqualitypocApplication
- environment variables:
    - spring.profiles.active=localwithpostgre
    - OS_MAPPING_KEY = THE_KEY

## Command line

### With Maven
mvn clean spring-boot:run -Dspring-boot.run.profiles=localwithh2 -DOS_MAPPING_KEY=THE_KEY

### With Java
java -jar -Dspring.profiles.active=localwithh2 -DOS_MAPPING_KEY=THE_KEY target/airqualitylimitedjs-0.0.1-SNAPSHOT.jar