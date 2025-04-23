package com.example.loginLab.demo.config;

import com.example.loginLab.demo.interceptor.RequestLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final RequestLoggingInterceptor requestLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        WebMvcConfigurer.super.addInterceptors(registry);

        /* log all api request */
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/**") // intercept all
                .excludePathPatterns(
                        "/auth/register",
                        "/auth/login"

                );
        
    }
}
