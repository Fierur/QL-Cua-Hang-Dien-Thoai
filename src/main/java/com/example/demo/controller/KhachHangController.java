package com.example.demo.controller;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.repository.KhachHangRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/khachhang")
public class KhachHangController {

    @Autowired
    private KhachHangRepository khachHangRepository;

    // Danh sách khách hàng, tìm kiếm theo tên hoặc SĐT
    @GetMapping
    public String danhSach(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                           Model model) {

        List<KhachHang> ds;
        if (keyword.isEmpty()) {
            ds = khachHangRepository.findAll();
        } else if (keyword.matches("\\d+")) {
            ds = khachHangRepository.findBySdtContaining(keyword);
        } else {
            ds = khachHangRepository.findByTenKHContainingIgnoreCase(keyword);
        }

        model.addAttribute("dsKH", ds);
        model.addAttribute("keyword", keyword);
        return "khachhang/index";
    }

    // Form thêm khách hàng
    @GetMapping("/them")
    public String them(Model model) {
        model.addAttribute("kh", new KhachHang());
        return "khachhang/them";
    }

    // Lưu khách hàng mới
    @PostMapping("/luu")
    public String luu(@ModelAttribute KhachHang kh) {
        khachHangRepository.save(kh);
        return "redirect:/khachhang";
    }

    // Form sửa khách hàng
    @GetMapping("/sua/{maKH}")
    public String sua(@PathVariable int maKH, Model model) {
        model.addAttribute("kh",
                khachHangRepository.findById(maKH).orElse(null));
        return "khachhang/sua";
    }

    // Cập nhật khách hàng
    @PostMapping("/capnhat")
    public String capNhat(@ModelAttribute KhachHang kh) {
        khachHangRepository.save(kh);
        return "redirect:/khachhang";
    }

    // Xóa khách hàng (chỉ Admin)
    @GetMapping("/xoa/{maKH}")
    public String xoa(@PathVariable int maKH, HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk != null && tk.isAdmin()) {
            khachHangRepository.deleteById(maKH);
        }
        return "redirect:/khachhang";
    }

    // Chi tiết khách hàng
    @GetMapping("/chitiet/{maKH}")
    public String chiTiet(@PathVariable int maKH, Model model) {
        model.addAttribute("kh",
                khachHangRepository.findById(maKH).orElse(null));
        return "khachhang/chitiet";
    }
}