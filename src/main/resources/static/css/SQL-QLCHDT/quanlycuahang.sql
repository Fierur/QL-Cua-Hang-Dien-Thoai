-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost
-- Thời gian đã tạo: Th5 28, 2026 lúc 04:20 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `quanlycuahang`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `banner`
--

CREATE TABLE `banner` (
  `maBanner` int(11) NOT NULL,
  `hinhAnh` text DEFAULT NULL,
  `linkDen` varchar(255) DEFAULT NULL,
  `moTa` varchar(255) DEFAULT NULL,
  `thuTu` int(11) DEFAULT NULL,
  `tieuDe` varchar(255) DEFAULT NULL,
  `trangThai` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `baohanh`
--

CREATE TABLE `baohanh` (
  `maBH` int(11) NOT NULL,
  `imei` varchar(20) NOT NULL,
  `maKH` int(11) NOT NULL,
  `maTK` int(11) NOT NULL,
  `ngayNhan` date NOT NULL,
  `ngayTra` date DEFAULT NULL,
  `tinhTrangLoi` text DEFAULT NULL,
  `nguyenNhan` varchar(255) DEFAULT NULL,
  `huongXuLy` varchar(255) NOT NULL,
  `ghiChu` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `baohanh`
--

INSERT INTO `baohanh` (`maBH`, `imei`, `maKH`, `maTK`, `ngayNhan`, `ngayTra`, `tinhTrangLoi`, `nguyenNhan`, `huongXuLy`, `ghiChu`) VALUES
(1, '352999001111003', 1, 2, '2026-05-20', NULL, 'Màn hình bị sọc', 'Lỗi phần cứng', 'DANG_XU_LY', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietdonhang`
--

CREATE TABLE `chitietdonhang` (
  `donGia` bigint(20) NOT NULL,
  `soLuong` int(11) NOT NULL,
  `maDH` int(11) NOT NULL,
  `maSP` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chitietdonhang`
--

INSERT INTO `chitietdonhang` (`donGia`, `soLuong`, `maDH`, `maSP`) VALUES
(22990000, 1, 1, 4),
(29990000, 1, 2, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietgiohang`
--

CREATE TABLE `chitietgiohang` (
  `soLuong` int(11) NOT NULL,
  `maGH` int(11) NOT NULL,
  `maSP` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chitietgiohang`
--

INSERT INTO `chitietgiohang` (`soLuong`, `maGH`, `maSP`) VALUES
(1, 1, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitiethoadon`
--

CREATE TABLE `chitiethoadon` (
  `maHD` int(11) NOT NULL,
  `imei` varchar(20) NOT NULL,
  `maSP` int(11) NOT NULL,
  `donGia` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chitiethoadon`
--

INSERT INTO `chitiethoadon` (`maHD`, `imei`, `maSP`, `donGia`) VALUES
(1, '352999001111003', 1, 34990000),
(2, '352999004444001', 4, 22990000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietphieunhap`
--

CREATE TABLE `chitietphieunhap` (
  `maPN` int(11) NOT NULL,
  `maSP` int(11) NOT NULL,
  `soLuong` int(11) NOT NULL DEFAULT 1,
  `giaNhap` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `chitietphieunhap`
--

INSERT INTO `chitietphieunhap` (`maPN`, `maSP`, `soLuong`, `giaNhap`) VALUES
(1, 1, 3, 28000000),
(1, 4, 2, 18000000),
(2, 2, 2, 24000000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `danhgia`
--

CREATE TABLE `danhgia` (
  `maDG` int(11) NOT NULL,
  `ngayDG` datetime(6) DEFAULT NULL,
  `noiDung` text DEFAULT NULL,
  `soSao` int(11) NOT NULL,
  `maKH` int(11) NOT NULL,
  `maSP` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `danhgia`
--

INSERT INTO `danhgia` (`maDG`, `ngayDG`, `noiDung`, `soSao`, `maKH`, `maSP`) VALUES
(1, '2026-05-28 03:37:44.000000', 'tốt', 5, 5, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `donhang`
--

CREATE TABLE `donhang` (
  `maDH` int(11) NOT NULL,
  `diaChiGiao` varchar(255) NOT NULL,
  `ghiChu` varchar(255) DEFAULT NULL,
  `hinhThucTT` varchar(255) NOT NULL,
  `ngayDat` datetime(6) NOT NULL,
  `sdtGiao` varchar(255) NOT NULL,
  `tongTien` bigint(20) NOT NULL,
  `trangThai` varchar(255) NOT NULL,
  `maKH` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `donhang`
--

INSERT INTO `donhang` (`maDH`, `diaChiGiao`, `ghiChu`, `hinhThucTT`, `ngayDat`, `sdtGiao`, `tongTien`, `trangThai`, `maKH`) VALUES
(1, '207 Cộng Hoà, Phường Bảy Hiền, Tp HCM', '', 'COD', '2026-05-28 04:09:18.000000', '0901000001', 22990000, 'CHO_DUYET', 5),
(2, '207 Cộng Hoà, Phường Bảy Hiền, Tp HCM', '', 'TIEN_MAT', '2026-05-28 05:17:17.000000', '0987498448', 29990000, 'CHO_DUYET', 4);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `giohang`
--

CREATE TABLE `giohang` (
  `maGH` int(11) NOT NULL,
  `maKH` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `giohang`
--

INSERT INTO `giohang` (`maGH`, `maKH`) VALUES
(1, 4),
(2, 5);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `maHD` int(11) NOT NULL,
  `ngayLap` date NOT NULL,
  `maKH` int(11) DEFAULT NULL,
  `maTK` int(11) NOT NULL,
  `hinhThucTT` varchar(255) NOT NULL,
  `tongTien` bigint(20) NOT NULL,
  `ghiChu` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `hoadon`
--

INSERT INTO `hoadon` (`maHD`, `ngayLap`, `maKH`, `maTK`, `hinhThucTT`, `tongTien`, `ghiChu`) VALUES
(1, '2026-05-15', 1, 2, 'TIEN_MAT', 34990000, NULL),
(2, '2026-05-16', 2, 3, 'CHUYEN_KHOAN', 22990000, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `imei`
--

CREATE TABLE `imei` (
  `imei` varchar(20) NOT NULL,
  `maSP` int(11) NOT NULL,
  `trangThai` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `imei`
--

INSERT INTO `imei` (`imei`, `maSP`, `trangThai`) VALUES
('352999001111001', 1, 'TRONG_KHO'),
('352999001111002', 1, 'TRONG_KHO'),
('352999001111003', 1, 'DA_BAN'),
('352999002222001', 2, 'TRONG_KHO'),
('352999002222002', 2, 'TRONG_KHO'),
('352999003333001', 3, 'TRONG_KHO'),
('352999003333002', 3, 'TRONG_KHO'),
('352999004444001', 4, 'DA_BAN'),
('352999004444002', 4, 'TRONG_KHO'),
('352999005555001', 5, 'TRONG_KHO');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `imeiphieunhap`
--

CREATE TABLE `imeiphieunhap` (
  `maPN` int(11) NOT NULL,
  `imei` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `maKH` int(11) NOT NULL,
  `tenKH` varchar(255) NOT NULL,
  `sdt` varchar(255) NOT NULL,
  `diaChi` varchar(255) DEFAULT NULL,
  `diemTichLuy` int(11) NOT NULL DEFAULT 0,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `matKhau` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

INSERT INTO `khachhang` (`maKH`, `tenKH`, `sdt`, `diaChi`, `diemTichLuy`, `avatar`, `email`, `matKhau`) VALUES
(1, 'Nguyễn Văn An', '0911111111', 'Quận 1, TP.HCM', 100, NULL, NULL, NULL),
(2, 'Trần Thị Bình', '0922222222', 'Quận 3, TP.HCM', 50, NULL, NULL, NULL),
(3, 'Lê Hoàng Cường', '0933333333', 'Bình Thạnh, TP.HCM', 0, NULL, NULL, NULL),
(4, 'Hồ Năng Quý', '0987498448', NULL, 0, NULL, 'hoquy902@gmail.com', 'Concac123!@#'),
(5, 'Nguyễn Quản Trị', '0901000001', NULL, 0, NULL, NULL, 'admin123');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhacungcap`
--

CREATE TABLE `nhacungcap` (
  `maNCC` int(11) NOT NULL,
  `tenNCC` varchar(255) NOT NULL,
  `sdt` varchar(255) DEFAULT NULL,
  `diaChi` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `nhacungcap`
--

INSERT INTO `nhacungcap` (`maNCC`, `tenNCC`, `sdt`, `diaChi`) VALUES
(1, 'Công ty TNHH Apple VN', '02812345678', 'Quận 1, TP.HCM'),
(2, 'Samsung Việt Nam', '02887654321', 'Quận 7, TP.HCM'),
(3, 'Xiaomi Việt Nam', '02899999999', 'Quận 10, TP.HCM');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phieunhap`
--

CREATE TABLE `phieunhap` (
  `maPN` int(11) NOT NULL,
  `ngayNhap` date NOT NULL,
  `maNCC` int(11) DEFAULT NULL,
  `maTK` int(11) NOT NULL,
  `ghiChu` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `phieunhap`
--

INSERT INTO `phieunhap` (`maPN`, `ngayNhap`, `maNCC`, `maTK`, `ghiChu`) VALUES
(1, '2026-05-01', 1, 1, 'Nhập hàng tháng 5'),
(2, '2026-05-10', 2, 1, 'Bổ sung Samsung');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `maSP` int(11) NOT NULL,
  `tenSP` varchar(255) NOT NULL,
  `hangSX` varchar(255) DEFAULT NULL,
  `loaiSP` varchar(255) DEFAULT NULL,
  `giaBan` bigint(20) NOT NULL,
  `moTa` text DEFAULT NULL,
  `soLuongTon` int(11) NOT NULL DEFAULT 0,
  `ram` varchar(255) DEFAULT NULL,
  `rom` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`maSP`, `tenSP`, `hangSX`, `loaiSP`, `giaBan`, `moTa`, `soLuongTon`, `ram`, `rom`) VALUES
(1, 'iPhone 15 Pro Max 256GB', 'Apple', 'Cao cấp', 34990000, 'Chip A17 Pro, camera 48MP', 5, NULL, NULL),
(2, 'Samsung Galaxy S24 Ultra', 'Samsung', 'Cao cấp', 29990000, 'Snapdragon 8 Gen 3', 3, NULL, NULL),
(3, 'Xiaomi 14 Pro', 'Xiaomi', 'Tầm trung', 18990000, 'Snapdragon 8 Gen 3', 7, NULL, NULL),
(4, 'iPhone 15 128GB', 'Apple', 'Tầm trung', 22990000, 'Chip A16, Dynamic Island', 4, NULL, NULL),
(5, 'Samsung Galaxy A55', 'Samsung', 'Phổ thông', 9990000, 'Exynos 1480, AMOLED', 10, NULL, NULL),
(6, 'TestProd', NULL, NULL, 100, NULL, 10, NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham_hinhanh`
--

CREATE TABLE `sanpham_hinhanh` (
  `maSP` int(11) NOT NULL,
  `url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham_hinhanh`
--

INSERT INTO `sanpham_hinhanh` (`maSP`, `url`) VALUES
(2, 'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/s/s/ss-s24-ultra-xam-222.png'),
(3, 'https://cdn.tgdd.vn/Products/Images/42/307882/xiaomi-14-pro-600x600.jpg'),
(4, 'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-15-plus-256gb-color-pink-image_3_1.png'),
(5, 'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/s/a/samsung-galaxy-a55_1__1.png'),
(1, 'https://cdn2.cellphones.com.vn/insecure/rs:fill:358:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-15-pro-max_2__5_2_1_1.jpg'),
(1, 'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/v/n/vn_iphone_15_pro_black_titanium_pdp_image_position-1a_black_titanium_color_1_1_1.jpg'),
(1, 'https://cdn2.cellphones.com.vn/x/media/catalog/product/v/n/vn_iphone_15_pro_white_titanium_pdp_image_position-1a_white_titanium_color_1_1_1.jpg'),
(6, 'https://example.com/1.jpg'),
(6, 'https://example.com/2.jpg');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `taikhoan`
--

CREATE TABLE `taikhoan` (
  `maTK` int(11) NOT NULL,
  `tenDangNhap` varchar(255) NOT NULL,
  `matKhau` varchar(255) NOT NULL,
  `vaiTro` varchar(255) NOT NULL,
  `hoTen` varchar(255) NOT NULL,
  `sdt` varchar(255) DEFAULT NULL,
  `trangThai` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `taikhoan`
--

INSERT INTO `taikhoan` (`maTK`, `tenDangNhap`, `matKhau`, `vaiTro`, `hoTen`, `sdt`, `trangThai`) VALUES
(1, 'admin', 'admin123', 'ADMIN', 'Nguyễn Quản Trị', '0901000001', 1),
(2, 'staff1', 'staff123', 'STAFF', 'Trần Nhân Viên', '0901000002', 1),
(3, 'staff2', 'staff123', 'STAFF', 'Lê Thị Bán Hàng', '0901000003', 1);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `banner`
--
ALTER TABLE `banner`
  ADD PRIMARY KEY (`maBanner`);

--
-- Chỉ mục cho bảng `baohanh`
--
ALTER TABLE `baohanh`
  ADD PRIMARY KEY (`maBH`),
  ADD KEY `imei` (`imei`),
  ADD KEY `maKH` (`maKH`),
  ADD KEY `maTK` (`maTK`);

--
-- Chỉ mục cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD PRIMARY KEY (`maDH`,`maSP`),
  ADD KEY `FKao4h7md1aog6896k81dnqrdd0` (`maSP`);

--
-- Chỉ mục cho bảng `chitietgiohang`
--
ALTER TABLE `chitietgiohang`
  ADD PRIMARY KEY (`maGH`,`maSP`),
  ADD KEY `FKaahlmmkxdcrv2b51fdqlpibk2` (`maSP`);

--
-- Chỉ mục cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD PRIMARY KEY (`maHD`,`imei`),
  ADD KEY `imei` (`imei`),
  ADD KEY `maSP` (`maSP`);

--
-- Chỉ mục cho bảng `chitietphieunhap`
--
ALTER TABLE `chitietphieunhap`
  ADD PRIMARY KEY (`maPN`,`maSP`),
  ADD KEY `maSP` (`maSP`);

--
-- Chỉ mục cho bảng `danhgia`
--
ALTER TABLE `danhgia`
  ADD PRIMARY KEY (`maDG`),
  ADD KEY `FKl8kwf7wi3y7n1dmahlrpcjef5` (`maKH`),
  ADD KEY `FK8wsf8b6a4pk75hlxroyvpg2oj` (`maSP`);

--
-- Chỉ mục cho bảng `donhang`
--
ALTER TABLE `donhang`
  ADD PRIMARY KEY (`maDH`),
  ADD KEY `FK76bswmcy995cqjpc3ah4p8lb4` (`maKH`);

--
-- Chỉ mục cho bảng `giohang`
--
ALTER TABLE `giohang`
  ADD PRIMARY KEY (`maGH`),
  ADD UNIQUE KEY `UKr5rx1x0sj8db0gnsj2kplsmsf` (`maKH`);

--
-- Chỉ mục cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`maHD`),
  ADD KEY `maKH` (`maKH`),
  ADD KEY `maTK` (`maTK`);

--
-- Chỉ mục cho bảng `imei`
--
ALTER TABLE `imei`
  ADD PRIMARY KEY (`imei`),
  ADD KEY `maSP` (`maSP`);

--
-- Chỉ mục cho bảng `imeiphieunhap`
--
ALTER TABLE `imeiphieunhap`
  ADD PRIMARY KEY (`maPN`,`imei`),
  ADD KEY `imei` (`imei`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`maKH`),
  ADD UNIQUE KEY `sdt` (`sdt`);

--
-- Chỉ mục cho bảng `nhacungcap`
--
ALTER TABLE `nhacungcap`
  ADD PRIMARY KEY (`maNCC`);

--
-- Chỉ mục cho bảng `phieunhap`
--
ALTER TABLE `phieunhap`
  ADD PRIMARY KEY (`maPN`),
  ADD KEY `maNCC` (`maNCC`),
  ADD KEY `maTK` (`maTK`);

--
-- Chỉ mục cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`maSP`);

--
-- Chỉ mục cho bảng `sanpham_hinhanh`
--
ALTER TABLE `sanpham_hinhanh`
  ADD KEY `FK371bxafmsxuymiwncit3bn8bb` (`maSP`);

--
-- Chỉ mục cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD PRIMARY KEY (`maTK`),
  ADD UNIQUE KEY `tenDangNhap` (`tenDangNhap`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `banner`
--
ALTER TABLE `banner`
  MODIFY `maBanner` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `baohanh`
--
ALTER TABLE `baohanh`
  MODIFY `maBH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `danhgia`
--
ALTER TABLE `danhgia`
  MODIFY `maDG` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `donhang`
--
ALTER TABLE `donhang`
  MODIFY `maDH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `giohang`
--
ALTER TABLE `giohang`
  MODIFY `maGH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  MODIFY `maHD` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `maKH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `nhacungcap`
--
ALTER TABLE `nhacungcap`
  MODIFY `maNCC` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `phieunhap`
--
ALTER TABLE `phieunhap`
  MODIFY `maPN` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `maSP` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  MODIFY `maTK` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `baohanh`
--
ALTER TABLE `baohanh`
  ADD CONSTRAINT `baohanh_ibfk_1` FOREIGN KEY (`imei`) REFERENCES `imei` (`imei`),
  ADD CONSTRAINT `baohanh_ibfk_2` FOREIGN KEY (`maKH`) REFERENCES `khachhang` (`maKH`),
  ADD CONSTRAINT `baohanh_ibfk_3` FOREIGN KEY (`maTK`) REFERENCES `taikhoan` (`maTK`);

--
-- Các ràng buộc cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD CONSTRAINT `FKao4h7md1aog6896k81dnqrdd0` FOREIGN KEY (`maSP`) REFERENCES `sanpham` (`maSP`),
  ADD CONSTRAINT `FKhcdfh7j8c4swsnr4u2a3pysn7` FOREIGN KEY (`maDH`) REFERENCES `donhang` (`maDH`);

--
-- Các ràng buộc cho bảng `chitietgiohang`
--
ALTER TABLE `chitietgiohang`
  ADD CONSTRAINT `FK47vvsyjw8wr4254d4uy81ue9v` FOREIGN KEY (`maGH`) REFERENCES `giohang` (`maGH`),
  ADD CONSTRAINT `FKaahlmmkxdcrv2b51fdqlpibk2` FOREIGN KEY (`maSP`) REFERENCES `sanpham` (`maSP`);

--
-- Các ràng buộc cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD CONSTRAINT `chitiethoadon_ibfk_1` FOREIGN KEY (`maHD`) REFERENCES `hoadon` (`maHD`),
  ADD CONSTRAINT `chitiethoadon_ibfk_2` FOREIGN KEY (`imei`) REFERENCES `imei` (`imei`),
  ADD CONSTRAINT `chitiethoadon_ibfk_3` FOREIGN KEY (`maSP`) REFERENCES `sanpham` (`maSP`);

--
-- Các ràng buộc cho bảng `chitietphieunhap`
--
ALTER TABLE `chitietphieunhap`
  ADD CONSTRAINT `chitietphieunhap_ibfk_1` FOREIGN KEY (`maPN`) REFERENCES `phieunhap` (`maPN`),
  ADD CONSTRAINT `chitietphieunhap_ibfk_2` FOREIGN KEY (`maSP`) REFERENCES `sanpham` (`maSP`);

--
-- Các ràng buộc cho bảng `danhgia`
--
ALTER TABLE `danhgia`
  ADD CONSTRAINT `FK8wsf8b6a4pk75hlxroyvpg2oj` FOREIGN KEY (`maSP`) REFERENCES `sanpham` (`maSP`),
  ADD CONSTRAINT `FKl8kwf7wi3y7n1dmahlrpcjef5` FOREIGN KEY (`maKH`) REFERENCES `khachhang` (`maKH`);

--
-- Các ràng buộc cho bảng `donhang`
--
ALTER TABLE `donhang`
  ADD CONSTRAINT `FK76bswmcy995cqjpc3ah4p8lb4` FOREIGN KEY (`maKH`) REFERENCES `khachhang` (`maKH`);

--
-- Các ràng buộc cho bảng `giohang`
--
ALTER TABLE `giohang`
  ADD CONSTRAINT `FKc3cqfc7e0acc9wiqn681evq6c` FOREIGN KEY (`maKH`) REFERENCES `khachhang` (`maKH`);

--
-- Các ràng buộc cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `hoadon_ibfk_1` FOREIGN KEY (`maKH`) REFERENCES `khachhang` (`maKH`),
  ADD CONSTRAINT `hoadon_ibfk_2` FOREIGN KEY (`maTK`) REFERENCES `taikhoan` (`maTK`);

--
-- Các ràng buộc cho bảng `imei`
--
ALTER TABLE `imei`
  ADD CONSTRAINT `imei_ibfk_1` FOREIGN KEY (`maSP`) REFERENCES `sanpham` (`maSP`);

--
-- Các ràng buộc cho bảng `imeiphieunhap`
--
ALTER TABLE `imeiphieunhap`
  ADD CONSTRAINT `imeiphieunhap_ibfk_1` FOREIGN KEY (`maPN`) REFERENCES `phieunhap` (`maPN`),
  ADD CONSTRAINT `imeiphieunhap_ibfk_2` FOREIGN KEY (`imei`) REFERENCES `imei` (`imei`);

--
-- Các ràng buộc cho bảng `phieunhap`
--
ALTER TABLE `phieunhap`
  ADD CONSTRAINT `phieunhap_ibfk_1` FOREIGN KEY (`maNCC`) REFERENCES `nhacungcap` (`maNCC`),
  ADD CONSTRAINT `phieunhap_ibfk_2` FOREIGN KEY (`maTK`) REFERENCES `taikhoan` (`maTK`);

--
-- Các ràng buộc cho bảng `sanpham_hinhanh`
--
ALTER TABLE `sanpham_hinhanh`
  ADD CONSTRAINT `FK371bxafmsxuymiwncit3bn8bb` FOREIGN KEY (`maSP`) REFERENCES `sanpham` (`maSP`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
