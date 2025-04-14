package com.example.loginLab.demo.controller;

import com.example.loginLab.demo.service.UserService;
import com.example.loginLab.demo.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Object>> allUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/userid/{userId}")
    public ResponseEntity<ApiResponse<Object>> getUserById(@Valid @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
