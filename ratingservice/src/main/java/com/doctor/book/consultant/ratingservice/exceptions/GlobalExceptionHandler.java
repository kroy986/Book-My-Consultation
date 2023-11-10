package com.doctor.book.consultant.ratingservice.exceptions;


import com.doctor.book.consultant.ratingservice.constants.ResponseConstants;
import com.doctor.book.consultant.ratingservice.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
                BAD_REQUEST);

    }
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleValidationError(ResponseStatusException ex) {

        if (BAD_REQUEST.equals(ex.getStatusCode())) {
            return new ResponseEntity<>(new ErrorResponse("ERR_PAYMENT_PENDING",
                    ex.getReason(),null), BAD_REQUEST);
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

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleBadCredentialsError(BadCredentialsException ex) {
        return new ResponseEntity<>(new ErrorResponse(ResponseConstants.BAD_CREDENTIALS,
                String.valueOf(HttpStatus.FORBIDDEN.value())), HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleExpiredJwtError(ExpiredJwtException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse(ResponseConstants.TOKEN_EXPIRED,
                String.valueOf(HttpStatus.FORBIDDEN.value())), HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new ErrorResponse(ResponseConstants.INTERNAL_SERVER_ERROR,
                ResponseConstants.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
