package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

// Khóa chính ghép (maPN, maSP) cho bảng ChiTietPhieuNhap
@Embeddable
public class ChiTietPhieuNhapId implements Serializable {
    private int maPN;
    private int maSP;

    public ChiTietPhieuNhapId() {}
    public ChiTietPhieuNhapId(int maPN, int maSP) {
        this.maPN = maPN;
        this.maSP = maSP;
    }

    public int getMaPN() { return maPN; }
    public void setMaPN(int maPN) { this.maPN = maPN; }

    public int getMaSP() { return maSP; }
    public void setMaSP(int maSP) { this.maSP = maSP; }
}
