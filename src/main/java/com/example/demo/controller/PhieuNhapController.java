package com.example.demo.controller;

import com.example.demo.entity.ChiTietPhieuNhap;
import com.example.demo.entity.ChiTietPhieuNhapId;
import com.example.demo.entity.IMEI;
import com.example.demo.entity.NhaCungCap;
import com.example.demo.entity.PhieuNhap;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.repository.ChiTietPhieuNhapRepository;
import com.example.demo.repository.IMEIRepository;
import com.example.demo.repository.NhaCungCapRepository;
import com.example.demo.repository.PhieuNhapRepository;
import com.example.demo.repository.SanPhamRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Controller
@RequestMapping("/phieunhap")
public class PhieuNhapController {

    @Autowired private PhieuNhapRepository phieuNhapRepository;
    @Autowired private NhaCungCapRepository nhaCungCapRepository;
    @Autowired private SanPhamRepository sanPhamRepository;
    @Autowired private IMEIRepository imeiRepository;
    @Autowired private ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;

    @GetMapping
    public String danhSach(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin";
        }
        model.addAttribute("dsPN", phieuNhapRepository.findAll());
        return "phieunhap/index";
    }

    @GetMapping("/them")
    public String them(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin";
        }
        addFormData(model);
        model.addAttribute("formLines", List.of(new FormLine()));
        return "phieunhap/them";
    }

    @PostMapping("/luu")
    @Transactional
    public String luu(@RequestParam(value = "maNCC", required = false) Integer maNCC,
                      @RequestParam(value = "tenNCCMoi", defaultValue = "") String tenNCCMoi,
                      @RequestParam(value = "sdtNCCMoi", defaultValue = "") String sdtNCCMoi,
                      @RequestParam(value = "diaChiNCCMoi", defaultValue = "") String diaChiNCCMoi,
                      @RequestParam(value = "tenSP", required = false) List<String> tenSPList,
                      @RequestParam(value = "giaBan", required = false) List<String> giaBanList,
                      @RequestParam(value = "soLuong", required = false) List<String> soLuongList,
                      @RequestParam(value = "giaNhap", required = false) List<String> giaNhapList,
                      @RequestParam(value = "ghiChu", defaultValue = "") String ghiChu,
                      HttpServletRequest request,
                      HttpSession session,
                      Model model) {
        if (!isAdmin(session)) {
            return "redirect:/admin";
        }

        List<FormLine> formLines = buildFormLines(tenSPList, giaBanList, soLuongList, giaNhapList, request);
        List<String> errors = new ArrayList<>();

        NhaCungCap ncc = resolveSupplier(maNCC, tenNCCMoi, sdtNCCMoi, diaChiNCCMoi, errors);
        List<ImportLine> importLines = resolveImportLines(formLines, errors);

        if (!errors.isEmpty()) {
            addFormData(model);
            model.addAttribute("loi", String.join(" ", errors));
            model.addAttribute("selectedMaNCC", maNCC);
            model.addAttribute("tenNCCMoi", tenNCCMoi);
            model.addAttribute("sdtNCCMoi", sdtNCCMoi);
            model.addAttribute("diaChiNCCMoi", diaChiNCCMoi);
            model.addAttribute("ghiChu", ghiChu);
            model.addAttribute("formLines", formLines.isEmpty() ? List.of(new FormLine()) : formLines);
            return "phieunhap/them";
        }

        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (ncc.getMaNCC() == 0) {
            ncc = nhaCungCapRepository.save(ncc);
        }

        PhieuNhap pn = new PhieuNhap();
        pn.setNgayNhap(LocalDate.now());
        pn.setNhaCungCap(ncc);
        pn.setTaiKhoan(tk);
        pn.setGhiChu(ghiChu);
        PhieuNhap savedPn = phieuNhapRepository.save(pn);

        for (ImportLine line : importLines) {
            SanPham sp = line.sanPham;
            if (sp == null) {
                sp = new SanPham();
                sp.setTenSP(line.tenSP);
                sp.setGiaBan(line.giaBan);
                sp.setSoLuongTon(0);
                sp = sanPhamRepository.save(sp);
            }

            ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap();
            chiTiet.setId(new ChiTietPhieuNhapId(savedPn.getMaPN(), sp.getMaSP()));
            chiTiet.setPhieuNhap(savedPn);
            chiTiet.setSanPham(sp);
            chiTiet.setSoLuong(line.soLuong);
            chiTiet.setGiaNhap(line.giaNhap);
            chiTietPhieuNhapRepository.save(chiTiet);

            for (String imeiValue : line.imeis) {
                IMEI imei = new IMEI();
                imei.setImei(imeiValue);
                imei.setSanPham(sp);
                imei.setTrangThai("TRONG_KHO");
                imeiRepository.save(imei);
            }

            sp.setSoLuongTon(sp.getSoLuongTon() + line.soLuong);
            sanPhamRepository.save(sp);
        }

        return "redirect:/phieunhap";
    }

    @GetMapping("/chitiet/{maPN}")
    public String chiTiet(@PathVariable int maPN, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin";
        }
        model.addAttribute("pn",
                phieuNhapRepository.findById(maPN).orElse(null));
        return "phieunhap/chitiet";
    }

    @GetMapping("/xoa/{maPN}")
    public String xoa(@PathVariable int maPN, HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        if (tk != null && tk.isAdmin()) {
            phieuNhapRepository.deleteById(maPN);
        }
        return "redirect:/phieunhap";
    }

    private void addFormData(Model model) {
        model.addAttribute("pn", new PhieuNhap());
        model.addAttribute("dsNCC", nhaCungCapRepository.findAll());
        model.addAttribute("dsSP", sanPhamRepository.findAll());
    }

    private NhaCungCap resolveSupplier(Integer maNCC,
                                       String tenNCCMoi,
                                       String sdtNCCMoi,
                                       String diaChiNCCMoi,
                                       List<String> errors) {
        String newName = trim(tenNCCMoi);
        boolean hasNewSupplier = !newName.isEmpty()
                || !trim(sdtNCCMoi).isEmpty()
                || !trim(diaChiNCCMoi).isEmpty();

        if (hasNewSupplier) {
            if (newName.isEmpty()) {
                errors.add("Tên nhà cung cấp mới không được để trống.");
                return null;
            }

            NhaCungCap ncc = new NhaCungCap();
            ncc.setTenNCC(newName);
            ncc.setSdt(trim(sdtNCCMoi));
            ncc.setDiaChi(trim(diaChiNCCMoi));
            return ncc;
        }

        if (maNCC == null) {
            errors.add("Vui lòng chọn nhà cung cấp hoặc nhập nhà cung cấp mới.");
            return null;
        }

        NhaCungCap ncc = nhaCungCapRepository.findById(maNCC).orElse(null);
        if (ncc == null) {
            errors.add("Nhà cung cấp đã chọn không tồn tại.");
        }
        return ncc;
    }

    private List<ImportLine> resolveImportLines(List<FormLine> formLines, List<String> errors) {
        List<ImportLine> importLines = new ArrayList<>();
        Set<String> productNames = new HashSet<>();
        Set<String> formImeis = new HashSet<>();

        if (formLines.isEmpty()) {
            errors.add("Vui lòng nhập ít nhất một sản phẩm.");
            return importLines;
        }

        for (int i = 0; i < formLines.size(); i++) {
            FormLine formLine = formLines.get(i);
            int displayIndex = i + 1;
            String tenSP = trim(formLine.getTenSP());

            if (tenSP.isEmpty()) {
                errors.add("Dòng " + displayIndex + ": tên sản phẩm không được để trống.");
                continue;
            }

            String productKey = tenSP.toLowerCase(Locale.ROOT);
            if (!productNames.add(productKey)) {
                errors.add("Dòng " + displayIndex + ": không nhập trùng sản phẩm trong cùng phiếu.");
                continue;
            }

            Integer soLuong = parsePositiveInt(formLine.getSoLuong());
            Long giaNhap = parseNonNegativeLong(formLine.getGiaNhap());
            Long giaBan = parseNonNegativeLong(formLine.getGiaBan());
            List<SanPham> existingProducts = sanPhamRepository.findByTenSP(tenSP);
            SanPham existingProduct = existingProducts.isEmpty() ? null : existingProducts.get(0);

            if (soLuong == null) {
                errors.add("Dòng " + displayIndex + ": số lượng phải lớn hơn 0.");
            }
            if (giaNhap == null) {
                errors.add("Dòng " + displayIndex + ": giá nhập không hợp lệ.");
            }
            if (existingProduct == null && giaBan == null) {
                errors.add("Dòng " + displayIndex + ": sản phẩm mới cần nhập giá bán.");
            }

            List<String> cleanImeis = new ArrayList<>();
            for (String imei : formLine.getImeis()) {
                String cleanImei = trim(imei);
                if (!cleanImei.isEmpty()) {
                    cleanImeis.add(cleanImei);
                }
            }

            if (soLuong != null && cleanImeis.size() != soLuong) {
                errors.add("Dòng " + displayIndex + ": số IMEI phải đúng bằng số lượng nhập.");
            }

            for (String imei : cleanImeis) {
                String imeiKey = imei.toLowerCase(Locale.ROOT);
                if (!formImeis.add(imeiKey)) {
                    errors.add("IMEI " + imei + " bị trùng trong phiếu nhập.");
                }
                if (imeiRepository.existsById(imei)) {
                    errors.add("IMEI " + imei + " đã tồn tại trong kho.");
                }
            }

            if (soLuong != null && giaNhap != null && (existingProduct != null || giaBan != null)) {
                importLines.add(new ImportLine(tenSP, existingProduct, giaBan, soLuong, giaNhap, cleanImeis));
            }
        }

        return importLines;
    }

    private List<FormLine> buildFormLines(List<String> tenSPList,
                                          List<String> giaBanList,
                                          List<String> soLuongList,
                                          List<String> giaNhapList,
                                          HttpServletRequest request) {
        List<FormLine> lines = new ArrayList<>();
        int rowCount = tenSPList == null ? 0 : tenSPList.size();

        for (int i = 0; i < rowCount; i++) {
            FormLine line = new FormLine();
            line.setTenSP(getValue(tenSPList, i));
            line.setGiaBan(getValue(giaBanList, i));
            line.setSoLuong(getValue(soLuongList, i));
            line.setGiaNhap(getValue(giaNhapList, i));

            String[] imeis = request.getParameterValues("imeis[" + i + "]");
            if (imeis != null) {
                line.setImeis(List.of(imeis));
            }
            lines.add(line);
        }

        return lines;
    }

    private String getValue(List<String> values, int index) {
        if (values == null || index >= values.size()) {
            return "";
        }
        return values.get(index);
    }

    private Integer parsePositiveInt(String value) {
        try {
            int parsed = Integer.parseInt(trim(value));
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Long parseNonNegativeLong(String value) {
        try {
            String trimmed = trim(value);
            if (trimmed.isEmpty()) {
                return null;
            }
            long parsed = Long.parseLong(trimmed);
            return parsed >= 0 ? parsed : null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isAdmin(HttpSession session) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("taiKhoan");
        return tk != null && tk.isAdmin();
    }

    public static class FormLine {
        private String tenSP = "";
        private String giaBan = "";
        private String soLuong = "1";
        private String giaNhap = "";
        private List<String> imeis = new ArrayList<>(List.of(""));

        public String getTenSP() { return tenSP; }
        public void setTenSP(String tenSP) { this.tenSP = tenSP; }

        public String getGiaBan() { return giaBan; }
        public void setGiaBan(String giaBan) { this.giaBan = giaBan; }

        public String getSoLuong() { return soLuong; }
        public void setSoLuong(String soLuong) { this.soLuong = soLuong; }

        public String getGiaNhap() { return giaNhap; }
        public void setGiaNhap(String giaNhap) { this.giaNhap = giaNhap; }

        public List<String> getImeis() { return imeis; }
        public void setImeis(List<String> imeis) { this.imeis = imeis; }
    }

    private static class ImportLine {
        private final String tenSP;
        private final SanPham sanPham;
        private final long giaBan;
        private final int soLuong;
        private final long giaNhap;
        private final List<String> imeis;

        private ImportLine(String tenSP, SanPham sanPham, Long giaBan, int soLuong, long giaNhap, List<String> imeis) {
            this.tenSP = tenSP;
            this.sanPham = sanPham;
            this.giaBan = giaBan == null ? 0 : giaBan;
            this.soLuong = soLuong;
            this.giaNhap = giaNhap;
            this.imeis = imeis;
        }
    }
}
