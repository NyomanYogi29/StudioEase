/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import DAO.PemesananDAO;
import config.DBConnection;
import model.Pemesanan;
import model.AlatMusik;
import DataTransferObject.PemesananDetailDTO;

import java.util.List;
import java.sql.*;
import javax.swing.*;

/**
 *
 * @author nyoma
 */
public class PemesananController {
    private PemesananDAO pemesananDAO;
    
    public PemesananController()
    {
        try
        {
            this.pemesananDAO = new PemesananDAO();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean buatPemesananDenganAlat(Pemesanan pemesanan, List<AlatMusik> alatList) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            int idPemesananBaru = pemesananDAO.insert(pemesanan, conn);
            if (idPemesananBaru == -1) {
                throw new SQLException("Gagal membuat pemesanan utama, ID tidak didapatkan.");
            }
            if (alatList != null && !alatList.isEmpty()) {
                pemesananDAO.addDetailAlat(idPemesananBaru, alatList, conn);
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    
    
    
    public List<PemesananDetailDTO> getAllPemesananDetails()
    {
        return pemesananDAO.getAllPemesananWithDetails();
    }
    
    public PemesananDetailDTO getPemesananDetail(int idPemesanan)
    {
        return pemesananDAO.getPemesananDetailById(idPemesanan);
    }
    
    // FOR ADMIN
    public List<Pemesanan> getAllPemesananForAdmin()
    {
        return pemesananDAO.getAllPemesananWithPenyewa();
    }
    
    public boolean updatePemesananStatus(int idPemesanan, String newStatus)
    {
        return pemesananDAO.updateStatus(idPemesanan, newStatus);
    }
    
    public void deletePemesanan(int idPemesanan) throws SQLException
    {
        pemesananDAO.delete(idPemesanan);
    }
    
    public List<PemesananDetailDTO> getRiwayatPemesananByPenyewa(String idUser) {
        return pemesananDAO.getPemesananByPenyewaId(idUser);
    }
}
