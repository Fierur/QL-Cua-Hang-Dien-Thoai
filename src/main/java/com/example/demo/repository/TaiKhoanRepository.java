package com.example.demo.repository;

import com.example.demo.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    // Dùng cho login: tìm theo username + password
    Optional<TaiKhoan> findByTenDangNhapAndMatKhau(String tenDangNhap, String matKhau);
    // Kiểm tra username đã tồn tại chưa khi thêm mới
    boolean existsByTenDangNhap(String tenDangNhap);
}