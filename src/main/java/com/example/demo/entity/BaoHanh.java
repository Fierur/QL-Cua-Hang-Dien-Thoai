package com.example.demo.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// Bảng BaoHanh: phiếu bảo hành mỗi khi khách mang máy đến sửa
// huongXuLy: DANG_XU_LY | DOI_MAY | SUA_XONG | TU_CHOI
@Entity
@Table(name = "BaoHanh")
public class BaoHanh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maBH")
    private int maBH;

    // IMEI máy được bảo hành
    @ManyToOne
    @JoinColumn(name = "imei", referencedColumnName = "imei")
    private IMEI imeiEntity;

    // Khách hàng mang máy đến
    @ManyToOne
    @JoinColumn(name = "maKH", referencedColumnName = "maKH")
    private KhachHang khachHang;

    // Nhân viên kỹ thuật tiếp nhận
    @ManyToOne
    @JoinColumn(name = "maTK", referencedColumnName = "maTK")
    private TaiKhoan taiKhoan;

    @Column(name = "ngayNhan", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayNhan;

    @Column(name = "ngayTra")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayTra;

    @Column(name = "tinhTrangLoi", columnDefinition = "TEXT")
    private String tinhTrangLoi;

    @Column(name = "nguyenNhan")
    private String nguyenNhan;

    @Column(name = "huongXuLy", nullable = false)
    private String huongXuLy = "DANG_XU_LY";

    @Column(name = "ghiChu")
    private String ghiChu;

    public BaoHanh() {}

    public int getMaBH() { return maBH; }
    public void setMaBH(int maBH) { this.maBH = maBH; }

    public IMEI getImeiEntity() { return imeiEntity; }
    public void setImeiEntity(IMEI imeiEntity) { this.imeiEntity = imeiEntity; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }

    public LocalDate getNgayNhan() { return ngayNhan; }
    public void setNgayNhan(LocalDate ngayNhan) { this.ngayNhan = ngayNhan; }

    public LocalDate getNgayTra() { return ngayTra; }
    public void setNgayTra(LocalDate ngayTra) { this.ngayTra = ngayTra; }

    public String getTinhTrangLoi() { return tinhTrangLoi; }
    public void setTinhTrangLoi(String tinhTrangLoi) { this.tinhTrangLoi = tinhTrangLoi; }

    public String getNguyenNhan() { return nguyenNhan; }
    public void setNguyenNhan(String nguyenNhan) { this.nguyenNhan = nguyenNhan; }

    public String getHuongXuLy() { return huongXuLy; }
    public void setHuongXuLy(String huongXuLy) { this.huongXuLy = huongXuLy; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}