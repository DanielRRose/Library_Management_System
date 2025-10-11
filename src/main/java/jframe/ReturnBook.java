/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author daniel
 */
public class ReturnBook extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(ReturnBook.class.getName());

    /**
     * Creates new form IssueBook
     */
    public ReturnBook() {
        initComponents();
        setIssuedBooksToTable();
        setStudentsToTable();
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
                lblStudentNameDisplay.setText(rs.getString(3));
                lblDueDateDisplay.setText(rs.getString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStudentsToTable() {

        try {
            Connection conn = DBConnection.getConnection();
            String sqlQuery = "SELECT * FROM library_ms.student_details";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            DefaultTableModel model = (DefaultTableModel) tblStudents.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int studentId = rs.getInt("id");
                String studentName = rs.getString("student_name");

                Object[] obj = { studentId, studentName };
                model.addRow(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIssuedBooksToTable() {

        try {
            Connection conn = DBConnection.getConnection();
            String sqlQuery = "SELECT * FROM library_ms.issue_book_details";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            DefaultTableModel model = (DefaultTableModel) tblIssuedBooks.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int issueId = rs.getInt("id");
                int bookId = rs.getInt("book_id");
                int studentId = rs.getInt("student_id");

                String bookName = rs.getString("book_name");
                String issueDate = rs.getDate("issue_date").toString();
                String dueDate = rs.getDate("due_date").toString();

                Object[] obj = { issueId, bookId, studentId, bookName, issueDate, dueDate };
                model.addRow(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean selectBookRecord() {

        boolean success = false;

        try {
            int bookId = Integer.parseInt(txtFldStudentId.getText());
            int studentId = Integer.parseInt(txtFldStudentId.getText());
            Connection conn = DBConnection.getConnection();
            String sqlQuery = "SELECT * FROM library_ms.issue_book_details WHERE book_id = ? AND student_id = ?";
            PreparedStatement prepStat = conn.prepareStatement(sqlQuery);
            prepStat.setInt(1, bookId);
            prepStat.setInt(2, studentId);
            ResultSet rs = prepStat.executeQuery();

            if (rs.next()) {
                lblIssueIdDisplay.setText(rs.getString("id"));
                lblBookIdDisplay.setText(rs.getString("book_id"));
                lblBookNameDisplay.setText(rs.getString("book_name"));
                lblStudentIdDisplay.setText(rs.getString("student_id"));
                lblStudentNameDisplay.setText(rs.getString("student_name"));
                lblIssueDateDisplay.setText(rs.getString("issue_date"));
                lblDueDateDisplay.setText(rs.getString("due_date"));

                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    public boolean updateBooks() {

        boolean isUpdated = false;
        try {
            int bookId = Integer.parseInt(lblBookIdDisplay.getText());
            Connection conn = DBConnection.getConnection();
            String sqlUpdate = "UPDATE library_ms.book_details SET quantity = quantity + 1 WHERE book_id = ?";
            PreparedStatement prepStatment = conn.prepareStatement(sqlUpdate);
            prepStatment.setInt(1, bookId);

            int rowCount = prepStatment.executeUpdate();
            if (rowCount > 0) {
                isUpdated = true;
                setIssuedBooksToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to Update Book quantity");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpdated;

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
                int oldQuantity = Integer.parseInt(lblDueDateDisplay.getText());
                int updatedQuantity = oldQuantity - 1;
                lblDueDateDisplay.setText(Integer.toString(updatedQuantity));
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

    public boolean returnBook() {
        boolean bookReturned = false;

        try {
            int id = Integer.parseInt(lblIssueIdDisplay.getText());
            int book_id = Integer.parseInt(lblBookIdDisplay.getText());
            Connection conn = DBConnection.getConnection();
            String sqlDelete = "DELETE FROM library_ms.issue_book_details WHERE id = ? AND book_id = ?";
            PreparedStatement prepStat = conn.prepareStatement(sqlDelete);
            prepStat.setInt(1, id);
            prepStat.setInt(2, book_id);
            int rowCount = prepStat.executeUpdate();

            if (rowCount > 0) {
                bookReturned = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookReturned;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_Main = new javax.swing.JPanel();
        panelBookDetails = new javax.swing.JPanel();
        JPnlBack1 = new javax.swing.JPanel();
        lblBack1 = new javax.swing.JLabel();
        pnlBookDetails1 = new javax.swing.JLabel();
        lblBookId = new javax.swing.JLabel();
        lblBookName1 = new javax.swing.JLabel();
        lblStudentName = new javax.swing.JLabel();
        lblIssueDate = new javax.swing.JLabel();
        lblBookNameDisplay = new javax.swing.JLabel();
        lblStudentNameDisplay = new javax.swing.JLabel();
        lblDueDateDisplay = new javax.swing.JLabel();
        lblBookIdDisplay = new javax.swing.JLabel();
        lblDueDate = new javax.swing.JLabel();
        lblIssueDateDisplay = new javax.swing.JLabel();
        lblIssueId = new javax.swing.JLabel();
        lblIssueIdDisplay = new javax.swing.JLabel();
        lblStudentId = new javax.swing.JLabel();
        lblStudentIdDisplay = new javax.swing.JLabel();
        panelStudentDetails = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudents = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblIssuedBooks = new javax.swing.JTable();
        lblStudentTableHeader = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFldStudentId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFldBookId = new javax.swing.JTextField();
        btbReturnBook = new javax.swing.JButton();
        lblValideIdStatus = new javax.swing.JLabel();
        lblValBkStatus = new javax.swing.JLabel();
        btbSelectBook = new javax.swing.JButton();

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
                .addComponent(lblBack1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPnlBack1Layout.setVerticalGroup(
            JPnlBack1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPnlBack1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBack1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelBookDetails.add(JPnlBack1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 60));

        pnlBookDetails1.setFont(new java.awt.Font("Liberation Sans", 1, 25)); // NOI18N
        pnlBookDetails1.setForeground(new java.awt.Color(255, 255, 255));
        pnlBookDetails1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Literature_100px_1.png"))); // NOI18N
        pnlBookDetails1.setText("Issued Book Details");
        pnlBookDetails1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(255, 255, 255)));
        panelBookDetails.add(pnlBookDetails1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 350, -1));

        lblBookId.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblBookId.setForeground(new java.awt.Color(255, 255, 255));
        lblBookId.setText("Book Id:");
        panelBookDetails.add(lblBookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));

        lblBookName1.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblBookName1.setForeground(new java.awt.Color(255, 255, 255));
        lblBookName1.setText("Book Name:");
        panelBookDetails.add(lblBookName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, -1, -1));

        lblStudentName.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblStudentName.setForeground(new java.awt.Color(255, 255, 255));
        lblStudentName.setText("Student Name:");
        panelBookDetails.add(lblStudentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 520, -1, -1));

        lblIssueDate.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblIssueDate.setForeground(new java.awt.Color(255, 255, 255));
        lblIssueDate.setText("Issue Date:");
        panelBookDetails.add(lblIssueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, -1, -1));

        lblBookNameDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblBookNameDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblBookNameDisplay.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelBookDetails.add(lblBookNameDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 370, 140, 30));

        lblStudentNameDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblStudentNameDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblStudentNameDisplay.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelBookDetails.add(lblStudentNameDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 520, 140, 30));

        lblDueDateDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblDueDateDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblDueDateDisplay.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelBookDetails.add(lblDueDateDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 650, 140, 30));

        lblBookIdDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblBookIdDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblBookIdDisplay.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelBookDetails.add(lblBookIdDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 140, 30));

        lblDueDate.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblDueDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDueDate.setText("Due Date:");
        panelBookDetails.add(lblDueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, -1, -1));

        lblIssueDateDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblIssueDateDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblIssueDateDisplay.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelBookDetails.add(lblIssueDateDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 590, 140, 30));

        lblIssueId.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblIssueId.setForeground(new java.awt.Color(255, 255, 255));
        lblIssueId.setText("Issue Id:");
        panelBookDetails.add(lblIssueId, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        lblIssueIdDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblIssueIdDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblIssueIdDisplay.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelBookDetails.add(lblIssueIdDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, 140, 30));

        lblStudentId.setFont(new java.awt.Font("Liberation Sans", 0, 25)); // NOI18N
        lblStudentId.setForeground(new java.awt.Color(255, 255, 255));
        lblStudentId.setText("Student Id:");
        panelBookDetails.add(lblStudentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, -1, -1));

        lblStudentIdDisplay.setFont(new java.awt.Font("Liberation Sans", 0, 20)); // NOI18N
        lblStudentIdDisplay.setForeground(new java.awt.Color(0, 0, 0));
        lblStudentIdDisplay.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        panelBookDetails.add(lblStudentIdDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 450, 140, 30));

        panel_Main.add(panelBookDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 865));

        panelStudentDetails.setBackground(new java.awt.Color(102, 102, 255));
        panelStudentDetails.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Student Id", "Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblStudents);

        panelStudentDetails.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 670, 330));

        tblIssuedBooks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Issue Id", "Book Id", "Student Id", "Book Name", "Issue Date", "Due Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblIssuedBooks);

        panelStudentDetails.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 437, 670, 380));

        lblStudentTableHeader.setFont(new java.awt.Font("Liberation Sans", 1, 25)); // NOI18N
        lblStudentTableHeader.setForeground(new java.awt.Color(0, 0, 0));
        lblStudentTableHeader.setText("Students");
        panelStudentDetails.add(lblStudentTableHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, -1, -1));

        jLabel5.setFont(new java.awt.Font("Liberation Sans", 1, 25)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Issued Books");
        panelStudentDetails.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 400, -1, -1));

        panel_Main.add(panelStudentDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 690, 865));

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 25)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/return_Icon64x.png"))); // NOI18N
        jLabel1.setText("Return Book");
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(0, 0, 0)));
        panel_Main.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 210, -1, -1));

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Enter Student id:");
        panel_Main.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 310, 220, 30));

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
        panel_Main.add(txtFldStudentId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 350, 200, 30));

        jLabel3.setFont(new java.awt.Font("Liberation Sans", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Enter Book Id:");
        panel_Main.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 430, 220, 30));

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
        panel_Main.add(txtFldBookId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 470, 200, 30));

        btbReturnBook.setText("Return Book");
        btbReturnBook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btbReturnBookMouseClicked(evt);
            }
        });
        btbReturnBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbReturnBookActionPerformed(evt);
            }
        });
        panel_Main.add(btbReturnBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 610, 240, -1));

        lblValideIdStatus.setForeground(new java.awt.Color(204, 0, 0));
        panel_Main.add(lblValideIdStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 200, 130, 30));
        panel_Main.add(lblValBkStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 320, 140, 30));

        btbSelectBook.setText("Select Record");
        btbSelectBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbSelectBookActionPerformed(evt);
            }
        });
        panel_Main.add(btbSelectBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 560, 240, -1));

        getContentPane().add(panel_Main, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1541, 865));

        setSize(new java.awt.Dimension(1541, 865));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btbSelectBookActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btbSelectBookActionPerformed
        if (selectBookRecord()) {

        } else {
            JOptionPane.showMessageDialog(this, "Unable to get selected record");
        }
    }// GEN-LAST:event_btbSelectBookActionPerformed

    private void btbReturnBookActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btbReturnBookActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btbReturnBookActionPerformed

    private void btbReturnBookMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_btbReturnBookMouseClicked
        if (returnBook()) {
            JOptionPane.showMessageDialog(this, "Book Returned");
            updateBooks();
        } else {
            JOptionPane.showMessageDialog(this, "Error! Unable to Return Book");
        }
    }// GEN-LAST:event_btbReturnBookMouseClicked

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
        // if (hasQuantity()) {
        // if (!alreadyIssued()) {
        // if (issueBook()) {
        // JOptionPane.showMessageDialog(this, "Successfully Issued Book");
        // updateBookCount();
        // } else {
        // JOptionPane.showMessageDialog(this, "Failed to Issued Book");
        // }
        // } else {
        // JOptionPane.showMessageDialog(this, "Student already has book checked out!");
        // }
        // } else {
        // JOptionPane.showMessageDialog(this, "No Book Copies avaible, Stock less than
        // 1");
        // }

    }// GEN-LAST:event_btbIssueBookActionPerformed

    private void txtFldStudentIdFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtFldStudentIdFocusLost
        // if (txtFldStudentId.getText() != "" &&
        // txtFldStudentId.getText().matches("^[0-9]+$")) {
        // getStudentDetails();
        // lblValideIdStatus.setText("");
        // } else {
        // lblValideIdStatus.setForeground(Color.RED);
        // lblValideIdStatus.setText("Invalid Student Id");
        // }
    }// GEN-LAST:event_txtFldStudentIdFocusLost

    private void txtFldBookIdFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtFldBookIdFocusLost
        if (txtFldBookId.getText() != "" && txtFldBookId.getText().matches("^[0-9]+$")) {
            getBookDetails();
            lblValBkStatus.setText("");
        } else {
            lblValBkStatus.setForeground(Color.RED);
            lblValBkStatus.setText("Invalid Book Id");
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
        java.awt.EventQueue.invokeLater(() -> new ReturnBook().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPnlBack1;
    private javax.swing.JButton btbReturnBook;
    private javax.swing.JButton btbSelectBook;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBack1;
    private javax.swing.JLabel lblBookId;
    private javax.swing.JLabel lblBookIdDisplay;
    private javax.swing.JLabel lblBookName1;
    private javax.swing.JLabel lblBookNameDisplay;
    private javax.swing.JLabel lblDueDate;
    private javax.swing.JLabel lblDueDateDisplay;
    private javax.swing.JLabel lblIssueDate;
    private javax.swing.JLabel lblIssueDateDisplay;
    private javax.swing.JLabel lblIssueId;
    private javax.swing.JLabel lblIssueIdDisplay;
    private javax.swing.JLabel lblStudentId;
    private javax.swing.JLabel lblStudentIdDisplay;
    private javax.swing.JLabel lblStudentName;
    private javax.swing.JLabel lblStudentNameDisplay;
    private javax.swing.JLabel lblStudentTableHeader;
    private javax.swing.JLabel lblValBkStatus;
    private javax.swing.JLabel lblValideIdStatus;
    private javax.swing.JPanel panelBookDetails;
    private javax.swing.JPanel panelStudentDetails;
    private javax.swing.JPanel panel_Main;
    private javax.swing.JLabel pnlBookDetails1;
    private javax.swing.JTable tblIssuedBooks;
    private javax.swing.JTable tblStudents;
    private javax.swing.JTextField txtFldBookId;
    private javax.swing.JTextField txtFldStudentId;
    // End of variables declaration//GEN-END:variables
}
