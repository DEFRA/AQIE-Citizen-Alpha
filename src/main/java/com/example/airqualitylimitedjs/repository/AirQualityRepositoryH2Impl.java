package com.example.airqualitylimitedjs.repository;

import com.example.airqualitylimitedjs.domain.AirQuality;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// TODO When AirQuality is made a @Entity, replicate what was done for Site with CustomisedSiteRepository. DBConfig can then be deleted.
public class AirQualityRepositoryH2Impl implements AirQualityRepository {
    @Override
    public List<AirQuality> findNearest(Double easting, Double northing) {
        List<AirQuality> result = new ArrayList<>();

        AirQuality airQuality = new AirQuality();
        airQuality.setId(1);
        airQuality.setIndex(3);
        airQuality.setPolygon("POLYGON((-59000 1221000,-59000 1223000,-57000 1223000,-57000 1221000,-59000 1221000))");
        result.add(airQuality);

        return result;
    }

    @Override
    public Optional<AirQuality> findById(Integer airQualityId) {
        // TODO
        return Optional.empty();
    }
}
