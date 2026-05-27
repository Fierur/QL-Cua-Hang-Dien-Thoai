package com.example.demo.repository;

import com.example.demo.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    // Tìm kiếm khách hàng theo tên (không phân biệt hoa thường)
    List<KhachHang> findByTenKHContainingIgnoreCase(String tenKH);
    // Tra cứu theo số điện thoại (tìm gần đúng)
    List<KhachHang> findBySdtContaining(String sdt);
    
    // Tìm chính xác theo số điện thoại (để login)
    KhachHang findBySdt(String sdt);
}