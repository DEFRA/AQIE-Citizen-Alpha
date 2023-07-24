package com.example.airqualitylimitedjs.service;

import com.example.airqualitylimitedjs.domain.LocationPoint;
import com.example.airqualitylimitedjs.domain.PostcodeApiResponse;
import com.example.airqualitylimitedjs.domain.PostcodeDetails;
import com.example.airqualitylimitedjs.exception.LocationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class PostcodeService {
    private final static String POSTCODE_API_URL_PATTERN = "https://api.postcodes.io/postcodes/%s";

    private final RestTemplate restTemplate;

    public PostcodeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LocationPoint getLocationPoint(String postcode) throws LocationException {
        // TODO Add a retry on the API call.
        try {
            LocationPoint result = null;
            PostcodeApiResponse apiResponse = restTemplate.getForObject(String.format(POSTCODE_API_URL_PATTERN, postcode), PostcodeApiResponse.class);
            if (apiResponse != null) {
                PostcodeDetails postcodeDetails = apiResponse.getResult();
                if (postcodeDetails != null) {
                    result = new LocationPoint();
                    result.setEasting(postcodeDetails.getEastings());
                    result.setNorthing(postcodeDetails.getNorthings());
                    log.info("The point for postcode {} is {}", postcode, result);
                }
            }

            if (result == null) {
                throw new LocationException(String.format("Unexpected apiResponse: %s", apiResponse), HttpStatusCode.valueOf(500));
            }

            return result;
        } catch (HttpStatusCodeException e) {
            throw new LocationException(e.getMessage(), e.getStatusCode());
        }
    }

}
