package com.example.loginLab.demo.controller;

import com.example.loginLab.demo.service.UserService;
import com.example.loginLab.demo.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> getUserProfile
            (HttpServletRequest request) {
        return ResponseEntity.ok(userService.fetchUserProfileInformation(request));
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Object>> allUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/all/data")
    public ResponseEntity<ApiResponse<Object>> allUsersData(
            @Valid @RequestParam(defaultValue = "0") int page,
            @Valid @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(userService.getAllUsersChunkData(page, size));
    }


    @GetMapping("/userid/{userId}")
    public ResponseEntity<ApiResponse<Object>> getUserById(@Valid @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logOutUser
            (HttpServletRequest request, HttpServletResponse response) {
        userService.logOut(request, response);
        return ResponseEntity.ok(new ApiResponse<>("success", "Logout successful", null, null));
    }
}
