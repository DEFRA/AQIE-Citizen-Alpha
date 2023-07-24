package com.example.airqualitylimitedjs.endpoint;

import com.example.airqualitylimitedjs.domain.Site;
import com.example.airqualitylimitedjs.dto.SiteDto;
import com.example.airqualitylimitedjs.exception.LocationException;
import com.example.airqualitylimitedjs.exception.SiteNotFoundException;
import com.example.airqualitylimitedjs.mapper.SiteMapper;
import com.example.airqualitylimitedjs.service.SiteService;
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
@RequestMapping("api/sites")
@Validated
public class SiteController {
    static final String SITE_NOT_FOUND_MSG = "Site with id %s does not exist.";

    private final SiteService siteService;
    private final SiteMapper siteMapper;

    public SiteController(SiteService siteService, SiteMapper siteMapper) {
        this.siteService = siteService;
        this.siteMapper = siteMapper;
    }

    @GetMapping("/nearest")
    public CollectionModel<EntityModel<SiteDto>> nearest(@RequestParam(value = "postcode") @NotEmpty @Postcode String postcode) throws LocationException {
        log.info("Entering nearest with postcode = {}", postcode);
        List<Site> sites = siteService.findNearest(postcode);
        List<EntityModel<SiteDto>> siteDtos = siteMapper.map(sites).stream().map(site -> {
            try {
                return EntityModel.of(site,
                        linkTo(methodOn(SiteController.class).findOne(site.getId())).withSelfRel());
            } catch (SiteNotFoundException e) {
                throw new RuntimeException(e);
            }
        }) //
        .toList();

        return CollectionModel.of(siteDtos, linkTo(methodOn(SiteController.class).nearest(postcode)).withSelfRel());
    }

    @GetMapping("/{siteId}")
    public EntityModel<SiteDto> findOne(@PathVariable("siteId") String siteId) throws SiteNotFoundException {
        log.info("Entering findOne with siteId = {}", siteId);
        Optional<Site> siteOptional = siteService.findById(siteId);
        if (siteOptional.isPresent()) {
            SiteDto siteDto = siteMapper.siteToSiteDto(siteOptional.get());
            return EntityModel.of(siteDto,
                    linkTo(methodOn(SiteController.class).findOne(siteId)).withSelfRel(),
                    linkTo(methodOn(SiteController.class).findAll()).withRel("sites"));
        } else {
            String errorMsg = format(SITE_NOT_FOUND_MSG, siteId);
            log.error(errorMsg);
            throw new SiteNotFoundException(errorMsg);
        }
    }

    @GetMapping
    public CollectionModel<EntityModel<SiteDto>> findAll() {
        log.info("Entering findAll");
        List<EntityModel<SiteDto>> siteDtos = siteMapper.map(siteService.findAll()).stream().map(site -> {
                    try {
                        return EntityModel.of(site,
                                linkTo(methodOn(SiteController.class).findOne(site.getId())).withSelfRel());
                    } catch (SiteNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }) //
                .toList();
        return CollectionModel.of(siteDtos, linkTo(methodOn(SiteController.class).findAll()).withSelfRel());
    }
}
