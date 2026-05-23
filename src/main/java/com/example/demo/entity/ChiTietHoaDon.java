package com.example.demo.entity;

import jakarta.persistence.*;

// Bảng ChiTietHoaDon: mỗi dòng là 1 máy (IMEI) trong hóa đơn
@Entity
@Table(name = "ChiTietHoaDon")
public class ChiTietHoaDon {

    @EmbeddedId
    private ChiTietHoaDonId id = new ChiTietHoaDonId();

    @ManyToOne
    @MapsId("maHD")
    @JoinColumn(name = "maHD")
    private HoaDon hoaDon;

    // IMEI máy được bán
    @ManyToOne
    @MapsId("imei")
    @JoinColumn(name = "imei")
    private IMEI imeiEntity;

    // Sản phẩm (lưu riêng để truy vấn nhanh)
    @ManyToOne
    @JoinColumn(name = "maSP", referencedColumnName = "maSP")
    private SanPham sanPham;

    @Column(name = "donGia", nullable = false)
    private long donGia;

    public ChiTietHoaDon() {}

    public ChiTietHoaDonId getId() { return id; }
    public void setId(ChiTietHoaDonId id) { this.id = id; }

    public HoaDon getHoaDon() { return hoaDon; }
    public void setHoaDon(HoaDon hoaDon) { this.hoaDon = hoaDon; }

    public IMEI getImeiEntity() { return imeiEntity; }
    public void setImeiEntity(IMEI imeiEntity) { this.imeiEntity = imeiEntity; }

    public SanPham getSanPham() { return sanPham; }
    public void setSanPham(SanPham sanPham) { this.sanPham = sanPham; }

    public long getDonGia() { return donGia; }
    public void setDonGia(long donGia) { this.donGia = donGia; }
}