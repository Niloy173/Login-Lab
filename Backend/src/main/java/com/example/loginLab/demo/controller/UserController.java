package com.example.loginLab.demo.controller;

import com.example.loginLab.demo.service.UserService;
import com.example.loginLab.demo.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Object>> allUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/all/data")
    public ResponseEntity<ApiResponse<Object>> allUsersData(
            @Valid @RequestParam(defaultValue = "0") int page,
            @Valid @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable  = PageRequest.of(page, size);

        return ResponseEntity.ok(userService.getAllUsersChunkData(pageable));
    }


    @GetMapping("/userid/{userId}")
    public ResponseEntity<ApiResponse<Object>> getUserById(@Valid @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
