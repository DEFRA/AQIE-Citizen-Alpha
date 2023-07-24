package com.example.airqualitylimitedjs.service;

import com.example.airqualitylimitedjs.domain.AirQuality;
import com.example.airqualitylimitedjs.domain.LocationPoint;
import com.example.airqualitylimitedjs.exception.LocationException;
import com.example.airqualitylimitedjs.repository.AirQualityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AirQualityService {
    private final PostcodeService postcodeService;
    private final AirQualityRepository airQualityRepository;

    public AirQualityService(PostcodeService postcodeService, AirQualityRepository airQualityRepository) {
        this.postcodeService = postcodeService;
        this.airQualityRepository = airQualityRepository;
        log.info("Class of airQualityRepository is {}", airQualityRepository.getClass());
    }

    public List<AirQuality> findNearest(String postcode) throws LocationException {
        LocationPoint locationPoint = postcodeService.getLocationPoint(postcode);
        return airQualityRepository.findNearest(locationPoint.getEasting(), locationPoint.getNorthing());
    }

    public Optional<AirQuality> findById(Integer airQualityId) {
        return airQualityRepository.findById(airQualityId);
    }
}
