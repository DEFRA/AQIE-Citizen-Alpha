package com.example.airqualitylimitedjs.mapper;

import com.example.airqualitylimitedjs.domain.Measurement;
import com.example.airqualitylimitedjs.dto.MeasurementDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MeasurementMapper {
    Measurement measurementDtoToMeasurement(MeasurementDto dto);

    @Mapping(target="siteId", source="entity.site.id")
    MeasurementDto measurementToMeasurementDto(Measurement entity);

    List<MeasurementDto> map(List<Measurement> measurements);
}
