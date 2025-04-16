package com.example.loginLab.demo.filter;

import com.example.loginLab.demo.config.UserAuthProvider;
import com.example.loginLab.demo.exception.securityException.CustomAccessDeniedHandler;
import com.example.loginLab.demo.exception.securityException.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilter {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    private final UserAuthProvider userAuthProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource()))
                .addFilterBefore(new JwtFilter(userAuthProvider), BasicAuthenticationFilter.class)
                /* no need to handle session as stateless */
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(
                                        "/auth/register",
                                        "/auth/login"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                );

        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        /* allowing backend to receive credentials
        which are sent from frontend through headers */
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        //config.setMaxAge(600L); // cache preflight response for some time

        String[] origins = allowedOrigins.split(",");
        config.setAllowedOrigins(Arrays.asList(origins));


        source.registerCorsConfiguration("/**", config);
        return source;

    }

}
