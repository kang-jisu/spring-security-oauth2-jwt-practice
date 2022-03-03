package com.example.security.securitypractice.controller;

import com.example.security.securitypractice.auth.PrincipalDetails;
import org.springframework.security.core.Authentication;
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


    @GetMapping("/api/v1/user")
    public String user(Authentication authentication) {
        System.out.println((PrincipalDetails) authentication.getPrincipal());
        return "user";
    }

    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }
}
