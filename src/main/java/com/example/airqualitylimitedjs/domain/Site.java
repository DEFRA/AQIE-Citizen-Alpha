package com.example.airqualitylimitedjs.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "site")
@Entity
public class Site {
    @Id
    @GeneratedValue
    private String id;

    private String name;

    private String uka_site_id;

    @Enumerated(EnumType.STRING)
    private UsageType usage_type;

    private Double easting;
    private Double northing;
}
