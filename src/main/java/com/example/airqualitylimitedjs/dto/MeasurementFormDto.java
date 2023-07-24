package com.example.airqualitylimitedjs.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeasurementFormDto {
    private String siteId;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
}
