package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChiTietPhieuNhapId that)) return false;
        return maPN == that.maPN && maSP == that.maSP;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPN, maSP);
    }
}
