package com.example.airqualitylimitedjs.dto;

import com.example.airqualitylimitedjs.domain.PollutantCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HighestMeasurementFormDto extends MeasurementFormDto {
    private PollutantCode pollutantCode;
}
