package Controller;

import DAO.LaporanDAO;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class LaporanController {
    
    private LaporanDAO laporanDAO;

    public LaporanController() {
        try {
            this.laporanDAO = new LaporanDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            // Tampilkan pesan error jika koneksi gagal
        }
    }

    // Filter
    public double getTotalPemasukan(Date start, Date end) { return laporanDAO.getTotalPemasukan(start, end); }
    public int getTotalTransaksi(Date start, Date end) { return laporanDAO.getTotalTransaksi(start, end); }
    public List<String> getStudioTerlarisLeaderboard(Date start, Date end) {
        return laporanDAO.getStudioTerlarisLeaderboard(start, end);
    }
    //public List<String> getAlatTerlaris(Date start, Date end) { return laporanDAO.getAlatTerlarisLeaderboard(start, end); }

    // Semua
    public double getTotalPemasukan() { return laporanDAO.getTotalPemasukan(); }
    public int getTotalTransaksi() { return laporanDAO.getTotalTransaksi(); }
    public List<String> getStudioTerlarisLeaderboard() {
        return laporanDAO.getStudioTerlarisLeaderboard();
    }
    
    // --- TAMBAHKAN METHOD YANG HILANG DI SINI ---
    public List<String> getAlatTerlaris() {
        return laporanDAO.getAlatTerlarisLeaderboard();
    }
}