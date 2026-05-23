package com.example.demo.repository;

import com.example.demo.entity.BaoHanh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaoHanhRepository extends JpaRepository<BaoHanh, Integer> {
    // Lọc theo trạng thái xử lý
    List<BaoHanh> findByHuongXuLy(String huongXuLy);
    // Tìm phiếu bảo hành theo IMEI
    List<BaoHanh> findByImeiEntity_Imei(String imei);
    // Đếm phiếu bảo hành đang xử lý
    long countByHuongXuLy(String huongXuLy);
}
