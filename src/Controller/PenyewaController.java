/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import DAO.PenyewaDAO;
import model.Penyewa;

import java.util.List;
import java.sql.*;
/**
 *
 * @author nyoma
 */
public class PenyewaController {
    private PenyewaDAO penyewaDAO;

    public PenyewaController() {
        try{
            penyewaDAO = new PenyewaDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Penyewa> getAllPenyewa() {
        return penyewaDAO.getAllPenyewa();
    }

    public boolean addPenyewa(Penyewa penyewa) {
        return penyewaDAO.insertPenyewa(penyewa);
    }

    public boolean updatePenyewa(Penyewa penyewa) {
        return penyewaDAO.updatePenyewa(penyewa);
    }

    public boolean deletePenyewa(String idPenyewa) {
        return penyewaDAO.deletePenyewa(idPenyewa);
    }
}
