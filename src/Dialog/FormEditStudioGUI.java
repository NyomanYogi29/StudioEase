/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Dialog;

import Controller.StudioController;
import GUI.AdminDashboardGUI;
import model.Studio;
import javax.swing.JOptionPane;

/**
 *
 * @author nyoma
 */
public class FormEditStudioGUI extends javax.swing.JDialog {

    private Studio studioToEdit;
    private StudioController studioController;
    private final AdminDashboardGUI parentDashboard;
    
    /**
     * Creates new form FormEditStudioGUI
     */
    public FormEditStudioGUI(AdminDashboardGUI parent, boolean modal, Studio studio) {
        super(parent, modal);
        this.parentDashboard = parent; 
        initComponents();
        setLocationRelativeTo(parent); 
        
        this.studioToEdit = studio;
        this.studioController = new StudioController();

        populateFields(); 
    }
    
    private void populateFields() {
        titleLabel.setText("EDIT STUDIO: " + studioToEdit.getIdStudio()); 
        idStudioTextField.setText(studioToEdit.getIdStudio()); 
        idStudioTextField.setEditable(false); 
        
        fasilitasTextArea.setText(studioToEdit.getFasilitas()); 
        kapasitasTextField.setText(String.valueOf(studioToEdit.getKapasitas())); 
        hargaTextField.setText(String.valueOf(studioToEdit.getHargaPerJam())); 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        idStudioLabel = new javax.swing.JLabel();
        fasilitasLabel = new javax.swing.JLabel();
        kapasitasLabel = new javax.swing.JLabel();
        hargaLabekl = new javax.swing.JLabel();
        idStudioTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        fasilitasTextArea = new javax.swing.JTextArea();
        kapasitasTextField = new javax.swing.JTextField();
        hargaTextField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(0, 0, 0));
        titleLabel.setText("EDIT STUDIO YANG DIPILIH");

        idStudioLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        idStudioLabel.setForeground(new java.awt.Color(0, 0, 0));
        idStudioLabel.setText("ID Studio:");

        fasilitasLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        fasilitasLabel.setForeground(new java.awt.Color(0, 0, 0));
        fasilitasLabel.setText("Fasilitas:");

        kapasitasLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        kapasitasLabel.setForeground(new java.awt.Color(0, 0, 0));
        kapasitasLabel.setText("Kapasitas:");

        hargaLabekl.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        hargaLabekl.setForeground(new java.awt.Color(0, 0, 0));
        hargaLabekl.setText("Harga/jam:");

        idStudioTextField.setForeground(new java.awt.Color(0, 0, 0));

        fasilitasTextArea.setColumns(20);
        fasilitasTextArea.setForeground(new java.awt.Color(0, 0, 0));
        fasilitasTextArea.setRows(5);
        jScrollPane1.setViewportView(fasilitasTextArea);

        kapasitasTextField.setForeground(new java.awt.Color(0, 0, 0));
        kapasitasTextField.setToolTipText("");

        hargaTextField.setForeground(new java.awt.Color(0, 0, 0));

        saveButton.setBackground(new java.awt.Color(0, 0, 0));
        saveButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titleLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(hargaLabekl)
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(hargaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(saveButton)
                                    .addGap(92, 92, 92))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(kapasitasLabel)
                                .addComponent(fasilitasLabel)
                                .addComponent(idStudioLabel))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(idStudioTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(kapasitasTextField)))))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(titleLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idStudioLabel)
                    .addComponent(idStudioTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fasilitasLabel)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kapasitasLabel)
                    .addComponent(kapasitasTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hargaLabekl)
                    .addComponent(hargaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(saveButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        String idStudio = idStudioTextField.getText(); 
        String newFasilitas = fasilitasTextArea.getText(); 
        String newKapasitasStr = kapasitasTextField.getText(); 
        String newHargaStr = hargaTextField.getText();
        if (newFasilitas.isEmpty() || newKapasitasStr.isEmpty() || newHargaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int newKapasitas = Integer.parseInt(newKapasitasStr);
            int newHarga = Integer.parseInt(newHargaStr);

            //  Tampilkan dialog konfirmasi
            int confirm = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menyimpan perubahan untuk studio ID: " + idStudio + "?",
                "Konfirmasi Perubahan", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Studio updatedStudio = new Studio(idStudio, newFasilitas, newKapasitas, newHarga);
                boolean success = studioController.updateStudio(updatedStudio);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Data studio berhasil diperbarui!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    parentDashboard.loadStudioTable();
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal memperbarui data studio!", "Error Database", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Kapasitas dan Harga harus berupa angka!", "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_saveButtonActionPerformed

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
//            java.util.logging.Logger.getLogger(FormEditStudioGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(FormEditStudioGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(FormEditStudioGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(FormEditStudioGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                FormEditStudioGUI dialog = new FormEditStudioGUI(new AdminDashboardGUI(), true, Studio studio);
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
    private javax.swing.JLabel fasilitasLabel;
    private javax.swing.JTextArea fasilitasTextArea;
    private javax.swing.JLabel hargaLabekl;
    private javax.swing.JTextField hargaTextField;
    private javax.swing.JLabel idStudioLabel;
    private javax.swing.JTextField idStudioTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel kapasitasLabel;
    private javax.swing.JTextField kapasitasTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
