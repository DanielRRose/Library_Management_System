/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

/**
 *
 * @author daniel
 */
public class IssueBook extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(IssueBook.class.getName());

    /**
     * Creates new form IssueBook
     */
    public IssueBook() {
        initComponents();
    }

    // fetches the book details from ms_libray.book_details and populates book
    // detail section
    public void getBookDetails() {

        try {
            int bookId = Integer.parseInt(txtFldBookId.getText());

            Connection conn = DBConnection.getConnection();
            String sqlQuery = "SELECT * FROM library_ms.book_details WHERE book_id = %d"
                    .formatted(bookId);
            PreparedStatement prepStatement = conn.prepareStatement(sqlQuery);
            ResultSet rs = prepStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                lblBookIdDisplay.setText(Integer.toString(id));
                lblBookNameDisplay.setText(rs.getString(2));
                lblAuthorDisplay.setText(rs.getString(3));
                lblQuantityDisplay.setText(rs.getString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean issueBook() {

        boolean result = false;

        try {
            int bookId = Integer.parseInt(lblBookIdDisplay.getText());
            String bookName = lblBookNameDisplay.getText();
            int studentId = Integer.parseInt(lblStudentIdDisplay.getText());
            String studentName = lblStudentNameDisplay.getText();
            java.sql.Date issueDate = java.sql.Date.valueOf(txtFldIssueDate.getText());
            java.sql.Date dueDate = java.sql.Date.valueOf(txtFldDueDate.getText());

            Connection conn = DBConnection.getConnection();
            String sqlInsert = """
                    INSERT INTO library_ms.issue_book_details (book_id, book_name, student_id, student_name, issue_date, due_date, state)
                    VALUES (?,?,?,?,?,?,?)
                    """;
            PreparedStatement prepStatement = conn.prepareStatement(sqlInsert);
            prepStatement.setInt(1, bookId);
            prepStatement.setString(2, bookName);
            prepStatement.setInt(3, studentId);
            prepStatement.setString(4, studentName);
            prepStatement.setDate(5, issueDate);
            prepStatement.setDate(6, dueDate);
            prepStatement.setString(7, "checked_out");
            int rowCount = prepStatement.executeUpdate();

            if (rowCount > 0) {
                result = true;
            } else {
                result = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // update the quantity in libray_ms.book_details
    public void updateBookCount() {
        int bookId = Integer.parseInt(lblBookIdDisplay.getText());

        try {
            Connection conn = DBConnection.getConnection();
            String sqlUpdate = "UPDATE library_ms.book_details SET quantity = quantity - 1 WHERE book_id = ?";
            PreparedStatement prepStatment = conn.prepareStatement(sqlUpdate);
            prepStatment.setInt(1, bookId);

            int rowCount = prepStatment.executeUpdate();
            if (rowCount > 0) {
                int oldQuantity = Integer.parseInt(lblQuantityDisplay.getText());
                int updatedQuantity = oldQuantity - 1;
                lblQuantityDisplay.setText(Integer.toString(updatedQuantity));
            } else {
                JOptionPane.showMessageDialog(this, "Failed to Update Book quantity");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean hasQuantity() {

        boolean Instock = false;

        try {
            int bookId = Integer.parseInt(lblBookIdDisplay.getText());
            Connection conn = DBConnection.getConnection();
            String sqlQuery = "SELECT * FROM library_ms.book_details WHERE book_id = ?";
            PreparedStatement prepStmt = conn.prepareStatement(sqlQuery);
            prepStmt.setInt(1, bookId);
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                int quantity = Integer.parseInt(rs.getString("quantity"));
                if (quantity > 0) {
                    Instock = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Instock;
    }

    public boolean alreadyIssued() {

        boolean isIssued = false;

        try {

            int bookId = Integer.parseInt(lblBookIdDisplay.getText());
            int studentId = Integer.parseInt(lblStudentIdDisplay.getText());
            Connection conn = DBConnection.getConnection();
            String sqlQuery = "SELECT * FROM library_ms.issue_book_details WHERE student_id = ? and book_id = ? and state = ?";
            PreparedStatement prepStatement = conn.prepareStatement(sqlQuery);
            prepStatement.setInt(1, studentId);
            prepStatement.setInt(2, bookId);
            prepStatement.setString(3, "checked_out");

            ResultSet rs = prepStatement.executeQuery();
            if (rs.next()) {
                isIssued = true;
            } else {
                isIssued = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isIssued;
    }

    public void getStudentDetails() {

        try {
            int studentId = Integer.parseInt(txtFldStudentId.getText());

            Connection conn = DBConnection.getConnection();
            String sqlQuery = "SELECT * FROM library_ms.student_details WHERE id = %d"
                    .formatted(studentId);
            PreparedStatement prepStatement = conn.prepareStatement(sqlQuery);
            ResultSet rs = prepStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                int age = rs.getInt(3);
                lblStudentIdDisplay.setText(Integer.toString(id));
                lblStudentNameDisplay.setText(rs.getString(2));
                lblStudentAgeDisplay.setText(Integer.toString(age));
                lblStudentGenderDisplay.setText(rs.getString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_Main = new javax.swing.JPanel();
        panelBookDetails = new javax.swing.JPanel();
        JPnlBack1 = new javax.swing.JPanel();
        lblBack1 = new javax.swing.JLabel();
        pnlBookDetails1 = new javax.swing.JLabel();
        lblBookId1 = new javax.swing.JLabel();
        lblBookName1 = new javax.swing.JLabel();
        lblAuthor1 = new javax.swing.JLabel();
        lblQuantity1 = new javax.swing.JLabel();
        lblBookNameDisplay = new javax.swing.JLabel();
        lblAuthorDisplay = new javax.swing.JLabel();
        lblQuantityDisplay = new javax.swing.JLabel();
        lblBookIdDisplay = new javax.swing.JLabel();
        panelStudentDetails = new javax.swing.JPanel();
        pnlBookDetails = new javax.swing.JLabel();
        lblStudentId = new javax.swing.JLabel();
        lblStudentName = new javax.swing.JLabel();
        lblStudentAge = new javax.swing.JLabel();
        lblStudentGender = new javax.swing.JLabel();
        lblStudentIdDisplay = new javax.swing.JLabel();
        lblStudentNameDisplay = new javax.swing.JLabel();
        lblStudentAgeDisplay = new javax.swing.JLabel();
        lblStudentGenderDisplay = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFldStudentId = new javax.swing.JTextField();
        txtFldDueDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtFldBookId = new javax.swing.JTextField();
        txtFldIssueDate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btbIssueBook = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_Main.setBackground(new java.awt.Color(255, 255, 255));
        panel_Main.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelBookDetails.setBackground(new java.awt.Color(0, 153, 153));
        panelBookDetails.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JPnlBack1.setBackground(new java.awt.Color(102, 102, 255));

        lblBack1.setBackground(new java.awt.Color(102, 102, 255));
        lblBack1.setFont(new java.awt.Font("Liberation Sans", 1, 17)); // NOI18N
        lblBack1.setForeground(new java.awt.Color(255, 255, 255));
        lblBack1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Rewind_48px.png"))); // NOI18N
        lblBack1.setText("Back");
        lblBack1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBack1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout JPnlBack1Layout = new javax.swing.GroupLayout(JPnlBack1);
        JPnlBack1.setLayout(JPnlBack1Layout);
        JPnlBack1Layout.setHorizontalGroup(
                JPnlBack1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(JPnlBack1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 110,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        JPnlBack1Layout.setVerticalGroup(
                JPnlBack1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(JPnlBack1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblBack1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        panelBookDetails.add(JPnlBack1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 60));

        pnlBookDetails1.setFont(new java.awt.Font("Liberation Sans", 1, 25)); // NOI18N
        pnlBookDetails1.setForeground(new java.awt.Color(255, 255, 255));
        pnlBookDetails1.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Literature_100px_1.png"))); // NOI18N
        pnlBookDetails1.setText("    Book Details");
        pnlBookDetails1
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(255, 255, 255)));
        panelBookDetails.add(pnlBookDetails1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 350, -1));

        lblBookId1.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblBookId1.setForeground(new java.awt.Color(255, 255, 255));
        lblBookId1.setText("Book Id:");
        panelBookDetails.add(lblBookId1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        lblBookName1.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblBookName1.setForeground(new java.awt.Color(255, 255, 255));
        lblBookName1.setText("Book Name:");
        panelBookDetails.add(lblBookName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, -1, -1));

        lblAuthor1.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblAuthor1.setForeground(new java.awt.Color(255, 255, 255));
        lblAuthor1.setText("Author:");
        panelBookDetails.add(lblAuthor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, -1, -1));

        lblQuantity1.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblQuantity1.setForeground(new java.awt.Color(255, 255, 255));
        lblQuantity1.setText("Quantity:");
        panelBookDetails.add(lblQuantity1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, -1, -1));

        lblBookNameDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblBookNameDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblBookNameDisplay
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelBookDetails.add(lblBookNameDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 360, 140, 30));

        lblAuthorDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblAuthorDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblAuthorDisplay
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelBookDetails.add(lblAuthorDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 430, 140, 30));

        lblQuantityDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblQuantityDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblQuantityDisplay
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelBookDetails.add(lblQuantityDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 500, 140, 30));

        lblBookIdDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblBookIdDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblBookIdDisplay
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelBookDetails.add(lblBookIdDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 290, 140, 30));

        panel_Main.add(panelBookDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 865));

        panelStudentDetails.setBackground(new java.awt.Color(102, 102, 255));
        panelStudentDetails.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlBookDetails.setFont(new java.awt.Font("Liberation Sans", 1, 25)); // NOI18N
        pnlBookDetails.setForeground(new java.awt.Color(255, 255, 255));
        pnlBookDetails.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/AddNewBookIcons/icons8_Student_Registration_100px_2.png"))); // NOI18N
        pnlBookDetails.setText("    Student Details");
        pnlBookDetails
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(255, 255, 255)));
        panelStudentDetails.add(pnlBookDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 350, -1));

        lblStudentId.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblStudentId.setForeground(new java.awt.Color(255, 255, 255));
        lblStudentId.setText("Student Id:");
        panelStudentDetails.add(lblStudentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        lblStudentName.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblStudentName.setForeground(new java.awt.Color(255, 255, 255));
        lblStudentName.setText("Name:");
        panelStudentDetails.add(lblStudentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, -1, -1));

        lblStudentAge.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblStudentAge.setForeground(new java.awt.Color(255, 255, 255));
        lblStudentAge.setText("Age:");
        panelStudentDetails.add(lblStudentAge, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, -1, -1));

        lblStudentGender.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblStudentGender.setForeground(new java.awt.Color(255, 255, 255));
        lblStudentGender.setText("Gender:");
        panelStudentDetails.add(lblStudentGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, -1, -1));

        lblStudentIdDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblStudentIdDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblStudentIdDisplay
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelStudentDetails.add(lblStudentIdDisplay,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 280, 140, 30));

        lblStudentNameDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblStudentNameDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblStudentNameDisplay
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelStudentDetails.add(lblStudentNameDisplay,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 350, 140, 30));

        lblStudentAgeDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblStudentAgeDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblStudentAgeDisplay
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelStudentDetails.add(lblStudentAgeDisplay,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 420, 140, 30));

        lblStudentGenderDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblStudentGenderDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblStudentGenderDisplay
                .setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelStudentDetails.add(lblStudentGenderDisplay,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 490, 140, 30));

        panel_Main.add(panelStudentDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 500, 865));

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 25)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/books-stack-of-three.png"))); // NOI18N
        jLabel1.setText("    Issue Book");
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(0, 0, 0)));
        panel_Main.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 60, -1, -1));

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Enter Student id:");
        panel_Main.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 160, 220, 30));

        txtFldStudentId.setBackground(new java.awt.Color(204, 204, 204));
        txtFldStudentId.setForeground(new java.awt.Color(0, 0, 0));
        txtFldStudentId.setBorder(new javax.swing.border.MatteBorder(null));
        txtFldStudentId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFldStudentIdFocusLost(evt);
            }
        });
        txtFldStudentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFldStudentIdActionPerformed(evt);
            }
        });
        panel_Main.add(txtFldStudentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 200, 200, 30));

        txtFldDueDate.setBackground(new java.awt.Color(204, 204, 204));
        txtFldDueDate.setForeground(new java.awt.Color(0, 0, 0));
        txtFldDueDate.setBorder(new javax.swing.border.MatteBorder(null));
        txtFldDueDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFldDueDateActionPerformed(evt);
            }
        });
        panel_Main.add(txtFldDueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 570, 200, 30));

        jLabel3.setFont(new java.awt.Font("Liberation Sans", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Enter Book Id:");
        panel_Main.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 280, 220, 30));

        jLabel4.setFont(new java.awt.Font("Liberation Sans", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Issue Date:");
        panel_Main.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 390, 170, 40));

        jLabel5.setFont(new java.awt.Font("Liberation Sans", 1, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Due Date");
        panel_Main.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 510, 170, 40));

        txtFldBookId.setBackground(new java.awt.Color(204, 204, 204));
        txtFldBookId.setForeground(new java.awt.Color(0, 0, 0));
        txtFldBookId.setBorder(new javax.swing.border.MatteBorder(null));
        txtFldBookId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFldBookIdFocusLost(evt);
            }
        });
        txtFldBookId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFldBookIdActionPerformed(evt);
            }
        });
        panel_Main.add(txtFldBookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 320, 200, 30));

        txtFldIssueDate.setBackground(new java.awt.Color(204, 204, 204));
        txtFldIssueDate.setForeground(new java.awt.Color(0, 0, 0));
        txtFldIssueDate.setBorder(new javax.swing.border.MatteBorder(null));
        txtFldIssueDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFldIssueDateActionPerformed(evt);
            }
        });
        panel_Main.add(txtFldIssueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 450, 200, 30));

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("DD/MM/YYYY Format");
        panel_Main.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 430, 180, -1));

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("DD/MM/YYYY Format");
        panel_Main.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 550, 180, -1));

        btbIssueBook.setText("Issue Book");
        btbIssueBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbIssueBookActionPerformed(evt);
            }
        });
        panel_Main.add(btbIssueBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 660, 240, -1));

        getContentPane().add(panel_Main, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1541, 865));

        setSize(new java.awt.Dimension(1541, 865));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lblBack1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_lblBack1MouseClicked
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
        dispose();
    }// GEN-LAST:event_lblBack1MouseClicked

    private void txtFldStudentIdActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFldStudentIdActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFldStudentIdActionPerformed

    private void txtFldDueDateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFldDueDateActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFldDueDateActionPerformed

    private void txtFldBookIdActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFldBookIdActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFldBookIdActionPerformed

    private void txtFldIssueDateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtFldIssueDateActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtFldIssueDateActionPerformed

    private void btbIssueBookActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btbIssueBookActionPerformed
        if (hasQuantity()) {
            if (!alreadyIssued()) {
                if (issueBook()) {
                    JOptionPane.showMessageDialog(this, "Successfully Issued Book");
                    updateBookCount();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to Issued Book");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Student already has book checked out!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No Book Copies avaible, Stock less than 1");
        }

    }// GEN-LAST:event_btbIssueBookActionPerformed

    private void txtFldStudentIdFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtFldStudentIdFocusLost
        if (txtFldStudentId.getText() != "" && txtFldStudentId.getText().matches("^[0-9]+$")) {
            getStudentDetails();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Student Id");
        }
    }// GEN-LAST:event_txtFldStudentIdFocusLost

    private void txtFldBookIdFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtFldBookIdFocusLost
        if (txtFldBookId.getText() != "" && txtFldBookId.getText().matches("^[0-9]+$")) {
            getBookDetails();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Book Id");
        }
    }// GEN-LAST:event_txtFldBookIdFocusLost

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
        java.awt.EventQueue.invokeLater(() -> new IssueBook().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPnlBack1;
    private javax.swing.JButton btbIssueBook;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lblAuthor1;
    private javax.swing.JLabel lblAuthorDisplay;
    private javax.swing.JLabel lblBack1;
    private javax.swing.JLabel lblBookId1;
    private javax.swing.JLabel lblBookIdDisplay;
    private javax.swing.JLabel lblBookName1;
    private javax.swing.JLabel lblBookNameDisplay;
    private javax.swing.JLabel lblQuantity1;
    private javax.swing.JLabel lblQuantityDisplay;
    private javax.swing.JLabel lblStudentAge;
    private javax.swing.JLabel lblStudentAgeDisplay;
    private javax.swing.JLabel lblStudentGender;
    private javax.swing.JLabel lblStudentGenderDisplay;
    private javax.swing.JLabel lblStudentId;
    private javax.swing.JLabel lblStudentIdDisplay;
    private javax.swing.JLabel lblStudentName;
    private javax.swing.JLabel lblStudentNameDisplay;
    private javax.swing.JPanel panelBookDetails;
    private javax.swing.JPanel panelStudentDetails;
    private javax.swing.JPanel panel_Main;
    private javax.swing.JLabel pnlBookDetails;
    private javax.swing.JLabel pnlBookDetails1;
    private javax.swing.JTextField txtFldBookId;
    private javax.swing.JTextField txtFldDueDate;
    private javax.swing.JTextField txtFldIssueDate;
    private javax.swing.JTextField txtFldStudentId;
    // End of variables declaration//GEN-END:variables
}
