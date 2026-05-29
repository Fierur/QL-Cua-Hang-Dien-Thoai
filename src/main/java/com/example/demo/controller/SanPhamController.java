package com.example.demo.controller;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.repository.SanPhamRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private com.example.demo.service.FileStorageService fileStorageService;

    // Form thêm sản phẩm mới
    @GetMapping("/them")
    public String them(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/sanpham";
        }
        model.addAttribute("sp", new SanPham());
        model.addAttribute("dsSP", sanPhamRepository.findAll());
        return "sanpham/them";
    }

    // Lưu sản phẩm mới
    @PostMapping("/luu")
    public String luu(@ModelAttribute SanPham sp,
                      @RequestParam(value = "tenSPMoi", defaultValue = "") String tenSPMoi,
                      @RequestParam(value = "files", required = false) org.springframework.web.multipart.MultipartFile[] files,
                      @RequestParam(value = "links", required = false) String[] links,
                      HttpSession session,
                      Model model) {
        if (!isAdmin(session)) {
            return "redirect:/sanpham";
        }
        if (!applyProductName(sp, tenSPMoi)) {
            model.addAttribute("sp", sp);
            model.addAttribute("dsSP", sanPhamRepository.findAll());
            model.addAttribute("loi", "Vui lòng chọn tên sản phẩm có sẵn hoặc nhập tên sản phẩm mới.");
            return "sanpham/them";
        }
        
        java.util.List<String> hinhAnhs = new java.util.ArrayList<>();
        
        // Handle external links
        if (links != null) {
            for (String link : links) {
                if (link != null && !link.trim().isEmpty()) {
                    // Split by comma in case user pastes multiple links in one input
                    String[] splitLinks = link.split(",");
                    for (String splitLink : splitLinks) {
                        if (!splitLink.trim().isEmpty()) {
                            hinhAnhs.add(splitLink.trim());
                        }
                    }
                }
            }
        }
        
        // Handle uploaded files
        if (files != null) {
            for (org.springframework.web.multipart.MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String url = fileStorageService.storeFile(file);
                    if (url != null) {
                        hinhAnhs.add(url);
                    }
                }
            }
        }
        
        sp.setHinhAnhs(hinhAnhs);
        sanPhamRepository.save(sp);
        return "redirect:/sanpham";
    }

    // Form sửa sản phẩm
    @GetMapping("/sua/{maSP}")
    public String sua(@PathVariable int maSP, Model model) {
        model.addAttribute("sp",
                sanPhamRepository.findById(maSP).orElse(null));
        model.addAttribute("dsSP", sanPhamRepository.findAll());
        return "sanpham/sua";
    }

    // Cập nhật sản phẩm
    @PostMapping("/capnhat")
    public String capNhat(@ModelAttribute SanPham sp,
                          @RequestParam(value = "tenSPMoi", defaultValue = "") String tenSPMoi,
                          @RequestParam(value = "files", required = false) org.springframework.web.multipart.MultipartFile[] files,
                          @RequestParam(value = "links", required = false) String[] links,
                          @RequestParam(value = "keepImages", required = false) String[] keepImages,
                          Model model) {
        if (!applyProductName(sp, tenSPMoi)) {
            model.addAttribute("sp", sp);
            model.addAttribute("dsSP", sanPhamRepository.findAll());
            model.addAttribute("loi", "Vui lòng chọn tên sản phẩm có sẵn hoặc nhập tên sản phẩm mới.");
            return "sanpham/sua";
        }
        
        java.util.List<String> hinhAnhs = new java.util.ArrayList<>();
        
        // Keep existing images selected by user
        if (keepImages != null) {
            for (String img : keepImages) {
                if (img != null && !img.trim().isEmpty()) {
                    hinhAnhs.add(img.trim());
                }
            }
        }
        
        // Handle new external links
        if (links != null) {
            for (String link : links) {
                if (link != null && !link.trim().isEmpty()) {
                    String[] splitLinks = link.split(",");
                    for (String splitLink : splitLinks) {
                        if (!splitLink.trim().isEmpty()) {
                            hinhAnhs.add(splitLink.trim());
                        }
                    }
                }
            }
        }
        
        // Handle newly uploaded files
        if (files != null) {
            for (org.springframework.web.multipart.MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String url = fileStorageService.storeFile(file);
                    if (url != null) {
                        hinhAnhs.add(url);
                    }
                }
            }
        }
        
        sp.setHinhAnhs(hinhAnhs);
        sanPhamRepository.save(sp);
        return "redirect:/sanpham";
    }

    // Xóa sản phẩm (chỉ Admin)
    @GetMapping("/xoa/{maSP}")
    public String xoa(@PathVariable int maSP, HttpSession session, RedirectAttributes redirectAttributes) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk != null && tk.isAdmin()) {
            try {
                sanPhamRepository.deleteById(maSP);
                redirectAttributes.addFlashAttribute("thanhCong", "Đã xóa sản phẩm.");
            } catch (DataAccessException ex) {
                redirectAttributes.addFlashAttribute("loi",
                        "Không thể xóa sản phẩm vì đã có dữ liệu nhập hàng, IMEI, hóa đơn, đơn hàng hoặc bảo hành liên quan.");
            }
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

    private boolean isAdmin(HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        return tk != null && tk.isAdmin();
    }

    private boolean applyProductName(SanPham sp, String tenSPMoi) {
        String newName = tenSPMoi == null ? "" : tenSPMoi.trim();
        if (!newName.isEmpty()) {
            sp.setTenSP(newName);
        }
        return sp.getTenSP() != null && !sp.getTenSP().trim().isEmpty();
    }
}
