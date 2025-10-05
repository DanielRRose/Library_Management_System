/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author daniel
 */
public class ManageStudents extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(ManageStudents.class.getName());

    /**
     * Creates new form ManageBooks
     */

    // String book_name;
    // String author;
    // int book_id;
    // int quantity;
    DefaultTableModel model;

    public ManageStudents() {
        initComponents();
        setStudentDetailsToTable();
    }

    // populate rSTableBookDetails with TABLE library_ms.student_details
    public void setStudentDetailsToTable() {

        try {
            Connection conn = DBConnection.getConnection();
            String sqlQuery = "SELECT * FROM library_ms.student_details";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            model = (DefaultTableModel) rSTableStudentDetails.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String student_name = rs.getString("student_name");
                int age = Integer.parseInt(rs.getString("age"));
                String gender = rs.getString("gender");
                Object[] obj = { id, student_name, age, gender };
                model.addRow(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // add new book to TABLE library_ms.student_details
    public void addStudentToTable() {

        // TODO impement condition when txtFieldStudent_Id is empty.
        // Let TABLE auto_increment book_id

        if (validateFields()) {
            if (recordExists() == false) {

                try {
                    Connection conn = DBConnection.getConnection();
                    String sqlInsert = "INSERT INTO library_ms.student_details (id, student_name, age, gender) VALUES (?, ?, ?, ?)";
                    PreparedStatement prepStatement = conn.prepareStatement(sqlInsert);
                    prepStatement.setInt(1, Integer.parseInt(txtFieldStudentId.getText()));
                    prepStatement.setString(2, txtFieldStudentName.getText());
                    prepStatement.setInt(3, Integer.parseInt(txtFieldAge.getText()));
                    prepStatement.setString(4, cmbBoxGender.getSelectedItem().toString());

                    int updatedRowCount = prepStatement.executeUpdate();

                    if (updatedRowCount > 0) {
                        JOptionPane.showMessageDialog(this, "Add successfull");
                        setStudentDetailsToTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Unable to Add to Table");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "This Student Already Exists!");
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Fields can not be empty");
        }

    }

    // update book in TABLE library_ms.student_details
    public void updateStudentInTable() {

        if (validateFields()) {
            System.out.println("Attempting to update record");
            if (recordExists()) {
                try {
                    Connection conn = DBConnection.getConnection();
                    String sqlUpdate = String.format(
                            "UPDATE library_ms.student_details SET student_name = '%s', age = %d, gender = '%s' WHERE id = %d",
                            txtFieldStudentName.getText(),
                            Integer.parseInt(txtFieldAge.getText()),
                            cmbBoxGender.getSelectedItem().toString(),
                            Integer.parseInt(txtFieldStudentId.getText()));

                    System.out.println(sqlUpdate);
                    PreparedStatement preparedStatement = conn.prepareStatement(sqlUpdate);
                    int updatedRowCount = preparedStatement.executeUpdate();
                    System.out.println("Row update count: " + updatedRowCount);
                    if (updatedRowCount > 0) {
                        setStudentDetailsToTable();
                        JOptionPane.showMessageDialog(this, "Update Successfull");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Unable To Locate Record!");
            }
        }
    }

    // delete student from TABLE library_ms.student_details
    public void deleteStudentInTable() {

        if (recordExists()) {
            try {
                Connection conn = DBConnection.getConnection();
                String sqlDelete = String.format("DELETE FROM library_ms.student_details WHERE id = %d",
                        Integer.parseInt(txtFieldStudentId.getText()));
                PreparedStatement prepStatement = conn.prepareStatement(sqlDelete);
                int updatedRowCount = prepStatement.executeUpdate();

                if (updatedRowCount > 0) {
                    setStudentDetailsToTable();
                    JOptionPane.showMessageDialog(this, "Student Successfully Deleted");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Unable To Locate Record!");
        }
    }

    // checks if student exists in TABLE library_ms.student_details using id
    public boolean recordExists() {

        System.out.println("Attempting to Locate Record");

        try {
            int id = Integer.parseInt(txtFieldStudentId.getText());

            Connection conn = DBConnection.getConnection();
            String sqlQuery = "SELECT EXISTS (SELECT 1 FROM library_ms.student_details WHERE id = %d)"
                    .formatted(id);
            PreparedStatement prepStatment = conn.prepareStatement(sqlQuery);
            ResultSet rs = prepStatment.executeQuery();

            if (rs.next()) {
                System.out.println("Record exists!");
                String columnValue = rs.getString(1);
                System.out.println("column value at 1 = " + columnValue);
                return true;
            } else {
                 return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    // currently only validates if text fields are not null and not ""
    public boolean validateFields() {

        // TODO: validate txtFieldStudentId, and txtFieldAge is int
        // TODO: validate txtFieldBookName, and txtFieldAuthorName is String

        String[] studentDetailsArr = new String[4];
        String bookId = txtFieldStudentId.getText();
        String bookName = txtFieldStudentName.getText();
        String author = txtFieldAge.getText();
        String quantity = cmbBoxGender.getSelectedItem().toString();
        studentDetailsArr[0] = bookId;
        studentDetailsArr[1] = bookName;
        studentDetailsArr[2] = author;
        studentDetailsArr[3] = quantity;

        for (int i = 0; i < studentDetailsArr.length; i++) {
            System.out.println(studentDetailsArr[i]);
            if (studentDetailsArr[i] == null || studentDetailsArr[i].equals("")) {
                return false;
            }
        }
        return true;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPnlControls = new javax.swing.JPanel();
        JPnlBack = new javax.swing.JPanel();
        lblBack = new javax.swing.JLabel();
        lblAcountIcon = new javax.swing.JLabel();
        txtFieldStudentId = new javax.swing.JTextField();
        lblStudentIdTxt = new javax.swing.JLabel();
        lblStudentNameTxt = new javax.swing.JLabel();
        lblAcountIcon1 = new javax.swing.JLabel();
        txtFieldStudentName = new javax.swing.JTextField();
        lblAgeTxt = new javax.swing.JLabel();
        lblAcountIcon2 = new javax.swing.JLabel();
        lblGenderTxt = new javax.swing.JLabel();
        lblAcountIcon3 = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnAdd2 = new javax.swing.JButton();
        cmbBoxGender = new javax.swing.JComboBox<>();
        txtFieldAge = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        rSTableStudentDetails = new rojerusan.RSTableMetro();
        JLblManageStudents = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(1541, 828));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JPnlControls.setBackground(new java.awt.Color(102, 102, 255));
        JPnlControls.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JPnlBack.setBackground(new java.awt.Color(255, 51, 51));

        lblBack.setFont(new java.awt.Font("Liberation Sans", 1, 17)); // NOI18N
        lblBack.setForeground(new java.awt.Color(255, 255, 255));
        lblBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Rewind_48px.png"))); // NOI18N
        lblBack.setText("Back");
        lblBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBackMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout JPnlBackLayout = new javax.swing.GroupLayout(JPnlBack);
        JPnlBack.setLayout(JPnlBackLayout);
        JPnlBackLayout.setHorizontalGroup(
            JPnlBackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPnlBackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBack, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPnlBackLayout.setVerticalGroup(
            JPnlBackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPnlBackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBack)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        JPnlControls.add(JPnlBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 60));

        lblAcountIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Contact_26px.png"))); // NOI18N
        JPnlControls.add(lblAcountIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));
        
        JPnlControls.add(txtFieldStudentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 260, 30));

        lblStudentIdTxt.setForeground(new java.awt.Color(255, 255, 255));
        lblStudentIdTxt.setText("Enter Student Id");
        JPnlControls.add(lblStudentIdTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 170, -1));

        lblStudentNameTxt.setForeground(new java.awt.Color(255, 255, 255));
        lblStudentNameTxt.setText("Enter Student Name");
        JPnlControls.add(lblStudentNameTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, 170, -1));

        lblAcountIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Moleskine_26px.png"))); // NOI18N
        JPnlControls.add(lblAcountIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        JPnlControls.add(txtFieldStudentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 260, 30));

        lblAgeTxt.setForeground(new java.awt.Color(255, 255, 255));
        lblAgeTxt.setText("Enter Age");
        JPnlControls.add(lblAgeTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 170, -1));

        lblAcountIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Collaborator_Male_26px.png"))); // NOI18N
        JPnlControls.add(lblAcountIcon2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        lblGenderTxt.setForeground(new java.awt.Color(255, 255, 255));
        lblGenderTxt.setText("Enter Gender");
        JPnlControls.add(lblGenderTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, 170, -1));

        lblAcountIcon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Unit_26px.png"))); // NOI18N
        JPnlControls.add(lblAcountIcon3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, -1, -1));

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setFont(new java.awt.Font("Liberation Sans", 1, 17)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        JPnlControls.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 430, 90, -1));

        btnUpdate.setBackground(new java.awt.Color(204, 204, 0));
        btnUpdate.setFont(new java.awt.Font("Liberation Sans", 1, 17)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        JPnlControls.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 430, 90, -1));

        btnAdd2.setBackground(new java.awt.Color(0, 153, 0));
        btnAdd2.setFont(new java.awt.Font("Liberation Sans", 1, 17)); // NOI18N
        btnAdd2.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd2.setText("Add");
        btnAdd2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        JPnlControls.add(btnAdd2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 90, -1));

        cmbBoxGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "male", "female" }));
        JPnlControls.add(cmbBoxGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 374, 260, 30));
        JPnlControls.add(txtFieldAge, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 294, 260, 30));

        getContentPane().add(JPnlControls, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, 830));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rSTableStudentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "student_id", "student_name", "age", "gender"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });

        jScrollPane1.setViewportView(rSTableStudentDetails);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 760, 130));

        JLblManageStudents.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        JLblManageStudents.setForeground(new java.awt.Color(255, 51, 0));
        JLblManageStudents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Student_Male_52px.png"))); // NOI18N
        JLblManageStudents.setText("Manage Students");
        JLblManageStudents.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(255, 51, 0)));
        jPanel2.add(JLblManageStudents, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 390, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 0, 1190, 830));

        setSize(new java.awt.Dimension(1541, 865));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblBackMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_lblBackMouseClicked
        HomePage home = new HomePage();
        home.setVisible(true);
        dispose();
    }// GEN-LAST:event_lblBackMouseClicked

    private void txtFieldBookIdFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtFieldBookIdFocusLost
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFieldBookIdFocusLost

    private void txtFieldBookIdActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFieldBookIdActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFieldBookIdActionPerformed

    private void txtFieldBookNameFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtFieldBookNameFocusLost
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFieldBookNameFocusLost

    private void txtFieldBookNameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFieldBookNameActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFieldBookNameActionPerformed

    private void txtFieldAuthorNameFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtFieldAuthorNameFocusLost
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFieldAuthorNameFocusLost

    private void txtFieldAuthorNameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFieldAuthorNameActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFieldAuthorNameActionPerformed

    private void txtFieldQuantityFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtFieldQuantityFocusLost
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFieldQuantityFocusLost

    private void txtFieldQuantityActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFieldQuantityActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFieldQuantityActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDeleteActionPerformed
        deleteStudentInTable();
    }// GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnUpdateActionPerformed
        updateStudentInTable();
    }// GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddActionPerformed
        addStudentToTable();
    }// GEN-LAST:event_btnAddActionPerformed

    private void rSTableBookDetailsMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_rSTableBookDetailsMouseClicked
        int rowN0 = rSTableStudentDetails.getSelectedRow();
        TableModel model = rSTableStudentDetails.getModel();

        txtFieldStudentId.setText(model.getValueAt(rowN0, 0).toString());
        txtFieldStudentName.setText(model.getValueAt(rowN0, 1).toString());
        txtFieldAge.setText(model.getValueAt(rowN0, 2).toString());
        cmbBoxGender.setSelectedItem(model.getValueAt(rowN0, 3).toString());

    }// GEN-LAST:event_rSTableBookDetailsMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ManageStudents().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLblManageStudents;
    private javax.swing.JPanel JPnlBack;
    private javax.swing.JPanel JPnlControls;
    private javax.swing.JButton btnAdd2;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbBoxGender;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAcountIcon;
    private javax.swing.JLabel lblAcountIcon1;
    private javax.swing.JLabel lblAcountIcon2;
    private javax.swing.JLabel lblAcountIcon3;
    private javax.swing.JLabel lblAgeTxt;
    private javax.swing.JLabel lblBack;
    private javax.swing.JLabel lblGenderTxt;
    private javax.swing.JLabel lblStudentIdTxt;
    private javax.swing.JLabel lblStudentNameTxt;
    private rojerusan.RSTableMetro rSTableStudentDetails;
    private javax.swing.JTextField txtFieldAge;
    private javax.swing.JTextField txtFieldStudentId;
    private javax.swing.JTextField txtFieldStudentName;
    // End of variables declaration//GEN-END:variables
}
