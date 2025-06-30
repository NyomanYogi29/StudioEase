/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;
import Dialog.FormAddStudioGUI;
import Dialog.FormEditStudioGUI;
import Controller.PemesananController;
import Controller.StudioController;
import Controller.LaporanController;
import Controller.UserController;
import model.Pemesanan;
import model.Studio;
import model.User;
import DataTransferObject.PemesananDetailDTO;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.Date;
import javax.swing.JLabel;
import java.sql.*;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author nyoma
 */
public class AdminDashboardGUI extends javax.swing.JFrame {

    /**
     * Creates new form AdminDashboardGUI
     */
    
    private PemesananController pemesananController;
    private DefaultTableModel pemesananTableModel;
    
    private StudioController studioController;
    private DefaultTableModel studioTableModel;
    
    private UserController userController;
    private DefaultTableModel penyewaTableModel;
    
    private LaporanController laporanController;
    
    public AdminDashboardGUI() {
        initComponents();
        
        this.pemesananController = new PemesananController();
        this.studioController = new StudioController();
        this.laporanController = new LaporanController();
        this.userController = new UserController();
        
        setupPemesananTable();
        setupStudioTable();
        setupPenyewaTable();
        tampilkanLaporanSemua();
    }
    
    private void setupPemesananTable()
    {
        String[] columnNames = {"ID Pesan", "Penyewa", "ID Studio", "Waktu Mulai", "Waktu Selesai", "Status"};
        pemesananTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        
        pemesananTable.setModel(pemesananTableModel);
        loadPemesananTable();
    }
    
    private void loadPemesananTable()
    {
        pemesananTableModel.setRowCount(0);
        List<Pemesanan> daftarPemesanan = pemesananController.getAllPemesananForAdmin();
        
        // Buat formatter untuk menampilkan waktu dengan format yang mudah dibaca
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        for (Pemesanan p: daftarPemesanan)
        {
            // Ambil data dari getWaktuMulai() dan getWaktuSelesai()
            Object[] rowData = {
                p.getIdPemesanan(),
                p.getNamaPenyewa(),
                p.getIdStudio(),
                p.getWaktuMulai().format(formatter), 
                p.getWaktuSelesai().format(formatter), 
                p.getStatus()
            };
            pemesananTableModel.addRow(rowData);
        }
    }
    
    private void setupStudioTable()
    {
        String[] columnNames = {"ID Studio", "Fasilitas", "Kapasitas", "Harga per Jam"};
        studioTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        
        studioTable.setModel(studioTableModel);
        loadStudioTable();
        
    }
    
    public void loadStudioTable()
    {
        studioTableModel.setRowCount(0);
        List<Studio> daftarStudio = studioController.getAllStudio();
        
        for (Studio s: daftarStudio)
        {
            Object[] rowData = {
                s.getIdStudio(),
                s.getFasilitas(),
                s.getKapasitas(),
                s.getHargaPerJam()
            };
            studioTableModel.addRow(rowData);
        }
    }
    
    private void setupPenyewaTable() {
        String[] columnNames = {"ID Penyewa", "Nama", "Email", "No. HP"};
        penyewaTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        penyewaTable.setModel(penyewaTableModel); 
        loadPenyewaTable();
    }

    public void loadPenyewaTable() {
        penyewaTableModel.setRowCount(0);
        List<User> daftarPenyewa = userController.getAllPenyewa(); 

        for (User p : daftarPenyewa) {
            Object[] rowData = {
                p.getIdUser(),       
                p.getNama(),     
                p.getEmail(),    
                p.getPhoneNumber() 
            };
            penyewaTableModel.addRow(rowData);
        }
    }
    
    private void tampilkanLaporanSemua() {
        // Panggil DAO melalui controller tanpa filter
        double pemasukan = laporanController.getTotalPemasukan();
        int transaksi = laporanController.getTotalTransaksi();
        List<String> studioLeaderBoard = laporanController.getStudioTerlarisLeaderboard();
        List<String> alat = laporanController.getAlatTerlaris();
        updateLabelLaporan(pemasukan, transaksi, studioLeaderBoard, alat);
    }
    
