/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nyoma
 */
public class Studio {
    private String idStudio;
    private String fasilitas;
    private int kapasitas;
    private int hargaPerJam;
    
    public Studio(String idStudio, String fasilitas, int kapasitas, int hargaPerJam)
    {
        this.idStudio = idStudio;
        this.fasilitas = fasilitas;
        this.kapasitas = kapasitas;
        this.hargaPerJam = hargaPerJam;
    }
    
    public String getIdStudio()
    {
        return idStudio;
    }
    
    public void setIdStudio(String idStudio)
    {
        this.idStudio = idStudio;
    }
    
    public String getFasilitas()
    {
        return fasilitas;
    }
    
    public void setFasilitas(String fasilitas)
    {
        this.fasilitas = fasilitas;
    }
    
    public int getKapasitas()
    {
        return kapasitas;
    }
    
    public void setKapasitas(int kapasitas)
    {
        this.kapasitas = kapasitas;
    }
    
    public int getHargaPerJam()
    {
        return hargaPerJam;
    }
    
    public void setHargaPerJam(int hargaPerJam)
    {
        this.hargaPerJam = hargaPerJam;
    }
}
