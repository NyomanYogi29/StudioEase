/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import model.Pemesanan;
import model.AlatMusik;
import config.DBConnection;
import DataTransferObject.PemesananDetailDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.sql.Timestamp;

/**
 *
 * @author nyoma
 */
public class PemesananDAO {
    private final Connection conn;
    
    public PemesananDAO() throws SQLException
    {
        this.conn = DBConnection.getConnection();
    }
    
    public int insert(Pemesanan p, Connection conn) throws SQLException {
        String query = "INSERT INTO pemesanan (id_user, id_studio, waktu_mulai, waktu_selesai, total_harga, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getIdPenyewa());
            ps.setString(2, p.getIdStudio());
            ps.setObject(3, p.getWaktuMulai());
            ps.setObject(4, p.getWaktuSelesai());

            ps.setInt(5, p.getTotalHarga());
            ps.setString(6, p.getStatus()); 

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); 
                    }
                }
            }
        }
        return -1; 
    }
    
    public int getLastPemesananIdFromDB() {
        int lastId = 0;
        String query = "SELECT MAX(id_pemesanan) AS max_id FROM pemesanan";
        
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                lastId = rs.getInt("max_id"); 
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return lastId;
    }
    
    public int generateNextPemesananId() {
        int lastId = getLastPemesananIdFromDB();
        return lastId + 1;
    }
    
    // Dengan Join
    
    public List<PemesananDetailDTO> getAllPemesananWithDetails() {
        List<PemesananDetailDTO> listPemesananDetail = new ArrayList<>();
        String sql = "SELECT "
                + "p.id_pemesanan, "
                + "p.waktu_mulai, "      
                + "p.waktu_selesai, "    
                + "p.total_harga, "
                + "u.nama AS nama_penyewa, "
                + "s.kapasitas AS kapasitas_studio " 
                + "FROM pemesanan p " 
                + "JOIN users u ON p.id_user = u.id_user " 
                + "JOIN studio s ON p.id_studio = s.id_studio " 
                + "ORDER BY p.id_pemesanan DESC;";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                PemesananDetailDTO detail = new PemesananDetailDTO(
                        rs.getInt("id_pemesanan"), 
                        rs.getObject("waktu_mulai", LocalDateTime.class),  
                        rs.getObject("waktu_selesai", LocalDateTime.class),
                        rs.getInt("total_harga"),
                        rs.getString("nama_penyewa"),
                        rs.getString("email_penyewa"),
                        rs.getString("no_hp_penyewa"),
                        rs.getString("id_studio"),
                        rs.getString("fasilitas_studio"),
                        rs.getInt("kapasitas_studio")
                );
                listPemesananDetail.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listPemesananDetail;
    }
    
    
   public PemesananDetailDTO getPemesananDetailById(int idPemesanan) { 
        PemesananDetailDTO detail = null;
        String sql = "SELECT "
                + "p.id_pemesanan, "
                + "p.waktu_mulai, "      
                + "p.waktu_selesai, "
                + "p.total_harga, "
                // ... sisa kolom lain ...
                + "s.kapasitas AS kapasitas_studio " 
                + "FROM pemesanan p " 
                + "JOIN users u ON p.id_user = u.id_user " 
                + "JOIN studio s ON p.id_studio = s.id_studio " 
                + "WHERE p.id_pemesanan = ?"; 

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPemesanan); 
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    detail = new PemesananDetailDTO(
                            rs.getInt("id_pemesanan"), 
                            rs.getObject("waktu_mulai", LocalDateTime.class),
                            rs.getObject("waktu_selesai", LocalDateTime.class),
                            rs.getInt("total_harga"),
                            rs.getString("nama_penyewa"),
                            rs.getString("email_penyewa"),
                            rs.getString("no_hp_penyewa"),
                            rs.getString("id_studio"),
                            rs.getString("fasilitas_studio"),
                            rs.getInt("kapasitas_studio")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detail;
    }
   
    public List<Pemesanan> getAllPemesananWithPenyewa()
    {
        List<Pemesanan> listPemesanan = new ArrayList<>();
        String sql = "SELECT "
                + "p.id_pemesanan, "
                + "p.id_user,"
                + "u.nama AS nama_penyewa, "
                + "p.id_studio, "
                + "p.waktu_mulai, "      
                + "p.waktu_selesai, "    
                + "p.total_harga, "
                + "p.status " 
                + "FROM pemesanan p " 
                + "JOIN users u ON p.id_user = u.id_user " 
                + "ORDER BY p.waktu_mulai DESC"; // Urutkan berdasarkan waktu_mulai
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                Pemesanan p = new Pemesanan(
                        rs.getInt("id_pemesanan"),
                        rs.getString("id_user"),
                        rs.getString("id_studio"),
                        rs.getObject("waktu_mulai", LocalDateTime.class), // <-- GANTI
                        rs.getObject("waktu_selesai", LocalDateTime.class),// <-- GANTI
                        rs.getInt("total_harga"),
                        rs.getString("status"),
                        rs.getString("nama_penyewa")
                );
                listPemesanan.add(p);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return listPemesanan;
    }
    
    public boolean updateStatus(int idPemesanan, String newStatus)
    {
        String sql = "UPDATE pemesanan SET status = ? WHERE id_pemesanan = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, idPemesanan);
            return pstmt.executeUpdate() > 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    public void delete(int idPemesanan) throws SQLException
    {
        Connection conn = null;
        PreparedStatement psDetail = null;
        PreparedStatement psPemesanan = null;

        String deleteDetailSQL = "DELETE FROM detail_pemesanan_alat WHERE id_pemesanan = ?";
        String deletePemesananSQL = "DELETE FROM pemesanan WHERE id_pemesanan = ?";

        try {
            conn = DBConnection.getConnection(); 
            conn.setAutoCommit(false);
            
            psDetail = conn.prepareStatement(deleteDetailSQL);
            psDetail.setInt(1, idPemesanan);
            psDetail.executeUpdate();
            
            psPemesanan = conn.prepareStatement(deletePemesananSQL);
            psPemesanan.setInt(1, idPemesanan);
            psPemesanan.executeUpdate();

            conn.commit(); 

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); 
            }
            throw e; 
        } finally {
            if (psDetail != null) psDetail.close();
            if (psPemesanan != null) psPemesanan.close();
            if (conn != null) {
                conn.setAutoCommit(true); 
                conn.close();
            }
        }
        
    }
    
    public void addDetailAlat(int idPemesanan, List<AlatMusik> idAlatList, Connection conn) throws SQLException {
        String sql = "INSERT INTO detail_pemesanan_alat (id_pemesanan, id_alat) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (AlatMusik alat : idAlatList) {
                pstmt.setInt(1, idPemesanan);
                pstmt.setString(2, alat.getIdAlat()); 
                pstmt.addBatch(); 
            }
            pstmt.executeBatch(); 
        }
    }
    
    public List<PemesananDetailDTO> getPemesananByPenyewaId(String idUser) {
        List<PemesananDetailDTO> listPemesanan = new ArrayList<>();
        String sql = "SELECT p.id_pemesanan, "
                + "p.waktu_mulai, " 
                + "p.waktu_selesai, " 
                + "p.total_harga, u.nama AS nama_penyewa, u.email AS email_penyewa, "
                + "u.phone_number AS no_hp_penyewa, s.id_studio, "
                + "s.fasilitas AS fasilitas_studio, s.kapasitas AS kapasitas_studio "
                + "FROM pemesanan p "
                + "JOIN users u ON p.id_user = u.id_user "
                + "JOIN studio s ON p.id_studio = s.id_studio "
                + "WHERE p.id_user = ? "
                + "ORDER BY p.waktu_mulai DESC;"; // Urutkan berdasarkan waktu_mulai

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idUser);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PemesananDetailDTO detail = new PemesananDetailDTO(
                        rs.getInt("id_pemesanan"), 
                        rs.getObject("waktu_mulai", LocalDateTime.class), 
                        rs.getObject("waktu_selesai", LocalDateTime.class),
                        rs.getInt("total_harga"), 
                        rs.getString("nama_penyewa"),
                        rs.getString("email_penyewa"), 
                        rs.getString("no_hp_penyewa"),
                        rs.getString("id_studio"), 
                        rs.getString("fasilitas_studio"),
                        rs.getInt("kapasitas_studio")
                    );
                    listPemesanan.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listPemesanan;
    }
    
    public boolean isStudioAvailable(String idStudio, LocalDateTime waktuMulai, LocalDateTime waktuSelesai) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pemesanan WHERE id_studio = ? AND (waktu_mulai < ? AND waktu_selesai > ?)";
//            String sql= "SELECT COUNT(*) FROM pemesanan WHERE id_studio = ? AND status = 'Dipesan' "
//                    + "AND ((waktu_mulai < ? AND waktu_selesai > ?) OR (waktu_mulai >= ? AND waktu_mulai < ?))";
        // SELECT COUNT(*) FROM jadwal_sewa WHERE id_ruangan = ? AND tanggal = ? "
            // + "AND ((jam_mulai < ? AND jam_selesai > ?) OR (jam_mulai >= ? AND jam_mulai < ?))
            //SELECT COUNT(*) FROM pemesanan WHERE id_studio = ? AND status = 'Dipesan' AND "
              //  + "((waktu_mulai < ? AND waktu_selesai> ?) OR (waktu_mulai >= ? AND waktu_selesai< ?))
        try (PreparedStatement ps = this.conn.prepareStatement(sql)) { 
            ps.setString(1, idStudio);
            ps.setObject(2, waktuSelesai);
            ps.setObject(3, waktuMulai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false; 
    }
    
}
