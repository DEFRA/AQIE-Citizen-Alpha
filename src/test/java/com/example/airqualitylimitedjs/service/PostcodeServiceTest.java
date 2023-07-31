package com.example.airqualitylimitedjs.service;

import com.example.airqualitylimitedjs.domain.LocationPoint;
import com.example.airqualitylimitedjs.domain.PostcodeApiResponse;
import com.example.airqualitylimitedjs.domain.PostcodeDetails;
import com.example.airqualitylimitedjs.exception.LocationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostcodeServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PostcodeService postcodeService;

    @Nested
    @DisplayName("Tests for getLocationPoint")
    class getLocationPoint {
        @Test
        public void givenAPIThrowsServerSideException_expectLocationException() {
            final String msg = "unexpected issue";
            HttpStatusCodeException apiException = HttpServerErrorException.create(msg, HttpStatusCode.valueOf(500), "status text", null, "{\"status\":500,\"error\":\"unexpected issue\"}".getBytes(), null);
            when(restTemplate.getForObject(anyString(), any())).thenThrow(apiException);

            LocationException exception = assertThrows(LocationException.class, () -> postcodeService.getLocationPoint("a"));
            assertEquals(HttpStatusCode.valueOf(500), exception.getHttpStatusCode());
            assertEquals(msg, exception.getMessage());
        }

        @Test
        public void givenAPIReturnsNull_expectLocationException() {
            when(restTemplate.getForObject(anyString(), any())).thenReturn(null);
            LocationException exception = assertThrows(LocationException.class, () -> postcodeService.getLocationPoint("a"));
            assertEquals(HttpStatusCode.valueOf(500), exception.getHttpStatusCode());
            assertEquals("Unexpected apiResponse: null", exception.getMessage());
        }

        @Test
        public void givenAPIReturnsNullResult_expectLocationException() {
            PostcodeApiResponse apiResponse = new PostcodeApiResponse();
            apiResponse.setStatus(500);
            when(restTemplate.getForObject(anyString(), any())).thenReturn(apiResponse);
            LocationException exception = assertThrows(LocationException.class, () -> postcodeService.getLocationPoint("a"));
            assertEquals(HttpStatusCode.valueOf(500), exception.getHttpStatusCode());
            assertEquals("Unexpected apiResponse: PostcodeApiResponse(status=500, result=null)", exception.getMessage());
        }

        @Test
        public void givenAPIReturnsNonNullResult_expectLocationPoint() throws LocationException {
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            final String postcode = RandomStringUtils.random(length, useLetters, useNumbers);

            PostcodeApiResponse apiResponse = new PostcodeApiResponse();
            PostcodeDetails postcodeDetails = new PostcodeDetails();
            final Double eastings = Math.random();
            postcodeDetails.setEastings(eastings);
            final Double northings = Math.random();
            postcodeDetails.setNorthings(northings);
            apiResponse.setResult(postcodeDetails);
            when(restTemplate.getForObject("https://api.postcodes.io/postcodes/" + postcode, PostcodeApiResponse.class)).thenReturn(apiResponse);

            final LocationPoint result = new LocationPoint();
            result.setEasting(eastings);
            result.setNorthing(northings);
            assertEquals(result, postcodeService.getLocationPoint(postcode));
        }
    }
}
