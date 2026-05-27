package com.example.demo.controller;

import com.example.demo.entity.KhachHang;
import com.example.demo.repository.KhachHangRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class CustomerProfileController {

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private com.example.demo.repository.GioHangRepository gioHangRepository;

    private KhachHang getSessionCustomer(HttpSession session) {
        return (KhachHang) session.getAttribute("customerAccount");
    }

    private int getCartItemCount(KhachHang kh) {
        if (kh == null) return 0;
        var gh = gioHangRepository.findByKhachHang(kh);
        if (gh == null || gh.getChiTiet() == null) return 0;
        return gh.getChiTiet().stream().mapToInt(c -> c.getSoLuong()).sum();
    }

    @GetMapping
    public String profile(Model model, HttpSession session) {
        KhachHang kh = getSessionCustomer(session);
        if (kh == null) return "redirect:/login";

        // Refresh customer from DB
        kh = khachHangRepository.findById(kh.getMaKH()).orElse(null);
        if (kh == null) return "redirect:/login";

        model.addAttribute("customer", kh);
        model.addAttribute("cartCount", getCartItemCount(kh));
        
        return "storefront/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam String tenKH,
                                @RequestParam String sdt,
                                @RequestParam(required = false) String diaChi,
                                HttpSession session,
                                RedirectAttributes redirectAttrs) {
        KhachHang kh = getSessionCustomer(session);
        if (kh == null) return "redirect:/login";

        KhachHang dbKh = khachHangRepository.findById(kh.getMaKH()).orElse(null);
        if (dbKh != null) {
            dbKh.setTenKH(tenKH);
            dbKh.setSdt(sdt);
            if (diaChi != null) dbKh.setDiaChi(diaChi);
            
            khachHangRepository.save(dbKh);
            session.setAttribute("customerAccount", dbKh);
            redirectAttrs.addFlashAttribute("successMsg", "Cập nhật thông tin thành công!");
        }

        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttrs) {
        KhachHang kh = getSessionCustomer(session);
        if (kh == null) return "redirect:/login";

        KhachHang dbKh = khachHangRepository.findById(kh.getMaKH()).orElse(null);
        if (dbKh != null) {
            if (dbKh.getMatKhau().equals(currentPassword)) {
                dbKh.setMatKhau(newPassword);
                khachHangRepository.save(dbKh);
                session.setAttribute("customerAccount", dbKh);
                redirectAttrs.addFlashAttribute("pwdSuccessMsg", "Đổi mật khẩu thành công!");
            } else {
                redirectAttrs.addFlashAttribute("pwdErrorMsg", "Mật khẩu hiện tại không đúng!");
            }
        }

        return "redirect:/profile";
    }
}
