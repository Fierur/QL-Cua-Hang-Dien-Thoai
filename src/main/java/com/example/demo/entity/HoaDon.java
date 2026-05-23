package com.example.demo.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

// Bảng HoaDon: hóa đơn bán hàng cho khách
@Entity
@Table(name = "HoaDon")
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHD")
    private int maHD;

    @Column(name = "ngayLap", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayLap;

    // Khách hàng (có thể null nếu khách vãng lai)
    @ManyToOne
    @JoinColumn(name = "maKH", referencedColumnName = "maKH")
    private KhachHang khachHang;

    // Nhân viên bán hàng
    @ManyToOne
    @JoinColumn(name = "maTK", referencedColumnName = "maTK")
    private TaiKhoan taiKhoan;

    // TIEN_MAT | CHUYEN_KHOAN | TRA_GOP
    @Column(name = "hinhThucTT", nullable = false)
    private String hinhThucTT;

    @Column(name = "tongTien", nullable = false)
    private long tongTien;

    @Column(name = "ghiChu")
    private String ghiChu;

    // Danh sách IMEI máy trong hóa đơn
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL)
    private List<ChiTietHoaDon> chiTiet;

    public HoaDon() {}

    public int getMaHD() { return maHD; }
    public void setMaHD(int maHD) { this.maHD = maHD; }

    public LocalDate getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDate ngayLap) { this.ngayLap = ngayLap; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }

    public String getHinhThucTT() { return hinhThucTT; }
    public void setHinhThucTT(String hinhThucTT) { this.hinhThucTT = hinhThucTT; }

    public long getTongTien() { return tongTien; }
    public void setTongTien(long tongTien) { this.tongTien = tongTien; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public List<ChiTietHoaDon> getChiTiet() { return chiTiet; }
    public void setChiTiet(List<ChiTietHoaDon> chiTiet) { this.chiTiet = chiTiet; }
}