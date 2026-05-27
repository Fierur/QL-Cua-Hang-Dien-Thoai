package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChiTietDonHangId implements Serializable {

    @Column(name = "maDH")
    private int maDH;

    @Column(name = "maSP")
    private int maSP;

    public ChiTietDonHangId() {}

    public ChiTietDonHangId(int maDH, int maSP) {
        this.maDH = maDH;
        this.maSP = maSP;
    }

    public int getMaDH() { return maDH; }
    public void setMaDH(int maDH) { this.maDH = maDH; }

    public int getMaSP() { return maSP; }
    public void setMaSP(int maSP) { this.maSP = maSP; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietDonHangId that = (ChiTietDonHangId) o;
        return maDH == that.maDH && maSP == that.maSP;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDH, maSP);
    }
}
