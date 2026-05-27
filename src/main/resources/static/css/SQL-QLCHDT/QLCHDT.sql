-- QUẢN LÝ CỬA HÀNG ĐIỆN THOẠI
-- Nhóm 09 - Môn Lập trình Java Web
-- MySQL Workbench 8.0

DROP DATABASE IF EXISTS quanlycuahang;
CREATE DATABASE quanlycuahang CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quanlycuahang;

-- 1. TÀI KHOẢN (login)
CREATE TABLE TaiKhoan (
    maTK       INT AUTO_INCREMENT PRIMARY KEY,
    tenDangNhap VARCHAR(50)  NOT NULL UNIQUE,
    matKhau    VARCHAR(255) NOT NULL,
    vaiTro     ENUM('ADMIN','STAFF') NOT NULL DEFAULT 'STAFF',
    hoTen      VARCHAR(100) NOT NULL,
    sdt        VARCHAR(15),
    trangThai  TINYINT(1)   NOT NULL DEFAULT 1  -- 1=hoạt động, 0=khóa
);

-- 2. KHÁCH HÀNG
CREATE TABLE KhachHang (
    maKH       INT AUTO_INCREMENT PRIMARY KEY,
    tenKH      VARCHAR(100) NOT NULL,
    sdt        VARCHAR(15)  NOT NULL UNIQUE,
    diaChi     VARCHAR(255),
    diemTichLuy INT         NOT NULL DEFAULT 0
);

-- 3. NHÀ CUNG CẤP
CREATE TABLE NhaCungCap (
    maNCC      INT AUTO_INCREMENT PRIMARY KEY,
    tenNCC     VARCHAR(100) NOT NULL,
    sdt        VARCHAR(15),
    diaChi     VARCHAR(255)
);

-- 4. SẢN PHẨM (danh mục)
CREATE TABLE SanPham (
    maSP       INT AUTO_INCREMENT PRIMARY KEY,
    tenSP      VARCHAR(150) NOT NULL,
    hangSX     VARCHAR(100),
    loaiSP     VARCHAR(100),
    giaBan     DECIMAL(15,0) NOT NULL DEFAULT 0,
    moTa       TEXT,
    soLuongTon INT          NOT NULL DEFAULT 0
);

-- 5. IMEI (mỗi thiết bị vật lý trong kho)
CREATE TABLE IMEI (
    imei       VARCHAR(20)  PRIMARY KEY,
    maSP       INT          NOT NULL,
    trangThai  ENUM('TRONG_KHO','DA_BAN','BAO_HANH') NOT NULL DEFAULT 'TRONG_KHO',
    FOREIGN KEY (maSP) REFERENCES SanPham(maSP)
);

-- 6. PHIẾU NHẬP KHO
CREATE TABLE PhieuNhap (
    maPN       INT AUTO_INCREMENT PRIMARY KEY,
    ngayNhap   DATE         NOT NULL,
    maNCC      INT,
    maTK       INT          NOT NULL,  -- nhân viên lập phiếu
    ghiChu     VARCHAR(255),
    FOREIGN KEY (maNCC) REFERENCES NhaCungCap(maNCC),
    FOREIGN KEY (maTK)  REFERENCES TaiKhoan(maTK)
);

CREATE TABLE ChiTietPhieuNhap (
    maPN       INT          NOT NULL,
    maSP       INT          NOT NULL,
    soLuong    INT          NOT NULL DEFAULT 1,
    giaNhap    DECIMAL(15,0) NOT NULL DEFAULT 0,
    PRIMARY KEY (maPN, maSP),
    FOREIGN KEY (maPN) REFERENCES PhieuNhap(maPN),
    FOREIGN KEY (maSP) REFERENCES SanPham(maSP)
);

CREATE TABLE IMEIPhieuNhap (
    maPN       INT          NOT NULL,
    imei       VARCHAR(20)  NOT NULL,
    PRIMARY KEY (maPN, imei),
    FOREIGN KEY (maPN)  REFERENCES PhieuNhap(maPN),
    FOREIGN KEY (imei)  REFERENCES IMEI(imei)
);

-- 7. HÓA ĐƠN BÁN HÀNG
CREATE TABLE HoaDon (
    maHD           INT AUTO_INCREMENT PRIMARY KEY,
    ngayLap        DATE         NOT NULL,
    maKH           INT,
    maTK           INT          NOT NULL,  -- nhân viên bán
    hinhThucTT     ENUM('TIEN_MAT','CHUYEN_KHOAN','TRA_GOP') NOT NULL DEFAULT 'TIEN_MAT',
    tongTien       DECIMAL(15,0) NOT NULL DEFAULT 0,
    ghiChu         VARCHAR(255),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maTK) REFERENCES TaiKhoan(maTK)
);

