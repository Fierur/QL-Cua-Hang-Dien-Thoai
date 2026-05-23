package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

// Khóa chính ghép (maHD, imei) cho bảng ChiTietHoaDon
@Embeddable
public class ChiTietHoaDonId implements Serializable {
    private int maHD;
    private String imei;

    public ChiTietHoaDonId() {}
    public ChiTietHoaDonId(int maHD, String imei) {
        this.maHD = maHD;
        this.imei = imei;
    }

    public int getMaHD() { return maHD; }
    public void setMaHD(int maHD) { this.maHD = maHD; }

    public String getImei() { return imei; }
    public void setImei(String imei) { this.imei = imei; }
}
