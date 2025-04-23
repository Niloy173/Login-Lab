package com.example.loginLab.demo.service;

import com.example.loginLab.demo.config.AppSecurityProperties;
import com.example.loginLab.demo.config.UserAuthProvider;
import com.example.loginLab.demo.dto.UserDto;
import com.example.loginLab.demo.entity.User;
import com.example.loginLab.demo.exception.AppException;
import com.example.loginLab.demo.mapper.UserMapper;
import com.example.loginLab.demo.repository.UserRepository;
import com.example.loginLab.demo.util.ApiResponse;
import com.example.loginLab.demo.util.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{


    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AppSecurityProperties appSecurityProperties;
    private final UserAuthProvider userAuthProvider;
    private final Utils utils;


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
    public ApiResponse<Object> getAllUsersChunkData(int page, int size) {

        if(page < 0 || size < 0) {
            throw new AppException("Invalid page or size", HttpStatus.BAD_REQUEST);
        }
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        Page<User> data = userRepository.findAll(pageable);

        if(data.isEmpty()) {
            return new ApiResponse<>("success", "No users found", null, null);
        }


        List<UserDto> users = data.getContent()
                .stream()
                .map(userMapper::entityToDto)
                .toList();



        Map<String,Object> metaData = new HashMap<>();
        metaData.put("currentPage", data.getNumber());
        metaData.put("totalItems", data.getTotalElements());
        metaData.put("totalPages", data.getTotalPages());

        ApiResponse<Object> response = new ApiResponse<>("success", "Users found", users, metaData);
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

    @Override
    public ApiResponse<Object> fetchUserProfileInformation(HttpServletRequest request) {

        String token = Utils.getCookie(request,appSecurityProperties.getTokenCookieName());

        if(token == null) {
            throw new AppException("Header not valid", HttpStatus.BAD_REQUEST);
        }

        Long userId = userAuthProvider.extractUserId(token);

        Optional<User> findUser = userRepository.findUserByUserid(userId);

        if(findUser.isEmpty()) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }

        UserDto userDto = userMapper.entityToDto(findUser.get());
        log.info("userDto {}", userDto);

        ApiResponse<Object> response = new ApiResponse<>("success", "User found", userDto, null);

        return response;

    }

    @Override
    public void logOut(HttpServletRequest request, HttpServletResponse response) {

        String token = Utils.getCookie(request,appSecurityProperties.getTokenCookieName());

        if(token == null) {
            throw new AppException("Header not valid", HttpStatus.BAD_REQUEST);
        }

        utils.clearCookie(response,appSecurityProperties.getTokenCookieName());
    }
}
