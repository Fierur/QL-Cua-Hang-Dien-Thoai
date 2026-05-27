package com.example.demo.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DonHang")
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDH")
    private int maDH;

    @ManyToOne
    @JoinColumn(name = "maKH", referencedColumnName = "maKH", nullable = false)
    private KhachHang khachHang;

    @Column(name = "ngayDat", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ngayDat;

    @Column(name = "tongTien", nullable = false)
    private long tongTien;

    @Column(name = "trangThai", nullable = false)
    // Trạng thái: CHO_DUYET, DA_DUYET, DA_GIAO, DA_HUY
    private String trangThai = "CHO_DUYET";

    @Column(name = "diaChiGiao", nullable = false)
    private String diaChiGiao;

    @Column(name = "sdtGiao", nullable = false)
    private String sdtGiao;

    @Column(name = "ghiChu")
    private String ghiChu;

    // COD | CHUYEN_KHOAN
    @Column(name = "hinhThucTT", nullable = false)
    private String hinhThucTT;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
    private List<ChiTietDonHang> chiTiet;

    public DonHang() {}

    public int getMaDH() { return maDH; }
    public void setMaDH(int maDH) { this.maDH = maDH; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public LocalDateTime getNgayDat() { return ngayDat; }
    public void setNgayDat(LocalDateTime ngayDat) { this.ngayDat = ngayDat; }

    public long getTongTien() { return tongTien; }
    public void setTongTien(long tongTien) { this.tongTien = tongTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getDiaChiGiao() { return diaChiGiao; }
    public void setDiaChiGiao(String diaChiGiao) { this.diaChiGiao = diaChiGiao; }

    public String getSdtGiao() { return sdtGiao; }
    public void setSdtGiao(String sdtGiao) { this.sdtGiao = sdtGiao; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getHinhThucTT() { return hinhThucTT; }
    public void setHinhThucTT(String hinhThucTT) { this.hinhThucTT = hinhThucTT; }

    public List<ChiTietDonHang> getChiTiet() { return chiTiet; }
    public void setChiTiet(List<ChiTietDonHang> chiTiet) { this.chiTiet = chiTiet; }
}
