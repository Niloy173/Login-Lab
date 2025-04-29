package com.example.loginLab.demo.service;

import com.example.loginLab.demo.config.AppSecurityProperties;
import com.example.loginLab.demo.config.PasswordConfiguration;
import com.example.loginLab.demo.config.UserAuthProvider;
import com.example.loginLab.demo.dto.Credentials;
import com.example.loginLab.demo.dto.LoginResponse;
import com.example.loginLab.demo.dto.SignUpDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordConfiguration passwordConfiguration;
    private final UserAuthProvider userAuthProvider;
    private final AppSecurityProperties appSecurityProperties;
    private final Utils utils;


    @Override
    public ApiResponse<Object> Register(SignUpDto signUpDto) {

        log.info("Registering user: {}", signUpDto);

//        if(userRepository.existsByUsername(signUpDto.username().toLowerCase())){
//            throw new AppException("Username already registered", HttpStatus.CONFLICT);
//        }

        if(userRepository.existsByEmail(signUpDto.email())){
            throw new AppException("Email already registered", HttpStatus.CONFLICT);
        }

        UserDto convertedUser = new UserDto();

        convertedUser.setUsername(signUpDto.username());
        convertedUser.setEmail(signUpDto.email());
        convertedUser.setPassword(passwordConfiguration.passwordEncoder().encode(CharBuffer.wrap(signUpDto.password())));
        convertedUser.setRole(signUpDto.role());

        User user = userMapper.dtoToEntity(convertedUser);
        User savedUser = userRepository.save(user);

        return new ApiResponse<>("success", "User registered successfully", null, null);
    }

    @Override
    public LoginResponse Login(Credentials credentials) {

        Optional<User> user = userRepository.findUserByEmail(credentials.email());

        if(user.isEmpty()){
            throw new AppException("Invalid user", HttpStatus.NOT_FOUND);
        }

        if(passwordConfiguration.passwordEncoder().matches(CharBuffer.wrap(credentials.password()),
                user.get().getPassword())) {

            UserDto userDto = userMapper.entityToDto(user.get());

            LoginResponse loginResponse = new LoginResponse(
                    userDto.getUserid(),
                    userDto.getRole(),
                    userAuthProvider.createToken(userDto)

            );

            return loginResponse;

//            return new ApiResponse<>("success", "Login successful", loginResponse, null);
        }

        throw new AppException("Invalid credentials", HttpStatus.UNAUTHORIZED);
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

//        // Invalidate session first
//        HttpSession session = request.getSession(false); // Don't create if not exists
//        if (session != null) {
//            session.invalidate(); // Clears session
//        }
//
//        // Remove JSESSIONID cookie
//        utils.clearCookie(response, "JSESSIONID");

        utils.clearCookie(response,appSecurityProperties.getTokenCookieName());
    }
}
