package com.example.loginLab.demo.service;

import com.example.loginLab.demo.util.ApiResponse;

public interface UserService {

    ApiResponse<Object> getAllUsers();

    ApiResponse<Object> getAllUsersChunkData(int page, int size);

    ApiResponse<Object> getUserById(Long userId);

}