    private void updateLabelLaporan(double pemasukan, int transaksi, List<String> studioLeaderboard, List<String> alat) {
        System.out.println("DEBUG 3: Method updateLabelLaporan dipanggil.");
        System.out.println("         Data Leaderboard yang diterima: " + studioLeaderboard.size() + " item.");

        java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("id", "ID"));

        totalPemasukanLabel.setText(formatter.format(pemasukan));
        totalTransaksiLabel.setText(transaksi + " Pesanan");
        
        alatTambahanPanel.removeAll();
        alatTambahanPanel.add(alatTerlarisTabLabel);
        if(alat.isEmpty())
        {
            JLabel pesan = new JLabel("Tidak ada data pemesanan.");
            pesan.setForeground(java.awt.Color.WHITE); // Samakan warna agar terlihat
            alatTambahanPanel.add(pesan);
        } else {
            for (String peringkat : alat) {
                JLabel labelPeringkat = new JLabel(peringkat);
                labelPeringkat.setForeground(java.awt.Color.WHITE); 
                alatTambahanPanel.add(labelPeringkat);
            }
        }
        alatTambahanPanel.setLayout(new javax.swing.BoxLayout(alatTambahanPanel, javax.swing.BoxLayout.Y_AXIS));
        
        alatTambahanPanel.revalidate();
        alatTambahanPanel.repaint();

        studioTerlarisPanel.removeAll();
        studioTerlarisPanel.add(studioTerlarisTabLabel);
        if (studioLeaderboard.isEmpty()) {
            JLabel pesan = new JLabel("Tidak ada data pemesanan.");
            pesan.setForeground(java.awt.Color.WHITE); // Samakan warna agar terlihat
            studioTerlarisPanel.add(pesan);
        } else {
            for (String peringkat : studioLeaderboard) {
                JLabel labelPeringkat = new JLabel(peringkat);
                labelPeringkat.setForeground(java.awt.Color.WHITE); 
                studioTerlarisPanel.add(labelPeringkat);
            }
        }
        studioTerlarisPanel.setLayout(new javax.swing.BoxLayout(studioTerlarisPanel, javax.swing.BoxLayout.Y_AXIS));
        
        studioTerlarisPanel.revalidate();
        studioTerlarisPanel.repaint();

