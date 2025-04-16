package com.example.loginLab.demo.exception.securityException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json");
        String error = (String) request.getAttribute("auth_error");
        if (error == null) {
            error = "Unauthorized";
        }

        response.getWriter().write(String.format("""
        {
            "status": 401,
            "error": "Unauthorized",
            "message": "%s"
        }
    """, error));
    }
}
