package com.example.demo.repository;

import com.example.demo.entity.ChiTietDonHang;
import com.example.demo.entity.ChiTietDonHangId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, ChiTietDonHangId> {
}
