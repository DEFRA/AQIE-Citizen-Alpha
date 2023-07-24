package com.example.airqualitylimitedjs.repository;

import com.example.airqualitylimitedjs.domain.Site;

import java.util.List;

public interface CustomisedSiteRepository {
    List<Site> findNearest(Double easting, Double northing);
}
