package com.example.airqualitylimitedjs.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilTest {
    @Test
    public void rewriteOSVectorMappingResponse_givenJSONWithOSUrlAndAPIKey_expectJSONWithOSVectorMappingProxyUrlAndNoAPIKey() {
        String inputStr = "{\"version\":8,\"sprite\":\"https://api.os.uk/maps/vector/v1/vts/resources/sprites/sprite?key=THE_KEY\",\"glyphs\":\"https://api.os.uk/maps/vector/v1/vts/resources/fonts/{fontstack}/{range}.pbf?key=THE_KEY\",\"sources\":{\"esri\":{\"type\":\"vector\",\"url\":\"https://api.os.uk/maps/vector/v1/vts?key=THE_KEY\"}}}";
        String outputStr = "{\"version\":8,\"sprite\":\"http://localhost:8080/proxy_os_vector_mapping/maps/vector/v1/vts/resources/sprites/sprite\",\"glyphs\":\"http://localhost:8080/proxy_os_vector_mapping/maps/vector/v1/vts/resources/fonts/{fontstack}/{range}.pbf\",\"sources\":{\"esri\":{\"type\":\"vector\",\"url\":\"http://localhost:8080/proxy_os_vector_mapping/maps/vector/v1/vts\"}}}";
        assertEquals(outputStr, StringUtil.rewriteOSVectorMappingResponse(inputStr));
    }

    @Test
    public void getOSVectorMappingPath_given_expect() {
        String inputStr = "/proxy_os_vector_mapping/maps/vector/v1/";
        String outputStr = "https://api.os.uk/maps/vector/v1/";
        assertEquals(outputStr, StringUtil.getOSVectorMappingUrl(inputStr));
    }
}
