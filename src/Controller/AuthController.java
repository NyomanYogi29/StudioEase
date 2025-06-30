/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import DAO.AuthDAO;
import model.User;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author nyoma
 */
public class AuthController {
    private AuthDAO authDao;

    public AuthController() {
        try {
             authDao = new AuthDAO();
        } catch (Exception e) {
        }
    }

    public User login(String email, String password) {
        return authDao.login(email, password);
    }


    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("MD5 Algorithm not found", e);
        }
    }
}
