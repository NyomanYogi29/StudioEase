// REVISI UNTUK PemesananDetailDTO.java

package DataTransferObject; 

import java.time.LocalDateTime; // GANTI import java.util.Date;

public class PemesananDetailDTO {
    private int idPemesanan;
    private int totalHarga;

    private String namaPenyewa;
    private String emailPenyewa;
    private String noHpPenyewa;

    private String idStudio;
    private String fasilitasStudio;
    private int kapasitasStudio;
    
    private LocalDateTime waktuMulai;
    private LocalDateTime waktuSelesai;
    
    // Sesuaikan Constructor
    public PemesananDetailDTO(int idPemesanan, LocalDateTime waktuMulai, LocalDateTime waktuSelesai, int totalHarga, // <-- Perubahan
                              String namaPenyewa, String emailPenyewa, String noHpPenyewa,
                              String idStudio, String fasilitasStudio, int kapasitasStudio) {
        this.idPemesanan = idPemesanan;
        this.waktuMulai = waktuMulai; 
        this.waktuSelesai = waktuSelesai; 
        this.totalHarga = totalHarga;
        this.namaPenyewa = namaPenyewa;
        this.emailPenyewa = emailPenyewa;
        this.noHpPenyewa = noHpPenyewa;
        this.idStudio = idStudio;
        this.fasilitasStudio = fasilitasStudio;
        this.kapasitasStudio = kapasitasStudio;
    }

    // --- Getter dan Setter ---
    public int getIdPemesanan() { return idPemesanan; }

    public int getTotalHarga() {
        return totalHarga;
    }

    public String getNamaPenyewa() {
        return namaPenyewa;
    }

    public String getEmailPenyewa() {
        return emailPenyewa;
    }

    public String getNoHpPenyewa() {
        return noHpPenyewa;
    }

    public String getNamaStudio() {
        return idStudio;
    }

    public String getFasilitasStudio() {
        return fasilitasStudio;
    }

    public int getKapasitasStudio() {
        return kapasitasStudio;
    }
    
    public LocalDateTime getWaktuMulai() { return waktuMulai; }
    public LocalDateTime getWaktuSelesai() { return waktuSelesai; }
    
    @Override
    public String toString() {
        // Anda bisa sesuaikan ini jika perlu
        return "PemesananDetailDTO{" +
                "idPemesanan='" + idPemesanan + '\'' +
                ", waktuMulai=" + waktuMulai + 
                ", waktuSelesai=" + waktuSelesai + 
                ", totalHarga=" + totalHarga +
                ", namaPenyewa='" + namaPenyewa + '\'' +
                ", emailPenyewa='" + emailPenyewa + '\'' +
                ", noHpPenyewa='" + noHpPenyewa + '\'' +
                ", idStudio='" + idStudio + '\'' +
                ", fasilitasStudio='" + fasilitasStudio + '\'' +
                ", kapasitasStudio=" + kapasitasStudio +
                '}';
    }
}