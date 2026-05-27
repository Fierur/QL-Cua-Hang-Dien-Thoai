package com.example.demo.repository;

import com.example.demo.entity.ChiTietGioHang;
import com.example.demo.entity.ChiTietGioHangId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, ChiTietGioHangId> {
}
