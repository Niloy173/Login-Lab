package com.example.loginLab.demo.controller;

import com.example.loginLab.demo.config.AppSecurityProperties;
import com.example.loginLab.demo.dto.Credentials;
import com.example.loginLab.demo.dto.LoginResponse;
import com.example.loginLab.demo.dto.SignUpDto;
import com.example.loginLab.demo.service.AuthService;
import com.example.loginLab.demo.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    //private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final AppSecurityProperties appSecurityProperties;

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAuthStatus(Authentication authentication) {
        // Just return 200 OK if token is valid and user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(true); // or even ResponseEntity.ok().build()
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> getUserProfile
            (HttpServletRequest request) {
        return ResponseEntity.ok(authService.fetchUserProfileInformation(request));
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> registerNewUser(@Valid @RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(authService.Register(signUpDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> loginUser
            (@Valid @RequestBody Credentials credentials,
             HttpServletResponse response) {

        LoginResponse loginResponse = authService.Login(credentials);

        ResponseCookie cookie = ResponseCookie.from(appSecurityProperties.getTokenCookieName(), loginResponse.token())
                .httpOnly(true)
                .secure(false) // use true in prod with HTTPS
                .path("/")
                .sameSite("Lax") // or "Strict"
                .maxAge(appSecurityProperties.getJwtExpirationInMs()) // 10 min
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());


        return ResponseEntity
                .ok(new ApiResponse<>(
                        "success",
                        "Login successful",
                        new LoginResponse(
                                loginResponse.userid(),
                                loginResponse.role(),
                                null
                        ),
                        null));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logOutUser
            (HttpServletRequest request, HttpServletResponse response) {
        authService.logOut(request, response);
        return ResponseEntity.ok(new ApiResponse<>("success", "Logout successful", null, null));
    }

}
