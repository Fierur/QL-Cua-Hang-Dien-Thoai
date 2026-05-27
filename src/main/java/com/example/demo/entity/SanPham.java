package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Bảng SanPham: danh mục sản phẩm (model điện thoại)
// soLuongTon được cập nhật tự động khi nhập/bán hàng
@Entity
@Table(name = "SanPham")
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maSP")
    private int maSP;

    @Column(name = "tenSP", nullable = false)
    private String tenSP;

    @Column(name = "hangSX")
    private String hangSX;

    @Column(name = "loaiSP")
    private String loaiSP;

    @Column(name = "giaBan", nullable = false)
    private long giaBan;

    @Column(name = "moTa", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "ram")
    private String ram;

    @Column(name = "rom")
    private String rom;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "SanPham_HinhAnh", joinColumns = @JoinColumn(name = "maSP"))
    @Column(name = "url")
    private List<String> hinhAnhs = new ArrayList<>();

    // Tổng số máy đang trong kho (trangThai = TRONG_KHO)
    @Column(name = "soLuongTon")
    private int soLuongTon = 0;

    public SanPham() {}

    public int getMaSP() { return maSP; }
    public void setMaSP(int maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public String getHangSX() { return hangSX; }
    public void setHangSX(String hangSX) { this.hangSX = hangSX; }

    public String getLoaiSP() { return loaiSP; }
    public void setLoaiSP(String loaiSP) { this.loaiSP = loaiSP; }

    public long getGiaBan() { return giaBan; }
    public void setGiaBan(long giaBan) { this.giaBan = giaBan; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getRam() { return ram; }
    public void setRam(String ram) { this.ram = ram; }

    public String getRom() { return rom; }
    public void setRom(String rom) { this.rom = rom; }

    public int getSoLuongTon() { return soLuongTon; }
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }

    public List<String> getHinhAnhs() { return hinhAnhs; }
    public void setHinhAnhs(List<String> hinhAnhs) { this.hinhAnhs = hinhAnhs; }
}