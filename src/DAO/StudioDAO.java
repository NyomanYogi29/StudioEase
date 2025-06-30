/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import model.Studio;
import config.DBConnection;

import java.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Pemesanan;
import model.Penyewa;

/**
 *
 * @author nyoma
 */
public class StudioDAO {
    private final Connection conn;
    
    public StudioDAO() throws SQLException
    {
        this.conn = DBConnection.getConnection();
    }
    
    public List<Studio> getAllStudio() {
        List<Studio> list = new ArrayList<>();
        String sql = "SELECT * FROM studio";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Studio studio = new Studio(
                        rs.getString("id_studio"),
                        rs.getString("fasilitas"),
                        rs.getInt("kapasitas"),
                        rs.getInt("harga_per_jam")
                );
                list.add(studio);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    
    
    public Studio getById(String id) {
        Studio studio = null;
        String sql = "SELECT * FROM studio WHERE id_studio = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    studio = new Studio(
                            rs.getString("id_studio"),
                            rs.getString("fasilitas"),
                            rs.getInt("kapasitas"),
                            rs.getInt("harga_per_jam")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studio;
    }
    
    public boolean insert(Studio s) {
        try {
            String query = "INSERT INTO studio (id_studio, fasilitas, kapasitas, harga_per_jam) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, s.getIdStudio());
            ps.setString(2, s.getFasilitas());
            ps.setInt(3, s.getKapasitas());
            ps.setInt(4, s.getHargaPerJam());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean delete(String idStudio)
    {
        String sql = "DELETE FROM studio WHERE id_studio = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, idStudio);
            return pstmt.executeUpdate() > 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean update(Studio studio) {
        String sql = "UPDATE studio SET fasilitas = ?, kapasitas = ?, harga_per_jam = ? WHERE id_studio = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studio.getFasilitas());
            pstmt.setInt(2, studio.getKapasitas());
            pstmt.setInt(3, studio.getHargaPerJam());
            pstmt.setString(4, studio.getIdStudio()); 
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
