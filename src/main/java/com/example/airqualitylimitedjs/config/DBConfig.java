package com.example.airqualitylimitedjs.config;

import com.example.airqualitylimitedjs.repository.AirQualityRepository;
import com.example.airqualitylimitedjs.repository.AirQualityRepositoryH2Impl;
import com.example.airqualitylimitedjs.repository.AirQualityRepositoryPostgresqlImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DBConfig {
    @Bean(name = "airQualityRepository")
    @ConditionalOnProperty(prefix = "database", name = "mode", havingValue = "h2")
    public AirQualityRepository h2AirQualityRepository() {
        return new AirQualityRepositoryH2Impl();
    }

    @Bean(name = "airQualityRepository")
    @ConditionalOnProperty(prefix = "database", name = "mode", havingValue = "postgresql")
    public AirQualityRepository postgresqlAirQualityRepository(DataSource dataSource) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return new AirQualityRepositoryPostgresqlImpl(jdbcTemplate);
    }
}
