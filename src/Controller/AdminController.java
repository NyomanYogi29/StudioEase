/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import DAO.AdminDAO;
import model.Admin;

import java.util.List;
import java.sql.*;

/**
 *
 * @author nyoma
 */
public class AdminController {
    private AdminDAO adminDAO;
    
    public AdminController()
    {
        try {
            adminDAO = new AdminDAO();
        } 
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public List<Admin> getAllAdmin()
    {
        return adminDAO.getAllAdmin();
    }
    
    public boolean addAdmin(Admin admin)
    {
        return adminDAO.insertAdmin(admin);
    }
    
    public boolean updateAdmin(Admin admin)
    {
        return adminDAO.updateAdmin(admin);
    }
    
    public boolean deleteAdmin(int id)
    {
        return adminDAO.deleteAdmin(id);
    }
}
