package com.example.loginLab.demo.dto;

public record LoginResponse
        (Long userid,
         String role,
         String token) {
}
