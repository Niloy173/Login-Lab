package com.example.loginLab.demo.service;

import com.example.loginLab.demo.dto.Credentials;
import com.example.loginLab.demo.dto.LoginResponse;
import com.example.loginLab.demo.dto.SignUpDto;
import com.example.loginLab.demo.util.ApiResponse;

public interface AuthService {

    ApiResponse<Object> Register(SignUpDto signUpDto);

    LoginResponse Login(Credentials credentials);

}
