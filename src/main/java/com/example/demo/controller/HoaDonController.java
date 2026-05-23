package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hoadon")
public class HoaDonController {

    @Autowired private HoaDonRepository hoaDonRepository;
    @Autowired private KhachHangRepository khachHangRepository;
    @Autowired private SanPhamRepository sanPhamRepository;
    @Autowired private IMEIRepository imeiRepository;

    // Danh sách hóa đơn
    @GetMapping
    public String danhSach(Model model) {
        model.addAttribute("dsHD", hoaDonRepository.findAll());
        return "hoadon/index";
    }

    // Form tạo hóa đơn mới
    @GetMapping("/them")
    public String them(Model model) {
        model.addAttribute("hd", new HoaDon());
        model.addAttribute("dsKH", khachHangRepository.findAll());
        model.addAttribute("dsSP", sanPhamRepository.findBySoLuongTonGreaterThan(0));
        return "hoadon/them";
    }

    // Lấy danh sách IMEI còn trong kho của 1 sản phẩm (dùng cho dropdown chọn IMEI)
    @GetMapping("/imei/{maSP}")
    @ResponseBody
    public List<String> layImei(@PathVariable int maSP) {
        List<IMEI> list = imeiRepository.findBySanPham_MaSPAndTrangThai(maSP, "TRONG_KHO");
        List<String> result = new ArrayList<>();
        for (IMEI i : list) {
            result.add(i.getImei());
        }
        return result;
    }

    // Lưu hóa đơn (1 máy = 1 IMEI)
    @PostMapping("/luu")
    public String luu(@RequestParam("maKH") String maKHStr,
                      @RequestParam("maSP") int maSP,
                      @RequestParam("imeiChon") String imeiChon,
                      @RequestParam("hinhThucTT") String hinhThucTT,
                      @RequestParam(value = "ghiChu", defaultValue = "") String ghiChu,
                      HttpSession session) {

        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");

        // Lấy IMEI và sản phẩm
        IMEI imei = imeiRepository.findById(imeiChon).orElse(null);
        SanPham sp = sanPhamRepository.findById(maSP).orElse(null);

        if (imei == null || sp == null) {
            return "redirect:/hoadon/them";
        }

        // Tạo hóa đơn
        HoaDon hd = new HoaDon();
        hd.setNgayLap(LocalDate.now());
        hd.setHinhThucTT(hinhThucTT);
        hd.setTongTien(sp.getGiaBan());
        hd.setGhiChu(ghiChu);
        hd.setTaiKhoan(tk);

        // Gán khách hàng nếu có
        if (!maKHStr.isEmpty()) {
            int maKH = Integer.parseInt(maKHStr);
            hd.setKhachHang(khachHangRepository.findById(maKH).orElse(null));
        }

        HoaDon saved = hoaDonRepository.save(hd);

        // Tạo chi tiết hóa đơn
        ChiTietHoaDon ct = new ChiTietHoaDon();
        ct.getId().setMaHD(saved.getMaHD());
        ct.getId().setImei(imeiChon);
        ct.setHoaDon(saved);
        ct.setImeiEntity(imei);
        ct.setSanPham(sp);
        ct.setDonGia(sp.getGiaBan());

        // Cập nhật trạng thái IMEI → đã bán
        imei.setTrangThai("DA_BAN");
        imeiRepository.save(imei);

        // Cập nhật tồn kho sản phẩm
        sp.setSoLuongTon(sp.getSoLuongTon() - 1);
        sanPhamRepository.save(sp);

        return "redirect:/hoadon";
    }

    // Chi tiết hóa đơn
    @GetMapping("/chitiet/{maHD}")
    public String chiTiet(@PathVariable int maHD, Model model) {
        model.addAttribute("hd",
                hoaDonRepository.findById(maHD).orElse(null));
        return "hoadon/chitiet";
    }

    // Xóa hóa đơn (chỉ Admin)
    @GetMapping("/xoa/{maHD}")
    public String xoa(@PathVariable int maHD, HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk != null && tk.isAdmin()) {
            hoaDonRepository.deleteById(maHD);
        }
        return "redirect:/hoadon";
    }
}