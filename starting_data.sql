-- Data awal yang sudah dibersihkan dan kompatibel dengan H2

INSERT INTO `alat_musik` (`id_alat`, `nama_alat`, `harga_per_jam`) VALUES
('A001', 'Gitar', 10000),
('A002', 'Drum', 20000),
('A003', 'Keyboard', 15000),
('A004', 'Microphone', 5000);

INSERT INTO `users` (`id_user`, `nama`, `email`, `password`, `phone_number`, `role`) VALUES
('ADMIN01', 'Nyoman Yogi', 'yogi@email.com', 'fc1ee982bf3c48bfb066b68d7de2fd837effd0f50188d3dbbdcab7ed89c3097a', '085157580906', 'admin'),
('P01', 'Arya Adi', 'adi@email.com', '14f95c389a79b74dcca86973a9be0810357dbf5c4043566123cef1f7a21f4ad5', '08123456789', 'penyewa'),
('P02', 'a', 'a', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', '08123456789', 'penyewa'),
('P03', 'Nyoman', 'nyoman@email.com', 'e36f59a8540576fddee77507f2a838b82322bb3ed918c56aad54db249d1479b5', '08764531233', 'penyewa');

INSERT INTO `studio` (`id_studio`, `fasilitas`, `kapasitas`, `harga_per_jam`) VALUES
('S001', 'AC, Soundproof, MiniBar', 5, 100000),
('S003', 'AC, Proyektor, Meja', 7, 92000),
('S004', 'AC, WI-FI, Proyektor, ', 5, 100000),
('STD001', 'AC, Wi-Fi, Cermin Dinding Penuh, Sound System', 15, 150000),
('STD002', 'AC, Wi-Fi, Proyektor, Papan Tulis', 25, 200000),
('STD003', 'AC, Wi-Fi, Sound System Profesional, Panggung Kecil', 30, 250000),
('STD004', 'AC, Wi-Fi, Cermin', 10, 100000),
('STD005', 'Wi-Fi, Meja dan Kursi, Sound System', 20, 175000);

-- Data pemesanan di bawah ini bersifat opsional, bisa Anda masukkan jika ingin ada riwayat awal
-- Jika ingin memulai dengan data pemesanan kosong, Anda bisa menghapus bagian INSERT INTO pemesanan dan detail_pemesanan_alat

INSERT INTO `pemesanan` (`id_pemesanan`, `id_user`, `id_studio`, `waktu_mulai`, `waktu_selesai`, `total_harga`, `status`) VALUES
(1, 'P01', 'S001', '2025-06-18 08:00:00', '2025-06-18 13:00:00', 750000, 'Selesai'),
(2, 'P01', 'STD003', '2025-06-26 11:00:00', '2025-06-26 13:00:00', 600000, 'Selesai'),
(3, 'P02', 'STD003', '2025-06-26 10:00:00', '2025-06-26 13:00:00', 840000, 'Dipesan'),
(6, 'P02', 'STD002', '2025-06-26 11:00:00', '2025-06-26 15:00:00', 960000, 'Dipesan'),
(9, 'P02', 'S003', '2025-06-26 11:00:00', '2025-06-26 15:00:00', 520000, 'Dipesan');

INSERT INTO `detail_pemesanan_alat` (`id_detail`, `id_pemesanan`, `id_alat`) VALUES
(1, 9, 'A002'),
(2, 9, 'A003'),
(3, 9, 'A004');