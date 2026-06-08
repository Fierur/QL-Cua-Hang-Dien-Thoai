package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StorefrontController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private ChiTietGioHangRepository chiTietGioHangRepository;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private DanhGiaRepository danhGiaRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    // Helper: Lấy khách hàng đang đăng nhập
    private KhachHang getSessionCustomer(HttpSession session) {
        KhachHang kh = (KhachHang) session.getAttribute("customerAccount");
        if (kh == null) {
            TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
            if (tk != null) {
                String sdt = tk.getSdt() != null && !tk.getSdt().isEmpty() ? tk.getSdt() : "ADMIN_" + tk.getMaTK();
                kh = khachHangRepository.findBySdt(sdt);
                if (kh == null) {
                    kh = new KhachHang();
                    kh.setTenKH(tk.getHoTen());
                    kh.setSdt(sdt);
                    kh.setMatKhau(tk.getMatKhau());
                    kh = khachHangRepository.save(kh);
                    GioHang gh = new GioHang(kh);
                    gioHangRepository.save(gh);
                }
                session.setAttribute("customerAccount", kh);
            }
        }
        return kh;
    }

    // Helper: Đếm số lượng sản phẩm trong giỏ hàng
    private int getCartItemCount(KhachHang kh) {
        if (kh == null) return 0;
        GioHang gh = gioHangRepository.findByKhachHang(kh);
        if (gh == null || gh.getChiTiet() == null) return 0;
        return gh.getChiTiet().stream().mapToInt(ChiTietGioHang::getSoLuong).sum();
    }

    // 1. Trang chủ (danh sách sản phẩm nổi bật, mới nhất, theo hãng)
    @GetMapping
    public String index(Model model, HttpSession session) {
        KhachHang kh = getSessionCustomer(session);
        model.addAttribute("customer", kh);
        model.addAttribute("cartCount", getCartItemCount(kh));

        // Banners
        model.addAttribute("banners", bannerRepository.findByTrangThaiTrueOrderByThuTuAsc());

        // Danh sách hãng sản xuất (nếu cần cho menu)
        model.addAttribute("dsHangSX", sanPhamRepository.findDistinctHangSX());

        // Sản phẩm nổi bật (Flash Sale section) - Top 8 giá cao nhất
        model.addAttribute("spNoiBat", sanPhamRepository.findTop8BySoLuongTonGreaterThanOrderByGiaBanDesc(0));

        // Sản phẩm mới nhất - Top 8 mới nhất
        model.addAttribute("spMoiNhat", sanPhamRepository.findTop8BySoLuongTonGreaterThanOrderByMaSPDesc(0));

        // Điện thoại Apple
        model.addAttribute("spApple", sanPhamRepository.findTop8ByHangSXIgnoreCaseAndSoLuongTonGreaterThanOrderByMaSPDesc("Apple", 0));

        // Điện thoại Samsung
        model.addAttribute("spSamsung", sanPhamRepository.findTop8ByHangSXIgnoreCaseAndSoLuongTonGreaterThanOrderByMaSPDesc("Samsung", 0));

        return "storefront/index";
    }

    // Tất cả sản phẩm / Tìm kiếm & Lọc (trang riêng)
    @GetMapping("/products")
    public String products(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "") String hangSX,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(defaultValue = "") String ram,
            @RequestParam(defaultValue = "") String rom,
            @RequestParam(defaultValue = "default") String sort,
            @RequestParam(defaultValue = "0") int page,
            Model model, HttpSession session) {

        KhachHang kh = getSessionCustomer(session);
        model.addAttribute("customer", kh);
        model.addAttribute("cartCount", getCartItemCount(kh));
        model.addAttribute("dsHangSX", sanPhamRepository.findDistinctHangSX());

        Sort sortObj;
        switch (sort) {
            case "price-asc": sortObj = Sort.by("giaBan").ascending(); break;
            case "price-desc": sortObj = Sort.by("giaBan").descending(); break;
            default: sortObj = Sort.by("maSP").descending(); break;
        }

        Pageable pageable = PageRequest.of(page, 12, sortObj);
        
        Page<SanPham> pageResult = sanPhamRepository.searchProducts(
            q, hangSX, minPrice, maxPrice, ram, rom, pageable
        );

        model.addAttribute("dsSP", pageResult.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("totalItems", pageResult.getTotalElements());
        model.addAttribute("keyword", q);
        model.addAttribute("hangSXFilter", hangSX);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("ram", ram);
        model.addAttribute("rom", rom);
        model.addAttribute("sortBy", sort);

        return "storefront/products";
    }

    // 2. Chi tiết sản phẩm + đánh giá
    @GetMapping("/product/{maSP}")
    public String productDetail(@PathVariable int maSP, Model model, HttpSession session) {
        KhachHang kh = getSessionCustomer(session);
        model.addAttribute("customer", kh);
        model.addAttribute("cartCount", getCartItemCount(kh));

        SanPham sp = sanPhamRepository.findById(maSP).orElse(null);
        if (sp == null) return "redirect:/";

        model.addAttribute("sp", sp);

        // Đánh giá
        List<DanhGia> dsDG = danhGiaRepository.findBySanPhamOrderByNgayDGDesc(sp);
        model.addAttribute("dsDanhGia", dsDG);
        model.addAttribute("soLuongDG", danhGiaRepository.countBySanPham(sp));

        Double avg = danhGiaRepository.findAverageBySanPham(sp);
        model.addAttribute("diemTB", avg != null ? Math.round(avg * 10.0) / 10.0 : 0);

        // Kiểm tra khách hàng đã đánh giá chưa
        if (kh != null) {
            model.addAttribute("daDanhGia", danhGiaRepository.existsByKhachHangAndSanPham(kh, sp));
        }

        // Sản phẩm liên quan (cùng hãng)
        List<SanPham> spLienQuan = sanPhamRepository.findByHangSX(sp.getHangSX());
        spLienQuan.removeIf(s -> s.getMaSP() == maSP);
        if (spLienQuan.size() > 4) spLienQuan = spLienQuan.subList(0, 4);
        model.addAttribute("spLienQuan", spLienQuan);

        return "storefront/product-detail";
    }

    // Gửi đánh giá
    @PostMapping("/product/{maSP}/review")
    public String submitReview(@PathVariable int maSP,
                               @RequestParam int soSao,
                               @RequestParam String noiDung,
                               HttpSession session) {
        KhachHang kh = getSessionCustomer(session);
        if (kh == null) return "redirect:/login";

        SanPham sp = sanPhamRepository.findById(maSP).orElse(null);
        if (sp == null) return "redirect:/";

        // Kiểm tra đã đánh giá chưa
        if (danhGiaRepository.existsByKhachHangAndSanPham(kh, sp)) {
            return "redirect:/product/" + maSP;
        }

        DanhGia dg = new DanhGia();
        dg.setKhachHang(kh);
        dg.setSanPham(sp);
        dg.setSoSao(Math.min(5, Math.max(1, soSao)));
        dg.setNoiDung(noiDung);
        dg.setNgayDG(LocalDateTime.now());
        danhGiaRepository.save(dg);

        return "redirect:/product/" + maSP + "#reviews";
    }

    // 3. Wishlist (Sản phẩm yêu thích)
    @GetMapping("/wishlist")
    public String wishlist(@RequestParam(required = false) List<Integer> ids, Model model, HttpSession session) {
        KhachHang kh = getSessionCustomer(session);
        model.addAttribute("customer", kh);
        model.addAttribute("cartCount", getCartItemCount(kh));

        if (ids != null && !ids.isEmpty()) {
            model.addAttribute("dsSP", sanPhamRepository.findAllById(ids));
        } else {
            model.addAttribute("dsSP", new ArrayList<>());
        }
        return "storefront/wishlist";
    }

    // 4. Xem giỏ hàng
    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        KhachHang kh = getSessionCustomer(session);
        if (kh == null) return "redirect:/login";

        model.addAttribute("customer", kh);
        GioHang gh = gioHangRepository.findByKhachHang(kh);
        if (gh == null) {
            gh = new GioHang(kh);
            gioHangRepository.save(gh);
        }

        model.addAttribute("cart", gh);
        model.addAttribute("cartCount", getCartItemCount(kh));

        long total = 0;
        if (gh.getChiTiet() != null) {
            for (ChiTietGioHang ct : gh.getChiTiet()) {
                total += ct.getSanPham().getGiaBan() * ct.getSoLuong();
            }
        }
        model.addAttribute("total", total);

        return "storefront/cart";
    }

    // 4. Thêm vào giỏ hàng
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam int maSP,
                            @RequestParam(defaultValue = "1") int soLuong,
                            HttpSession session,
                            HttpServletRequest request,
                            RedirectAttributes redirectAttributes) {
        KhachHang kh = getSessionCustomer(session);
        if (kh == null) return "redirect:/login";

        String redirectTo = getRedirectToPreviousPage(request);

        GioHang gh = gioHangRepository.findByKhachHang(kh);
        if (gh == null) {
            gh = new GioHang(kh);
            gh = gioHangRepository.save(gh);
        }

        SanPham sp = sanPhamRepository.findById(maSP).orElse(null);
        if (sp != null) {
            soLuong = Math.max(1, soLuong);
            ChiTietGioHangId id = new ChiTietGioHangId(gh.getMaGH(), maSP);
            ChiTietGioHang ct = chiTietGioHangRepository.findById(id).orElse(null);
            int soLuongTrongGio = ct != null ? ct.getSoLuong() : 0;
            int soLuongSauKhiThem = soLuongTrongGio + soLuong;

            if (soLuongSauKhiThem > sp.getSoLuongTon()) {
                redirectAttributes.addFlashAttribute("loi",
                        "Không thể thêm " + soLuongSauKhiThem + " sản phẩm vào giỏ. Tồn kho chỉ còn "
                                + sp.getSoLuongTon() + " sản phẩm.");
                return redirectTo;
            }

            if (ct != null) {
                ct.setSoLuong(ct.getSoLuong() + soLuong);
            } else {
                ct = new ChiTietGioHang(gh, sp, soLuong);
            }
            chiTietGioHangRepository.save(ct);
            redirectAttributes.addFlashAttribute("thanhCong", "Đã thêm sản phẩm vào giỏ hàng.");
        }

        return redirectTo;
    }

    // 5. Xóa khỏi giỏ hàng
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam int maSP, HttpSession session) {
        KhachHang kh = getSessionCustomer(session);
        if (kh == null) return "redirect:/login";

        GioHang gh = gioHangRepository.findByKhachHang(kh);
        if (gh != null) {
            ChiTietGioHangId id = new ChiTietGioHangId(gh.getMaGH(), maSP);
            chiTietGioHangRepository.deleteById(id);
        }
        return "redirect:/cart";
    }

    // 6. Trang Đặt hàng (Checkout)
    @GetMapping("/checkout")
    public String checkoutPage(Model model, HttpSession session) {
        KhachHang kh = getSessionCustomer(session);
        if (kh == null) return "redirect:/login";

        GioHang gh = gioHangRepository.findByKhachHang(kh);
        if (gh == null || gh.getChiTiet() == null || gh.getChiTiet().isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("customer", kh);
        model.addAttribute("cart", gh);
        model.addAttribute("cartCount", getCartItemCount(kh));

        long total = 0;
        for (ChiTietGioHang ct : gh.getChiTiet()) {
            total += ct.getSanPham().getGiaBan() * ct.getSoLuong();
        }
        model.addAttribute("total", total);

        return "storefront/checkout";
    }

    // 6. Xử lý Đặt hàng (Checkout)
    @PostMapping("/checkout")
    public String checkout(@RequestParam String diaChiGiao,
                           @RequestParam String sdtGiao,
                           @RequestParam String hinhThucTT,
                           @RequestParam(required = false) String ghiChu,
                           HttpSession session) {

        KhachHang kh = getSessionCustomer(session);
        if (kh == null) return "redirect:/login";

        GioHang gh = gioHangRepository.findByKhachHang(kh);
        if (gh == null || gh.getChiTiet() == null || gh.getChiTiet().isEmpty()) {
            return "redirect:/cart";
        }

        long total = 0;
        for (ChiTietGioHang ct : gh.getChiTiet()) {
            total += ct.getSanPham().getGiaBan() * ct.getSoLuong();
        }

        DonHang dh = new DonHang();
        dh.setKhachHang(kh);
        dh.setNgayDat(LocalDateTime.now());
        dh.setTongTien(total);
        dh.setDiaChiGiao(diaChiGiao);
        dh.setSdtGiao(sdtGiao);
        dh.setHinhThucTT(hinhThucTT);
        dh.setGhiChu(ghiChu);

        List<ChiTietDonHang> chiTietList = new ArrayList<>();
        for (ChiTietGioHang ct : gh.getChiTiet()) {
            ChiTietDonHang ctdh = new ChiTietDonHang(dh, ct.getSanPham(), ct.getSoLuong(), ct.getSanPham().getGiaBan());
            chiTietList.add(ctdh);
        }
        dh.setChiTiet(chiTietList);

        donHangRepository.save(dh);

        // Clear cart
        chiTietGioHangRepository.deleteAll(gh.getChiTiet());

        return "redirect:/orders";
    }

    // 7. Lịch sử đơn hàng
    @GetMapping("/orders")
    public String orderHistory(Model model, HttpSession session) {
        KhachHang kh = getSessionCustomer(session);
        if (kh == null) return "redirect:/login";

        model.addAttribute("customer", kh);
        model.addAttribute("cartCount", getCartItemCount(kh));
        
        List<DonHang> dsDH = donHangRepository.findByKhachHangOrderByNgayDatDesc(kh);
        
        java.util.Map<Integer, List<String>> orderImeis = new java.util.HashMap<>();
        for (DonHang dh : dsDH) {
            if ("DA_GIAO".equals(dh.getTrangThai())) {
                List<com.example.demo.entity.HoaDon> hds = hoaDonRepository.findByGhiChu("Hóa đơn từ Đơn đặt hàng Online #" + dh.getMaDH());
                if (!hds.isEmpty()) {
                    com.example.demo.entity.HoaDon hd = hds.get(0);
                    List<String> imeis = new ArrayList<>();
                    if (hd.getChiTiet() != null) {
                        for (com.example.demo.entity.ChiTietHoaDon ct : hd.getChiTiet()) {
                            if (ct.getImeiEntity() != null) {
                                imeis.add(ct.getImeiEntity().getImei());
                            }
                        }
                    }
                    orderImeis.put(dh.getMaDH(), imeis);
                }
            }
        }
        
        model.addAttribute("dsDH", dsDH);
        model.addAttribute("orderImeis", orderImeis);

        return "storefront/orders";
    }

    private String getRedirectToPreviousPage(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isBlank()) {
            return "redirect:/cart";
        }

        try {
            URI uri = new URI(referer);
            boolean sameHost = uri.getHost() == null || uri.getHost().equalsIgnoreCase(request.getServerName());
            if (!sameHost) {
                return "redirect:/cart";
            }

            String path = uri.getRawPath() == null || uri.getRawPath().isBlank() ? "/" : uri.getRawPath();
            String query = uri.getRawQuery();
            return "redirect:" + path + (query == null ? "" : "?" + query);
        } catch (URISyntaxException ex) {
            return "redirect:/cart";
        }
    }
}
