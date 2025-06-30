/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import DAO.StudioDAO;
import model.User;
import DAO.UserDAO;
import java.sql.SQLException;
import java.util.*;
import model.Role;

/**
 *
 * @author nyoma
 */
public class UserController {
    private UserDAO userDAO;
    
    public UserController()
    {
        try
        {
            this.userDAO = new UserDAO();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public List<User> getAllPenyewa()
    {
        if(userDAO != null)
        {
            return userDAO.getUsersByRole(Role.PENYEWA);
        }
        return new ArrayList<>();
    }
    
    public User loginUser(String email, String password)
    {
        return userDAO.loginUser(email, password);
    }
    
    public boolean isEmailTaken(String email)
    {
        return userDAO.isEmailTaken(email);
    }
    
    public boolean registerUser(User user)
    {
        return userDAO.registerUser(user);
    }
    
    public boolean deleteUser(String idUser) {
        if(userDAO != null) {
            return userDAO.deleteUser(idUser);
        }
        return false;
    }
}
