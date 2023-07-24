package com.example.airqualitylimitedjs.endpoint;

import com.example.airqualitylimitedjs.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("proxy_os_vector_mapping/**")
public class OSVectorMappingController {
    @Value("${os.mapping.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public OSVectorMappingController() {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping
    public ResponseEntity<byte[]> getVectorTile(HttpServletRequest request) {
        final String osUrl = StringUtil.getOSVectorMappingUrl(request.getServletPath());
        log.info("osURL = {}", osUrl);

        final String queryStr = request.getQueryString();
        log.info("query string = {}", queryStr);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(osUrl)
                .query(queryStr)
                .queryParam("key", apiKey)
                .encode()
                .toUriString();
        log.info("urlTemplate = {}", urlTemplate);

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

        ResponseEntity<byte[]> result = restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, byte[].class);

        HttpHeaders resultHeaders = result.getHeaders();
        List<String> contentTypes = resultHeaders.get("Content-Type");
        log.info("contentTypes = {} for urlTemplate = {}", contentTypes, urlTemplate);
        if (contentTypes != null && contentTypes.contains("application/json")) {
            byte[] bytes = result.getBody();
            if (bytes != null) {
                List<String> contentEncoding = resultHeaders.get("Content-Encoding");
                log.info("contentEncoding = {}", contentEncoding);
                if (contentEncoding != null && contentEncoding.contains("gzip")) {
                    String originalResultString = StringUtil.decompress(bytes);
                    String newResultString = StringUtil.rewriteOSVectorMappingResponse(originalResultString);
                    log.info("gzip path - returning newResultString = {}", newResultString);
                    result = new ResponseEntity<>(newResultString.getBytes(), HttpStatusCode.valueOf(200));
                } else {
                    String originalResultString = new String(bytes, StandardCharsets.UTF_8);
                    String newResultString = StringUtil.rewriteOSVectorMappingResponse(originalResultString);
                    log.info("non gzip path - returning newResultString = {}", newResultString);
                    result = new ResponseEntity<>(newResultString.getBytes(StandardCharsets.UTF_8), HttpStatusCode.valueOf(200));
                }
            }
        }

        return result;
    }
}
