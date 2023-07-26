package com.example.airqualitylimitedjs.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilTest {
    @Nested
    @DisplayName("Tests for rewriteOSVectorMappingResponse")
    class rewriteOSVectorMappingResponse {
        @Test
        public void givenStringContainingOSUrlAndAPIKey_expectStringContainingOSVectorMappingProxyUrlAndNoAPIKey() {
            String inputStr = "{\"version\":8,\"sprite\":\"https://api.os.uk/maps/vector/v1/vts/resources/sprites/sprite?key=THE_KEY\",\"glyphs\":\"https://api.os.uk/maps/vector/v1/vts/resources/fonts/{fontstack}/{range}.pbf?key=THE_KEY\",\"sources\":{\"esri\":{\"type\":\"vector\",\"url\":\"https://api.os.uk/maps/vector/v1/vts?key=THE_KEY\"}}}";
            String outputStr = "{\"version\":8,\"sprite\":\"http://localhost:8080/proxy_os_vector_mapping/maps/vector/v1/vts/resources/sprites/sprite\",\"glyphs\":\"http://localhost:8080/proxy_os_vector_mapping/maps/vector/v1/vts/resources/fonts/{fontstack}/{range}.pbf\",\"sources\":{\"esri\":{\"type\":\"vector\",\"url\":\"http://localhost:8080/proxy_os_vector_mapping/maps/vector/v1/vts\"}}}";
            assertEquals(outputStr, StringUtil.rewriteOSVectorMappingResponse(inputStr));
        }

        @Test
        public void givenStringNotContainingOSUrl_expectOutputEqualsToInput() {
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            String inputStr = RandomStringUtils.random(length, useLetters, useNumbers);;
            assertEquals(inputStr, StringUtil.rewriteOSVectorMappingResponse(inputStr));
        }
    }

    @Test
    public void getOSVectorMappingPath_given_expect() {
        String inputStr = "/proxy_os_vector_mapping/maps/vector/v1/";
        String outputStr = "https://api.os.uk/maps/vector/v1/";
        assertEquals(outputStr, StringUtil.getOSVectorMappingUrl(inputStr));
    }
}
