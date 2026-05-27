package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "GioHang")
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maGH")
    private int maGH;

    // Mỗi khách hàng có 1 giỏ hàng (hoặc nhiều, nhưng thường là 1 active)
    @OneToOne
    @JoinColumn(name = "maKH", referencedColumnName = "maKH", nullable = false)
    private KhachHang khachHang;

    @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietGioHang> chiTiet;

    public GioHang() {}

    public GioHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public int getMaGH() { return maGH; }
    public void setMaGH(int maGH) { this.maGH = maGH; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public List<ChiTietGioHang> getChiTiet() { return chiTiet; }
    public void setChiTiet(List<ChiTietGioHang> chiTiet) { this.chiTiet = chiTiet; }
}
