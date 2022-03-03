package com.example.security.securitypractice.controller;

import com.example.security.securitypractice.auth.PrincipalDetails;
import com.example.security.securitypractice.model.CurrentUser;
import com.example.security.securitypractice.model.User;
import com.example.security.securitypractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser PrincipalDetails principalDetails) {
        return userRepository.findById(principalDetails.getId())
                .orElseThrow(() -> new IllegalArgumentException());
    }
}