CREATE TABLE ChiTietHoaDon (
    maHD       INT          NOT NULL,
    imei       VARCHAR(20)  NOT NULL,
    maSP       INT          NOT NULL,
    donGia     DECIMAL(15,0) NOT NULL DEFAULT 0,
    PRIMARY KEY (maHD, imei),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    FOREIGN KEY (imei) REFERENCES IMEI(imei),
    FOREIGN KEY (maSP) REFERENCES SanPham(maSP)
);

-- 8. BẢO HÀNH
CREATE TABLE BaoHanh (
    maBH       INT AUTO_INCREMENT PRIMARY KEY,
    imei       VARCHAR(20)  NOT NULL,
    maKH       INT          NOT NULL,
    maTK       INT          NOT NULL,  -- nhân viên kỹ thuật
    ngayNhan   DATE         NOT NULL,
    ngayTra    DATE,
    tinhTrangLoi  TEXT,
    nguyenNhan    VARCHAR(255),
    huongXuLy     ENUM('DANG_XU_LY','DOI_MAY','SUA_XONG','TU_CHOI') NOT NULL DEFAULT 'DANG_XU_LY',
    ghiChu        VARCHAR(255),
    FOREIGN KEY (imei) REFERENCES IMEI(imei),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maTK) REFERENCES TaiKhoan(maTK)
);

-- DỮ LIỆU MẪU

-- Tài khoản (mật khẩu: admin123 / staff123 - lưu plain text cho demo)
INSERT INTO TaiKhoan (tenDangNhap, matKhau, vaiTro, hoTen, sdt) VALUES
('admin',  'admin123',  'ADMIN', 'Nguyễn Quản Trị',   '0901000001'),
('staff1', 'staff123',  'STAFF', 'Trần Nhân Viên',     '0901000002'),
('staff2', 'staff123',  'STAFF', 'Lê Thị Bán Hàng',   '0901000003');

-- Khách hàng
INSERT INTO KhachHang (tenKH, sdt, diaChi, diemTichLuy) VALUES
('Nguyễn Văn An',   '0911111111', 'Quận 1, TP.HCM',    100),
('Trần Thị Bình',   '0922222222', 'Quận 3, TP.HCM',    50),
('Lê Hoàng Cường',  '0933333333', 'Bình Thạnh, TP.HCM', 0);

-- Nhà cung cấp
INSERT INTO NhaCungCap (tenNCC, sdt, diaChi) VALUES
('Công ty TNHH Apple VN',       '02812345678', 'Quận 1, TP.HCM'),
('Samsung Việt Nam',             '02887654321', 'Quận 7, TP.HCM'),
('Xiaomi Việt Nam',              '02899999999', 'Quận 10, TP.HCM');

-- Sản phẩm
INSERT INTO SanPham (tenSP, hangSX, loaiSP, giaBan, moTa, soLuongTon) VALUES
('iPhone 15 Pro Max 256GB',  'Apple',   'Cao cấp',    34990000, 'Chip A17 Pro, camera 48MP', 5),
('Samsung Galaxy S24 Ultra', 'Samsung', 'Cao cấp',    29990000, 'Snapdragon 8 Gen 3',        3),
('Xiaomi 14 Pro',            'Xiaomi',  'Tầm trung',  18990000, 'Snapdragon 8 Gen 3',        7),
('iPhone 15 128GB',          'Apple',   'Tầm trung',  22990000, 'Chip A16, Dynamic Island',  4),
('Samsung Galaxy A55',       'Samsung', 'Phổ thông',   9990000, 'Exynos 1480, AMOLED',       10);

-- IMEI
INSERT INTO IMEI (imei, maSP, trangThai) VALUES
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

-- Phiếu nhập mẫu
INSERT INTO PhieuNhap (ngayNhap, maNCC, maTK, ghiChu) VALUES
('2026-05-01', 1, 1, 'Nhập hàng tháng 5'),
('2026-05-10', 2, 1, 'Bổ sung Samsung');

INSERT INTO ChiTietPhieuNhap (maPN, maSP, soLuong, giaNhap) VALUES
(1, 1, 3, 28000000),
(1, 4, 2, 18000000),
(2, 2, 2, 24000000);

-- Hóa đơn mẫu
INSERT INTO HoaDon (ngayLap, maKH, maTK, hinhThucTT, tongTien) VALUES
('2026-05-15', 1, 2, 'TIEN_MAT',      34990000),
('2026-05-16', 2, 3, 'CHUYEN_KHOAN',  22990000);

INSERT INTO ChiTietHoaDon (maHD, imei, maSP, donGia) VALUES
(1, '352999001111003', 1, 34990000),
(2, '352999004444001', 4, 22990000);

-- Bảo hành mẫu
INSERT INTO BaoHanh (imei, maKH, maTK, ngayNhan, tinhTrangLoi, nguyenNhan, huongXuLy) VALUES
('352999001111003', 1, 2, '2026-05-20', 'Màn hình bị sọc', 'Lỗi phần cứng', 'DANG_XU_LY');