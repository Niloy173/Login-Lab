package com.example.loginLab.demo.controller;

import com.example.loginLab.demo.dto.Credentials;
import com.example.loginLab.demo.dto.SignUpDto;
import com.example.loginLab.demo.service.AuthService;
import com.example.loginLab.demo.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> registerNewUser(@Valid @RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(authService.Register(signUpDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> registerNewUser(@Valid @RequestBody Credentials credentials) {
        return ResponseEntity.ok(authService.Login(credentials));
    }
}
