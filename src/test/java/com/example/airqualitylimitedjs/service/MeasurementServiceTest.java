package com.example.airqualitylimitedjs.service;

import com.example.airqualitylimitedjs.domain.Measurement;
import com.example.airqualitylimitedjs.domain.PollutantCode;
import com.example.airqualitylimitedjs.domain.Site;
import com.example.airqualitylimitedjs.repository.MeasurementRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataRetrievalFailureException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeasurementServiceTest {
    @Mock
    private MeasurementRepository measurementRepository;

    @InjectMocks
    private MeasurementService measurementService;

    @Nested
    @DisplayName("Tests for findHighestMeasurements")
    class findHighestMeasurements {
        @Test
        public void givenNoMeasurement_expectEmptyList() {
            final String stationId = "1";
            when(measurementRepository.findBySiteId(stationId)).thenReturn(emptyList());
            assertTrue(measurementService.findHighestMeasurements(stationId, PollutantCode.OZONE, null, null).isEmpty());
        }

        @Test
        public void givenASingleMeasurementAtTopValue_expectListContainingOnlyThisMeasurement() {
            final String stationId = "1";

            List<Measurement> measurementList = new ArrayList<>();
            Measurement topMeasurement = buildMeasurement(1, stationId, PollutantCode.OZONE, 13);
            measurementList.add(topMeasurement);
            measurementList.add(buildMeasurement(2, stationId, PollutantCode.OZONE, 5));
            measurementList.add(buildMeasurement(3, stationId, PollutantCode.PM10, 25));
            when(measurementRepository.findBySiteId(stationId)).thenReturn(measurementList);

            List<Measurement> results = measurementService.findHighestMeasurements(stationId, PollutantCode.OZONE, null, null);
            assertEquals(1, results.size());
            assertEquals(topMeasurement, results.get(0));
        }

        @Test
        public void givenMultipleMeasurementsAtTopValue_expectListContainingAllMeasurementsAtTopValue() {
            final String stationId = "1";
            final Integer topValue = 13;

            List<Measurement> measurementList = new ArrayList<>();
            Measurement topMeasurement_1 = buildMeasurement(1, stationId, PollutantCode.OZONE, topValue);
            measurementList.add(topMeasurement_1);
            measurementList.add(buildMeasurement(2, stationId, PollutantCode.OZONE, 5));
            Measurement topMeasurement_2 = buildMeasurement(3, stationId, PollutantCode.OZONE, topValue);
            measurementList.add(topMeasurement_2);
            measurementList.add(buildMeasurement(4, stationId, PollutantCode.OZONE, 8));
            Measurement topMeasurement_3 = buildMeasurement(5, stationId, PollutantCode.OZONE, topValue);
            measurementList.add(topMeasurement_3);
            when(measurementRepository.findBySiteId(stationId)).thenReturn(measurementList);

            List<Measurement> results = measurementService.findHighestMeasurements(stationId, PollutantCode.OZONE, null, null);
            assertEquals(3, results.size());
            assertTrue(results.containsAll(List.of(topMeasurement_1, topMeasurement_2, topMeasurement_3)));
        }

        private Measurement buildMeasurement(Integer measurementId, String stationId, PollutantCode pollutantCode, Integer quantity) {
            Measurement result = new Measurement();
            result.setId(measurementId);

            Map<PollutantCode, Integer> pollutantCodeQuantityMap = new HashMap<>();
            pollutantCodeQuantityMap.put(pollutantCode, quantity);
            result.setPollutantCodeQuantityMap(pollutantCodeQuantityMap);

            Site site = new Site();
            site.setId(stationId);
            result.setSite(site);

            result.setSampledTime(LocalDateTime.now());
            return result;
        }
    }

    @Nested
    @DisplayName("Tests for findById")
    class findById {
        @Test
        public void givenExistingMeasurementId_expectMeasurementToBeRetrieved() {
            final Integer measurementId = new Random().nextInt();
            Measurement measurement = new Measurement();
            when(measurementRepository.findById(measurementId)).thenReturn(Optional.of(measurement));

            assertEquals(measurement, measurementService.findById(measurementId).get());
        }

        @Test
        public void givenNonExistingMeasurementId_expectEmptyOptional() {
            final Integer measurementId = new Random().nextInt();
            when(measurementRepository.findById(measurementId)).thenReturn(Optional.empty());

            assertTrue(measurementService.findById(measurementId).isEmpty());
        }

        @Test
        public void givenDbIssue_expectException() {
            final Integer measurementId = new Random().nextInt();
            String errorMsg = "Unable to access the measurement db";
            when(measurementRepository.findById(measurementId)).thenThrow(new DataRetrievalFailureException(errorMsg));

            Exception exception = assertThrows(DataRetrievalFailureException.class, () -> measurementService.findById(measurementId));
            assertEquals(errorMsg, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests for create")
    class create {
        @Test
        public void givenMeasurement_expectMeasurementToBeSaved() {
            final Measurement measurement = new Measurement();
            measurementService.create(measurement);
            verify(measurementRepository, times(1)).save(measurement);
        }

        @Test
        public void givenDbIssue_expectException() {
            final Measurement measurement = new Measurement();
            String errorMsg = "Unable to access the measurement db";
            when(measurementRepository.save(measurement)).thenThrow(new DataRetrievalFailureException(errorMsg));

            Exception exception = assertThrows(DataRetrievalFailureException.class, () -> measurementService.create(measurement));
            assertEquals(errorMsg, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests for findAll")
    class findAll {
        @ParameterizedTest
        @NullSource
        @MethodSource("provideTo")
        public void givenNullSiteIdAndFrom_expectAllMeasurements(LocalDateTime to) {
            final Measurement measurement1 = new Measurement();
            final Measurement measurement2 = new Measurement();
            List<Measurement> result = List.of(measurement1, measurement2);
            when(measurementRepository.findAll()).thenReturn(result);

            assertEquals(result, measurementService.findAll(null, null, to));
            verify(measurementRepository, times(1)).findAll();
            verifyNoMoreInteractions(measurementRepository);
        }

        private static Stream<Arguments> provideTo() {
            return Stream.of(
                    Arguments.of(LocalDateTime.now()),
                    Arguments.of(LocalDateTime.of(2023, 07, 14, 13, 13))
            );
        }

        @Test
        public void givenNullSiteIdAndNonNullFromAndTo_expectAllMeasurementsBetweenFromAndTo() {
            final LocalDateTime from = LocalDateTime.MIN;
            final LocalDateTime to = from.plusDays(Math.abs(new Random().nextInt()));

            final Measurement measurement1 = new Measurement();
            final Measurement measurement2 = new Measurement();
            List<Measurement> result = List.of(measurement1, measurement2);
            when(measurementRepository.findBySampledTimeBetween(from, to)).thenReturn(result);

            assertEquals(result, measurementService.findAll(null, from, to));
            verify(measurementRepository, times(1)).findBySampledTimeBetween(from, to);
            verifyNoMoreInteractions(measurementRepository);
        }

        @Test
        public void givenNullSiteIdAndToAndNonNullFrom_expectAllMeasurementsBetweenFromAndNow() {
            try (MockedStatic<LocalDateTime> localDateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
                LocalDateTime mockedNow = LocalDateTime.now();
                localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(mockedNow);

                final LocalDateTime from = LocalDateTime.MIN;

                final Measurement measurement1 = new Measurement();
                final Measurement measurement2 = new Measurement();
                List<Measurement> result = List.of(measurement1, measurement2);
                when(measurementRepository.findBySampledTimeBetween(from, mockedNow)).thenReturn(result);

                assertEquals(result, measurementService.findAll(null, from, null));
                verify(measurementRepository, times(1)).findBySampledTimeBetween(from, mockedNow);
                verifyNoMoreInteractions(measurementRepository);
            }
        }

        @ParameterizedTest
        @NullSource
        @MethodSource("provideTo")
        public void givenNonNullSiteIdAndNullFrom_expectAllMeasurements(LocalDateTime to) {
            final String siteId = generateSiteId();
            final Measurement measurement1 = new Measurement();
            final Measurement measurement2 = new Measurement();
            List<Measurement> result = List.of(measurement1, measurement2);
            when(measurementRepository.findBySiteId(siteId)).thenReturn(result);

            assertEquals(result, measurementService.findAll(siteId, null, to));
            verify(measurementRepository, times(1)).findBySiteId(siteId);
            verifyNoMoreInteractions(measurementRepository);
        }

        @Test
        public void givenNonNullSiteIdAndFromAndTo_expectAllMeasurementsBetweenFromAndToForGivenSite() {
            final String siteId = generateSiteId();
            final LocalDateTime from = LocalDateTime.MIN;
            final LocalDateTime to = from.plusDays(Math.abs(new Random().nextInt()));

            final Measurement measurement1 = new Measurement();
            final Measurement measurement2 = new Measurement();
            List<Measurement> result = List.of(measurement1, measurement2);
            when(measurementRepository.findBySiteIdAndSampledTimeBetween(siteId, from, to)).thenReturn(result);

            assertEquals(result, measurementService.findAll(siteId, from, to));
            verify(measurementRepository, times(1)).findBySiteIdAndSampledTimeBetween(siteId, from, to);
            verifyNoMoreInteractions(measurementRepository);
        }

        @Test
        public void givenNullToAndNonNullSiteIdAndFrom_expectAllMeasurementsBetweenFromAndNow() {
            try (MockedStatic<LocalDateTime> localDateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
                final String siteId = generateSiteId();
                LocalDateTime mockedNow = LocalDateTime.now();
                localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(mockedNow);

                final LocalDateTime from = LocalDateTime.MIN;

                final Measurement measurement1 = new Measurement();
                final Measurement measurement2 = new Measurement();
                List<Measurement> result = List.of(measurement1, measurement2);
                when(measurementRepository.findBySiteIdAndSampledTimeBetween(siteId, from, mockedNow)).thenReturn(result);

                assertEquals(result, measurementService.findAll(siteId, from, null));
                verify(measurementRepository, times(1)).findBySiteIdAndSampledTimeBetween(siteId, from, mockedNow);
                verifyNoMoreInteractions(measurementRepository);
            }
        }

        private String generateSiteId() {
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            return RandomStringUtils.random(length, useLetters, useNumbers);
        }
    }
}
