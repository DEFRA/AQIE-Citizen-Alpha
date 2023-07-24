package com.example.airqualitylimitedjs.repository;

import com.example.airqualitylimitedjs.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, String>, CustomisedSiteRepository {
}
