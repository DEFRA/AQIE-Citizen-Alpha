package com.example.airqualitylimitedjs.repository;

import com.example.airqualitylimitedjs.domain.Site;
import com.example.airqualitylimitedjs.domain.UsageType;
import com.example.airqualitylimitedjs.repository.rowmapper.SiteRowMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class CustomisedSiteRepositoryImpl implements CustomisedSiteRepository {
    private static final String H2_MODE = "h2";

    public final static String ID_COLUMN_NAME = "id";
    public final static String NAME_COLUMN_NAME = "name";
    public final static String UKA_SITE_ID_COLUMN_NAME = "uka_site_id";
    public final static String USAGE_TYPE_COLUMN_NAME = "usage_type";
    public final static String EASTING_COLUMN_NAME = "easting";
    public final static String NORTHING_COLUMN_NAME = "northing";

    private final static String NEAREST_SQL_FIRST_PART = String.format("SELECT id AS \"%s\", name AS \"%s\", uka_site_id AS \"%s\", usage_type AS \"%s\", ST_X(geom) AS \"%s\", ST_Y(geom) AS \"%s\"",
            ID_COLUMN_NAME, NAME_COLUMN_NAME, UKA_SITE_ID_COLUMN_NAME, USAGE_TYPE_COLUMN_NAME, EASTING_COLUMN_NAME, NORTHING_COLUMN_NAME);

    // Note that, rather than using %f, I tried to use :easting & :northing with a MapSqlParameterSource. But it would not substitute.
    private final static String NEAREST_SQL_SECOND_PART = ", geom <-> 'SRID=27700;POINT(%f %f)'::geometry AS dist FROM public.site ORDER BY dist LIMIT 1";

    @Value("${database.mode}")
    private String databaseMode;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SiteRowMapper siteRowMapper;

    public CustomisedSiteRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.siteRowMapper = new SiteRowMapper();
    }

    @Override
    public List<Site> findNearest(Double easting, Double northing) {
        if (databaseMode.equalsIgnoreCase(H2_MODE)) {
            // TODO Is there a way to add a GIS extension to H2 and remove the hardcoding below?
            Site result = new Site();
            result.setId("1000002");
            result.setName("DUKINFIELD 2");
            result.setUka_site_id("UKA10609");
            result.setUsage_type(UsageType.primary);
            result.setEasting(394000d);
            result.setNorthing(397900d);
            return List.of(result);
        } else {
            // TODO: In NEAREST_SQL_SECOND_PART, make the LIMIT 1 a parameter so we can show the x nearest ones.
            String sql = NEAREST_SQL_FIRST_PART + String.format(NEAREST_SQL_SECOND_PART, easting, northing);
            return jdbcTemplate.query(sql, siteRowMapper);
        }
    }
}
