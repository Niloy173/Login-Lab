package com.example.loginLab.demo.service;

import com.example.loginLab.demo.dto.UserDto;
import com.example.loginLab.demo.entity.User;
import com.example.loginLab.demo.exception.AppException;
import com.example.loginLab.demo.mapper.UserMapper;
import com.example.loginLab.demo.repository.UserRepository;
import com.example.loginLab.demo.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{


    private final UserMapper userMapper;
    private final UserRepository userRepository;


    @Override
    public ApiResponse<Object> getAllUsers() {


        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(userMapper::entityToDto)
                .toList();

        if(users.isEmpty()) {
            return new ApiResponse<>("success", "No users found", null, null);
        }

        ApiResponse<Object> response = new ApiResponse<>("success", "Users found", users, null);
        return response;

    }

    @Override
    public ApiResponse<Object> getUserById(Long userId) {

        Optional<User> user = userRepository.findUserByUserid(userId);

        if(user.isEmpty()) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }

        UserDto userDto = userMapper.entityToDto(user.get());

        ApiResponse<Object> response = new ApiResponse<>("success", "User found", userDto, null);

        return response;
    }
}
