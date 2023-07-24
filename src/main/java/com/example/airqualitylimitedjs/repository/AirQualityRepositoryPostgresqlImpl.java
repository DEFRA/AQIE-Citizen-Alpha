package com.example.airqualitylimitedjs.repository;

import com.example.airqualitylimitedjs.domain.AirQuality;
import com.example.airqualitylimitedjs.repository.rowmapper.AirQualityRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Optional;

// TODO When AirQuality is made a @Entity, replicate what was done for Site with CustomisedSiteRepository. DBConfig can then be deleted.
@Slf4j
public class AirQualityRepositoryPostgresqlImpl implements AirQualityRepository {
    public final static String INDEX_COLUMN_NAME = "index";
    public final static String POLYGON_COLUMN_NAME = "polygon";

    private final static String NEAREST_SQL = String.format("select aq_index AS \"%s\", ST_AsText(ST_Transform(geom, 27700)) AS \"%s\" from day1 where ST_Covers(geom, ST_Point(:easting, :northing, 27700)) = TRUE",
            INDEX_COLUMN_NAME, POLYGON_COLUMN_NAME);

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AirQualityRowMapper airQualityRowMapper;

    public AirQualityRepositoryPostgresqlImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.airQualityRowMapper = new AirQualityRowMapper();
    }

    @Override
    public List<AirQuality> findNearest(Double easting, Double northing) {
        // TODO - Once we have more data in the DB, remove this hardcoding of easting & northing
        // TODO - To have a match, use -59000d, 1221000d
        // TODO - To have no match, use -59002d, 1221000d
        easting = -59000d;
        northing = 1221000d;
        log.warn("ATTENTION: easting & northing hardcoded temporarily to {}, {}", easting, northing);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("easting", easting)
                .addValue("northing", northing);

        return jdbcTemplate.query(NEAREST_SQL, sqlParameterSource, airQualityRowMapper);
    }

    @Override
    public Optional<AirQuality> findById(Integer airQualityId) {
        // TODO
        return Optional.empty();
    }
}