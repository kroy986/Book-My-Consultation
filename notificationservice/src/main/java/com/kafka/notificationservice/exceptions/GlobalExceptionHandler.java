package com.kafka.notificationservice.exceptions;


import com.kafka.notificationservice.constants.ResponseConstants;
import com.kafka.notificationservice.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex) {

        log.error("MethodArgumentNotValidException", ex.getMessage());
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return new ResponseEntity<>(new ErrorResponse("ERR_INVALID_INPUT",
                "Invalid Input. Parameter name: ",
                errors),
                HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleValidationError(ResponseStatusException ex) {

        if (HttpStatus.BAD_REQUEST.equals(ex.getStatusCode())) {
            return new ResponseEntity<>(new ErrorResponse(ex.getReason(),
                    String.valueOf(ex.getStatusCode().value())), HttpStatus.BAD_REQUEST);
        }

        if (HttpStatus.CONFLICT.equals(ex.getStatusCode())) {
            return new ResponseEntity<>(new ErrorResponse(ex.getReason(),
                    String.valueOf(ex.getStatusCode().value())), HttpStatus.CONFLICT);
        }

        if (HttpStatus.NOT_FOUND.equals(ex.getStatusCode())) {
            return new ResponseEntity<>(new ErrorResponse("ERR_RESOURCE_NOT_FOUND",
                    "Requested resource is not available",
                    null),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ErrorResponse(ResponseConstants.INTERNAL_SERVER_ERROR,
                ResponseConstants.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse(ResponseConstants.INTERNAL_SERVER_ERROR,
                ResponseConstants.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
