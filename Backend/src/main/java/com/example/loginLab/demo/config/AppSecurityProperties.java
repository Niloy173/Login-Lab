package com.example.loginLab.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.security")
public class AppSecurityProperties {

    private String jwtSecret;
    private long jwtExpirationInMs;
    private String tokenCookieName;
}