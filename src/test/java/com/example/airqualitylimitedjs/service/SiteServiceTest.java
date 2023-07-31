package com.example.airqualitylimitedjs.service;

import com.example.airqualitylimitedjs.domain.LocationPoint;
import com.example.airqualitylimitedjs.domain.Site;
import com.example.airqualitylimitedjs.exception.LocationException;
import com.example.airqualitylimitedjs.repository.SiteRepository;
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

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SiteServiceTest {
    @Mock
    private PostcodeService postcodeService;

    @Mock
    private SiteRepository siteRepository;

    @InjectMocks
    private SiteService siteService;

    @Nested
    @DisplayName("Tests for findNearest")
    class findNearest {
        @Test
        public void givenValidPostcode_expectSiteList() throws LocationException {
            final String validPostcode = generatePostcode();
            LocationPoint locationPoint = new LocationPoint();
            final Double easting = Math.random();
            locationPoint.setEasting(easting);
            final Double northing = Math.random();
            locationPoint.setNorthing(northing);
            when(postcodeService.getLocationPoint(validPostcode)).thenReturn(locationPoint);

            Site site = new Site();
            final List<Site> siteList = List.of(site);
            when(siteRepository.findNearest(easting, northing)).thenReturn(siteList);

            assertEquals(siteList, siteService.findNearest(validPostcode));
        }

        @Test
        public void givenInvalidPostcode_expectLocationException() throws LocationException {
            final String invalidPostcode = generatePostcode();
            final String errorMsg = String.format("%s does no exist", invalidPostcode);
            when(postcodeService.getLocationPoint(invalidPostcode)).thenThrow(new LocationException(errorMsg, HttpStatusCode.valueOf(404)));

            Exception exception = assertThrows(LocationException.class, () -> siteService.findNearest(invalidPostcode));
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

            String errorMsg = "Unable to access the site db";
            when(siteRepository.findNearest(easting, northing)).thenThrow(new DataRetrievalFailureException(errorMsg));

            Exception exception = assertThrows(DataRetrievalFailureException.class, () -> siteService.findNearest(validPostcode));
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
        public void givenExistingSiteId_expectSiteToBeRetrieved() {
            final String siteId = generateSiteId();
            Site site = new Site();
            when(siteRepository.findById(siteId)).thenReturn(Optional.of(site));

            assertEquals(site, siteService.findById(siteId).get());
        }

        @Test
        public void givenNonExistingSiteId_expectEmptyOptional() {
            final String siteId = generateSiteId();
            when(siteRepository.findById(siteId)).thenReturn(Optional.empty());

            assertTrue(siteService.findById(siteId).isEmpty());
        }

        @Test
        public void givenDbIssue_expectException() {
            final String siteId = generateSiteId();
            String errorMsg = "Unable to access the site db";
            when(siteRepository.findById(siteId)).thenThrow(new DataRetrievalFailureException(errorMsg));

            Exception exception = assertThrows(DataRetrievalFailureException.class, () -> siteService.findById(siteId));
            assertEquals(errorMsg, exception.getMessage());
        }

        private String generateSiteId() {
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            return RandomStringUtils.random(length, useLetters, useNumbers);
        }
    }

    @Nested
    @DisplayName("Tests for findAll")
    class findAll {
        @Test
        public void givenSomeSites_expectAllSitesToBeRetrieved() {
            Site site1 = new Site();
            Site site2 = new Site();
            List<Site> existingSites = List.of(site1, site2);
            when(siteRepository.findAll()).thenReturn(existingSites);

            assertEquals(existingSites, siteService.findAll());
        }

        @Test
        public void givenNoSite_expectEmptyList() {
            when(siteRepository.findAll()).thenReturn(emptyList());
            assertTrue(siteService.findAll().isEmpty());
        }

        @Test
        public void givenDbIssue_expectException() {
            String errorMsg = "Unable to access the site db";
            when(siteRepository.findAll()).thenThrow(new DataRetrievalFailureException(errorMsg));

            Exception exception = assertThrows(DataRetrievalFailureException.class, () -> siteService.findAll());
            assertEquals(errorMsg, exception.getMessage());
        }
    }
}
