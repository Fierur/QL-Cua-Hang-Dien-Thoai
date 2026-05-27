package com.example.demo.repository;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    GioHang findByKhachHang(KhachHang khachHang);
}
