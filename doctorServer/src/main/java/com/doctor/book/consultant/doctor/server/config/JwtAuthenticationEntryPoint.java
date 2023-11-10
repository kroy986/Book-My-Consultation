package com.doctor.book.consultant.doctor.server.config;

import com.doctor.book.consultant.doctor.server.response.AuthErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        AuthErrorResponse authErrorResponse = AuthErrorResponse.builder()
                .timestamp(Calendar.getInstance().getTime())
                .status(HttpServletResponse.SC_FORBIDDEN)
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .path(request.getRequestURI()).build();

        String jsonErrorMessage = objectMapper.writeValueAsString(authErrorResponse);
        PrintWriter writer = response.getWriter();
        writer.println(jsonErrorMessage);
    }
}
