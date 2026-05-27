package com.example.demo.repository;

import com.example.demo.entity.DonHang;
import com.example.demo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
    List<DonHang> findByKhachHangOrderByNgayDatDesc(KhachHang khachHang);
    List<DonHang> findByTrangThaiOrderByNgayDatDesc(String trangThai);
}
