package com.example.airqualitylimitedjs.mapper;

import com.example.airqualitylimitedjs.domain.Site;
import com.example.airqualitylimitedjs.dto.SiteDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SiteMapper {
    Site siteDtoToSite(SiteDto dto);
    SiteDto siteToSiteDto(Site entity);
    List<SiteDto> map(List<Site> sites);
}
