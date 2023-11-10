package com.book.consultation.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {
    @GetMapping("/userServiceFallBack")
    public String userServiceFallBackMessage(){
        return "User Service is down now";
    }
    @GetMapping("/contactServiceFallBack")
    public String contactServiceFallBackMessage(){
        return "Contact Service is down now";
    }
}