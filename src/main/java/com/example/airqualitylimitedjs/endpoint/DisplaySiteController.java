package com.example.airqualitylimitedjs.endpoint;

import com.example.airqualitylimitedjs.dto.AddressDto;
import com.example.airqualitylimitedjs.dto.ErrorDto;
import com.example.airqualitylimitedjs.exception.LocationException;
import com.example.airqualitylimitedjs.mapper.SiteMapper;
import com.example.airqualitylimitedjs.service.SiteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DisplaySiteController {
    private final SiteService siteService;
    private final SiteMapper siteMapper;

    public DisplaySiteController(SiteService siteService, SiteMapper siteMapper) {
        this.siteService = siteService;
        this.siteMapper = siteMapper;
    }

    @GetMapping("/sites")
    public String findAll(Model model) {
        model.addAttribute("siteDtos", siteMapper.map(siteService.findAll()));
        return "sites/allSites";
    }

    @GetMapping("/nearestSites")
    public String nearestSitesForm(Model model) {
        model.addAttribute("addressDto", new AddressDto());
        return "sites/nearestSitesForm";
    }

    @PostMapping("/nearestSites")
    public String nearestSitesSubmit(@ModelAttribute AddressDto addressDto, Model model) {
        model.addAttribute("addressDto", addressDto);

        try {
            model.addAttribute("siteDtos", siteMapper.map(siteService.findNearest(addressDto.getPostcode())));
            return "sites/nearestSitesResult";
        } catch (LocationException e) {
            ErrorDto errorDto = new ErrorDto();
            errorDto.setMessage(e.getMessage());
            model.addAttribute("errorDto", errorDto);
            return "sites/error";
        }
    }
}