package com.example.airqualitylimitedjs.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Table(name = "measurements")
@Entity
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measurements_generator")
    @SequenceGenerator(name = "measurements_generator", sequenceName = "measurements_id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    private Site site;

    @ElementCollection
    @CollectionTable(name = "measurement_pollutant_mapping",
            joinColumns = {@JoinColumn(name = "measurement_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "pollutant_code")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "quantity")
    private Map<PollutantCode, Integer> pollutantCodeQuantityMap;

    private LocalDateTime sampledTime;
}
