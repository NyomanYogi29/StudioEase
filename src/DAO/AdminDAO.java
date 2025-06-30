/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import model.Admin;
import config.DBConnection;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import model.Penyewa;

/**
 *
 * @author nyoma
 */
public class AdminDAO {
    private final Connection connection;
    
    public AdminDAO() throws SQLException {
        connection = DBConnection.getConnection();
    }
    
    public List<Admin> getAllAdmin() {
        List<Admin> list = new ArrayList<>();
        String sql = "SELECT * FROM admin";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Admin a = new Admin(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                list.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public boolean insertAdmin(Admin a) {
        String sql = "INSERT INTO penyewa (id, name, email, password) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, a.getId());
            stmt.setString(2, a.getName());
            stmt.setString(3, a.getEmail());
            stmt.setString(4, a.getPassword());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateAdmin(Admin a) {
        String sql = "UPDATE admin SET name = ?, email = ?, password = ?, WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, a.getName());
            stmt.setString(1, a.getEmail());
            stmt.setString(1, a.getPassword());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAdmin(int id) {
        String sql = "DELETE FROM admin WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
