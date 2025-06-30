// File: DAO/UserDAO.java
package DAO;

import config.DBConnection;
import generator.PenyewaIdGenerator;
import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class UserDAO {

    private final Connection conn;

    public UserDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }
    
    public List<User> getUsersByRole(Role role)
    {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, role.name().toLowerCase());
            
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    User user = new User(
                            rs.getString("id_user"),
                            rs.getString("nama"),
                            rs.getString("email"),
                            "PROTECTED",
                            rs.getString("phone_number"),
                            Role.valueOf(rs.getString("role").toUpperCase())
                    );
                    userList.add(user);
                }
            }
        } catch (SQLException e)
        {
            System.err.println("Error: " + e);
            e.printStackTrace();
        }
        return userList;
    }

    public User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        User user = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String idUser = rs.getString("id_user");
                    String nama = rs.getString("nama");
                    String userEmail = rs.getString("email");
                    String userPass = rs.getString("password");
                    String phone = rs.getString("phone_number");
                    Role role = Role.valueOf(rs.getString("role").toUpperCase());

                    user = new User(idUser, nama, userEmail, userPass, phone, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user; 
    }
    
    public boolean isEmailTaken(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; 
        }
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (id_user, nama, email, password, phone_number, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getIdUser());
            pstmt.setString(2, user.getNama());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword()); 
            pstmt.setString(5, user.getPhoneNumber());
            pstmt.setString(6, user.getRole().name().toLowerCase());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public String getLastUserId()
    {
        String lastId = null;
        String query = "SELECT id_user FROM users ORDER BY id_user DESC LIMIT 1";
        
        try{
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next())
            {
                lastId = rs.getString("id_user");
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
    
    public String generateUserId()
    {
        String lastId = getLastUserId();
        int lastCounter = extractCounter(lastId, "P");
        PenyewaIdGenerator idgenerator = new PenyewaIdGenerator("P", lastCounter, 2);
        return idgenerator.generateNextId();
    }
    
    public boolean deleteUser(String idUser) {
        String sql = "DELETE FROM users WHERE id_user = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idUser);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}