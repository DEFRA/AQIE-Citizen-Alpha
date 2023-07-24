package com.example.airqualitylimitedjs.service;

import com.example.airqualitylimitedjs.domain.LocationPoint;
import com.example.airqualitylimitedjs.domain.Site;
import com.example.airqualitylimitedjs.exception.LocationException;
import com.example.airqualitylimitedjs.repository.SiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiteService {
    private final PostcodeService postcodeService;
    private final SiteRepository siteRepository;

    public SiteService(PostcodeService postcodeService, SiteRepository siteRepository) {
        this.postcodeService = postcodeService;
        this.siteRepository = siteRepository;
    }

    public List<Site> findNearest(String postcode) throws LocationException {
        LocationPoint locationPoint = postcodeService.getLocationPoint(postcode);
        return siteRepository.findNearest(locationPoint.getEasting(), locationPoint.getNorthing());
    }

    public List<Site> findAll() {
        return siteRepository.findAll();
    }

    public Optional<Site> findById(String site_id) {
        return siteRepository.findById(site_id);
    }

}
