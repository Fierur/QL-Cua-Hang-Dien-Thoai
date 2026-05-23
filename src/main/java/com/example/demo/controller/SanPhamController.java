package com.example.demo.controller;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.repository.SanPhamRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sanpham")
public class SanPhamController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    // Danh sách sản phẩm, hỗ trợ tìm kiếm theo tên
    @GetMapping
    public String danhSach(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                           Model model) {

        List<SanPham> ds = keyword.isEmpty()
                ? sanPhamRepository.findAll()
                : sanPhamRepository.findByTenSPContainingIgnoreCase(keyword);

        model.addAttribute("dsSP", ds);
        model.addAttribute("keyword", keyword);
        return "sanpham/index";
    }

    // Form thêm sản phẩm mới
    @GetMapping("/them")
    public String them(Model model) {
        model.addAttribute("sp", new SanPham());
        return "sanpham/them";
    }

    // Lưu sản phẩm mới
    @PostMapping("/luu")
    public String luu(@ModelAttribute SanPham sp) {
        sanPhamRepository.save(sp);
        return "redirect:/sanpham";
    }

    // Form sửa sản phẩm
    @GetMapping("/sua/{maSP}")
    public String sua(@PathVariable int maSP, Model model) {
        model.addAttribute("sp",
                sanPhamRepository.findById(maSP).orElse(null));
        return "sanpham/sua";
    }

    // Cập nhật sản phẩm
    @PostMapping("/capnhat")
    public String capNhat(@ModelAttribute SanPham sp) {
        sanPhamRepository.save(sp);
        return "redirect:/sanpham";
    }

    // Xóa sản phẩm (chỉ Admin)
    @GetMapping("/xoa/{maSP}")
    public String xoa(@PathVariable int maSP, HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk != null && tk.isAdmin()) {
            sanPhamRepository.deleteById(maSP);
        }
        return "redirect:/sanpham";
    }

    // Chi tiết sản phẩm
    @GetMapping("/chitiet/{maSP}")
    public String chiTiet(@PathVariable int maSP, Model model) {
        model.addAttribute("sp",
                sanPhamRepository.findById(maSP).orElse(null));
        return "sanpham/chitiet";
    }
}