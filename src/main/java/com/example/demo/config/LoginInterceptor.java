package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// Interceptor: chặn mọi request, kiểm tra đã login chưa
// Nếu chưa login → redirect về /login
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession();
        Object taiKhoan = session.getAttribute("taiKhoan");

        // Cho phép qua nếu đã login
        if (taiKhoan != null) {
            return true;
        }

        // Chưa login → về trang đăng nhập
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}