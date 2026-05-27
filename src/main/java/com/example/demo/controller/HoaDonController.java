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
    @Autowired private ChiTietHoaDonRepository chiTietHoaDonRepository;

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

    // Lưu hóa đơn (Hỗ trợ nhiều máy)
    @PostMapping("/luu")
    public String luu(@RequestParam("maKH") String maKHStr,
                      @RequestParam("imeis") List<String> imeis,
                      @RequestParam("hinhThucTT") String hinhThucTT,
                      @RequestParam(value = "ghiChu", defaultValue = "") String ghiChu,
                      HttpSession session) {

        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        
        if (imeis == null || imeis.isEmpty()) {
            return "redirect:/hoadon/them";
        }

        // Tạo hóa đơn
        HoaDon hd = new HoaDon();
        hd.setNgayLap(LocalDate.now());
        hd.setHinhThucTT(hinhThucTT);
        hd.setGhiChu(ghiChu);
        hd.setTaiKhoan(tk);

        // Gán khách hàng nếu có
        if (!maKHStr.isEmpty()) {
            int maKH = Integer.parseInt(maKHStr);
            hd.setKhachHang(khachHangRepository.findById(maKH).orElse(null));
        }

        long tongTien = 0;
        HoaDon saved = hoaDonRepository.save(hd);

        for (String imeiStr : imeis) {
            IMEI imei = imeiRepository.findById(imeiStr).orElse(null);
            if (imei != null && "TRONG_KHO".equals(imei.getTrangThai())) {
                SanPham sp = imei.getSanPham();
                
                // Tạo chi tiết hóa đơn
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.getId().setMaHD(saved.getMaHD());
                ct.getId().setImei(imeiStr);
                ct.setHoaDon(saved);
                ct.setImeiEntity(imei);
                ct.setSanPham(sp);
                ct.setDonGia(sp.getGiaBan());
                
                chiTietHoaDonRepository.save(ct);
                tongTien += sp.getGiaBan();

                // Cập nhật trạng thái IMEI → đã bán
                imei.setTrangThai("DA_BAN");
                imeiRepository.save(imei);

                // Cập nhật tồn kho sản phẩm
                sp.setSoLuongTon(sp.getSoLuongTon() - 1);
                sanPhamRepository.save(sp);
            }
        }
        
        saved.setTongTien(tongTien);
        hoaDonRepository.save(saved);

        return "redirect:/hoadon";
    }

    // Chi tiết hóa đơn
    @GetMapping("/chitiet/{maHD}")
    public String chiTiet(@PathVariable int maHD, Model model) {
        model.addAttribute("hd",
                hoaDonRepository.findById(maHD).orElse(null));
        return "hoadon/chitiet";
    }

    // Form sửa hóa đơn
    @GetMapping("/sua/{maHD}")
    public String sua(@PathVariable int maHD, Model model) {
        HoaDon hd = hoaDonRepository.findById(maHD).orElse(null);
        if (hd == null) return "redirect:/hoadon";

        List<ChiTietHoaDon> chiTiets = chiTietHoaDonRepository.findByHoaDon_MaHD(maHD);

        model.addAttribute("hd", hd);
        model.addAttribute("dsChiTiet", chiTiets);
        model.addAttribute("dsKH", khachHangRepository.findAll());
        model.addAttribute("dsSP", sanPhamRepository.findBySoLuongTonGreaterThan(0));
        return "hoadon/sua";
    }

    // Cập nhật hóa đơn
    @PostMapping("/capnhat")
    public String capNhat(@RequestParam("maHD") int maHD,
                          @RequestParam("maKH") String maKHStr,
                          @RequestParam(value = "imeis", required = false) List<String> imeis,
                          @RequestParam("hinhThucTT") String hinhThucTT,
                          @RequestParam(value = "ghiChu", defaultValue = "") String ghiChu,
                          HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk == null || !tk.isAdmin()) {
            return "redirect:/hoadon";
        }

        HoaDon hd = hoaDonRepository.findById(maHD).orElse(null);
        if (hd != null) {
            hd.setHinhThucTT(hinhThucTT);
            hd.setGhiChu(ghiChu);
            if (!maKHStr.isEmpty()) {
                int maKH = Integer.parseInt(maKHStr);
                hd.setKhachHang(khachHangRepository.findById(maKH).orElse(null));
            } else {
                hd.setKhachHang(null);
            }
            
            // Xóa các chi tiết cũ và hoàn trả kho
            List<ChiTietHoaDon> oldChiTiets = chiTietHoaDonRepository.findByHoaDon_MaHD(maHD);
            for (ChiTietHoaDon ct : oldChiTiets) {
                IMEI imei = ct.getImeiEntity();
                SanPham sp = ct.getSanPham();
                imei.setTrangThai("TRONG_KHO");
                imeiRepository.save(imei);
                sp.setSoLuongTon(sp.getSoLuongTon() + 1);
                sanPhamRepository.save(sp);
                chiTietHoaDonRepository.delete(ct);
            }
            
            long tongTien = 0;
            if (imeis != null && !imeis.isEmpty()) {
                for (String imeiStr : imeis) {
                    IMEI imei = imeiRepository.findById(imeiStr).orElse(null);
                    if (imei != null && ("TRONG_KHO".equals(imei.getTrangThai()) || oldChiTiets.stream().anyMatch(c -> c.getId().getImei().equals(imeiStr)))) {
                        SanPham sp = imei.getSanPham();
                        
                        ChiTietHoaDon ct = new ChiTietHoaDon();
                        ct.getId().setMaHD(maHD);
                        ct.getId().setImei(imeiStr);
                        ct.setHoaDon(hd);
                        ct.setImeiEntity(imei);
                        ct.setSanPham(sp);
                        ct.setDonGia(sp.getGiaBan());
                        
                        chiTietHoaDonRepository.save(ct);
                        tongTien += sp.getGiaBan();

                        imei.setTrangThai("DA_BAN");
                        imeiRepository.save(imei);

                        sp.setSoLuongTon(sp.getSoLuongTon() - 1);
                        sanPhamRepository.save(sp);
                    }
                }
            }
            
            hd.setTongTien(tongTien);
            hoaDonRepository.save(hd);
        }
        return "redirect:/hoadon";
    }

    // Xóa hóa đơn (chỉ Admin)
    @GetMapping("/xoa/{maHD}")
    public String xoa(@PathVariable int maHD, HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk != null && tk.isAdmin()) {
            // Hoàn trả IMEI và số lượng tồn
            List<ChiTietHoaDon> chiTiets = chiTietHoaDonRepository.findByHoaDon_MaHD(maHD);
            for (ChiTietHoaDon ct : chiTiets) {
                IMEI imei = ct.getImeiEntity();
                SanPham sp = ct.getSanPham();
                if (imei != null) {
                    imei.setTrangThai("TRONG_KHO");
                    imeiRepository.save(imei);
                }
                if (sp != null) {
                    sp.setSoLuongTon(sp.getSoLuongTon() + 1);
                    sanPhamRepository.save(sp);
                }
            }
            // Spring Data JPA cascade hoặc tự động xóa chi tiết nếu cấu hình đúng, 
            // nhưng để an toàn ta có thể xóa tay chi tiết trước:
            chiTietHoaDonRepository.deleteAll(chiTiets);
            hoaDonRepository.deleteById(maHD);
        }
        return "redirect:/hoadon";
    }
}