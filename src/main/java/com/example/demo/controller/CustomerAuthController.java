package com.example.demo.controller;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.GioHang;
import com.example.demo.repository.KhachHangRepository;
import com.example.demo.repository.GioHangRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerAuthController {

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private GioHangRepository gioHangRepository;


    @GetMapping("/register")
    public String showRegister(HttpSession session, Model model) {
        if (session.getAttribute("customerAccount") != null) {
            return "redirect:/";
        }
        model.addAttribute("khachHang", new KhachHang());
        return "storefront/register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute KhachHang khachHang,
                                  Model model,
                                  HttpSession session) {
        
        // Kiểm tra xem SĐT đã tồn tại chưa
        if (khachHangRepository.findBySdt(khachHang.getSdt()) != null) {
            model.addAttribute("loi", "Số điện thoại này đã được đăng ký!");
            model.addAttribute("khachHang", khachHang);
            return "storefront/register";
        }
        
        // Lưu khách hàng mới
        KhachHang savedKH = khachHangRepository.save(khachHang);
        
        // Tạo giỏ hàng rỗng cho khách hàng mới
        GioHang gh = new GioHang(savedKH);
        gioHangRepository.save(gh);
        
        // Auto login
        session.setAttribute("customerAccount", savedKH);
        return "redirect:/";
    }

}
