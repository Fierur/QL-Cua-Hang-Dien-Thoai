package com.example.demo.repository;

import com.example.demo.entity.PhieuNhap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PhieuNhapRepository extends JpaRepository<PhieuNhap, Integer> {
    // Lọc phiếu nhập theo khoảng ngày (dùng cho báo cáo)
    List<PhieuNhap> findByNgayNhapBetween(LocalDate from, LocalDate to);
}
