package com.example.demo.repository;

import com.example.demo.entity.DanhGia;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    List<DanhGia> findBySanPhamOrderByNgayDGDesc(SanPham sanPham);

    boolean existsByKhachHangAndSanPham(KhachHang khachHang, SanPham sanPham);

    @Query("SELECT AVG(d.soSao) FROM DanhGia d WHERE d.sanPham = ?1")
    Double findAverageBySanPham(SanPham sanPham);

    long countBySanPham(SanPham sanPham);
}
