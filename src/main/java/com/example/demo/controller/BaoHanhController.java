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
@RequestMapping("/baohanh")
public class BaoHanhController {

    @Autowired private BaoHanhRepository baoHanhRepository;
    @Autowired private KhachHangRepository khachHangRepository;
    @Autowired private IMEIRepository imeiRepository;
    @Autowired private SanPhamRepository sanPhamRepository;

    // Danh sách phiếu bảo hành, lọc theo trạng thái
    @GetMapping
    public String danhSach(@RequestParam(value = "trangThai", defaultValue = "") String trangThai,
                           Model model) {

        model.addAttribute("dsBH",
                trangThai.isEmpty()
                        ? baoHanhRepository.findAll()
                        : baoHanhRepository.findByHuongXuLy(trangThai));

        model.addAttribute("trangThaiFilter", trangThai);
        return "baohanh/index";
    }

    // Form tiếp nhận bảo hành mới
    @GetMapping("/them")
    public String them(Model model) {
        model.addAttribute("bh", new BaoHanh());
        model.addAttribute("dsKH", khachHangRepository.findAll());
        model.addAttribute("dsIMEI", imeiRepository.findAll());
        return "baohanh/them";
    }

    // Lưu phiếu bảo hành
    @PostMapping("/luu")
    public String luu(@RequestParam("imeiStr") String imeiStr,
                      @RequestParam("maKH") int maKH,
                      @RequestParam("tinhTrangLoi") String tinhTrangLoi,
                      @RequestParam(value = "ghiChu", defaultValue = "") String ghiChu,
                      HttpSession session) {

        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        IMEI imei = imeiRepository.findById(imeiStr.trim()).orElse(null);
        KhachHang kh = khachHangRepository.findById(maKH).orElse(null);

        if (imei == null || kh == null) {
            return "redirect:/baohanh/them";
        }

        BaoHanh bh = new BaoHanh();
        bh.setImeiEntity(imei);
        bh.setKhachHang(kh);
        bh.setTaiKhoan(tk);
        bh.setNgayNhan(LocalDate.now());
        bh.setTinhTrangLoi(tinhTrangLoi);
        bh.setGhiChu(ghiChu);
        bh.setHuongXuLy("DANG_XU_LY");

        // Cập nhật trạng thái IMEI → đang bảo hành
        imei.setTrangThai("BAO_HANH");
        imeiRepository.save(imei);

        // Trừ tồn kho nếu máy đang trong kho bị gửi bảo hành
        SanPham sp = imei.getSanPham();
        if (sp.getSoLuongTon() > 0) {
            sp.setSoLuongTon(sp.getSoLuongTon() - 1);
            sanPhamRepository.save(sp);
        }

        baoHanhRepository.save(bh);
        return "redirect:/baohanh";
    }

    // Form cập nhật kết quả xử lý bảo hành
    @GetMapping("/capnhat/{maBH}")
    public String formCapNhat(@PathVariable int maBH, Model model) {
        model.addAttribute("bh",
                baoHanhRepository.findById(maBH).orElse(null));
        return "baohanh/capnhat";
    }

    // Lưu kết quả bảo hành
    @PostMapping("/luucapnhat")
    public String luuCapNhat(@RequestParam("maBH") int maBH,
                             @RequestParam("nguyenNhan") String nguyenNhan,
                             @RequestParam("huongXuLy") String huongXuLy,
                             @RequestParam(value = "ghiChu", defaultValue = "") String ghiChu) {

        BaoHanh bh = baoHanhRepository.findById(maBH).orElse(null);
        if (bh == null) return "redirect:/baohanh";

        bh.setNguyenNhan(nguyenNhan);
        bh.setHuongXuLy(huongXuLy);
        bh.setGhiChu(ghiChu);

        // Nếu đã xử lý xong → cập nhật ngày trả và trạng thái IMEI
        if ("SUA_XONG".equals(huongXuLy) || "DOI_MAY".equals(huongXuLy) || "TU_CHOI".equals(huongXuLy)) {
            bh.setNgayTra(LocalDate.now());

            IMEI imei = bh.getImeiEntity();
            // Máy trả lại kho nếu sửa xong hoặc từ chối
            if ("SUA_XONG".equals(huongXuLy) || "TU_CHOI".equals(huongXuLy)) {
                imei.setTrangThai("DA_BAN");
            }
            imeiRepository.save(imei);
        }

        baoHanhRepository.save(bh);
        return "redirect:/baohanh";
    }

    // Chi tiết phiếu bảo hành
    @GetMapping("/chitiet/{maBH}")
    public String chiTiet(@PathVariable int maBH, Model model) {
        model.addAttribute("bh",
                baoHanhRepository.findById(maBH).orElse(null));
        return "baohanh/chitiet";
    }

    // Xóa phiếu bảo hành (chỉ Admin)
    @GetMapping("/xoa/{maBH}")
    public String xoa(@PathVariable int maBH, HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk != null && tk.isAdmin()) {
            baoHanhRepository.deleteById(maBH);
        }
        return "redirect:/baohanh";
    }
}