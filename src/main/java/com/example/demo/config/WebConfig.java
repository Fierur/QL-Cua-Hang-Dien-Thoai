package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Đăng ký interceptor, loại trừ /login và static files
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")                  // Áp dụng cho tất cả
                .excludePathPatterns(
                        "/login",                        // Trang đăng nhập
                        "/login/xacnhan",               // POST login
                        "/css/**", "/js/**", "/img/**"  // Static files
                );
    }
}