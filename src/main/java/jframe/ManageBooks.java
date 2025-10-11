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
public class ManageBooks extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(ManageBooks.class.getName());

    /**
     * Creates new form ManageBooks
     */

    // String book_name;
    // String author;
    // int book_id;
    // int quantity;
    DefaultTableModel model;

    public ManageBooks() {
        initComponents();
        setBookDetailsToTable();
    }

    // populate rSTableBookDetails with TABLE library_ms.book_details
    public void setBookDetailsToTable() {

        try {
            Connection conn = DBConnection.getConnection();
            String sqlQuery = "SELECT * FROM library_ms.book_details";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            model = (DefaultTableModel) rSTableBookDetails.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                Object[] obj = { bookId, bookName, author, quantity };
                model.addRow(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshBookDetailTable() {

    }

    // add new book to TABLE library_ms.book_details
    public void addBookToTable() {

        // TODO impement condition when txtFieldBookId is empty. Let TABLE
        // auto_increment book_id

        if (validateBookFields()) {
            // if (recordExists() == false) {

                try {
                    Connection conn = DBConnection.getConnection();
                    String sqlInsert = "INSERT INTO library_ms.book_details (book_id, book_name, author, quantity) VALUES (?, ?, ?, ?)";
                    PreparedStatement prepStatement = conn.prepareStatement(sqlInsert);
                    prepStatement.setInt(1, Integer.parseInt(txtFieldBookId.getText()));
                    prepStatement.setString(2, txtFieldBookName.getText());
                    prepStatement.setString(3, txtFieldAuthorName.getText());
                    prepStatement.setInt(4, Integer.parseInt(txtFieldQuantity.getText()));

                    int updatedRowCount = prepStatement.executeUpdate();

                    if (updatedRowCount > 0) {
                        JOptionPane.showMessageDialog(this, "Add successfull");
                        setBookDetailsToTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Unable to Add to library");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "This Book Already Exists!");
            }
        // } else {
        //     JOptionPane.showMessageDialog(rootPane, "Fields can not be empty");
        // }

    }

    // update book in TABLE library_ms.book_details
    public void updateBookInTable() {

        // TODO: validate that book quanity does not go into negitive
        // TODO: detete book if quantity is 0

        if (validateBookFields()) {
            System.out.println("Attempting to update record");
            if (recordExists()) {
                try {
                    Connection conn = DBConnection.getConnection();
                    String sqlUpdate = String.format(
                            "UPDATE library_ms.book_details SET book_name = '%s', author = '%s', quantity = %d WHERE book_id = %d",
                            txtFieldBookName.getText(),
                            txtFieldAuthorName.getText(),
                            Integer.parseInt(txtFieldQuantity.getText()),
                            Integer.parseInt(txtFieldBookId.getText()));

                    System.out.println(sqlUpdate);
                    PreparedStatement preparedStatement = conn.prepareStatement(sqlUpdate);
                    int updatedRowCount = preparedStatement.executeUpdate();
                    System.out.println("Row update count: " + updatedRowCount);
                    if (updatedRowCount > 0) {
                        setBookDetailsToTable();
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

    // delete book from TABLE library_ms.book_details
    public void deleteBookInTable() {

        if (recordExists()) {
            try {
                Connection conn = DBConnection.getConnection();
                String sqlDelete = String.format("DELETE FROM library_ms.book_details WHERE book_id = %d",
                        Integer.parseInt(txtFieldBookId.getText()));
                PreparedStatement prepStatement = conn.prepareStatement(sqlDelete);
                int updatedRowCount = prepStatement.executeUpdate();

                if (updatedRowCount > 0) {
                    setBookDetailsToTable();
                    JOptionPane.showMessageDialog(this, "Book Successfully Deleted");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Unable To Locate Record!");
        }
    }

    // checks if book exists in TABLE library_ms.book_details using book_id
    public boolean recordExists() {

        System.out.println("Attempting to Locate Record");

        try {
            int bookId = Integer.parseInt(txtFieldBookId.getText());

            Connection conn = DBConnection.getConnection();
            String sqlQuery = "SELECT EXISTS (SELECT 1 FROM library_ms.book_details WHERE book_id = %d)"
                    .formatted(bookId);
            PreparedStatement prepStatment = conn.prepareStatement(sqlQuery);
            ResultSet rs = prepStatment.executeQuery();

            if (rs.next()) {
                System.out.println("Record exists!");
                String columnValue = rs.getString(1);
                System.out.println("column value at 1 = " + columnValue);
                // System.out.printf("book_id = %d, book_name = %s%n", rs.get, rs.getString("book_name"));
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
    public boolean validateBookFields() {

        // TODO: validate txtFieldBookId, and txtFieldQuantity is int
        // TODO: validate txtFieldBookName, and txtFieldAuthorName is String

        String[] bookDetailsArr = new String[4];
        String bookId = txtFieldBookId.getText();
        String bookName = txtFieldBookName.getText();
        String author = txtFieldAuthorName.getText();
        String quantity = txtFieldQuantity.getText();
        bookDetailsArr[0] = bookId;
        bookDetailsArr[1] = bookName;
        bookDetailsArr[2] = author;
        bookDetailsArr[3] = quantity;

        for (int i = 0; i < bookDetailsArr.length; i++) {
            System.out.println(bookDetailsArr[i]);
            if (bookDetailsArr[i] == null || bookDetailsArr[i].equals("")) {
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
        txtFieldBookId = new javax.swing.JTextField();
        lblBookIdTxt = new javax.swing.JLabel();
        lblBookNameTxt = new javax.swing.JLabel();
        lblAcountIcon1 = new javax.swing.JLabel();
        txtFieldBookName = new javax.swing.JTextField();
        lblAuthorNameTxt = new javax.swing.JLabel();
        lblAcountIcon2 = new javax.swing.JLabel();
        txtFieldAuthorName = new javax.swing.JTextField();
        lblQuantityTxt = new javax.swing.JLabel();
        lblAcountIcon3 = new javax.swing.JLabel();
        txtFieldQuantity = new javax.swing.JTextField();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnAdd2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        rSTableBookDetails = new rojerusan.RSTableMetro();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(1541, 828));
        setPreferredSize(new java.awt.Dimension(1541, 828));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JPnlControls.setBackground(new java.awt.Color(102, 102, 255));
        JPnlControls.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JPnlBack.setBackground(new java.awt.Color(0, 153, 153));

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

        txtFieldBookId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFieldBookIdFocusLost(evt);
            }
        });
        txtFieldBookId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFieldBookIdActionPerformed(evt);
            }
        });
        JPnlControls.add(txtFieldBookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 260, 30));

        lblBookIdTxt.setForeground(new java.awt.Color(255, 255, 255));
        lblBookIdTxt.setText("Enter Book Id");
        JPnlControls.add(lblBookIdTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 170, -1));

        lblBookNameTxt.setForeground(new java.awt.Color(255, 255, 255));
        lblBookNameTxt.setText("Enter Book Name");
        JPnlControls.add(lblBookNameTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, 170, -1));

        lblAcountIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Moleskine_26px.png"))); // NOI18N
        JPnlControls.add(lblAcountIcon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        txtFieldBookName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFieldBookNameFocusLost(evt);
            }
        });
        txtFieldBookName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFieldBookNameActionPerformed(evt);
            }
        });
        JPnlControls.add(txtFieldBookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 260, 30));

        lblAuthorNameTxt.setForeground(new java.awt.Color(255, 255, 255));
        lblAuthorNameTxt.setText("Enter Author Name");
        JPnlControls.add(lblAuthorNameTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 170, -1));

        lblAcountIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Collaborator_Male_26px.png"))); // NOI18N
        JPnlControls.add(lblAcountIcon2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        txtFieldAuthorName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFieldAuthorNameFocusLost(evt);
            }
        });
        txtFieldAuthorName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFieldAuthorNameActionPerformed(evt);
            }
        });
        JPnlControls.add(txtFieldAuthorName, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 290, 260, 30));

        lblQuantityTxt.setForeground(new java.awt.Color(255, 255, 255));
        lblQuantityTxt.setText("Enter Quantity");
        JPnlControls.add(lblQuantityTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, 170, -1));

        lblAcountIcon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Unit_26px.png"))); // NOI18N
        JPnlControls.add(lblAcountIcon3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, -1, -1));

        txtFieldQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFieldQuantityFocusLost(evt);
            }
        });
        txtFieldQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFieldQuantityActionPerformed(evt);
            }
        });
        JPnlControls.add(txtFieldQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 370, 260, 30));

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

        getContentPane().add(JPnlControls, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, 830));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rSTableBookDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "book_id", "book_name", "author", "quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        rSTableBookDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSTableBookDetailsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(rSTableBookDetails);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 580, 520));

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/books-stack-of-three.png"))); // NOI18N
        jLabel1.setText("Manage Books");
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(0, 0, 0)));
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 240, -1));

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
        deleteBookInTable();
    }// GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnUpdateActionPerformed
        updateBookInTable();
    }// GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddActionPerformed
        addBookToTable();
    }// GEN-LAST:event_btnAddActionPerformed

    private void rSTableBookDetailsMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_rSTableBookDetailsMouseClicked
        int rowN0 = rSTableBookDetails.getSelectedRow();
        TableModel model = rSTableBookDetails.getModel();

        txtFieldBookId.setText(model.getValueAt(rowN0, 0).toString());
        txtFieldBookName.setText(model.getValueAt(rowN0, 1).toString());
        txtFieldAuthorName.setText(model.getValueAt(rowN0, 2).toString());
        txtFieldQuantity.setText(model.getValueAt(rowN0, 3).toString());

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
        java.awt.EventQueue.invokeLater(() -> new ManageBooks().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPnlBack;
    private javax.swing.JPanel JPnlControls;
    private javax.swing.JButton btnAdd2;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAcountIcon;
    private javax.swing.JLabel lblAcountIcon1;
    private javax.swing.JLabel lblAcountIcon2;
    private javax.swing.JLabel lblAcountIcon3;
    private javax.swing.JLabel lblAuthorNameTxt;
    private javax.swing.JLabel lblBack;
    private javax.swing.JLabel lblBookIdTxt;
    private javax.swing.JLabel lblBookNameTxt;
    private javax.swing.JLabel lblQuantityTxt;
    private rojerusan.RSTableMetro rSTableBookDetails;
    private javax.swing.JTextField txtFieldAuthorName;
    private javax.swing.JTextField txtFieldBookId;
    private javax.swing.JTextField txtFieldBookName;
    private javax.swing.JTextField txtFieldQuantity;
    // End of variables declaration//GEN-END:variables
}
