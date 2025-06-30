/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import model.Penyewa;
import config.DBConnection;
import generator.PenyewaIdGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nyoma
 */
public class PenyewaDAO {
    private final Connection connection;

    public PenyewaDAO() throws SQLException {
        connection = DBConnection.getConnection();
    }

    public List<Penyewa> getAllPenyewa() {
        List<Penyewa> list = new ArrayList<>();
        String sql = "SELECT * FROM penyewa";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Penyewa p = new Penyewa(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("no_hp")
                );
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insertPenyewa(Penyewa p) {
        String sql = "INSERT INTO penyewa (id_penyewa, nama, email, password, no_hp) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getId());
            stmt.setString(2, p.getNama());
            stmt.setString(3, p.getEmail());
            stmt.setString(4, p.getPassword());
            stmt.setString(5, p.getPhoneNumber());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePenyewa(Penyewa p) {
        String sql = "UPDATE penyewa SET nama = ?, no_telepon = ?, email = ?, password = ?, WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getNama());
            stmt.setString(2, p.getPhoneNumber());
            stmt.setString(3, p.getEmail());
            stmt.setString(4, p.getId());
            stmt.setString(5, p.getPassword());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePenyewa(String id) {
        String sql = "DELETE FROM penyewa WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void insert(Penyewa penyewa) {
        try {
            Connection connection = DBConnection.getConnection();
            String query = "INSERT INTO penyewa (id_penyewa, nama, email, password, no_hp) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, penyewa.getId());
            ps.setString(2, penyewa.getNama());
            ps.setString(3, penyewa.getEmail());
            ps.setString(4, penyewa.getPassword());
            ps.setString(5, penyewa.getPhoneNumber());
            ps.executeUpdate();
            ps.close(); connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   
    
    public String getLastPenyewaId()
    {
        String lastId = null;
        String query = "SELECT id_penyewa FROM penyewa ORDER BY id_penyewa DESC LIMIT 1";
        
        try{
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next())
            {
                lastId = rs.getString("id_penyewa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return lastId;
    }
    
    public int extractCounter(String lastId, String prefix)
    {
        if (lastId != null && lastId.startsWith(prefix))
        {
            return Integer.parseInt(lastId.substring(prefix.length()));
        }
        return 0;
    }
    
    public String generatePenyewaId()
    {
        String lastId = getLastPenyewaId();
        int lastCounter = extractCounter(lastId, "P");
        PenyewaIdGenerator idgenerator = new PenyewaIdGenerator("P", lastCounter, 2);
        return idgenerator.generateNextId();
    }
}
