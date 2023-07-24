package com.example.airqualitylimitedjs.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class MeasurementDto {
    private Integer id;

    @NotBlank(message = "The siteId is required.")
    private String siteId;

    private Map<String, Integer> pollutantCodeQuantityMap;

    private LocalDateTime sampledTime;
}
