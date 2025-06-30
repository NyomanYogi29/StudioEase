/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import model.Studio;
import DAO.StudioDAO;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author nyoma
 */
public class StudioController {
    private StudioDAO studioDAO;
    
    public StudioController() 
    {
        try
        {
            this.studioDAO = new StudioDAO();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public List<Studio> getAllStudio()
    {
        return studioDAO.getAllStudio();
    }
    
    public boolean insertStudio(Studio studio)
    {
        return studioDAO.insert(studio);
    }
    
    public boolean deleteStudio(String idStudio)
    {
        return studioDAO.delete(idStudio);
    }
    
    public boolean updateStudio(Studio studio) {
        return studioDAO.update(studio);
    }
}
