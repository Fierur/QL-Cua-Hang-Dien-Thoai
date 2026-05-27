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
                .addPathPatterns(
                        "/admin/**",
                        "/sanpham/**",
                        "/khachhang/**",
                        "/hoadon/**",
                        "/phieunhap/**",
                        "/baohanh/**",
                        "/baocao/**",
                        "/taikhoan/**"
                )
                .excludePathPatterns(
                        "/sanpham/chitiet/**", // (nếu có storefront API)
                        "/login",
                        "/css/**", "/js/**", "/img/**"
                );
    }
}