package com.example.airqualitylimitedjs.service;

import com.example.airqualitylimitedjs.domain.Measurement;
import com.example.airqualitylimitedjs.domain.PollutantCode;
import com.example.airqualitylimitedjs.repository.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MeasurementService {
    private final MeasurementRepository measurementRepository;

    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public Optional<Measurement> findById(Integer measurementId) {
        return measurementRepository.findById(measurementId);
    }

    public List<Measurement> findAll(String siteId, LocalDateTime from, LocalDateTime to) {
        // TODO Attempt to simplify all these if/else
        List<Measurement> result;
        if (siteId == null) {
            if (from != null) {
                if (to == null) {
                    to = LocalDateTime.now();
                }
                result = measurementRepository.findBySampledTimeBetween(from, to);
            } else {
                result = measurementRepository.findAll();
            }
        } else {
            if (from != null) {
                if (to == null) {
                    to = LocalDateTime.now();
                }
                result = measurementRepository.findBySiteIdAndSampledTimeBetween(siteId, from, to);
            } else {
                result = measurementRepository.findBySiteId(siteId);
            }
        }
        return result;
    }

    public Measurement create(Measurement measurement) {
        return measurementRepository.save(measurement);
    }

    public List<Measurement> findHighestMeasurements(String stationId, PollutantCode pollutantCode, LocalDateTime from, LocalDateTime to) {
        List<Measurement> result = new ArrayList<>();
        List<Measurement> measurements = findAll(stationId, from, to);
        if (!measurements.isEmpty()) {
            Integer highestPollutantCodeQuantity = null;
            for (Measurement measurement : measurements) {
                Map<PollutantCode, Integer> pollutantCodeQuantityMap = measurement.getPollutantCodeQuantityMap();
                Integer pollutantCodeQuantity = pollutantCodeQuantityMap.get(pollutantCode);
                if (pollutantCodeQuantity != null) {
                    if (highestPollutantCodeQuantity == null) {
                        highestPollutantCodeQuantity = pollutantCodeQuantity;
                        result.add(measurement);
                    } else {
                        if (highestPollutantCodeQuantity.equals(pollutantCodeQuantity)) {
                            result.add(measurement);
                        }
                        if (pollutantCodeQuantity > highestPollutantCodeQuantity) {
                            highestPollutantCodeQuantity = pollutantCodeQuantity;
                            result.clear();
                            result.add(measurement);
                        }
                    }
                }
            }
        }
        return result;
    }
}
