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

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.GioHang;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.repository.GioHangRepository;

@Controller
public class LoginController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    // Trang đăng nhập (Dùng chung cho cả Khách hàng và Admin/Staff)
    @GetMapping("/login")
    public String trangLogin(HttpSession session) {
        if (session.getAttribute("customerAccount") != null || session.getAttribute("taiKhoan") != null) {
            return "redirect:/"; // Đã đăng nhập thì về trang chủ
        }
        return "storefront/login";
    }

    // Xử lý đăng nhập chung
    @PostMapping("/login")
    public String xacNhanLogin(@RequestParam("tenDangNhap") String tenDangNhap,
                               @RequestParam("matKhau") String matKhau,
                               HttpSession session,
                               Model model) {

        // 1. Kiểm tra xem có phải tài khoản Admin/Staff không (tenDangNhap)
        Optional<TaiKhoan> optTK = taiKhoanRepository.findByTenDangNhapAndMatKhau(tenDangNhap, matKhau);
        
        if (optTK.isPresent()) {
            TaiKhoan tk = optTK.get();
            if (tk.getTrangThai() == 0) {
                model.addAttribute("loi", "Tài khoản đã bị khóa. Liên hệ Admin!");
                return "storefront/login";
            }
            
            // Lưu session Admin
            session.setAttribute("taiKhoan", tk);
            
            // Tạo ẩn tài khoản Khách hàng cho Admin để mua hàng nếu chưa có
            KhachHang kh = null;
            if (tk.getSdt() != null && !tk.getSdt().trim().isEmpty()) {
                kh = khachHangRepository.findBySdt(tk.getSdt());
            }
            if (kh == null) {
                kh = new KhachHang();
                kh.setTenKH(tk.getHoTen());
                kh.setSdt(tk.getSdt() != null ? tk.getSdt() : ("ADMIN_" + tk.getTenDangNhap()));
                kh.setMatKhau(tk.getMatKhau());
                kh = khachHangRepository.save(kh);
                
                GioHang gh = new GioHang(kh);
                gioHangRepository.save(gh);
            }
            session.setAttribute("customerAccount", kh);
            
            return "redirect:/admin";
        }

        // 2. Nếu không phải Admin, kiểm tra Khách hàng (sử dụng sdt làm tên đăng nhập)
        KhachHang kh = khachHangRepository.findBySdt(tenDangNhap);
        if (kh != null && matKhau.equals(kh.getMatKhau())) {
            session.setAttribute("customerAccount", kh);
            return "redirect:/";
        }

        // 3. Đăng nhập thất bại
        model.addAttribute("loi", "Tên đăng nhập, SĐT hoặc mật khẩu không chính xác!");
        return "storefront/login";
    }

    // Đăng xuất (Dùng chung, hủy toàn bộ session)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // Trang chủ (dashboard) Admin
    @GetMapping("/admin")
    public String trangChu(HttpSession session) {
        if (session.getAttribute("taiKhoan") == null) {
            return "redirect:/login";
        }
        return "index";
    }
}
