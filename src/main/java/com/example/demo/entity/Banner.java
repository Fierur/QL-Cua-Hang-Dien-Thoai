package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Banner")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maBanner")
    private int maBanner;

    @Column(name = "tieuDe")
    private String tieuDe;

    @Column(name = "moTa")
    private String moTa;

    @Column(name = "hinhAnh", columnDefinition = "TEXT")
    private String hinhAnh;

    @Column(name = "linkDen")
    private String linkDen;

    @Column(name = "thuTu")
    private int thuTu = 0;

    @Column(name = "trangThai")
    private boolean trangThai = true;

    public Banner() {}

    public int getMaBanner() { return maBanner; }
    public void setMaBanner(int maBanner) { this.maBanner = maBanner; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public String getLinkDen() { return linkDen; }
    public void setLinkDen(String linkDen) { this.linkDen = linkDen; }

    public int getThuTu() { return thuTu; }
    public void setThuTu(int thuTu) { this.thuTu = thuTu; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}
