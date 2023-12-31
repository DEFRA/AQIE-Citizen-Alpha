package com.example.airqualitylimitedjs.endpoint;

import com.example.airqualitylimitedjs.domain.AirQuality;
import com.example.airqualitylimitedjs.dto.AirQualityDto;
import com.example.airqualitylimitedjs.mapper.AirQualityMapper;
import com.example.airqualitylimitedjs.service.AirQualityService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AirQualityController.class)
public class AirQualityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirQualityService airQualityService;

    @MockBean
    private AirQualityMapper airQualityMapper;

    @Nested
    @DisplayName("Tests for nearest")
    class nearest {
        @Test
        public void givenEmptyPostcode_expect400WithPostcodeMustNoBeEmptyMessage() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/aq/nearest?postcode="))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();
            assertTrue(result.getResponse().getContentAsString().contains("nearest.postcode: must not be empty"));
        }

        @Test
        public void givenInvalidPostcode_expect400WithPostcodeInvalidMessage() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/aq/nearest?postcode=a"))
                    .andDo(print())
                    .andExpect(status().is4xxClientError())
                    .andReturn();
            assertEquals("{\"message\":\"nearest.postcode: Invalid postcode\"}", result.getResponse().getContentAsString());
        }

        @Test
        public void givenValidPostcode_expect200WithCorrectContent() throws Exception {
            final String postcode = "SO160AU";
            List<AirQuality> airQualities = new ArrayList<>();
            when(airQualityService.findNearest(postcode)).thenReturn(airQualities);

            AirQualityDto airQualityDto = new AirQualityDto();
            final Integer aqId = 1;
            airQualityDto.setId(aqId);
            final Integer aqIndex = 3;
            airQualityDto.setIndex(aqIndex);
            final String polygon = "POLYGON((-59000 1221000,-59000 1223000,-57000 1223000,-57000 1221000,-59000 1221000))";
            airQualityDto.setPolygon(polygon);
            when(airQualityMapper.map(airQualities)).thenReturn(List.of(airQualityDto));

            MvcResult result = mockMvc.perform(get("/api/aq/nearest?postcode=" + postcode))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._embedded.airQualityDtoes", hasSize(1)))
                    .andExpect(jsonPath("$._embedded.airQualityDtoes[0].id", equalTo(aqId)))
                    .andExpect(jsonPath("$._embedded.airQualityDtoes[0].index", equalTo(aqIndex)))
                    .andExpect(jsonPath("$._embedded.airQualityDtoes[0].polygon", equalTo(polygon)))
                    .andReturn();

            // Line below is redundant. Kept to show how to verify the full response.
            assertEquals("{\"_embedded\":{\"airQualityDtoes\":[{\"id\":1,\"index\":3,\"polygon\":\"POLYGON((-59000 1221000,-59000 1223000,-57000 1223000,-57000 1221000,-59000 1221000))\",\"_links\":{\"self\":{\"href\":\"http://localhost/api/aq/1\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/api/aq/nearest?postcode=SO160AU\"}}}", result.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("Tests for findOne")
    class findOne {
        @Test
        public void givenExistingAirQualityId_expect200WithCorrectContent() throws Exception {
            final Integer airQualityId = new Random().nextInt();

            final AirQuality airQuality = new AirQuality();
            Optional<AirQuality> airQualityOptional = Optional.of(airQuality);
            when(airQualityService.findById(airQualityId)).thenReturn(airQualityOptional);

            final AirQualityDto airQualityDto = new AirQualityDto();
            airQualityDto.setId(airQualityId);
            final Integer airQualityIndex = new Random().nextInt();
            airQualityDto.setIndex(airQualityIndex);
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            final String polygon = RandomStringUtils.random(length, useLetters, useNumbers);
            airQualityDto.setPolygon(polygon);
            when(airQualityMapper.airQualityToAirQualityDto(airQuality)).thenReturn(airQualityDto);

            mockMvc.perform(get("/api/aq/" + airQualityId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", equalTo(airQualityId)))
                    .andExpect(jsonPath("$.index", equalTo(airQualityIndex)))
                    .andExpect(jsonPath("$.polygon", equalTo(polygon)))
                    .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/api/aq/" + airQualityId)))
                    .andReturn();
        }

        @Test
        public void givenNonExistingAirQualityId_expect404WithAirQualityNotFoundMessage() throws Exception {
            final Integer airQualityId = new Random().nextInt();

            when(airQualityService.findById(airQualityId)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/aq/" + airQualityId))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", equalTo("Air Quality with id " + airQualityId + " does not exist.")))
                    .andReturn();
        }

        @Test
        public void givenInvalidAirQualityId_expect400WithCorrectMessage() throws Exception {
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            final String invalidAirQualityId = RandomStringUtils.random(length, useLetters, useNumbers);

            mockMvc.perform(get("/api/aq/" + invalidAirQualityId))
                    .andDo(print())
                    .andExpect(jsonPath("$.detail", equalTo("Failed to convert 'airQualityId' with value: '" + invalidAirQualityId + "'")))
                    .andReturn();
        }
    }
}
