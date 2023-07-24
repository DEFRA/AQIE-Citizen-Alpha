package com.example.airqualitylimitedjs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostcodeDetails {
    private String postcode;
    private Integer quality;
    private Double eastings;
    private Double northings;
    private Double latitude;
    private Double longitude;
}
