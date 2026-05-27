package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ChiTietGioHang")
public class ChiTietGioHang {

    @EmbeddedId
    private ChiTietGioHangId id = new ChiTietGioHangId();

    @ManyToOne
    @MapsId("maGH")
    @JoinColumn(name = "maGH")
    private GioHang gioHang;

    @ManyToOne
    @MapsId("maSP")
    @JoinColumn(name = "maSP")
    private SanPham sanPham;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;

    public ChiTietGioHang() {}

    public ChiTietGioHang(GioHang gioHang, SanPham sanPham, int soLuong) {
        this.gioHang = gioHang;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.id = new ChiTietGioHangId(gioHang.getMaGH(), sanPham.getMaSP());
    }

    public ChiTietGioHangId getId() { return id; }
    public void setId(ChiTietGioHangId id) { this.id = id; }

    public GioHang getGioHang() { return gioHang; }
    public void setGioHang(GioHang gioHang) { this.gioHang = gioHang; }

    public SanPham getSanPham() { return sanPham; }
    public void setSanPham(SanPham sanPham) { this.sanPham = sanPham; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}
