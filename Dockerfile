FROM openjdk:20-jdk
MAINTAINER philippe.brossier@defra.gov.uk
COPY target/airqualitylimitedjs-0.0.1-SNAPSHOT.jar airqualitylimitedjs-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/airqualitylimitedjs-0.0.1-SNAPSHOT.jar"]