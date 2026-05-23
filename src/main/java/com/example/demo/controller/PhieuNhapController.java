package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/phieunhap")
public class PhieuNhapController {

    @Autowired private PhieuNhapRepository phieuNhapRepository;
    @Autowired private NhaCungCapRepository nhaCungCapRepository;
    @Autowired private SanPhamRepository sanPhamRepository;
    @Autowired private IMEIRepository imeiRepository;

    // Danh sách phiếu nhập
    @GetMapping
    public String danhSach(Model model) {
        model.addAttribute("dsPN", phieuNhapRepository.findAll());
        return "phieunhap/index";
    }

    // Form nhập hàng mới
    @GetMapping("/them")
    public String them(Model model) {
        model.addAttribute("pn", new PhieuNhap());
        model.addAttribute("dsNCC", nhaCungCapRepository.findAll());
        model.addAttribute("dsSP", sanPhamRepository.findAll());
        return "phieunhap/them";
    }

    // Lưu phiếu nhập kho
    @PostMapping("/luu")
    public String luu(@RequestParam("maNCC") int maNCC,
                      @RequestParam("maSP") int maSP,
                      @RequestParam("soLuong") int soLuong,
                      @RequestParam("giaNhap") long giaNhap,
                      @RequestParam("danhSachImei") String danhSachImei,
                      @RequestParam(value = "ghiChu", defaultValue = "") String ghiChu,
                      HttpSession session) {

        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        SanPham sp = sanPhamRepository.findById(maSP).orElse(null);
        NhaCungCap ncc = nhaCungCapRepository.findById(maNCC).orElse(null);

        if (sp == null) return "redirect:/phieunhap/them";

        // Tạo phiếu nhập
        PhieuNhap pn = new PhieuNhap();
        pn.setNgayNhap(LocalDate.now());
        pn.setNhaCungCap(ncc);
        pn.setTaiKhoan(tk);
        pn.setGhiChu(ghiChu);
        phieuNhapRepository.save(pn);

        // Thêm từng IMEI vào kho
        // danhSachImei: các IMEI ngăn cách bởi dấu xuống dòng hoặc dấu phẩy
        String[] imeiArr = danhSachImei.split("[,\n]+");
        int soLuongThucTe = 0;

        for (String imeiStr : imeiArr) {
            String cleaned = imeiStr.trim();
            if (!cleaned.isEmpty() && !imeiRepository.existsById(cleaned)) {
                IMEI imei = new IMEI();
                imei.setImei(cleaned);
                imei.setSanPham(sp);
                imei.setTrangThai("TRONG_KHO");
                imeiRepository.save(imei);
                soLuongThucTe++;
            }
        }

        // Cập nhật tồn kho sản phẩm
        sp.setSoLuongTon(sp.getSoLuongTon() + soLuongThucTe);
        sanPhamRepository.save(sp);

        return "redirect:/phieunhap";
    }

    // Chi tiết phiếu nhập
    @GetMapping("/chitiet/{maPN}")
    public String chiTiet(@PathVariable int maPN, Model model) {
        model.addAttribute("pn",
                phieuNhapRepository.findById(maPN).orElse(null));
        return "phieunhap/chitiet";
    }

    // Xóa phiếu nhập (chỉ Admin)
    @GetMapping("/xoa/{maPN}")
    public String xoa(@PathVariable int maPN, HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk != null && tk.isAdmin()) {
            phieuNhapRepository.deleteById(maPN);
        }
        return "redirect:/phieunhap";
    }
}