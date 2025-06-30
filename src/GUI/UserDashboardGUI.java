/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;
import model.Studio;
import model.User;
import model.AlatMusik;
import model.*;
import DAO.StudioDAO;
import DAO.*;
import config.DBConnection;
import generator.PemesananIdGenerator;
import Controller.PemesananController;
import Dialog.KonfirmasiPemesanan;

import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.element.Text;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
    

/**
 *
 * @author nyoma
 */
public class UserDashboardGUI extends javax.swing.JFrame {
    private User currentUser;
    private Studio studio;
    private StudioDAO studioDAO;
    private AlatMusikDAO alatMusikDAO;
    private double totalHarga = 0;
    private boolean alatSudahDitambahkan = false;
    private PemesananDAO pemesananDAO;
    
    // Alat musik
    private List<AlatMusik> daftarAlatYangDipilih = new ArrayList<>();
    private List<JCheckBox> alatCheckBoxes = new ArrayList<>();
    
    // Data untuk invoice
    private Studio studioTerakhir;
    private LocalDateTime waktuMulaiTerakhir;
    private LocalDateTime waktuSelesaiTerakhir;
    private List<AlatMusik> alatMusikTerakhir;
    private double totalHargaTerakhir;
    private String noInvoice;
    
    /**
     * Creates new form UserDashboardGUI
     */
    public UserDashboardGUI() throws SQLException {
        initComponents();
        studioDAO = new StudioDAO();
        alatMusikDAO = new AlatMusikDAOImpl();
        pemesananDAO = new PemesananDAO();
        loadStudioCombo();
        
        // Jam mulai
        cbJamMulai.removeAllItems();
        for (int i = 8; i <= 22; i++) {
            cbJamMulai.addItem(i + ":00");
        }
       
        createDynamicAlatCheckBoxes();
        // Spinner durasi sewa
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 5, 1);
        spinnerDurasi.setModel(model);
    }
    
    public UserDashboardGUI(User user) throws SQLException
    {
        this();
        this.currentUser = user;
        
        nameTextField.setText(currentUser.getNama());
        emailTextField.setText(currentUser.getEmail());
        phoneNumberTextField.setText(currentUser.getPhoneNumber());
    }
    
    private void loadStudioCombo() {
        cbStudio.removeAllItems();
        for (Studio s : studioDAO.getAllStudio()) {
            cbStudio.addItem(s.getIdStudio());
        }
    }

    private void createDynamicAlatCheckBoxes()
    {
        daftarAlatYangDipilih = alatMusikDAO.getAll();
        alatMusikPanel.setLayout(new BoxLayout(alatMusikPanel, BoxLayout.Y_AXIS));
        
        alatCheckBoxes.clear(); 
        alatMusikPanel.removeAll();
        
        for (AlatMusik alat:daftarAlatYangDipilih)
        {
            String checkBoxTextFormat = String.format("%s (+Rp %,d/jam)", alat.getNamaAlat(), alat.getHargaPerJam());
            JCheckBox checkBox = new JCheckBox(checkBoxTextFormat);
            
            checkBox.putClientProperty("alat", alat);
            
            checkBox.setForeground(new java.awt.Color(255,255,255));
            checkBox.setBackground(new java.awt.Color(102,102,102));
            alatCheckBoxes.add(checkBox);
            alatMusikPanel.add(checkBox);
        }
        
        alatMusikPanel.revalidate();
        alatMusikPanel.repaint();
    }
    private double additionalStuff()
    {
        int durasi = (Integer) spinnerDurasi.getValue();
        totalHarga = 0;
        
        for (JCheckBox checkBox : alatCheckBoxes)
        {
            if(checkBox.isSelected())
            {
                AlatMusik alat= (AlatMusik) checkBox.getClientProperty("alat");
                if(alat != null)
                {
                    totalHarga += alat.getHargaPerJam() *durasi;
                }
            }
        }
        return totalHarga;
    }
    
    private List<AlatMusik> getAlatMusik() throws SQLException
    {
        List<AlatMusik> listAlat = new ArrayList<>();

        for (JCheckBox checkBox : alatCheckBoxes) {
            if (checkBox.isSelected()) {
                // Retrieve the AlatMusik object and add it to the list
                AlatMusik alat = (AlatMusik) checkBox.getClientProperty("alat");
                if (alat != null) {
                    listAlat.add(alat);
                }
            }
        }
        
        return listAlat;
    }
    
    private double accumulatePrice()
    {
        int durasi = (Integer) spinnerDurasi.getValue();

        String idStudio = (String) cbStudio.getSelectedItem();
        Studio studio = studioDAO.getById(idStudio);
        if (studio == null) return 0;

        double totalStudio = studio.getHargaPerJam() * durasi;
        double total = totalStudio + additionalStuff();

        totalPriceTextField.setText("Rp " + total);
        return total;
    }
    
    private void generateInvoicePdf(String dest, String noInvoice, LocalDateTime waktuMulai, LocalDateTime waktuSelesai, User user, Studio studio, List<AlatMusik> alatList, double totalHarga) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(30, 30, 30, 30);
        
        // Kalkulasi durasi dari LocalDateTime untuk ditampilkan
        long durasi = Duration.between(waktuMulai, waktuSelesai).toHours();
        if (durasi == 0) durasi = 1; // Minimal 1 jam

        // Header 
        document.add(new Paragraph(new Text("INVOICE").setBold())
            .setTextAlignment(TextAlignment.CENTER).setFontSize(20));
    document.add(new Paragraph("Studio Musik Keren")
            .setTextAlignment(TextAlignment.CENTER).setFontSize(12));
    document.add(new Paragraph("\n"));

        // Informasi Invoice - Gunakan DateTimeFormatter
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        document.add(new Paragraph("Ditagihkan kepada: " + user.getNama()));
        document.add(new Paragraph("No. Invoice: " + noInvoice + "\nTanggal: " + waktuMulai.format(dateFormatter)));
        document.add(new Paragraph("\n\n"));

        // Tabel Rincian Biaya 
        float[] columnWidths = {4, 1, 1, 1};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Header Tabel
        table.addHeaderCell(new Cell().add(new Paragraph(new Text("Deskripsi").setBold())));
        table.addHeaderCell(new Cell().add(new Paragraph(new Text("Durasi").setBold())));
        table.addHeaderCell(new Cell().add(new Paragraph(new Text("Harga/Jam").setBold())));
        table.addHeaderCell(new Cell().add(new Paragraph(new Text("Subtotal").setBold())));

        // Isi Tabel - Sewa Studio
        double subtotalStudio = studio.getHargaPerJam() * durasi;
        table.addCell(new Cell().add(new Paragraph("Sewa " + studio.getIdStudio())));
        table.addCell(new Cell().add(new Paragraph(durasi + " jam")));
        table.addCell(new Cell().add(new Paragraph("Rp " + String.format("%,.0f", (double) studio.getHargaPerJam()))));
        table.addCell(new Cell().add(new Paragraph("Rp " + String.format("%,.0f", subtotalStudio))));

        // Isi Tabel - Alat Tambahan
        if (alatList != null && !alatList.isEmpty()) {
            for (AlatMusik alat : alatList) {
                double subtotalAlat = alat.getHargaPerJam() * durasi;
                table.addCell(new Cell().add(new Paragraph("Sewa Tambahan: " + alat.getNamaAlat())));
                table.addCell(new Cell().add(new Paragraph(durasi + " jam")));
                table.addCell(new Cell().add(new Paragraph("Rp " + String.format("%,.0f", (double) alat.getHargaPerJam()))));
                table.addCell(new Cell().add(new Paragraph("Rp " + String.format("%,.0f", subtotalAlat))));
            }
        }
        document.add(table);

        // Total
        document.add(new Paragraph("\n"));
        Paragraph total = new Paragraph(new Text("TOTAL: Rp " + String.format("%,.0f", totalHarga)).setBold())
            .setTextAlignment(TextAlignment.RIGHT).setFontSize(16);
        document.add(total);

        // Footer
        document.add(new Paragraph("\n\n\n"));
        document.add(new Paragraph("Terima kasih telah menyewa di Studio Musik Keren.\nPembayaran dapat dilakukan melalui transfer ke rekening BCA 123-456-7890 a/n Keren Sekali.")
                .setTextAlignment(TextAlignment.CENTER).setFontSize(10));

        document.close();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        studioChoosingPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        dateChooser = new com.toedter.calendar.JDateChooser();
        cbJamMulai = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        spinnerDurasi = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        studioChoosingPanel = new javax.swing.JPanel();
        lblStudio = new javax.swing.JLabel();
        cbStudio = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableStudioInfo = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnTambah = new javax.swing.JButton();
        alatMusikPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        phoneNumberTextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        totalPriceTextField = new javax.swing.JTextField();
        btnBayar = new javax.swing.JButton();
        backgorundLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        menuMasuk = new javax.swing.JMenu();
        menuMasukAdmin = new javax.swing.JMenuItem();
        menuMasukUser = new javax.swing.JMenuItem();
        transaksiMenu = new javax.swing.JMenu();
        printInvoice = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        studioChoosingPanel1.setBackground(new java.awt.Color(102, 102, 102));
        studioChoosingPanel1.setForeground(new java.awt.Color(102, 102, 102));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("TANGGAL & WAKTU PEMESANAN");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tanggal:");

        dateChooser.setBackground(new java.awt.Color(102, 102, 102));
        dateChooser.setForeground(new java.awt.Color(255, 255, 255));

        cbJamMulai.setBackground(new java.awt.Color(102, 102, 102));
        cbJamMulai.setForeground(new java.awt.Color(255, 255, 255));
        cbJamMulai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Pilih jam:");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Durasi:");

        javax.swing.GroupLayout studioChoosingPanel1Layout = new javax.swing.GroupLayout(studioChoosingPanel1);
        studioChoosingPanel1.setLayout(studioChoosingPanel1Layout);
        studioChoosingPanel1Layout.setHorizontalGroup(
            studioChoosingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studioChoosingPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(studioChoosingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, studioChoosingPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(studioChoosingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(studioChoosingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spinnerDurasi, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbJamMulai, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        studioChoosingPanel1Layout.setVerticalGroup(
            studioChoosingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, studioChoosingPanel1Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(studioChoosingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(studioChoosingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(cbJamMulai, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(studioChoosingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerDurasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(16, 16, 16))
        );

        getContentPane().add(studioChoosingPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 0, 290, 160));

        studioChoosingPanel.setBackground(new java.awt.Color(102, 102, 102));
        studioChoosingPanel.setForeground(new java.awt.Color(102, 102, 102));

        lblStudio.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblStudio.setForeground(new java.awt.Color(255, 255, 255));
        lblStudio.setText("PILIH STUDIO");

        cbStudio.setBackground(new java.awt.Color(102, 102, 102));
        cbStudio.setForeground(new java.awt.Color(255, 255, 255));
        cbStudio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbStudio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStudioActionPerformed(evt);
            }
        });

        tableStudioInfo.setBackground(new java.awt.Color(255, 255, 255));
        tableStudioInfo.setForeground(new java.awt.Color(0, 0, 0));
        tableStudioInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jScrollPane1.setViewportView(tableStudioInfo);

        javax.swing.GroupLayout studioChoosingPanelLayout = new javax.swing.GroupLayout(studioChoosingPanel);
        studioChoosingPanel.setLayout(studioChoosingPanelLayout);
        studioChoosingPanelLayout.setHorizontalGroup(
            studioChoosingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studioChoosingPanelLayout.createSequentialGroup()
                .addGroup(studioChoosingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(studioChoosingPanelLayout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(lblStudio))
                    .addGroup(studioChoosingPanelLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(cbStudio, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(studioChoosingPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        studioChoosingPanelLayout.setVerticalGroup(
            studioChoosingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studioChoosingPanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lblStudio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbStudio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        getContentPane().add(studioChoosingPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 340, 240));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setForeground(new java.awt.Color(102, 102, 102));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("ALAT MUSIK TAMBAHAN");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("(Opsional)");

        btnTambah.setBackground(new java.awt.Color(204, 204, 0));
        btnTambah.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(51, 51, 51));
        btnTambah.setText("Lihat Harga");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        alatMusikPanel.setBackground(new java.awt.Color(102, 102, 102));
        alatMusikPanel.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout alatMusikPanelLayout = new javax.swing.GroupLayout(alatMusikPanel);
        alatMusikPanel.setLayout(alatMusikPanelLayout);
        alatMusikPanelLayout.setHorizontalGroup(
            alatMusikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 241, Short.MAX_VALUE)
        );
        alatMusikPanelLayout.setVerticalGroup(
            alatMusikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 142, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(53, 53, 53)))
                .addGap(54, 54, 54))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(alatMusikPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alatMusikPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnTambah)
                .addGap(13, 13, 13))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 180, 290, 270));

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setForeground(new java.awt.Color(102, 102, 102));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("KONFIRMASI");

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Nama:");

        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Email:");

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("No. HP");

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Total Harga:");

        btnBayar.setBackground(new java.awt.Color(204, 0, 204));
        btnBayar.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        btnBayar.setForeground(new java.awt.Color(255, 255, 255));
        btnBayar.setText("KONFIRMASI");
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(jLabel11))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel12))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                                    .addComponent(emailTextField)
                                    .addComponent(phoneNumberTextField)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(totalPriceTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))))))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13)
                    .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 340, 200));

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

        transaksiMenu.setForeground(new java.awt.Color(255, 255, 255));
        transaksiMenu.setText("Transaksi");
        transaksiMenu.setEnabled(false);
        transaksiMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transaksiMenuActionPerformed(evt);
            }
        });

        printInvoice.setText("Cetak Invoice");
        printInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printInvoiceActionPerformed(evt);
            }
        });
        transaksiMenu.add(printInvoice);

        menuBar.add(transaksiMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbStudioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStudioActionPerformed
        // TODO add your handling code here:
        String selectedStudioId = (String) cbStudio.getSelectedItem();
        Studio s = studioDAO.getById(selectedStudioId);
        
        if (s == null)
        {
            System.out.println("Studio tidak di temukan untuk id: " + selectedStudioId);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tableStudioInfo.getModel();
        model.setRowCount(0);
        model.setColumnIdentifiers(new Object[]{"Properti", "Nilai"});
        model.addRow(new Object[]{"Nomor Studio", s.getIdStudio()});
        model.addRow(new Object[]{"Fasilitas", s.getFasilitas()});
        model.addRow(new Object[]{"Kapasitas", s.getKapasitas() + " orang"});
        model.addRow(new Object[]{"Harga per jam", "Rp " + s.getHargaPerJam()});
    
    }//GEN-LAST:event_cbStudioActionPerformed

    private void menuMasukAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMasukAdminActionPerformed
        // TODO add your handling code here:
        new UserLoginGUI().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuMasukAdminActionPerformed

    private void menuMasukUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMasukUserActionPerformed
        // TODO add your handling code here:
        System.exit(1);
    }//GEN-LAST:event_menuMasukUserActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        try{
            accumulatePrice();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage());
        }
        
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
        // TODO add your handling code here:
        
        int durasi = (Integer) spinnerDurasi.getValue();
        String idPenyewa = currentUser.getIdUser();
