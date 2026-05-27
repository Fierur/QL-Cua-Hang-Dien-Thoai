package com.example.demo.repository;

import com.example.demo.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    // Lọc hóa đơn theo khoảng ngày (báo cáo doanh thu)
    List<HoaDon> findByNgayLapBetween(LocalDate from, LocalDate to);

    // Tính tổng doanh thu trong khoảng ngày
    @Query("SELECT COALESCE(SUM(h.tongTien), 0) FROM HoaDon h WHERE h.ngayLap BETWEEN :from AND :to")
    Long tinhDoanhThu(@Param("from") LocalDate from, @Param("to") LocalDate to);

    // Đếm số hóa đơn theo khoảng ngày
    long countByNgayLapBetween(LocalDate from, LocalDate to);

    // Lấy hóa đơn theo ghi chú (để liên kết với đơn đặt hàng online)
    List<HoaDon> findByGhiChu(String ghiChu);
}
