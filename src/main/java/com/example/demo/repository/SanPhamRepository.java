package com.example.demo.repository;

import com.example.demo.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    // Tìm kiếm sản phẩm theo tên
    List<SanPham> findByTenSPContainingIgnoreCase(String tenSP);
    // Lọc theo hãng sản xuất
    List<SanPham> findByHangSX(String hangSX);
    // Lấy sản phẩm còn hàng
    List<SanPham> findBySoLuongTonGreaterThan(int soLuong);

    // Phân trang toàn bộ
    Page<SanPham> findAll(Pageable pageable);

    // Phân trang + lọc theo hãng
    Page<SanPham> findByHangSX(String hangSX, Pageable pageable);

    // Phân trang + tìm kiếm
    Page<SanPham> findByTenSPContainingIgnoreCase(String tenSP, Pageable pageable);

    // Phân trang + tìm kiếm + lọc hãng
    Page<SanPham> findByTenSPContainingIgnoreCaseAndHangSX(String tenSP, String hangSX, Pageable pageable);

    // Lấy danh sách hãng sản xuất duy nhất
    @Query("SELECT DISTINCT s.hangSX FROM SanPham s WHERE s.hangSX IS NOT NULL ORDER BY s.hangSX")
    List<String> findDistinctHangSX();

    // Sản phẩm nổi bật (có tồn kho, giới hạn)
    List<SanPham> findTop8BySoLuongTonGreaterThanOrderByGiaBanDesc(int soLuong);

    // Sản phẩm mới nhất
    List<SanPham> findTop8BySoLuongTonGreaterThanOrderByMaSPDesc(int soLuong);

    // Sản phẩm theo hãng
    List<SanPham> findTop8ByHangSXIgnoreCaseAndSoLuongTonGreaterThanOrderByMaSPDesc(String hangSX, int soLuong);

    // Tìm kiếm với bộ lọc nâng cao
    @Query("SELECT s FROM SanPham s WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(s.tenSP) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:hangSX IS NULL OR :hangSX = '' OR s.hangSX = :hangSX) AND " +
           "(:minPrice IS NULL OR s.giaBan >= :minPrice) AND " +
           "(:maxPrice IS NULL OR s.giaBan <= :maxPrice) AND " +
           "(:ram IS NULL OR :ram = '' OR s.ram = :ram) AND " +
           "(:rom IS NULL OR :rom = '' OR s.rom = :rom)")
    Page<SanPham> searchProducts(@Param("keyword") String keyword,
                                 @Param("hangSX") String hangSX,
                                 @Param("minPrice") Long minPrice,
                                 @Param("maxPrice") Long maxPrice,
                                 @Param("ram") String ram,
                                 @Param("rom") String rom,
                                 Pageable pageable);
}
