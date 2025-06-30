/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author nyoma
 */
public class Pemesanan {
    private int idPemesanan;
    private String idPenyewa;
    private String idStudio;
    private LocalDateTime waktuMulai;
    private LocalDateTime waktuSelesai;
    private int totalHarga;
    private String status;
    private String namaPenyewa;
    
    public Pemesanan(int idPemesanan, String idPenyewa, String idStudio, LocalDateTime waktuMulai, LocalDateTime waktuSelesai, int totalHarga, String status, String namaPenyewa) {
        this.idPemesanan = idPemesanan;
        this.idPenyewa = idPenyewa;
        this.idStudio = idStudio;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
        this.totalHarga = totalHarga;
        this.status = status;
        this.namaPenyewa = namaPenyewa;
    }

    public Pemesanan(int idPemesanan, String idPenyewa, String idStudio, LocalDateTime waktuMulai, LocalDateTime waktuSelesai, int totalHarga) {
        this.idPemesanan = idPemesanan;
        this.idPenyewa = idPenyewa;
        this.idStudio = idStudio;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
        this.totalHarga = totalHarga;
    }
    
    public Pemesanan()
    {
        
    }

    // Getter
    public int getIdPemesanan() { return idPemesanan; }
    public void setIdPemesanan(int idPemesanan) {this.idPemesanan = idPemesanan;}
    public String getIdPenyewa() { return idPenyewa; }
    public void setIdPenyewa(String idPenyewa) {this.idPenyewa = idPenyewa;}
    public String getIdStudio() { return idStudio; }
     public void setIdStudio(String idStudio) {this.idStudio = idStudio;}
    public int getTotalHarga() { return totalHarga; }
     public void setTotalHarga(int totalHarga) {this.totalHarga = totalHarga;}
    
    public LocalDateTime getWaktuMulai() { return waktuMulai; }
    public void setWaktuMulai(LocalDateTime waktuMulai) { this.waktuMulai = waktuMulai; }
    
    public LocalDateTime getWaktuSelesai() { return waktuSelesai; }
    public void setWaktuSelesai(LocalDateTime waktuSelesai) { this.waktuSelesai = waktuSelesai; }
    
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    
    public String getNamaPenyewa() {return namaPenyewa;}
    public void setNamaPenyewa(String namaPenyewa) {this.namaPenyewa = namaPenyewa;}
}
