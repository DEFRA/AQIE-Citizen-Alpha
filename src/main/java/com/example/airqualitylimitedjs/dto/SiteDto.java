package com.example.airqualitylimitedjs.dto;

import com.example.airqualitylimitedjs.domain.UsageType;
import lombok.Data;

@Data
public class SiteDto {
    private String id;

    private String name;

    private String uka_site_id;

    private UsageType usage_type;

    private Double easting;
    private Double northing;
}
