package com.example.loginLab.demo.service;

import com.example.loginLab.demo.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    ApiResponse<Object> getAllUsers();

    ApiResponse<Object> getAllUsersChunkData(int page, int size);

    ApiResponse<Object> getUserById(Long userId);

    ApiResponse<Object> fetchUserProfileInformation(HttpServletRequest request);

    void logOut(HttpServletRequest request, HttpServletResponse response);
}
