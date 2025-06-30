/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nyoma
 */
public class PemesananAlat {
    private String idPemesanan;
    private String idAlat;
    private int jumlah;

    public PemesananAlat(String idPemesanan, String idAlat, int jumlah) {
        this.idPemesanan = idPemesanan;
        this.idAlat = idAlat;
        this.jumlah = jumlah;
    }

    public String getIdPemesanan() { return idPemesanan; }
    public String getIdAlat() { return idAlat; }
    public int getJumlah() { return jumlah; }

    public void setIdPemesanan(String idPemesanan) {
        this.idPemesanan = idPemesanan;
    }
}
