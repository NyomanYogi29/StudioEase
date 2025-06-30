/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import config.DBConnection;
import model.User;
import model.Role;
import DAO.PenyewaDAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author agusseputra
 */
public class AuthDAO {
    private Connection connection;
    public AuthDAO() {
        try
        {
            connection = DBConnection.getConnection();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public User login(String email, String password) {
        try {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id_user"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        "PROTECTED",
                        rs.getString("phone_number"),
                        Role.valueOf(rs.getString("role").toUpperCase())
                );
            }
        } catch (SQLException e) {
            System.err.println("Login gagal: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}