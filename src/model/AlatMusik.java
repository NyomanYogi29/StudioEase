/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nyoma
 */
public class AlatMusik {
    private String idAlat;
    private String namaAlat;
    private int hargaPerJam;

    public AlatMusik(String idAlat, String namaAlat, int hargaPerJam) {
        this.idAlat = idAlat;
        this.namaAlat = namaAlat;
        this.hargaPerJam = hargaPerJam;
    }

    public String getIdAlat() {
        return idAlat;
    }

    public String getNamaAlat() {
        return namaAlat;
    }

    public int getHargaPerJam() {
        return hargaPerJam;
    }
}
