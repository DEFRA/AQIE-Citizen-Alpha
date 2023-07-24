package com.example.airqualitylimitedjs.repository.rowmapper;

import com.example.airqualitylimitedjs.domain.Site;
import com.example.airqualitylimitedjs.domain.UsageType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.airqualitylimitedjs.repository.CustomisedSiteRepositoryImpl.EASTING_COLUMN_NAME;
import static com.example.airqualitylimitedjs.repository.CustomisedSiteRepositoryImpl.ID_COLUMN_NAME;
import static com.example.airqualitylimitedjs.repository.CustomisedSiteRepositoryImpl.NAME_COLUMN_NAME;
import static com.example.airqualitylimitedjs.repository.CustomisedSiteRepositoryImpl.NORTHING_COLUMN_NAME;
import static com.example.airqualitylimitedjs.repository.CustomisedSiteRepositoryImpl.UKA_SITE_ID_COLUMN_NAME;
import static com.example.airqualitylimitedjs.repository.CustomisedSiteRepositoryImpl.USAGE_TYPE_COLUMN_NAME;

public class SiteRowMapper implements RowMapper<Site> {
    @Override
    public Site mapRow(ResultSet rs, int rowNum) throws SQLException {
        Site site = new Site();
        site.setId(rs.getString(ID_COLUMN_NAME));
        site.setName(rs.getString(NAME_COLUMN_NAME));
        site.setUka_site_id(rs.getString(UKA_SITE_ID_COLUMN_NAME));
        site.setUsage_type(UsageType.valueOf(rs.getString(USAGE_TYPE_COLUMN_NAME)));
        site.setEasting(rs.getDouble(EASTING_COLUMN_NAME));
        site.setNorthing(rs.getDouble(NORTHING_COLUMN_NAME));
        return site;
    }
}
