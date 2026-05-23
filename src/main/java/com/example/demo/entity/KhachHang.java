package com.example.demo.entity;

import jakarta.persistence.*;

// Bảng KhachHang: thông tin khách hàng mua điện thoại
@Entity
@Table(name = "KhachHang")
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maKH")
    private int maKH;

    @Column(name = "tenKH", nullable = false)
    private String tenKH;

    @Column(name = "sdt", nullable = false, unique = true)
    private String sdt;

    @Column(name = "diaChi")
    private String diaChi;

    @Column(name = "diemTichLuy")
    private int diemTichLuy = 0;

    public KhachHang() {}

    public int getMaKH() { return maKH; }
    public void setMaKH(int maKH) { this.maKH = maKH; }

    public String getTenKH() { return tenKH; }
    public void setTenKH(String tenKH) { this.tenKH = tenKH; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public int getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }
}