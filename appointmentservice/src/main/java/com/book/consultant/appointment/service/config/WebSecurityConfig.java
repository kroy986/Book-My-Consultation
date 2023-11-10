package com.book.consultant.appointment.service.config;

import com.book.consultant.appointment.service.filter.JwtAuthenticationFilter;
import com.book.consultant.appointment.service.service.TokenDoctorDetailsService;
import com.book.consultant.appointment.service.service.TokenUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeRequests()
                .requestMatchers("/api/doctor/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/appointments").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/appointments/**").hasAnyRole("USER","ADMIN")
                .requestMatchers("/api/prescriptions").hasRole("USER")
                .anyRequest()
                .authenticated()
                .and().exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private RequestMatcher queryParamRequestMatcher(String... paramNames) {
        return (request) -> {
            for (String paramName : paramNames) {
                if (request.getParameterMap().containsKey(paramName)) {
                    return true;
                }
            }
            return false;
        };
    }

     /*private RequestMatcher queryParamRequestMatcher(String paramName) {
        return (request) -> request.getParameterMap().containsKey(paramName);
    }

   private RequestMatcher queryParamRequestMatcher(String paramName, String paramValue) {
        return (request) -> {
            String value = request.getParameter(paramName);
            return paramValue.equals(value);
        };
    }*/
}
