package com.example.airqualitylimitedjs.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class StringUtil {
    private static final String ORDNANCE_SURVEY_URL = "https://api.os.uk";
    private static final String OS_VECTOR_MAPPING_PROXY_URL = "http://localhost:8080/proxy_os_vector_mapping";

    public static String rewriteOSVectorMappingResponse(String inputStr) {
        if (inputStr.contains(ORDNANCE_SURVEY_URL)) {
            String interimString = inputStr.replace(ORDNANCE_SURVEY_URL, OS_VECTOR_MAPPING_PROXY_URL);
            return interimString.replace(identifyApiKeyNameAndValue(interimString), "");
        } else {
            return inputStr;
        }
    }

    public static String getOSVectorMappingUrl(String initialServletPath) {
        return initialServletPath.replace("/proxy_os_vector_mapping", ORDNANCE_SURVEY_URL);
    }

    public static String decompress(byte[] compressedBytes) {
        log.info("Decompressing byte array...");
        try {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressedBytes));
            BufferedReader bf = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bf.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            log.error("Issue encountered while decompressing byte array.", e);
            throw new RuntimeException(e);
        }
    }

    public static byte[] compress(String input) {
        log.info("Compressing string...");
        if (input == null || input.isEmpty()) {
            return null;
        }

        try {
            ByteArrayOutputStream obj = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(obj);
            gzip.write(input.getBytes(StandardCharsets.UTF_8));
            gzip.close();
            return obj.toByteArray();
        } catch (IOException e) {
            log.error("Issue encountered while compressing String to byte array.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This identifies key=API_KEY in interimString.
     *
     * It assumes that key=API_KEY can appear in the following 3 forms:
     * - ...?key=THE_KEY"
     * - ...?key=THE_KEY&aParam=random"
     * - ...?aParam=random&key=THE_KEY"
     */
    private static String identifyApiKeyNameAndValue(String interimString) {
        int keyNamePosition = interimString.indexOf("key=");
        int firstDoubleQuoteAfterAPIKey = interimString.indexOf('"', keyNamePosition);
        int firstAndCharAfterAPIKey = interimString.indexOf('&', keyNamePosition);

        String result;
        if (firstAndCharAfterAPIKey == -1 || (firstAndCharAfterAPIKey > 0 && firstAndCharAfterAPIKey > firstDoubleQuoteAfterAPIKey)) {
            result = interimString.substring(keyNamePosition, firstDoubleQuoteAfterAPIKey);
        } else {
            result = interimString.substring(keyNamePosition, firstAndCharAfterAPIKey);
        }

        return result;
    }
}
