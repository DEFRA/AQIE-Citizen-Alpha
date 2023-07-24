package com.example.airqualitylimitedjs.endpoint;

import com.example.airqualitylimitedjs.domain.Measurement;
import com.example.airqualitylimitedjs.domain.PollutantCode;
import com.example.airqualitylimitedjs.dto.ErrorDto;
import com.example.airqualitylimitedjs.dto.HighestMeasurementFormDto;
import com.example.airqualitylimitedjs.dto.MeasurementFormDto;
import com.example.airqualitylimitedjs.mapper.MeasurementMapper;
import com.example.airqualitylimitedjs.service.MeasurementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class DisplayMeasurementController {
    private final MeasurementService measurementService;
    private final MeasurementMapper measurementMapper;

    public DisplayMeasurementController(MeasurementService measurementService, MeasurementMapper measurementMapper) {
        this.measurementService = measurementService;
        this.measurementMapper = measurementMapper;
    }

    @GetMapping("/measurements")
    public String measurementsForm(@RequestParam(name="siteId", required=true) String siteId, Model model) {
        model.addAttribute("siteId", siteId);
        return "measurements/measurementsForm";
    }

    @GetMapping("/highest_measurements")
    public String highestMeasurementsForm(@RequestParam(name="siteId", required=true) String siteId, Model model) {
        model.addAttribute("siteId", siteId);
        model.addAttribute("pollutantCodeOptions", Arrays.asList(PollutantCode.values()));
        return "measurements/highestMeasurementsForm";
    }

    @PostMapping("/measurements")
    public String measurementsSubmit(@ModelAttribute MeasurementFormDto measurementFormDto, Model model) {
        model.addAttribute("measurementFormDto", measurementFormDto);

        try {
            List<Measurement> measurements = measurementService.findAll(measurementFormDto.getSiteId(), measurementFormDto.getFromTime(), measurementFormDto.getToTime());
            model.addAttribute("measurementDtos", measurementMapper.map(measurements));
            return "measurements/measurementsResult";
        } catch (Exception e) {
            ErrorDto errorDto = new ErrorDto();
            errorDto.setMessage(e.getMessage());
            model.addAttribute("errorDto", errorDto);
            return "measurements/error";
        }
    }

    @PostMapping("/highest_measurements")
    public String highestMeasurementsSubmit(@ModelAttribute HighestMeasurementFormDto highestMeasurementFormDto, Model model) {
        model.addAttribute("highestMeasurementFormDto", highestMeasurementFormDto);

        try {
            List<Measurement> measurements = measurementService.findHighestMeasurements(highestMeasurementFormDto.getSiteId(), highestMeasurementFormDto.getPollutantCode(), highestMeasurementFormDto.getFromTime(), highestMeasurementFormDto.getToTime());
            model.addAttribute("measurementDtos", measurementMapper.map(measurements));
            return "measurements/highestMeasurementsResult";
        } catch (Exception e) {
            ErrorDto errorDto = new ErrorDto();
            errorDto.setMessage(e.getMessage());
            model.addAttribute("errorDto", errorDto);
            return "measurements/error";
        }
    }
}
