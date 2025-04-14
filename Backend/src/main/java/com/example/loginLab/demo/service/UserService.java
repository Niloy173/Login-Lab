package com.example.loginLab.demo.service;

import com.example.loginLab.demo.util.ApiResponse;

public interface UserService {

    ApiResponse<Object> getAllUsers();

    ApiResponse<Object> getUserById(Long userId);
}
