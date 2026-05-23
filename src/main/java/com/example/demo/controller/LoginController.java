package com.example.demo.controller;

import com.example.demo.entity.TaiKhoan;
import com.example.demo.repository.TaiKhoanRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    // Trang đăng nhập
    @GetMapping("/login")
    public String trangLogin() {
        return "login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login/xacnhan")
    public String xacNhanLogin(@RequestParam("tenDangNhap") String tenDangNhap,
                               @RequestParam("matKhau") String matKhau,
                               HttpSession session,
                               Model model) {

        Optional<TaiKhoan> optional = taiKhoanRepository.findByTenDangNhapAndMatKhau(tenDangNhap, matKhau);

        if (!optional.isPresent()) {
            model.addAttribute("loi", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "login";
        }

        TaiKhoan tk = optional.get();

        if (tk.getTrangThai() == 0) {
            model.addAttribute("loi", "Tài khoản đã bị khóa. Liên hệ Admin!");
            return "login";
        }

        // Lưu tài khoản vào session
        session.setAttribute("taiKhoan", tk);

        return "redirect:/";
    }

    // Đăng xuất
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // Trang chủ (dashboard)
    @GetMapping("/")
    public String trangChu() {
        return "index";
    }
}
