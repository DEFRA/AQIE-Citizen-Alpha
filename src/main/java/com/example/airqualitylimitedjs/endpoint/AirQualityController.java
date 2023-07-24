package com.example.airqualitylimitedjs.endpoint;

import com.example.airqualitylimitedjs.domain.AirQuality;
import com.example.airqualitylimitedjs.dto.AirQualityDto;
import com.example.airqualitylimitedjs.exception.AirQualityNotFoundException;
import com.example.airqualitylimitedjs.exception.LocationException;
import com.example.airqualitylimitedjs.mapper.AirQualityMapper;
import com.example.airqualitylimitedjs.service.AirQualityService;
import com.example.airqualitylimitedjs.validation.Postcode;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("api/aq")
@Validated
public class AirQualityController {
    private static final String AIR_QUALITY_NOT_FOUND_MSG = "Air Quality with id %d does not exist.";

    private final AirQualityService airQualityService;
    private final AirQualityMapper airQualityMapper;

    public AirQualityController(AirQualityService airQualityService, AirQualityMapper airQualityMapper) {
        this.airQualityService = airQualityService;
        this.airQualityMapper = airQualityMapper;
    }

    @GetMapping("/{airQualityId}")
    public EntityModel<AirQualityDto> findOne(@PathVariable("airQualityId") Integer airQualityId) throws AirQualityNotFoundException {
        log.info("Entering findOne with airQualityId = {}", airQualityId);
        Optional<AirQuality> airQualityOptional = airQualityService.findById(airQualityId);
        if (airQualityOptional.isPresent()) {
            AirQualityDto airQualityDto = airQualityMapper.airQualityToAirQualityDto(airQualityOptional.get());
            return EntityModel.of(airQualityDto,
                    linkTo(methodOn(AirQualityController.class).findOne(airQualityId)).withSelfRel());
        } else {
            String errorMsg = format(AIR_QUALITY_NOT_FOUND_MSG, airQualityId);
            log.error(errorMsg);
            throw new AirQualityNotFoundException(errorMsg);
        }
    }

    @GetMapping("/nearest")
    public CollectionModel<EntityModel<AirQualityDto>> nearest(@RequestParam(value = "postcode") @NotEmpty @Postcode String postcode) throws LocationException {
        log.info("Entering nearest with postcode = {}", postcode);

        List<AirQuality> airQualities = airQualityService.findNearest(postcode);
        log.info("airQualities size = {}", airQualities.size());

        List<AirQualityDto> airQualityDtos = airQualityMapper.map(airQualities);
        List<EntityModel<AirQualityDto>> result = airQualityDtos.stream().map(airQuality -> {
                    try {
                        return EntityModel.of(airQuality,
                                linkTo(methodOn(AirQualityController.class).findOne(airQuality.getId())).withSelfRel());
                    } catch (AirQualityNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }) //
                .toList();
        return CollectionModel.of(result, linkTo(methodOn(AirQualityController.class).nearest(postcode)).withSelfRel());
    }
}
