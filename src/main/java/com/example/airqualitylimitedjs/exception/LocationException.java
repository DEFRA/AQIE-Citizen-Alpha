package com.example.airqualitylimitedjs.exception;

import org.springframework.http.HttpStatusCode;

public class LocationException extends Exception {
    private final HttpStatusCode httpStatusCode;
    public LocationException(String msg, HttpStatusCode httpStatusCode) {
        super(msg);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}
