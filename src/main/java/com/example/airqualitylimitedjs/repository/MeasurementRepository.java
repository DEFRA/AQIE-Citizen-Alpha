package com.example.airqualitylimitedjs.repository;

import com.example.airqualitylimitedjs.domain.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    List<Measurement> findBySiteId(String siteId);

    List<Measurement> findBySiteIdAndSampledTimeBetween(String siteId, LocalDateTime from, LocalDateTime to);

    List<Measurement> findBySampledTimeBetween(LocalDateTime from, LocalDateTime to);
}
