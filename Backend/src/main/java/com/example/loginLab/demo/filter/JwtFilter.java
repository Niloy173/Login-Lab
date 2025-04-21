package com.example.loginLab.demo.filter;

import com.example.loginLab.demo.config.AppSecurityProperties;
import com.example.loginLab.demo.config.UserAuthProvider;
import com.example.loginLab.demo.exception.AppException;
import com.example.loginLab.demo.util.Utils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserAuthProvider userAuthProvider;
    private final AppSecurityProperties appSecurityProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

//        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

//        if(header != null) {

//            String[] authElements = header.split(" ");

//            if(authElements.length == 2 && authElements[0].equals("Bearer")) {

//                String token = authElements[1];

            String token = Utils.getCookie(request,appSecurityProperties.getTokenCookieName());
            log.info("token {}", token);

            if(token == null) {
                SecurityContextHolder.clearContext();
                throw new AppException("Header not valid", HttpStatus.BAD_REQUEST);
            }

                try {

                   if("GET".equals(request.getMethod())) {
                       SecurityContextHolder
                               .getContext()
                               .setAuthentication(userAuthProvider
                                       .validateToken(
                                               token,
                                               request,
                                               response));
                   } else {
                       SecurityContextHolder
                               .getContext()
                               .setAuthentication(userAuthProvider
                                       .validateTokenStrongly
                                               (token,
                                               request,
                                               response
                                               ));
                   }

                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                    throw new AppException("Invalid token", HttpStatus.UNAUTHORIZED);
                }
//            } else {
//                SecurityContextHolder.clearContext();
//                throw new AppException("Header not valid", HttpStatus.BAD_REQUEST);
//            }

//        }
        filterChain.doFilter(request,response);
    }

}
