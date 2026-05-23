package com.example.demo.controller;

import com.example.demo.entity.HoaDon;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/baocao")
public class BaoCaoController {

    @Autowired private HoaDonRepository hoaDonRepository;
    @Autowired private SanPhamRepository sanPhamRepository;
    @Autowired private BaoHanhRepository baoHanhRepository;
    @Autowired private PhieuNhapRepository phieuNhapRepository;

    // Trang báo cáo, mặc định lọc theo tháng hiện tại
    @GetMapping
    public String baoCao(
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,

            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,

            Model model) {

        // Mặc định: tháng hiện tại
        if (from == null) from = YearMonth.now().atDay(1);
        if (to == null)   to   = YearMonth.now().atEndOfMonth();

        // Danh sách hóa đơn trong kỳ
        List<HoaDon> dsHD = hoaDonRepository.findByNgayLapBetween(from, to);

        // Tổng doanh thu
        Long doanhThu = hoaDonRepository.tinhDoanhThu(from, to);

        // Số đơn hàng
        long soDon = hoaDonRepository.countByNgayLapBetween(from, to);

        // Tổng tồn kho (tất cả sản phẩm)
        long tongTonKho = sanPhamRepository.findAll()
                .stream().mapToLong(sp -> sp.getSoLuongTon()).sum();

        // Số máy đang bảo hành
        long soMayBH = baoHanhRepository.countByHuongXuLy("DANG_XU_LY");

        model.addAttribute("dsHD",      dsHD);
        model.addAttribute("doanhThu",  doanhThu != null ? doanhThu : 0L);
        model.addAttribute("soDon",     soDon);
        model.addAttribute("tongTonKho",tongTonKho);
        model.addAttribute("soMayBH",   soMayBH);
        model.addAttribute("from",      from);
        model.addAttribute("to",        to);

        return "baocao/index";
    }
}