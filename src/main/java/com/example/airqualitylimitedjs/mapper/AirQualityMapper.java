package com.example.airqualitylimitedjs.mapper;

import com.example.airqualitylimitedjs.domain.AirQuality;
import com.example.airqualitylimitedjs.dto.AirQualityDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AirQualityMapper {
    AirQuality airQualityDtoToAirQuality(AirQualityDto dto);
    AirQualityDto airQualityToAirQualityDto(AirQuality entity);
    List<AirQualityDto> map(List<AirQuality> entities);
}
