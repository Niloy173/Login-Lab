package com.example.loginLab.demo.service;

import com.example.loginLab.demo.util.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {

    ApiResponse<Object> getAllUsers();

    ApiResponse<Object> getAllUsersChunkData(Pageable pageable);

    ApiResponse<Object> getUserById(Long userId);
}
