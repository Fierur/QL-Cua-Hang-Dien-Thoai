package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ChiTietDonHang")
public class ChiTietDonHang {

    @EmbeddedId
    private ChiTietDonHangId id = new ChiTietDonHangId();

    @ManyToOne
    @MapsId("maDH")
    @JoinColumn(name = "maDH")
    private DonHang donHang;

    @ManyToOne
    @MapsId("maSP")
    @JoinColumn(name = "maSP")
    private SanPham sanPham;

    @Column(name = "soLuong", nullable = false)
    private int soLuong;

    @Column(name = "donGia", nullable = false)
    private long donGia;

    public ChiTietDonHang() {}

    public ChiTietDonHang(DonHang donHang, SanPham sanPham, int soLuong, long donGia) {
        this.donHang = donHang;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.id = new ChiTietDonHangId(donHang.getMaDH(), sanPham.getMaSP());
    }

    public ChiTietDonHangId getId() { return id; }
    public void setId(ChiTietDonHangId id) { this.id = id; }

    public DonHang getDonHang() { return donHang; }
    public void setDonHang(DonHang donHang) { this.donHang = donHang; }

    public SanPham getSanPham() { return sanPham; }
    public void setSanPham(SanPham sanPham) { this.sanPham = sanPham; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public long getDonGia() { return donGia; }
    public void setDonGia(long donGia) { this.donGia = donGia; }
}
