package com.example.loginLab.demo.filter;

import com.example.loginLab.demo.config.AppSecurityProperties;
import com.example.loginLab.demo.config.UserAuthProvider;
import com.example.loginLab.demo.dto.UserDto;
import com.example.loginLab.demo.exception.AppException;
import com.example.loginLab.demo.util.Utils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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

        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        String fullPath = path.substring(contextPath.length());

        // Skip token validation for Swagger and public endpoints
        if (fullPath.startsWith("/v3/api-docs") ||
                fullPath.startsWith("/swagger-ui") ||
                fullPath.startsWith("/auth/login") ||
                fullPath.startsWith("/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }



//        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

//        if(header != null) {

//            String[] authElements = header.split(" ");

//            if(authElements.length == 2 && authElements[0].equals("Bearer")) {

//                String token = authElements[1];

            String token = Utils.getCookie(request,appSecurityProperties.getTokenCookieName());
            //log.info("token {}", token);

            if(token == null) {
                SecurityContextHolder.clearContext();
                throw new AppException("Header not valid", HttpStatus.BAD_REQUEST);
            }

            Authentication authentication;

            try {

                   if("GET".equals(request.getMethod())) {

                    authentication =  userAuthProvider.validateToken(
                                               token,
                                               request,
                                               response);
                   } else {

                       authentication = userAuthProvider
                                       .validateTokenStrongly
                                               (token,
                                               request,
                                               response
                                               );

                   }

                   if (authentication != null) {
                       /* set auth in context */
                       SecurityContextHolder.getContext().setAuthentication(authentication);

                       // extract user info from Authentication and store it in request attribute
                       Object principal = authentication.getPrincipal();

                       if(principal instanceof UserDto user) {
                           request.setAttribute("userid", user.getUserid());
                           request.setAttribute("username", user.getUsername());
                           request.setAttribute("email", user.getEmail());
                           request.setAttribute("role", user.getRole());
                       }

                       log.info("username {} ", request.getAttribute("username"));
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
