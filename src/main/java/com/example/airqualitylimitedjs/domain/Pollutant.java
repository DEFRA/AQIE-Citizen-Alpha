package com.example.airqualitylimitedjs.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "pollutants")
@Entity
public class Pollutant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pollutants_generator")
    @SequenceGenerator(name = "pollutants_generator", sequenceName = "pollutants_id_seq", allocationSize = 1)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private PollutantCode code;

    private String name;

    private String description;
}
