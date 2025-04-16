package com.example.loginLab.demo.dto;

public record LoginResponse
        (Long userId, String role, String token) {
}
