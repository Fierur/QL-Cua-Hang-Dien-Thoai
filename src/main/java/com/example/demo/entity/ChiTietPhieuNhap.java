package com.example.demo.entity;

import jakarta.persistence.*;

// Bảng ChiTietPhieuNhap: từng dòng sản phẩm trong 1 phiếu nhập
@Entity
@Table(name = "ChiTietPhieuNhap")
public class ChiTietPhieuNhap {

    @EmbeddedId
    private ChiTietPhieuNhapId id = new ChiTietPhieuNhapId();

    // Quan hệ về phiếu nhập cha
    @ManyToOne
    @MapsId("maPN")
    @JoinColumn(name = "maPN")
    private PhieuNhap phieuNhap;

    // Sản phẩm được nhập
    @ManyToOne
    @MapsId("maSP")
    @JoinColumn(name = "maSP")
    private SanPham sanPham;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;

    @Column(name = "giaNhap", nullable = false)
    private long giaNhap;

    public ChiTietPhieuNhap() {}

    public ChiTietPhieuNhapId getId() { return id; }
    public void setId(ChiTietPhieuNhapId id) { this.id = id; }

    public PhieuNhap getPhieuNhap() { return phieuNhap; }
    public void setPhieuNhap(PhieuNhap phieuNhap) { this.phieuNhap = phieuNhap; }

    public SanPham getSanPham() { return sanPham; }
    public void setSanPham(SanPham sanPham) { this.sanPham = sanPham; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public long getGiaNhap() { return giaNhap; }
    public void setGiaNhap(long giaNhap) { this.giaNhap = giaNhap; }
}