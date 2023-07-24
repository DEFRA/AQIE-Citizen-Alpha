package com.example.airqualitylimitedjs.service;

import com.example.airqualitylimitedjs.domain.LocationPoint;
import com.example.airqualitylimitedjs.exception.LocationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostcodeServiceITCase {
    private final PostcodeService postcodeService = new PostcodeService(new RestTemplateBuilder().build());

    @Nested
    @DisplayName("Tests for getLocationPoint")
    class getLocationPoint {
        @Test
        public void givenValidPostcode_expectPopulatedLocationPoint() throws LocationException {
            LocationPoint result = postcodeService.getLocationPoint("SO160AU");
            assertEquals(437422d, result.getEasting());
            assertEquals(115291d, result.getNorthing());
        }

        @Test
        public void givenInvalidPostcode_expectLocationException() {
            LocationException exception = assertThrows(LocationException.class, () -> postcodeService.getLocationPoint("a"));
            assertEquals(HttpStatusCode.valueOf(404), exception.getHttpStatusCode());
            assertTrue(exception.getMessage().contains("Invalid postcode"));
        }
    }
}
