package com.example.demo.controller;

import com.example.demo.entity.Banner;
import com.example.demo.repository.BannerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/banners")
public class AdminBannerController {

    @Autowired
    private BannerRepository bannerRepository;

    @GetMapping
    public String list(Model model, HttpSession session) {
        if (session.getAttribute("taiKhoan") == null) return "redirect:/login";
        model.addAttribute("dsBanner", bannerRepository.findAll());
        model.addAttribute("banner", new Banner());
        return "admin/banners";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Banner banner, HttpSession session) {
        if (session.getAttribute("taiKhoan") == null) return "redirect:/login";
        bannerRepository.save(banner);
        return "redirect:/admin/banners";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("taiKhoan") == null) return "redirect:/login";
        Banner banner = bannerRepository.findById(id).orElse(null);
        if (banner == null) return "redirect:/admin/banners";
        model.addAttribute("banner", banner);
        model.addAttribute("dsBanner", bannerRepository.findAll());
        return "admin/banners";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("taiKhoan") == null) return "redirect:/login";
        bannerRepository.deleteById(id);
        return "redirect:/admin/banners";
    }
}
