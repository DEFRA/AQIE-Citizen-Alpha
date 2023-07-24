package com.example.airqualitylimitedjs.endpoint;

import com.example.airqualitylimitedjs.dto.ErrorDto;
import com.example.airqualitylimitedjs.exception.LocationException;
import com.example.airqualitylimitedjs.exception.MeasurementNotFoundException;
import com.example.airqualitylimitedjs.exception.SiteNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { SiteNotFoundException.class })
    protected ResponseEntity<Object> handleStationNotFoundException(SiteNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorDto(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { MeasurementNotFoundException.class })
    protected ResponseEntity<ErrorDto> handleMeasurementNotFoundException(MeasurementNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorDto(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    protected ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorDto(ex), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = { LocationException.class })
    protected ResponseEntity<ErrorDto> handleLocationException(LocationException ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorDto(ex), ex.getHttpStatusCode());
    }

    private ErrorDto buildErrorDto(Exception e) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(e.getMessage());
        return errorDto;
    }
}
