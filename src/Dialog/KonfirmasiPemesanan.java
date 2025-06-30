/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Dialog;

import Controller.PemesananController;
import generator.PemesananIdGenerator;
import model.*;
import GUI.UserDashboardGUI;

import javax.swing.*;
import java.time.LocalDateTime; 
import java.time.Duration;      
import java.time.format.DateTimeFormatter; 
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import java.awt.Color;
import java.text.SimpleDateFormat;
/**
 *
 * @author nyoma
 */
public class KonfirmasiPemesanan extends javax.swing.JDialog {
    
    private final UserDashboardGUI parentFrame;
    
    private User user;
    private Studio studio;
    private LocalDateTime waktuMulai;
    private LocalDateTime waktuSelesai;
    private double totalHarga;
    private List<AlatMusik> alatDipilih;
    
    private boolean isPemesananBerhasil = false;
    
    /**
     * Creates new form KonfirmasiPemesanan
     */
    
    
    
    public KonfirmasiPemesanan(UserDashboardGUI parent, boolean modal, User user, Studio studio, 
                               LocalDateTime waktuMulai, LocalDateTime waktuSelesai,
                               List<AlatMusik> alatDipilih, double totalHarga) {
        super(parent, modal);
        this.parentFrame = parent;
        initComponents();
        setLocationRelativeTo(parent);
        
        this.user = user;
        this.studio = studio;
        this.waktuMulai = waktuMulai; 
        this.waktuSelesai = waktuSelesai; 
        this.totalHarga = totalHarga;
        this.alatDipilih = alatDipilih;
        
        long durasiInHours = Duration.between(waktuMulai, waktuSelesai).toHours();
        
         namaTextField.setText(user.getNama());
        studioTextField.setText(studio.getIdStudio());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dateTextField.setText(waktuMulai.format(dateFormatter));
        durasiTextField.setText(durasiInHours + " jam");
        
        tampilkanRincianBiaya(studio, alatDipilih, totalHarga);
     
        
    }
    
    public boolean isBerhasil()
    {
        return this.isPemesananBerhasil;
    }
    
    
    
    private void tampilkanRincianBiaya(Studio studio, List<AlatMusik> alatDipilih, double totalHarga) {
        panelRincianBiaya.removeAll();
        panelRincianBiaya.setLayout(new BoxLayout(panelRincianBiaya, BoxLayout.Y_AXIS));

        // Kalkulasi durasi dari field class
        long durasi = Duration.between(this.waktuMulai, this.waktuSelesai).toHours();
        
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        JLabel labelNama = new JLabel("Nama Penyewa: " + user.getNama());
        labelNama.setForeground(Color.WHITE); 
        panelRincianBiaya.add(labelNama);

        JLabel labelStudioInfo = new JLabel("Studio: " + studio.getIdStudio());
        labelStudioInfo.setForeground(Color.WHITE);
        panelRincianBiaya.add(labelStudioInfo);

        // Tampilkan waktu menggunakan formatter baru
        JLabel labelTanggal = new JLabel("Tanggal: " + this.waktuMulai.format(dateFormatter));
        labelTanggal.setForeground(Color.WHITE);
        panelRincianBiaya.add(labelTanggal);

        JLabel labelWaktu = new JLabel("Waktu: " + this.waktuMulai.format(timeFormatter) + " - " + this.waktuSelesai.format(timeFormatter) + " (Durasi " + durasi + " jam)");
        labelWaktu.setForeground(Color.WHITE);
        panelRincianBiaya.add(labelWaktu);

        panelRincianBiaya.add(Box.createVerticalStrut(15)); 

        JLabel labelHeaderRincian = new JLabel("<html><font color='white'><b>RINCIAN BIAYA:</b></font></html>");
        panelRincianBiaya.add(labelHeaderRincian);
        panelRincianBiaya.add(new JSeparator());
        panelRincianBiaya.add(Box.createVerticalStrut(5));

        double hargaSewaStudio = studio.getHargaPerJam() * durasi;
        JLabel labelSewaStudio = new JLabel(
                "Sewa Studio: Rp " + formatter.format(studio.getHargaPerJam()) + " x " + durasi + " jam = Rp " + formatter.format(hargaSewaStudio)
        );
        labelSewaStudio.setForeground(Color.WHITE);
        panelRincianBiaya.add(labelSewaStudio);

        JLabel labelHeaderAlat = new JLabel("Alat Tambahan:");
        labelHeaderAlat.setForeground(Color.WHITE);
        panelRincianBiaya.add(labelHeaderAlat);

        if (alatDipilih == null || alatDipilih.isEmpty()) {
            JLabel labelTidakAdaAlat = new JLabel("  - Tidak ada");
            labelTidakAdaAlat.setForeground(Color.WHITE);
            panelRincianBiaya.add(labelTidakAdaAlat);
        } else {
            for (AlatMusik alat : alatDipilih) {
                double hargaSewaAlat = alat.getHargaPerJam() * durasi;
                JLabel labelAlat = new JLabel(
                        "  - " + alat.getNamaAlat() + ": Rp " + formatter.format(alat.getHargaPerJam()) + " x " + durasi + " jam = Rp " + formatter.format(hargaSewaAlat)
                );
                labelAlat.setForeground(Color.WHITE);
                panelRincianBiaya.add(labelAlat);
            }
        }

        panelRincianBiaya.add(Box.createVerticalStrut(10));
        panelRincianBiaya.add(new JSeparator());
        panelRincianBiaya.add(Box.createVerticalStrut(10));

        JLabel labelTotal = new JLabel(
                "<html><h3><font color='white'>TOTAL HARGA: Rp " + formatter.format(totalHarga) + "</font></h3></html>"
        );
        panelRincianBiaya.add(labelTotal);

        panelRincianBiaya.revalidate();
        panelRincianBiaya.repaint();
    }
                                             

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        studioTextField = new javax.swing.JTextField();
        durasiTextField = new javax.swing.JTextField();
        namaTextField = new javax.swing.JTextField();
        dateTextField = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        bayarButton = new javax.swing.JButton();
        batalButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        panelRincianBiaya = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("KONFIRMASI DETAIL PEMESANAN");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Nama:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Studio:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Tanggal: ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Durasi:");

        namaTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaTextFieldActionPerformed(evt);
            }
        });

        jPanel11.setBackground(new java.awt.Color(204, 0, 204));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        jPanel10.setBackground(new java.awt.Color(153, 0, 204));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(102, 0, 153));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        jPanel8.setBackground(new java.awt.Color(102, 0, 204));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        bayarButton.setBackground(new java.awt.Color(0, 0, 0));
        bayarButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bayarButton.setForeground(new java.awt.Color(255, 255, 255));
        bayarButton.setText("BAYAR SEKARANG");
        bayarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarButtonActionPerformed(evt);
            }
        });

        batalButton.setBackground(new java.awt.Color(0, 0, 0));
        batalButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        batalButton.setForeground(new java.awt.Color(255, 255, 255));
        batalButton.setText("BATAL");
        batalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalButtonActionPerformed(evt);
            }
        });

        panelRincianBiaya.setBackground(new java.awt.Color(51, 51, 51));
        panelRincianBiaya.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelRincianBiayaLayout = new javax.swing.GroupLayout(panelRincianBiaya);
        panelRincianBiaya.setLayout(panelRincianBiayaLayout);
        panelRincianBiayaLayout.setHorizontalGroup(
            panelRincianBiayaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );
        panelRincianBiayaLayout.setVerticalGroup(
            panelRincianBiayaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 166, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(panelRincianBiaya);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(durasiTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(studioTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(namaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(59, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(bayarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(batalButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(namaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(studioTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(dateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(durasiTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bayarButton)
                    .addComponent(batalButton))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void namaTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaTextFieldActionPerformed

    private void batalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_batalButtonActionPerformed

    private void bayarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarButtonActionPerformed
        // TODO add your handling code here:
        String input = JOptionPane.showInputDialog(this, "Total Tagihan: Rp " + String.format("%,.0f", totalHarga) + "\n\nMasukkan nominal pembayaran:", "Pembayaran", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;
        
        try {
            double amount = Double.parseDouble(input);
            if (amount < totalHarga) {
                JOptionPane.showMessageDialog(this, "Jumlah pembayaran yang diinupt kurang!", "Pembayaran Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Pemesanan pemesanan = new Pemesanan();
            pemesanan.setIdPenyewa(user.getIdUser());
            pemesanan.setIdStudio(studio.getIdStudio());
            pemesanan.setWaktuMulai(this.waktuMulai); 
            pemesanan.setWaktuSelesai(this.waktuSelesai); 
            pemesanan.setTotalHarga((int) totalHarga);
            pemesanan.setStatus("Dipesan");
            pemesanan.setNamaPenyewa(user.getNama());
            PemesananController pemesananController = new PemesananController();
            boolean sukses = pemesananController.buatPemesananDenganAlat(pemesanan, this.alatDipilih);
            
            if (sukses) {
                double kembalian = amount - totalHarga;
                JOptionPane.showMessageDialog(this, "Pembayaran dan pemesanan berhasil!\nKembalian Anda: Rp " + String.format("%,.0f", kembalian), "Sukses", JOptionPane.INFORMATION_MESSAGE);
                this.isPemesananBerhasil = true;
                this.dispose(); 
            } 
            else
            {
                 this.dispose();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input pembayaran harus berupa angka!", "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan sistem: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    
    }//GEN-LAST:event_bayarButtonActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(KonfirmasiPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(KonfirmasiPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(KonfirmasiPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(KonfirmasiPemesanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                KonfirmasiPemesanan dialog = new KonfirmasiPemesanan(new UserDashboardGUI(), true, user, studio, date, List<AlatMusik> alatPilihan, jam_mulai, durasi, totalHarga);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton batalButton;
    private javax.swing.JButton bayarButton;
    private javax.swing.JTextField dateTextField;
    private javax.swing.JTextField durasiTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField namaTextField;
    private javax.swing.JPanel panelRincianBiaya;
    private javax.swing.JTextField studioTextField;
    // End of variables declaration//GEN-END:variables
}
