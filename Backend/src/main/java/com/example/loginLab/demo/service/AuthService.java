package com.example.loginLab.demo.service;

import com.example.loginLab.demo.dto.Credentials;
import com.example.loginLab.demo.dto.LoginResponse;
import com.example.loginLab.demo.dto.SignUpDto;
import com.example.loginLab.demo.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    ApiResponse<Object> Register(SignUpDto signUpDto);

    LoginResponse Login(Credentials credentials);

    ApiResponse<Object> fetchUserProfileInformation(HttpServletRequest request);

    void logOut(HttpServletRequest request, HttpServletResponse response);
}
