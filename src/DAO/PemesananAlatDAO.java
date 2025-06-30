/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import model.PemesananAlat;
import config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author nyoma
 */
public class PemesananAlatDAO {
     public static void insert(PemesananAlat pa) {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "INSERT INTO pemesanan_alat (id_pemesanan, id_alat, jumlah) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, pa.getIdPemesanan());
            ps.setString(2, pa.getIdAlat());
            ps.setInt(3, pa.getJumlah());
            ps.executeUpdate();

            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
