package com.example.demo.controller;

import com.example.demo.entity.TaiKhoan;
import com.example.demo.repository.TaiKhoanRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/taikhoan")
public class TaiKhoanController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    // Danh sách tài khoản (chỉ Admin được truy cập)
    @GetMapping
    public String danhSach(Model model, HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk == null || !tk.isAdmin()) return "redirect:/admin";

        model.addAttribute("dsTK", taiKhoanRepository.findAll());
        return "taikhoan/index";
    }

    // Form thêm tài khoản (chỉ Admin)
    @GetMapping("/them")
    public String them(Model model, HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk == null || !tk.isAdmin()) return "redirect:/admin";

        model.addAttribute("tk", new TaiKhoan());
        return "taikhoan/them";
    }

    // Lưu tài khoản mới (chỉ Admin)
    @PostMapping("/luu")
    public String luu(@ModelAttribute TaiKhoan tk,
                      HttpSession session,
                      Model model) {

        TaiKhoan loginTK = (TaiKhoan) session.getAttribute("taiKhoan");
        if (loginTK == null || !loginTK.isAdmin()) return "redirect:/admin";

        if (taiKhoanRepository.existsByTenDangNhap(tk.getTenDangNhap())) {
            model.addAttribute("loi", "Tên đăng nhập đã tồn tại!");
            model.addAttribute("tk", tk);
            return "taikhoan/them";
        }

        taiKhoanRepository.save(tk);
        return "redirect:/taikhoan";
    }

    // Form sửa tài khoản (chỉ Admin)
    @GetMapping("/sua/{maTK}")
    public String sua(@PathVariable int maTK,
                      Model model,
                      HttpSession session) {

        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk == null || !tk.isAdmin()) return "redirect:/admin";

        model.addAttribute("tk",
                taiKhoanRepository.findById(maTK).orElse(null));
        return "taikhoan/sua";
    }

    // Cập nhật tài khoản (chỉ Admin)
    @PostMapping("/capnhat")
    public String capNhat(@ModelAttribute TaiKhoan tk, HttpSession session) {
        TaiKhoan loginTK = (TaiKhoan) session.getAttribute("taiKhoan");
        if (loginTK == null || !loginTK.isAdmin()) return "redirect:/admin";

        taiKhoanRepository.save(tk);
        return "redirect:/taikhoan";
    }

    // Khóa / mở khóa tài khoản (chỉ Admin, không tự khóa mình)
    @GetMapping("/khoatk/{maTK}")
    public String khoaTK(@PathVariable int maTK, HttpSession session) {
        TaiKhoan loginTK = (TaiKhoan) session.getAttribute("taiKhoan");
        if (loginTK == null || !loginTK.isAdmin()) return "redirect:/admin";
        if (loginTK.getMaTK() == maTK) return "redirect:/taikhoan"; // Không tự khóa

        TaiKhoan tk = taiKhoanRepository.findById(maTK).orElse(null);
        if (tk != null) {
            tk.setTrangThai(tk.getTrangThai() == 1 ? 0 : 1);
            taiKhoanRepository.save(tk);
        }
        return "redirect:/taikhoan";
    }
}