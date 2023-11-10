package com.book.consultation.auth.server.controller;

import com.book.consultation.auth.server.model.JwtTokenRequest;
import com.book.consultation.auth.server.model.JwtTokenResponse;
import com.book.consultation.auth.server.service.TokenDoctorDetailsService;
import com.book.consultation.auth.server.service.TokenUserDetailsService;
import com.book.consultation.auth.server.utility.JWTUtility;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private TokenUserDetailsService tokenUserDetailsService;

    @Autowired
    private TokenDoctorDetailsService tokenDoctorDetailsService;

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private JWTUtility helper;

    Map<String, String> map = new HashMap<>();

    @PostMapping("/user/token")
    public ResponseEntity<JwtTokenResponse> userLogin(@RequestBody JwtTokenRequest request) {

        this.doAuthenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = tokenUserDetailsService.loadUserByUsername(request.getUsername());

        try{
            if(map.get("userTokenkey") == null || !(helper.validateToken(map.get("userTokenkey"), userDetails))){
                String token = this.helper.generateToken(userDetails);
                map.put("userTokenkey", token);
            }
        }catch(ExpiredJwtException e){
            String token = this.helper.generateToken(userDetails);
            map.put("userTokenkey", token);
        }

        JwtTokenResponse response = JwtTokenResponse.builder()
                .accessToken(map.get("userTokenkey"))
                .tokenType("Bearer")
                . build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/doctor/token")
    public ResponseEntity<JwtTokenResponse> doctorLogin(@RequestBody JwtTokenRequest request) {

        this.doAuthenticate(request.getUsername(), request.getPassword());
        UserDetails doctorDetails = tokenDoctorDetailsService.loadUserByUsername(request.getUsername());

        try{
            if(map.get("doctorTokenkey") == null || !(helper.validateToken(map.get("doctorTokenkey"), doctorDetails))){
                String token = this.helper.generateToken(doctorDetails);
                map.put("doctorTokenkey", token);
            }
        }catch(ExpiredJwtException e){
            String token = this.helper.generateToken(doctorDetails);
            map.put("doctorTokenkey", token);
        }

        JwtTokenResponse response = JwtTokenResponse.builder()
                .accessToken(map.get("doctorTokenkey"))
                .tokenType("Bearer")
                . build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }
}
