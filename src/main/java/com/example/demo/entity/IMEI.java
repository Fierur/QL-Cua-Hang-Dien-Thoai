package com.example.demo.entity;

import jakarta.persistence.*;

// Bảng IMEI: mỗi máy điện thoại vật lý có 1 IMEI riêng
// trangThai: TRONG_KHO | DA_BAN | BAO_HANH
@Entity
@Table(name = "IMEI")
public class IMEI {

    @Id
    @Column(name = "imei", length = 20)
    private String imei;

    // Quan hệ nhiều IMEI → 1 SanPham
    @ManyToOne
    @JoinColumn(name = "maSP", referencedColumnName = "maSP")
    private SanPham sanPham;

    @Column(name = "trangThai", nullable = false)
    private String trangThai = "TRONG_KHO";

    public IMEI() {}

    public String getImei() { return imei; }
    public void setImei(String imei) { this.imei = imei; }

    public SanPham getSanPham() { return sanPham; }
    public void setSanPham(SanPham sanPham) { this.sanPham = sanPham; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}