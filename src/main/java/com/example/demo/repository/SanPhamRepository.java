package com.example.demo.repository;

import com.example.demo.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    // Tìm kiếm sản phẩm theo tên
    List<SanPham> findByTenSPContainingIgnoreCase(String tenSP);
    // Lọc theo hãng sản xuất
    List<SanPham> findByHangSX(String hangSX);
    // Lấy sản phẩm còn hàng
    List<SanPham> findBySoLuongTonGreaterThan(int soLuong);
}
