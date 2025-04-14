package com.example.loginLab.demo.dto;

public record SignUpDto(
        String username,
        String email,
        String role,
        char[] password) {
}
