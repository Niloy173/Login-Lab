package com.example.loginLab.demo.filter;

import com.example.loginLab.demo.config.UserAuthProvider;
import com.example.loginLab.demo.exception.AppException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserAuthProvider userAuthProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(header != null) {

            String[] authElements = header.split(" ");

            if(authElements.length == 2 && authElements[0].equals("Bearer")) {

                String token = authElements[1];

                try {

                   if("GET".equals(request.getMethod())) {
                       SecurityContextHolder
                               .getContext()
                               .setAuthentication(userAuthProvider.validateToken(token, request));
                   } else {
                       SecurityContextHolder
                               .getContext()
                               .setAuthentication(userAuthProvider.validateTokenStrongly(token, request));
                   }

                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                    throw new AppException("Invalid token", HttpStatus.UNAUTHORIZED);
                }
            } else {
                SecurityContextHolder.clearContext();
                throw new AppException("Header not valid", HttpStatus.BAD_REQUEST);
            }

        }
        filterChain.doFilter(request,response);
    }
}
