package com.book.consultation.auth.server.exceptions;

import com.book.consultation.auth.server.constants.ResponseConstants;
import com.book.consultation.auth.server.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * Global exception handler
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {


    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleValidationError(ResponseStatusException ex) {

        if (HttpStatus.BAD_REQUEST.equals(ex.getStatusCode())) {
            return new ResponseEntity<>(new ErrorResponse(ex.getReason(),
                    String.valueOf(ex.getStatusCode().value())), HttpStatus.BAD_REQUEST);
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
        return new ResponseEntity<>(new ErrorResponse(ResponseConstants.TOKEN_EXPIRED,
                String.valueOf(HttpStatus.FORBIDDEN.value())), HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(ResponseConstants.INTERNAL_SERVER_ERROR,
                ResponseConstants.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
