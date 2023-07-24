package com.example.airqualitylimitedjs.repository;

import com.example.airqualitylimitedjs.domain.Pollutant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollutantRepository extends JpaRepository<Pollutant, Integer> {
}
