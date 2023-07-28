package com.example.airqualitylimitedjs.service;

import com.example.airqualitylimitedjs.domain.AirQuality;
import com.example.airqualitylimitedjs.domain.LocationPoint;
import com.example.airqualitylimitedjs.exception.LocationException;
import com.example.airqualitylimitedjs.repository.AirQualityRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatusCode;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AirQualityServiceTest {
    @Mock
    private PostcodeService postcodeService;

    @Mock
    private AirQualityRepository airQualityRepository;

    @InjectMocks
    private AirQualityService airQualityService;

    @Nested
    @DisplayName("Tests for findNearest")
    class findNearest {
        @Test
        public void givenValidPostcode_expectAirQualityList() throws LocationException {
            final String validPostcode = generatePostcode();
            LocationPoint locationPoint = new LocationPoint();
            final Double easting = Math.random();
            locationPoint.setEasting(easting);
            final Double northing = Math.random();
            locationPoint.setNorthing(northing);
            when(postcodeService.getLocationPoint(validPostcode)).thenReturn(locationPoint);

            AirQuality airQuality = new AirQuality();
            final List<AirQuality> airQualityList = List.of(airQuality);
            when(airQualityRepository.findNearest(easting, northing)).thenReturn(airQualityList);

            assertEquals(airQualityList, airQualityService.findNearest(validPostcode));
        }

        @Test
        public void givenInvalidPostcode_expectLocationException() throws LocationException {
            final String invalidPostcode = generatePostcode();
            final String errorMsg = String.format("%s does no exist", invalidPostcode);
            when(postcodeService.getLocationPoint(invalidPostcode)).thenThrow(new LocationException(errorMsg, HttpStatusCode.valueOf(404)));

            Exception exception = assertThrows(LocationException.class, () -> airQualityService.findNearest(invalidPostcode));
            assertEquals(errorMsg, exception.getMessage());
        }

        @Test
        public void givenValidPostcode_andDbIssue_expectException() throws LocationException {
            final String validPostcode = generatePostcode();
            LocationPoint locationPoint = new LocationPoint();
            final Double easting = Math.random();
            locationPoint.setEasting(easting);
            final Double northing = Math.random();
            locationPoint.setNorthing(northing);
            when(postcodeService.getLocationPoint(validPostcode)).thenReturn(locationPoint);

            String errorMsg = "Unable to access the air quality db";
            when(airQualityRepository.findNearest(easting, northing)).thenThrow(new DataRetrievalFailureException(errorMsg));

            Exception exception = assertThrows(DataRetrievalFailureException.class, () -> airQualityService.findNearest(validPostcode));
            assertEquals(errorMsg, exception.getMessage());
        }


        private String generatePostcode() {
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            return RandomStringUtils.random(length, useLetters, useNumbers);
        }
    }

    @Nested
    @DisplayName("Tests for findById")
    class findById {
        @Test
        public void givenExistingAirQualityId_expectAirQualityToBeRetrieved() {
            final Integer airQualityId = new Random().nextInt();
            AirQuality airQuality = new AirQuality();
            when(airQualityRepository.findById(airQualityId)).thenReturn(Optional.of(airQuality));

            assertEquals(airQuality, airQualityService.findById(airQualityId).get());
        }

        @Test
        public void givenNonExistingAirQualityId_expectEmptyOptional() {
            final Integer airQualityId = new Random().nextInt();
            when(airQualityRepository.findById(airQualityId)).thenReturn(Optional.empty());

            assertTrue(airQualityService.findById(airQualityId).isEmpty());
        }

        @Test
        public void givenDbIssue_expectException() {
            final Integer airQualityId = new Random().nextInt();
            String errorMsg = "Unable to access the air quality db";
            when(airQualityRepository.findById(airQualityId)).thenThrow(new DataRetrievalFailureException(errorMsg));

            Exception exception = assertThrows(DataRetrievalFailureException.class, () -> airQualityService.findById(airQualityId));
            assertEquals(errorMsg, exception.getMessage());
        }
    }
}
