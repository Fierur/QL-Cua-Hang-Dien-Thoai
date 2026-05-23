package com.example.demo.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

// Bảng PhieuNhap: mỗi lần nhập hàng từ nhà cung cấp tạo 1 phiếu
@Entity
@Table(name = "PhieuNhap")
public class PhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maPN")
    private int maPN;

    @Column(name = "ngayNhap", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayNhap;

    // Quan hệ nhiều phiếu → 1 nhà cung cấp
    @ManyToOne
    @JoinColumn(name = "maNCC", referencedColumnName = "maNCC")
    private NhaCungCap nhaCungCap;

    // Nhân viên lập phiếu
    @ManyToOne
    @JoinColumn(name = "maTK", referencedColumnName = "maTK")
    private TaiKhoan taiKhoan;

    @Column(name = "ghiChu")
    private String ghiChu;

    // Danh sách chi tiết sản phẩm trong phiếu
    @OneToMany(mappedBy = "phieuNhap", cascade = CascadeType.ALL)
    private List<ChiTietPhieuNhap> chiTiet;

    public PhieuNhap() {}

    public int getMaPN() { return maPN; }
    public void setMaPN(int maPN) { this.maPN = maPN; }

    public LocalDate getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(LocalDate ngayNhap) { this.ngayNhap = ngayNhap; }

    public NhaCungCap getNhaCungCap() { return nhaCungCap; }
    public void setNhaCungCap(NhaCungCap nhaCungCap) { this.nhaCungCap = nhaCungCap; }

    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public List<ChiTietPhieuNhap> getChiTiet() { return chiTiet; }
    public void setChiTiet(List<ChiTietPhieuNhap> chiTiet) { this.chiTiet = chiTiet; }
}