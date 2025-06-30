package DAO;

import model.AlatMusik;
import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AlatMusikDAO {
    // Setiap objek AlatMusikDAO akan memiliki satu koneksi
    private final Connection conn;

    // Constructor untuk membuat koneksi saat objek dibuat
    public AlatMusikDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    // Method non-static untuk mengambil semua alat musik
    public List<AlatMusik> getAll() {
        List<AlatMusik> list = new ArrayList<>();
        String query = "SELECT * FROM alat_musik";
        
        // Menggunakan try-with-resources untuk Statement dan ResultSet
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                list.add(new AlatMusik(
                    rs.getString("id_alat"),
                    rs.getString("nama_alat"),
                    rs.getInt("harga_per_jam")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public AlatMusik getByNama(String nama) {
        AlatMusik alatMusik = null;
        String query = "SELECT * FROM alat_musik WHERE nama_alat = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nama);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    alatMusik = new AlatMusik(
                        rs.getString("id_alat"),
                        rs.getString("nama_alat"),
                        rs.getInt("harga_per_jam")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alatMusik;
    }
}