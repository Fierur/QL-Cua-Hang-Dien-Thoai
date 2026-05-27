package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChiTietGioHangId implements Serializable {

    @Column(name = "maGH")
    private int maGH;

    @Column(name = "maSP")
    private int maSP;

    public ChiTietGioHangId() {}

    public ChiTietGioHangId(int maGH, int maSP) {
        this.maGH = maGH;
        this.maSP = maSP;
    }

    public int getMaGH() { return maGH; }
    public void setMaGH(int maGH) { this.maGH = maGH; }

    public int getMaSP() { return maSP; }
    public void setMaSP(int maSP) { this.maSP = maSP; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietGioHangId that = (ChiTietGioHangId) o;
        return maGH == that.maGH && maSP == that.maSP;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maGH, maSP);
    }
}
