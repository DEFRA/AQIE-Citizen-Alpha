package com.example.airqualitylimitedjs.repository;

import com.example.airqualitylimitedjs.domain.AirQuality;

import java.util.List;
import java.util.Optional;

public interface AirQualityRepository {
    List<AirQuality> findNearest(Double easting, Double northing);
    Optional<AirQuality> findById(Integer airQualityId);
}
