package com.example.demo.entity;

import jakarta.persistence.*;

// Bảng TaiKhoan: lưu tài khoản đăng nhập hệ thống
// vaiTro: ADMIN (toàn quyền) | STAFF (không xóa, không quản lý tài khoản)
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maTK")
    private int maTK;

    @Column(name = "tenDangNhap", nullable = false, unique = true)
    private String tenDangNhap;

    @Column(name = "matKhau", nullable = false)
    private String matKhau;

    // ADMIN hoặc STAFF
    @Column(name = "vaiTro", nullable = false)
    private String vaiTro;

    @Column(name = "hoTen", nullable = false)
    private String hoTen;

    @Column(name = "sdt")
    private String sdt;

    // 1 = hoạt động, 0 = bị khóa
    @Column(name = "trangThai", nullable = false)
    private int trangThai = 1;

    public TaiKhoan() {}

    public int getMaTK() { return maTK; }
    public void setMaTK(int maTK) { this.maTK = maTK; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }

    // Kiểm tra vai trò tiện dùng trong Thymeleaf
    public boolean isAdmin() { return "ADMIN".equals(this.vaiTro); }
}