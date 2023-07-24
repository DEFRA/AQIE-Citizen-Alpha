package com.example.airqualitylimitedjs.endpoint;

import com.example.airqualitylimitedjs.domain.Measurement;
import com.example.airqualitylimitedjs.domain.PollutantCode;
import com.example.airqualitylimitedjs.domain.Site;
import com.example.airqualitylimitedjs.dto.MeasurementDto;
import com.example.airqualitylimitedjs.exception.MeasurementNotFoundException;
import com.example.airqualitylimitedjs.exception.SiteNotFoundException;
import com.example.airqualitylimitedjs.mapper.MeasurementMapper;
import com.example.airqualitylimitedjs.service.MeasurementService;
import com.example.airqualitylimitedjs.service.SiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.airqualitylimitedjs.endpoint.SiteController.SITE_NOT_FOUND_MSG;
import static java.lang.String.format;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("api/measurements")
public class MeasurementController {
    private static final String MEASUREMENT_NOT_FOUND_MSG = "No measurement found with id %d";
    private static final String MEASUREMENT_NOT_FOUND_FOR_SITE_MSG = "No measurement found for pollutant %s at site id %s";

    private final SiteService siteService;
    private final MeasurementService measurementService;

    private final MeasurementMapper measurementMapper;

    public MeasurementController(SiteService siteService, MeasurementService measurementService, MeasurementMapper measurementMapper) {
        this.siteService = siteService;
        this.measurementService = measurementService;
        this.measurementMapper = measurementMapper;
    }

    @GetMapping
    public CollectionModel<EntityModel<MeasurementDto>> findAll(@RequestParam(value = "siteId", required = false) String siteId,
                                                                @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                                @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        log.info("Entering findAll with siteId = {} - from = {} - to = {}", siteId, from, to);

        List<Measurement> measurements = measurementService.findAll(siteId, from, to);
        log.debug("Found {} measurements", measurements.size());

        List<EntityModel<MeasurementDto>> measurementDtos = measurementMapper.map(measurements).stream().map(measurement -> {
                    try {
                        return EntityModel.of(measurement,
                                linkTo(methodOn(MeasurementController.class).findOne(measurement.getId())).withSelfRel());
                    } catch (MeasurementNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }) //
                .toList();
        return CollectionModel.of(measurementDtos, linkTo(methodOn(MeasurementController.class).findAll(siteId, from, to)).withSelfRel());
    }

    @GetMapping("/{measurementId}")
    public EntityModel<MeasurementDto> findOne(@PathVariable("measurementId") Integer measurementId) throws MeasurementNotFoundException {
        log.info("Entering findOne with measurementId = {}", measurementId);
        Optional<Measurement> measurementOptional = measurementService.findById(measurementId);
        if (measurementOptional.isPresent()) {
            MeasurementDto measurementDto = measurementMapper.measurementToMeasurementDto(measurementOptional.get());
            return EntityModel.of(measurementDto,
                    linkTo(methodOn(MeasurementController.class).findOne(measurementId)).withSelfRel(),
                    linkTo(methodOn(MeasurementController.class).findAll(null, null, null)).withRel("measurements"));
        } else {
            String errorMsg = format(MEASUREMENT_NOT_FOUND_MSG, measurementId);
            log.error(errorMsg);
            throw new MeasurementNotFoundException(errorMsg);
        }
    }

    @PostMapping
    public EntityModel<MeasurementDto> saveMeasurement(@RequestBody MeasurementDto measurementDto) throws SiteNotFoundException, MeasurementNotFoundException {
        log.info("Entering saveMeasurement with measurementDto = {}", measurementDto);
        final String siteId = measurementDto.getSiteId();
        Optional<Site> siteOpt = siteService.findById(siteId);
        if (siteOpt.isEmpty()) {
            String errorMsg = format(SITE_NOT_FOUND_MSG, siteId);
            log.error(errorMsg);
            throw new SiteNotFoundException(errorMsg);
        } else {
            Measurement partiallyDefinedMeasurement = measurementMapper.measurementDtoToMeasurement(measurementDto);
            partiallyDefinedMeasurement.setSite(siteOpt.get());
            Measurement createdMeasurement = measurementService.create(partiallyDefinedMeasurement);
            return EntityModel.of(measurementMapper.measurementToMeasurementDto(createdMeasurement),
                    linkTo(methodOn(MeasurementController.class).findOne(createdMeasurement.getId())).withSelfRel(),
                    linkTo(methodOn(MeasurementController.class).findAll(null, null, null)).withRel("measurements"));
        }
    }

    @GetMapping(value = "/highest")
    public CollectionModel<EntityModel<MeasurementDto>> highestMeasurements(@RequestParam(value = "siteId") String siteId,
                                                                            @RequestParam(value = "pollutantCode") PollutantCode pollutantCode,
                                                                            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                                            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to)
            throws MeasurementNotFoundException {
        log.info("Entering highestMeasurements with siteId = {} - pollutantCode = {} - from = {} - to = {}", siteId, pollutantCode, from, to);
        List<Measurement> result = measurementService.findHighestMeasurements(siteId, pollutantCode, from, to);
        if (result == null || result.isEmpty()) {
            StringBuilder errorMsgBuilder = new StringBuilder(format(MEASUREMENT_NOT_FOUND_FOR_SITE_MSG, pollutantCode, siteId));
            if (from != null) {
                errorMsgBuilder.append(" between ");
                errorMsgBuilder.append(from);
                errorMsgBuilder.append(" and ");
                errorMsgBuilder.append(Objects.requireNonNullElseGet(to, LocalDateTime::now));
            }
            String errorMsg = errorMsgBuilder.toString();
            log.error(errorMsg);
            throw new MeasurementNotFoundException(errorMsg);
        }

        List<EntityModel<MeasurementDto>> measurementDtos = measurementMapper.map(result).stream().map(measurement -> {
                    try {
                        return EntityModel.of(measurement,
                                linkTo(methodOn(MeasurementController.class).findOne(measurement.getId())).withSelfRel());
                    } catch (MeasurementNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }) //
                .toList();
        return CollectionModel.of(measurementDtos, linkTo(methodOn(MeasurementController.class).highestMeasurements(siteId, pollutantCode, from, to)).withSelfRel());
    }
}
