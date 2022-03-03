package com.example.security.securitypractice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @PostMapping("/token")
    public String token(){
        return "token";
    }
}
