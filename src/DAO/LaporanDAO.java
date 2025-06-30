/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import config.DBConnection;
import java.sql.*;
import java.util.Date; 
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate; 
import java.time.ZoneId;

/**
 *
 * @author nyoma
 */
public class LaporanDAO {
    private final Connection conn;
    
    public LaporanDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }
    
    public double getTotalPemasukan(Date tanggalMulai, Date tanggalSelesai)
    {
        String sql = "SELECT SUM(total_harga) FROM pemesanan WHERE waktu_mulai >= ? AND waktu_mulai < ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            LocalDate endLocalDate = tanggalSelesai.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
            
            pstmt.setTimestamp(1, new java.sql.Timestamp(tanggalMulai.getTime()));
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(endLocalDate.atStartOfDay()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
    
     public int getTotalTransaksi(Date tglMulai, Date tglSelesai) {
        String sql = "SELECT COUNT(*) FROM pemesanan WHERE waktu_mulai >= ? AND waktu_mulai < ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            LocalDate endLocalDate = tglSelesai.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);

            pstmt.setTimestamp(1, new java.sql.Timestamp(tglMulai.getTime()));
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(endLocalDate.atStartOfDay()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public List<String> getStudioTerlarisLeaderboard(Date tglMulai, Date tglSelesai) {
        List<String> leaderboard = new ArrayList<>();
        String sql = "SELECT id_studio, COUNT(id_studio) AS jumlah FROM pemesanan WHERE waktu_mulai >= ? AND waktu_mulai < ? GROUP BY id_studio ORDER BY jumlah DESC LIMIT 3";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            LocalDate endLocalDate = tglSelesai.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
            
            pstmt.setTimestamp(1, new java.sql.Timestamp(tglMulai.getTime()));
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(endLocalDate.atStartOfDay()));

            try (ResultSet rs = pstmt.executeQuery()) {
                int rank = 1;
                while (rs.next()) { 
                    String Peringkat = rank + ". " + rs.getString("id_studio") + " (" + rs.getInt("jumlah") + "x)";
                    leaderboard.add(Peringkat);
                    rank++;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return leaderboard;
    }
    
    public double getTotalPemasukan() {
        String sql = "SELECT SUM(total_harga) FROM pemesanan";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getTotalTransaksi() {
        String sql = "SELECT COUNT(*) FROM pemesanan";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public List<String> getStudioTerlarisLeaderboard() {
        List<String> leaderboard = new ArrayList<>();
        String sql = "SELECT id_studio, COUNT(id_studio) AS jumlah FROM pemesanan GROUP BY id_studio ORDER BY jumlah DESC LIMIT 3";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            int rank = 1;
            while (rs.next()) {
                String Peringkat = rank + ". " + rs.getString("id_studio") + " (" + rs.getInt("jumlah") + "x)";
                leaderboard.add(Peringkat);
                rank++;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return leaderboard;
    }
    
    public List<String> getAlatTerlarisLeaderboard() {
        List<String> leaderboard = new ArrayList<>();
        // Query ini tidak memerlukan WHERE clause untuk tanggal
        String sql = "SELECT am.nama_alat, COUNT(dpa.id_alat) AS jumlah " +
                     "FROM detail_pemesanan_alat dpa " +
                     "JOIN alat_musik am ON dpa.id_alat = am.id_alat " +
                     "GROUP BY am.nama_alat " +
                     "ORDER BY jumlah DESC LIMIT 3";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int rank = 1;
            while (rs.next()) {
                String peringkat = rank + ". " + rs.getString("nama_alat") + " (" + rs.getInt("jumlah") + "x disewa)";
                leaderboard.add(peringkat);
                rank++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaderboard;
    }
    
}
