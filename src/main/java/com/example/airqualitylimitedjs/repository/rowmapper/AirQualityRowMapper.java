package com.example.airqualitylimitedjs.repository.rowmapper;

import com.example.airqualitylimitedjs.domain.AirQuality;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.airqualitylimitedjs.repository.AirQualityRepositoryPostgresqlImpl.INDEX_COLUMN_NAME;
import static com.example.airqualitylimitedjs.repository.AirQualityRepositoryPostgresqlImpl.POLYGON_COLUMN_NAME;

public class AirQualityRowMapper implements RowMapper<AirQuality> {
    @Override
    public AirQuality mapRow(ResultSet rs, int rowNum) throws SQLException {
        AirQuality airQuality = new AirQuality();
        airQuality.setId(rowNum);
        airQuality.setIndex(rs.getInt(INDEX_COLUMN_NAME));
        airQuality.setPolygon(rs.getString(POLYGON_COLUMN_NAME));

        return airQuality;
    }
}
