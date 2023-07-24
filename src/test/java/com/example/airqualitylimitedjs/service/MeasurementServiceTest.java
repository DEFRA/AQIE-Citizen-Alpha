package com.example.airqualitylimitedjs.service;

import com.example.airqualitylimitedjs.domain.Measurement;
import com.example.airqualitylimitedjs.domain.PollutantCode;
import com.example.airqualitylimitedjs.domain.Site;
import com.example.airqualitylimitedjs.repository.MeasurementRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
}