//        String idStudio = (String) cbStudio.getSelectedItem();
        Studio studio = studioDAO.getById((String) cbStudio.getSelectedItem());
        
        double hargaAwal = accumulatePrice();
        int totalHarga = (int) hargaAwal;
        
        
        Date tanggalTerpilih = dateChooser.getDate();
        if(tanggalTerpilih == null)
        {
            JOptionPane.showMessageDialog(this, "Pastikan anda memilih tanggal!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String jam_mulai = (String) cbJamMulai.getSelectedItem();
        if(cbJamMulai.getSelectedIndex() == -1)
        {
            JOptionPane.showMessageDialog(this, "Silakan memilih jam mulai sebelum melanjutkan!", "Peringatan!", JOptionPane.WARNING_MESSAGE);
        }
        
        
        String[] bagianWaktu = jam_mulai.split(":");
        int jamPilihan = Integer.parseInt(bagianWaktu[0]);
        
        // Ini adalah waktuMulai
        LocalDateTime waktuMulai = tanggalTerpilih.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atTime(jamPilihan, 0);
        
        // Kalkulasi waktuSelesai
        LocalDateTime waktuSelesai = waktuMulai.plusHours(durasi);
        LocalDateTime waktuSekarang = LocalDateTime.now();
        
        if(waktuMulai.isBefore(waktuSekarang))
        {
            JOptionPane.showMessageDialog(this, "Anda tidak boleh memilih tanggal\natau waktu yang sudah lewat!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
       try
       {
           PemesananDAO daoChecker = new PemesananDAO();
           boolean isAvailable = daoChecker.isStudioAvailable(studio.getIdStudio(), waktuMulai, waktuSelesai);
           
           if (!isAvailable)
           {
               JOptionPane.showMessageDialog(
                       this, 
                       "Maaf jadwal studio ini sudah terisi\nSilahkan pilih waktu lainnya!!", 
                       "Peringatan", JOptionPane.WARNING_MESSAGE);
               return;
           }
           
           List<AlatMusik> alatPilihan = getAlatMusik();
           this.studioTerakhir = studio;
           this.waktuMulaiTerakhir = waktuMulai;     
           this.waktuSelesaiTerakhir = waktuSelesai;
           this.alatMusikTerakhir = alatPilihan;
           this.totalHargaTerakhir = totalHarga;
           this.noInvoice = new PemesananIdGenerator().generateIdPemesanan();
           KonfirmasiPemesanan konfirmasiPesanan = new KonfirmasiPemesanan(
               this,
               true,
               currentUser,
               studio,
               waktuMulai,
               waktuSelesai,
               alatPilihan,
               totalHarga);
           konfirmasiPesanan.setVisible(true);
           if (konfirmasiPesanan.isBerhasil())
           {
               transaksiMenu.setEnabled(true);
           }
            
            
       } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnBayarActionPerformed

    private void printInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printInvoiceActionPerformed
        // TODO add your handling code here:
        if (this.studioTerakhir == null) {
        JOptionPane.showMessageDialog(this, "Belum ada transaksi untuk dicetak.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Invoice Sebagai PDF");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Documents", "pdf");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setSelectedFile(new File(this.noInvoice.replace("/", "-") + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String outputPath = fileToSave.getAbsolutePath();

            if (!outputPath.toLowerCase().endsWith(".pdf")) {
                outputPath += ".pdf";
            }

            try {
                // Panggil method generate PDF-nyas
                generateInvoicePdf(
                    outputPath,
                    this.noInvoice,
                    this.waktuMulaiTerakhir, 
                    this.waktuSelesaiTerakhir, 
                    this.currentUser,
                    this.studioTerakhir,
                    this.alatMusikTerakhir,
                    this.totalHargaTerakhir
                );
                JOptionPane.showMessageDialog(this,
                    "Invoice berhasil disimpan sebagai PDF!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_printInvoiceActionPerformed

    private void transaksiMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transaksiMenuActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_transaksiMenuActionPerformed

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
            java.util.logging.Logger.getLogger(UserDashboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserDashboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserDashboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserDashboardGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run()  {
                try
                {
                    new UserDashboardGUI().setVisible(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel alatMusikPanel;
    private javax.swing.JLabel backgorundLabel;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> cbJamMulai;
    private javax.swing.JComboBox<String> cbStudio;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStudio;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuMasuk;
    private javax.swing.JMenuItem menuMasukAdmin;
    private javax.swing.JMenuItem menuMasukUser;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField phoneNumberTextField;
    private javax.swing.JMenuItem printInvoice;
    private javax.swing.JSpinner spinnerDurasi;
    private javax.swing.JPanel studioChoosingPanel;
    private javax.swing.JPanel studioChoosingPanel1;
    private javax.swing.JTable tableStudioInfo;
    private javax.swing.JTextField totalPriceTextField;
    private javax.swing.JMenu transaksiMenu;
    // End of variables declaration//GEN-END:variables
}
