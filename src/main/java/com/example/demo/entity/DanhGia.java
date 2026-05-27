package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DanhGia")
public class DanhGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maDG")
    private int maDG;

    @ManyToOne
    @JoinColumn(name = "maKH", nullable = false)
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maSP", nullable = false)
    private SanPham sanPham;

    @Column(name = "soSao", nullable = false)
    private int soSao;

    @Column(name = "noiDung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "ngayDG")
    private LocalDateTime ngayDG = LocalDateTime.now();

    public DanhGia() {}

    public int getMaDG() { return maDG; }
    public void setMaDG(int maDG) { this.maDG = maDG; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public SanPham getSanPham() { return sanPham; }
    public void setSanPham(SanPham sanPham) { this.sanPham = sanPham; }

    public int getSoSao() { return soSao; }
    public void setSoSao(int soSao) { this.soSao = soSao; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public LocalDateTime getNgayDG() { return ngayDG; }
    public void setNgayDG(LocalDateTime ngayDG) { this.ngayDG = ngayDG; }
}
