package com.example.airqualitylimitedjs.endpoint;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Enumeration;

@Slf4j
@RestController
@RequestMapping("proxy_os_raster_mapping")
public class OSRasterMappingController {
    @Value("${os.mapping.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public OSRasterMappingController() {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getOSMap(HttpServletRequest request) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl("https://api.os.uk/maps/raster/v1/wmts")
                .query(request.getQueryString())
                .queryParam("key", apiKey)
                .encode()
                .toUriString();

        MultiValueMap<String, String> newHeaders = new LinkedMultiValueMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue =  request.getHeader(headerName);
                newHeaders.add(headerName, headerValue);
            }
        }
        HttpEntity<?> entity = new HttpEntity<>(newHeaders);

        return restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, byte[].class);
    }
}