        System.out.println("DEBUG 4: Panel Leaderboard selesai di-update.");
        System.out.println("DEBUG 4: Panel Leaderboard selesai di-update.");
    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        pemesananPanel = new javax.swing.JPanel();
        tabPemesananLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pemesananTable = new javax.swing.JTable();
        editStatusButton = new javax.swing.JButton();
        deletePemesananButton = new javax.swing.JButton();
        studioPanel = new javax.swing.JPanel();
        lblDataStudio = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        studioTable = new javax.swing.JTable();
        btnEditStudio = new javax.swing.JButton();
        btnDeleteStudio = new javax.swing.JButton();
        btnAddStudio = new javax.swing.JButton();
        penyewaPanel = new javax.swing.JPanel();
        tabPemesananLbl2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        penyewaTable = new javax.swing.JTable();
        deletePenyewaButton = new javax.swing.JButton();
        lihatRiwayatPenyewaButton = new javax.swing.JButton();
        laporanPanel = new javax.swing.JPanel();
        laporanLabel = new javax.swing.JLabel();
        periodeLabel = new javax.swing.JLabel();
        tanggalMulai = new com.toedter.calendar.JDateChooser();
        tanggalSelesai = new com.toedter.calendar.JDateChooser();
        tampilkanButton = new javax.swing.JButton();
        tampilkanSemuaButton = new javax.swing.JButton();
        totalPemasukanTab = new javax.swing.JPanel();
        totalPemasukanTabLabel = new javax.swing.JLabel();
        totalPemasukanLabel = new javax.swing.JLabel();
        studioTerlarisPanel = new javax.swing.JPanel();
        studioTerlarisTabLabel = new javax.swing.JLabel();
        totalTransaksiTab = new javax.swing.JPanel();
        totalTransaksiTabLabel = new javax.swing.JLabel();
        totalTransaksiLabel = new javax.swing.JLabel();
        alatTambahanPanel = new javax.swing.JPanel();
        alatTerlarisTabLabel = new javax.swing.JLabel();
        backgorundLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        menuMasuk = new javax.swing.JMenu();
        menuMasukAdmin = new javax.swing.JMenuItem();
        menuMasukUser = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(51, 51, 51));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));

        pemesananPanel.setBackground(new java.awt.Color(102, 102, 102));

        tabPemesananLabel.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tabPemesananLabel.setForeground(new java.awt.Color(255, 255, 255));
        tabPemesananLabel.setText("TAB PEMESANAN");

        pemesananTable.setBackground(new java.awt.Color(102, 102, 102));
        pemesananTable.setForeground(new java.awt.Color(255, 255, 255));
        pemesananTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(pemesananTable);

        editStatusButton.setBackground(new java.awt.Color(0, 0, 0));
        editStatusButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        editStatusButton.setForeground(new java.awt.Color(255, 255, 255));
        editStatusButton.setText("Ubah Status");
        editStatusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editStatusButtonActionPerformed(evt);
            }
        });

        deletePemesananButton.setBackground(new java.awt.Color(0, 0, 0));
        deletePemesananButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deletePemesananButton.setForeground(new java.awt.Color(255, 255, 255));
        deletePemesananButton.setText("Hapus Pemesanan");
        deletePemesananButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePemesananButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pemesananPanelLayout = new javax.swing.GroupLayout(pemesananPanel);
        pemesananPanel.setLayout(pemesananPanelLayout);
        pemesananPanelLayout.setHorizontalGroup(
            pemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pemesananPanelLayout.createSequentialGroup()
                .addGroup(pemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pemesananPanelLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(editStatusButton)
                        .addGap(18, 18, 18)
                        .addComponent(deletePemesananButton))
                    .addGroup(pemesananPanelLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(pemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tabPemesananLabel)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        pemesananPanelLayout.setVerticalGroup(
            pemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pemesananPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(tabPemesananLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pemesananPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editStatusButton)
                    .addComponent(deletePemesananButton))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Pemesanan", pemesananPanel);

        studioPanel.setBackground(new java.awt.Color(102, 102, 102));

        lblDataStudio.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblDataStudio.setForeground(new java.awt.Color(255, 255, 255));
        lblDataStudio.setText("TAB DATA STUDIO");

        studioTable.setBackground(new java.awt.Color(102, 0, 102));
        studioTable.setForeground(new java.awt.Color(255, 255, 255));
        studioTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(studioTable);

        btnEditStudio.setBackground(new java.awt.Color(0, 0, 0));
        btnEditStudio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEditStudio.setForeground(new java.awt.Color(255, 255, 255));
        btnEditStudio.setText("Edit Informasi Studio");
        btnEditStudio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditStudioActionPerformed(evt);
            }
        });

        btnDeleteStudio.setBackground(new java.awt.Color(0, 0, 0));
        btnDeleteStudio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDeleteStudio.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteStudio.setText("Hapus Studio");
        btnDeleteStudio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteStudioActionPerformed(evt);
            }
        });

        btnAddStudio.setBackground(new java.awt.Color(0, 0, 0));
        btnAddStudio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddStudio.setForeground(new java.awt.Color(255, 255, 255));
        btnAddStudio.setText("Tambah Studio");
        btnAddStudio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStudioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout studioPanelLayout = new javax.swing.GroupLayout(studioPanel);
        studioPanel.setLayout(studioPanelLayout);
        studioPanelLayout.setHorizontalGroup(
            studioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studioPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(studioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(studioPanelLayout.createSequentialGroup()
                        .addComponent(btnEditStudio)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteStudio)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddStudio))
                    .addComponent(lblDataStudio)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        studioPanelLayout.setVerticalGroup(
            studioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studioPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(lblDataStudio)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(studioPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditStudio)
                    .addComponent(btnDeleteStudio)
                    .addComponent(btnAddStudio))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Data Studio", studioPanel);

        penyewaPanel.setBackground(new java.awt.Color(102, 102, 102));

        tabPemesananLbl2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tabPemesananLbl2.setForeground(new java.awt.Color(255, 255, 255));
        tabPemesananLbl2.setText("TAB PENYEWA");

        penyewaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(penyewaTable);

        deletePenyewaButton.setBackground(new java.awt.Color(0, 0, 0));
        deletePenyewaButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deletePenyewaButton.setForeground(new java.awt.Color(255, 255, 255));
        deletePenyewaButton.setText("Hapus Penyewa");
        deletePenyewaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePenyewaButtonActionPerformed(evt);
            }
        });

        lihatRiwayatPenyewaButton.setBackground(new java.awt.Color(0, 0, 0));
        lihatRiwayatPenyewaButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lihatRiwayatPenyewaButton.setForeground(new java.awt.Color(255, 255, 255));
        lihatRiwayatPenyewaButton.setText("Lihat Riwayat Pemesanan");
        lihatRiwayatPenyewaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lihatRiwayatPenyewaButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout penyewaPanelLayout = new javax.swing.GroupLayout(penyewaPanel);
        penyewaPanel.setLayout(penyewaPanelLayout);
        penyewaPanelLayout.setHorizontalGroup(
            penyewaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(penyewaPanelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(tabPemesananLbl2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(penyewaPanelLayout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addGroup(penyewaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(penyewaPanelLayout.createSequentialGroup()
                        .addComponent(deletePenyewaButton)
                        .addGap(18, 18, 18)
                        .addComponent(lihatRiwayatPenyewaButton))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        penyewaPanelLayout.setVerticalGroup(
            penyewaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(penyewaPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(tabPemesananLbl2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(penyewaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deletePenyewaButton)
                    .addComponent(lihatRiwayatPenyewaButton))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Data Penyewa", penyewaPanel);

        laporanPanel.setBackground(new java.awt.Color(102, 102, 102));

        laporanLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        laporanLabel.setForeground(new java.awt.Color(255, 255, 255));
        laporanLabel.setText("LAPORAN PENDAPATAN");

        periodeLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        periodeLabel.setForeground(new java.awt.Color(255, 255, 255));
        periodeLabel.setText("Pilih periode laporan:");

        tanggalMulai.setForeground(new java.awt.Color(0, 0, 0));

        tanggalSelesai.setForeground(new java.awt.Color(0, 0, 0));

        tampilkanButton.setBackground(new java.awt.Color(0, 0, 0));
        tampilkanButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tampilkanButton.setForeground(new java.awt.Color(255, 255, 255));
        tampilkanButton.setText("Tampilkan");
        tampilkanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tampilkanButtonActionPerformed(evt);
            }
        });

        tampilkanSemuaButton.setBackground(new java.awt.Color(0, 0, 0));
        tampilkanSemuaButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tampilkanSemuaButton.setForeground(new java.awt.Color(255, 255, 255));
        tampilkanSemuaButton.setText("Tampilkan Semua");
        tampilkanSemuaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tampilkanSemuaButtonActionPerformed(evt);
            }
        });

        totalPemasukanTab.setBackground(new java.awt.Color(0, 0, 0));

        totalPemasukanTabLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalPemasukanTabLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalPemasukanTabLabel.setText("TOTAL PEMASUKAN");

        totalPemasukanLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalPemasukanLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalPemasukanLabel.setText("TOTAL PEMASUKAN");

        javax.swing.GroupLayout totalPemasukanTabLayout = new javax.swing.GroupLayout(totalPemasukanTab);
        totalPemasukanTab.setLayout(totalPemasukanTabLayout);
        totalPemasukanTabLayout.setHorizontalGroup(
            totalPemasukanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalPemasukanTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(totalPemasukanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalPemasukanTabLabel)
                    .addComponent(totalPemasukanLabel))
                .addContainerGap(177, Short.MAX_VALUE))
        );
        totalPemasukanTabLayout.setVerticalGroup(
            totalPemasukanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalPemasukanTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalPemasukanTabLabel)
                .addGap(18, 18, 18)
                .addComponent(totalPemasukanLabel)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        studioTerlarisPanel.setBackground(new java.awt.Color(0, 0, 0));

        studioTerlarisTabLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        studioTerlarisTabLabel.setForeground(new java.awt.Color(255, 255, 255));
        studioTerlarisTabLabel.setText("STUDIO TERLARIS");

        javax.swing.GroupLayout studioTerlarisPanelLayout = new javax.swing.GroupLayout(studioTerlarisPanel);
        studioTerlarisPanel.setLayout(studioTerlarisPanelLayout);
        studioTerlarisPanelLayout.setHorizontalGroup(
            studioTerlarisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studioTerlarisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(studioTerlarisTabLabel)
                .addContainerGap(194, Short.MAX_VALUE))
        );
        studioTerlarisPanelLayout.setVerticalGroup(
            studioTerlarisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studioTerlarisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(studioTerlarisTabLabel)
                .addContainerGap(99, Short.MAX_VALUE))
        );

        totalTransaksiTab.setBackground(new java.awt.Color(0, 0, 0));

        totalTransaksiTabLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalTransaksiTabLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalTransaksiTabLabel.setText("TOTAL TRANSAKSI");

        totalTransaksiLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalTransaksiLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalTransaksiLabel.setText("TOTAL TRANSAKSI");

        javax.swing.GroupLayout totalTransaksiTabLayout = new javax.swing.GroupLayout(totalTransaksiTab);
        totalTransaksiTab.setLayout(totalTransaksiTabLayout);
        totalTransaksiTabLayout.setHorizontalGroup(
            totalTransaksiTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalTransaksiTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(totalTransaksiTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalTransaksiTabLabel)
                    .addComponent(totalTransaksiLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        totalTransaksiTabLayout.setVerticalGroup(
            totalTransaksiTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalTransaksiTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalTransaksiTabLabel)
                .addGap(18, 18, 18)
                .addComponent(totalTransaksiLabel)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        alatTambahanPanel.setBackground(new java.awt.Color(0, 0, 0));

        alatTerlarisTabLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        alatTerlarisTabLabel.setForeground(new java.awt.Color(255, 255, 255));
        alatTerlarisTabLabel.setText("ALAT TAMBAHAN TERLARIS");

        javax.swing.GroupLayout alatTambahanPanelLayout = new javax.swing.GroupLayout(alatTambahanPanel);
        alatTambahanPanel.setLayout(alatTambahanPanelLayout);
        alatTambahanPanelLayout.setHorizontalGroup(
            alatTambahanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alatTambahanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(alatTerlarisTabLabel)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        alatTambahanPanelLayout.setVerticalGroup(
            alatTambahanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alatTambahanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(alatTerlarisTabLabel)
                .addContainerGap(99, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout laporanPanelLayout = new javax.swing.GroupLayout(laporanPanel);
        laporanPanel.setLayout(laporanPanelLayout);
        laporanPanelLayout.setHorizontalGroup(
            laporanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(laporanPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(laporanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(laporanPanelLayout.createSequentialGroup()
                        .addComponent(periodeLabel)
                        .addGap(18, 18, 18)
                        .addComponent(tanggalMulai, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(laporanLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tanggalSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(laporanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tampilkanSemuaButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tampilkanButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(157, 157, 157))
            .addGroup(laporanPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(laporanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(totalPemasukanTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(studioTerlarisPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(laporanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(totalTransaksiTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alatTambahanPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        laporanPanelLayout.setVerticalGroup(
            laporanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, laporanPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(laporanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(laporanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tanggalMulai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(periodeLabel)
                    .addComponent(tampilkanButton)
                    .addComponent(tanggalSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tampilkanSemuaButton)
                .addGap(18, 18, 18)
                .addGroup(laporanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(totalPemasukanTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalTransaksiTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(laporanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studioTerlarisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alatTambahanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Laporan", laporanPanel);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 620, 450));

        backgorundLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/gackground new.png"))); // NOI18N
        getContentPane().add(backgorundLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 640, 480));

        menuBar.setBackground(new java.awt.Color(51, 0, 51));
        menuBar.setForeground(new java.awt.Color(255, 255, 255));

        menuMasuk.setForeground(new java.awt.Color(255, 255, 255));
        menuMasuk.setText("Keluar");

        menuMasukAdmin.setText("Keluar akun");
        menuMasukAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMasukAdminActionPerformed(evt);
            }
        });
        menuMasuk.add(menuMasukAdmin);

        menuMasukUser.setText("Keluar aplikasi");
        menuMasukUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMasukUserActionPerformed(evt);
            }
        });
        menuMasuk.add(menuMasukUser);

        menuBar.add(menuMasuk);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddStudioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStudioActionPerformed
        // TODO add your handling code here:
        new FormAddStudioGUI(this, true).setVisible(true);
    }//GEN-LAST:event_btnAddStudioActionPerformed

    private void btnDeleteStudioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteStudioActionPerformed
        // TODO add your handling code here:
        int selectedRow = studioTable.getSelectedRow();
        if(selectedRow == -1)
        {
            JOptionPane.showMessageDialog(this, "Tidak ada tabel yang dipilih!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }

        String idStudio = (String) studioTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus studio dengan ID: " + idStudio + "?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION)
        {
            boolean success = studioController.deleteStudio(idStudio);
            if (success)
            {
                JOptionPane.showMessageDialog(this, "Data berhasil di hapus!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                loadStudioTable();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDeleteStudioActionPerformed

    private void btnEditStudioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditStudioActionPerformed
        // TODO add your handling code here:
        int selectedRow = studioTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih studio yang ingin diedit terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return; 
        }
        
        String idStudio = (String) studioTableModel.getValueAt(selectedRow, 0);
        String fasilitas = (String) studioTableModel.getValueAt(selectedRow, 1);
        int kapasitas = (Integer) studioTableModel.getValueAt(selectedRow, 2);
        int hargaPerJam = (Integer) studioTableModel.getValueAt(selectedRow, 3);
        
        Studio studioToEdit = new Studio(idStudio, fasilitas, kapasitas, hargaPerJam);

        FormEditStudioGUI editDialog = new FormEditStudioGUI(this, true, studioToEdit);
        editDialog.setVisible(true);
        loadStudioTable();
        
    }//GEN-LAST:event_btnEditStudioActionPerformed

    private void deletePemesananButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePemesananButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = pemesananTable.getSelectedRow();
        if(selectedRow == -1)
        {
            JOptionPane.showMessageDialog(this, "Tidak ada tabel yang dipilih!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }

        int idPemesanan = (int) pemesananTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus pemesanan dengan ID: " + idPemesanan + "?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION)
        {
            try
            {
                pemesananController.deletePemesanan(idPemesanan);
                JOptionPane.showMessageDialog(this, "Data pemesanan berhasil di hapus!");
                loadPemesananTable();
            } catch (SQLException e)
            {
                JOptionPane.showMessageDialog(
                        this, 
                        "Data gagal di hapus!\n" + e.getMessage(),
                        "Database Error!",
                        JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_deletePemesananButtonActionPerformed

    private void editStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editStatusButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = pemesananTable.getSelectedRow();
        if(selectedRow == -1)
        {
            JOptionPane.showMessageDialog(this, "Tidak ada tabel yang dipilih!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }

        int idPemesanan = (int) pemesananTableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) pemesananTableModel.getValueAt(selectedRow, 6);

        Object[] possibleStatus = {"Dipesan", "Selesai", "Dibatalkan"};
        Object selectedStatus = JOptionPane.showInputDialog(this, "Pilih status baru:", "Ubah Status Pemesanan",
            JOptionPane.PLAIN_MESSAGE, null, possibleStatus, currentStatus);

        if (selectedStatus != null)
        {
            boolean success = pemesananController.updatePemesananStatus(idPemesanan, (String) selectedStatus);
            if(success)
            {
                JOptionPane.showMessageDialog(this, "Status berhasil diupdate!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                loadPemesananTable();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Gagal update status!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_editStatusButtonActionPerformed

    private void tampilkanSemuaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tampilkanSemuaButtonActionPerformed
        // TODO add your handling code here:
        tampilkanLaporanSemua();
    }//GEN-LAST:event_tampilkanSemuaButtonActionPerformed

    private void tampilkanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tampilkanButtonActionPerformed
        // TODO add your handling code here:
        Date tglMulai = tanggalMulai.getDate();
        Date tglSelesai = tanggalSelesai.getDate();
        
        System.out.println("DEBUG 1: Tombol 'Tampilkan' diklik.");
        System.out.println("         Tanggal Mulai dari JDateChooser: " + tglMulai);
        System.out.println("         Tanggal Selesai dari JDateChooser: " + tglSelesai);


        if (tglMulai == null || tglSelesai == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih kedua tanggal untuk filter.", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tglMulai.after(tglSelesai)) {
            JOptionPane.showMessageDialog(this, "Tanggal mulai tidak boleh lebih akhir dari tanggal selesai.", "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        double pemasukan = laporanController.getTotalPemasukan(tglMulai, tglSelesai);
        int transaksi = laporanController.getTotalTransaksi(tglMulai, tglSelesai);
        List<String> studioLeaderboard = laporanController.getStudioTerlarisLeaderboard(tglMulai, tglSelesai);
        List<String> alat = laporanController.getAlatTerlaris();
        
        System.out.println("DEBUG 2: Hasil dari Controller diterima.");
        System.out.println("         Ukuran List Leaderboard: " + studioLeaderboard.size());
        for(String item : studioLeaderboard) {
            System.out.println("         - " + item);
        }

        
        updateLabelLaporan(pemasukan, transaksi, studioLeaderboard, alat);
    }//GEN-LAST:event_tampilkanButtonActionPerformed

    private void deletePenyewaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePenyewaButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = penyewaTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih penyewa yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idPenyewa = (String) penyewaTableModel.getValueAt(selectedRow, 0);
        String namaPenyewa = (String) penyewaTableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Anda yakin ingin menghapus penyewa:\n" + namaPenyewa + " (ID: " + idPenyewa + ")?\n\nPERINGATAN: Menghapus penyewa dapat menyebabkan error jika ia memiliki riwayat pemesanan.",
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = userController.deleteUser(idPenyewa);

            if (success) {
                JOptionPane.showMessageDialog(this, "Data penyewa berhasil dihapus.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                loadPenyewaTable(); // Muat ulang tabel untuk menampilkan perubahan
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data penyewa.\nPastikan penyewa tidak memiliki pemesanan yang aktif.", "Error Database", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deletePenyewaButtonActionPerformed

    private void lihatRiwayatPenyewaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lihatRiwayatPenyewaButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = penyewaTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih penyewa untuk melihat riwayatnya!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idUser = (String) penyewaTableModel.getValueAt(selectedRow, 0);
        String namaUser = (String) penyewaTableModel.getValueAt(selectedRow, 1);

        List<PemesananDetailDTO> riwayat = pemesananController.getRiwayatPemesananByPenyewa(idUser);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");

        StringBuilder message = new StringBuilder("Riwayat Pemesanan untuk: " + namaUser + "\n\n");
        if (riwayat.isEmpty()) {
            message.append("Tidak ditemukan riwayat pemesanan.");
        } else {
            for (PemesananDetailDTO dto : riwayat) {
                message.append("ID Pesan: ").append(dto.getIdPemesanan()).append("\n");
                 message.append("Waktu: ").append(dto.getWaktuMulai().format(formatter))
                       .append(" - ").append(dto.getWaktuSelesai().format(DateTimeFormatter.ofPattern("HH:mm"))).append("\n");
                message.append("Studio: ").append(dto.getNamaStudio()).append("\n");
                message.append("Total: Rp ").append(String.format("%,d", dto.getTotalHarga())).append("\n");
                message.append("---------------------------------\n");
            }
        }
        
        // JTextArea  bisa scroll
        javax.swing.JTextArea textArea = new javax.swing.JTextArea(message.toString());
        textArea.setRows(15);
        textArea.setColumns(40);
        textArea.setEditable(false);
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);

        JOptionPane.showMessageDialog(this, scrollPane, "Riwayat Pemesanan - " + namaUser, JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_lihatRiwayatPenyewaButtonActionPerformed

    private void menuMasukAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMasukAdminActionPerformed
        // TODO add your handling code here:
        new UserLoginGUI().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuMasukAdminActionPerformed

    private void menuMasukUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMasukUserActionPerformed
        // TODO add your handling code here:
        System.exit(1);
    }//GEN-LAST:event_menuMasukUserActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminDashboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboardGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel alatTambahanPanel;
    private javax.swing.JLabel alatTerlarisTabLabel;
    private javax.swing.JLabel backgorundLabel;
    private javax.swing.JButton btnAddStudio;
    private javax.swing.JButton btnDeleteStudio;
    private javax.swing.JButton btnEditStudio;
    private javax.swing.JButton deletePemesananButton;
    private javax.swing.JButton deletePenyewaButton;
    private javax.swing.JButton editStatusButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel laporanLabel;
    private javax.swing.JPanel laporanPanel;
    private javax.swing.JLabel lblDataStudio;
    private javax.swing.JButton lihatRiwayatPenyewaButton;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuMasuk;
    private javax.swing.JMenuItem menuMasukAdmin;
    private javax.swing.JMenuItem menuMasukUser;
    private javax.swing.JPanel pemesananPanel;
    private javax.swing.JTable pemesananTable;
    private javax.swing.JPanel penyewaPanel;
    private javax.swing.JTable penyewaTable;
    private javax.swing.JLabel periodeLabel;
    private javax.swing.JPanel studioPanel;
    private javax.swing.JTable studioTable;
    private javax.swing.JPanel studioTerlarisPanel;
    private javax.swing.JLabel studioTerlarisTabLabel;
    private javax.swing.JLabel tabPemesananLabel;
    private javax.swing.JLabel tabPemesananLbl2;
    private javax.swing.JButton tampilkanButton;
    private javax.swing.JButton tampilkanSemuaButton;
    private com.toedter.calendar.JDateChooser tanggalMulai;
    private com.toedter.calendar.JDateChooser tanggalSelesai;
    private javax.swing.JLabel totalPemasukanLabel;
    private javax.swing.JPanel totalPemasukanTab;
    private javax.swing.JLabel totalPemasukanTabLabel;
    private javax.swing.JLabel totalTransaksiLabel;
    private javax.swing.JPanel totalTransaksiTab;
    private javax.swing.JLabel totalTransaksiTabLabel;
    // End of variables declaration//GEN-END:variables
}
