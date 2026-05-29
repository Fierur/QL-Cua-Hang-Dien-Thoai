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
@RequestMapping("/donhang")
public class AdminDonHangController {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Autowired
    private IMEIRepository imeiRepository;
    
    @Autowired
    private SanPhamRepository sanPhamRepository;

    // Helper: Lấy tài khoản admin/staff đang đăng nhập
    private TaiKhoan getSessionAdmin(HttpSession session) {
        return (TaiKhoan) session.getAttribute("taiKhoan");
    }

    // 1. Danh sách đơn hàng
    @GetMapping
    public String index(Model model, HttpSession session) {
        TaiKhoan tk = getSessionAdmin(session);
        if (tk == null) return "redirect:/admin";

        model.addAttribute("dsDH", donHangRepository.findAll());
        return "DonHang/index";
    }

    // 2. Form duyệt đơn hàng (chọn IMEI)
    @GetMapping("/duyet/{maDH}")
    public String formDuyet(@PathVariable int maDH, Model model, HttpSession session) {
        TaiKhoan tk = getSessionAdmin(session);
        if (tk == null) return "redirect:/admin";

        DonHang dh = donHangRepository.findById(maDH).orElse(null);
        if (dh == null || !"CHO_DUYET".equals(dh.getTrangThai())) {
            return "redirect:/donhang";
        }
        
        model.addAttribute("dh", dh);
        
        // Lấy danh sách IMEI còn trống cho từng sản phẩm trong đơn hàng
        for (ChiTietDonHang ct : dh.getChiTiet()) {
            List<IMEI> availableImeis = imeiRepository.findBySanPhamAndTrangThai(ct.getSanPham(), "TRONG_KHO");
            model.addAttribute("imeis_" + ct.getSanPham().getMaSP(), availableImeis);
        }
        
        return "DonHang/duyet";
    }

    // 3. Xử lý duyệt đơn hàng và sinh Hóa Đơn
    @PostMapping("/duyet")
    public String xuLyDuyet(@RequestParam int maDH, 
                            @RequestParam List<String> selectedImeis, 
                            HttpSession session, 
                            Model model) {
        TaiKhoan tk = getSessionAdmin(session);
        if (tk == null) return "redirect:/admin";

        DonHang dh = donHangRepository.findById(maDH).orElse(null);
        if (dh == null || !"CHO_DUYET".equals(dh.getTrangThai())) {
            return "redirect:/donhang";
        }
        
        // Cần kiểm tra xem nhân viên đã chọn đủ số lượng IMEI chưa
        int requiredImeiCount = dh.getChiTiet().stream().mapToInt(ChiTietDonHang::getSoLuong).sum();
        if (selectedImeis == null || selectedImeis.size() != requiredImeiCount) {
            model.addAttribute("loi", "Vui lòng chọn đúng số lượng IMEI cho các sản phẩm.");
            model.addAttribute("dh", dh);
            for (ChiTietDonHang ct : dh.getChiTiet()) {
                List<IMEI> availableImeis = imeiRepository.findBySanPhamAndTrangThai(ct.getSanPham(), "TRONG_KHO");
                model.addAttribute("imeis_" + ct.getSanPham().getMaSP(), availableImeis);
            }
            return "DonHang/duyet";
        }
        
        // Tạo Hóa Đơn
        HoaDon hoaDon = new HoaDon();
        hoaDon.setKhachHang(dh.getKhachHang());
        hoaDon.setTaiKhoan(tk);
        hoaDon.setNgayLap(LocalDate.now());
        hoaDon.setHinhThucTT(dh.getHinhThucTT());
        hoaDon.setTongTien(dh.getTongTien());
        hoaDon.setGhiChu("Hóa đơn từ Đơn đặt hàng Online #" + dh.getMaDH());
        
        // Lưu HoaDon trước để có ID
        HoaDon savedHD = hoaDonRepository.save(hoaDon);
        
        List<ChiTietHoaDon> chiTietList = new ArrayList<>();
        
        for (String imeiStr : selectedImeis) {
            IMEI imei = imeiRepository.findById(imeiStr).orElse(null);
            if (imei != null) {
                // Đánh dấu IMEI đã bán
                imei.setTrangThai("DA_BAN");
                imeiRepository.save(imei);
                
                // Trừ tồn kho sản phẩm
                SanPham sp = imei.getSanPham();
                sp.setSoLuongTon(sp.getSoLuongTon() - 1);
                sanPhamRepository.save(sp);
                
                // Tạo chi tiết HĐ
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setHoaDon(savedHD);
                ct.setImeiEntity(imei);
                ct.setSanPham(sp);
                ct.setDonGia(sp.getGiaBan());
                ct.setId(new ChiTietHoaDonId(savedHD.getMaHD(), imei.getImei()));
                
                chiTietList.add(ct);
            }
        }
        
        chiTietHoaDonRepository.saveAll(chiTietList);
        
        // Cập nhật trạng thái Đơn Hàng
        dh.setTrangThai("DA_GIAO");
        donHangRepository.save(dh);

        return "redirect:/donhang";
    }
    
    // 4. Hủy đơn hàng
    @PostMapping("/huy")
    public String huyDon(@RequestParam int maDH, HttpSession session) {
        TaiKhoan tk = getSessionAdmin(session);
        if (tk == null) return "redirect:/admin";

        DonHang dh = donHangRepository.findById(maDH).orElse(null);
        if (dh != null && "CHO_DUYET".equals(dh.getTrangThai())) {
            dh.setTrangThai("DA_HUY");
            donHangRepository.save(dh);
        }
        return "redirect:/donhang";
    }
}
