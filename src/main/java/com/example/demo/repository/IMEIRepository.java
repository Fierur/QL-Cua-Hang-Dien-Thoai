package com.example.demo.repository;

import com.example.demo.entity.IMEI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IMEIRepository extends JpaRepository<IMEI, String> {
    // Lấy danh sách IMEI của 1 sản phẩm còn trong kho
    List<IMEI> findBySanPham_MaSPAndTrangThai(int maSP, String trangThai);
    // Đếm số máy theo trạng thái của 1 sản phẩm
    int countBySanPham_MaSPAndTrangThai(int maSP, String trangThai);
}
