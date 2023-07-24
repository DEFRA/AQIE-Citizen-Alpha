package com.example.airqualitylimitedjs.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DisplayMappingController {
    @GetMapping("/google_mapping")
    public String googleMapping(@RequestParam(name="easting", required=true) String easting,
                                   @RequestParam(name="northing", required=true) String northing,
                                   Model model) {
        model.addAttribute("easting", easting);
        model.addAttribute("northing", northing);
        return "mapping/google";
    }

    @GetMapping("/os_raster_mapping")
    public String osRasterMapping(@RequestParam(name="easting", required=true) String easting,
                                @RequestParam(name="northing", required=true) String northing,
                                Model model) {
        model.addAttribute("easting", easting);
        model.addAttribute("northing", northing);
        return "mapping/osRaster";
    }

    @GetMapping("/os_vector_mapping")
    public String osVectorMapping(@RequestParam(name="easting", required=true) String easting,
                                @RequestParam(name="northing", required=true) String northing,
                                Model model) {
        model.addAttribute("easting", easting);
        model.addAttribute("northing", northing);
        return "mapping/osVector";
    }
}
