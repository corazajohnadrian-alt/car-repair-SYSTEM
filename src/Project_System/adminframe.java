/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project_System;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableRowSorter;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 *
 * @author Adrian
 */
public class adminframe extends javax.swing.JFrame {

    private TableRowSorter<DefaultTableModel> requestSorter;
    private String currentRequestFilter = "ALL"; // Track current filter
    private TableRowSorter<DefaultTableModel> inventorySorter;
        static String username;

    
    /**
     * Creates new form adminframe
     */
    static String assigntable = "src\\problems.csv";

    public adminframe(String username) {
        initComponents();
        DefaultTableModel model = (DefaultTableModel) tableInventory1.getModel();
        inventorySorter = new TableRowSorter<>(model);                        
        tableInventory1.setRowSorter(inventorySorter);     
        loadInventoryToTable();
        assigntable();
        loadPartsUsageReport();

        loadDashboard();
        initListener();
        setSize(1330, 744);
        setLocationRelativeTo(null);
        loadRequestsTable();
        initRequestsTableListener();
        
        setupRequestSorter();
        setupSearchListener();
        this.username = username;
        
        
         Tab.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
        public void stateChanged(javax.swing.event.ChangeEvent e) {
            if (Tab.getSelectedComponent() == AssignMechanics) {
                assigntable(); // Refresh the table when tab is selected
            }
            if (Tab.getSelectedComponent() == Reports) {  // ← add this
            loadPartsUsageReport();
        }
          }
        });
         
        //This is for modern scroll bar
        jScrollPane2.setVerticalScrollBar(new Project_System.Design.ScrollBarCustom());
                loadIssuesToTable();
                
                jTextField5.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            performSearch();
        }
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            performSearch();
        }
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            performSearch();
        }
        });
    
    }
    
    private void performSearch() {
    String query = jTextField5.getText().trim();
    if (query.isEmpty()) {
        inventorySorter.setRowFilter(null);
    } else {
        inventorySorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 0, 1, 4));
    }
}
    
    private void setupRequestSorter() {
    DefaultTableModel model = (DefaultTableModel) request.getModel();
    requestSorter = new TableRowSorter<>(model);
    request.setRowSorter(requestSorter);
}

private void setupSearchListener() {
    jTextField7.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void changedUpdate(javax.swing.event.DocumentEvent e) { searchRequests(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { searchRequests(); }
        public void insertUpdate(javax.swing.event.DocumentEvent e) { searchRequests(); }
    });
}

private void searchRequests() {
    if (requestSorter == null) return;
    String searchText = jTextField7.getText().trim();
    if (searchText.isEmpty()) {
        filterByStatus(currentRequestFilter);
    } else {
        RowFilter<Object, Object> statusFilter = getStatusFilter(currentRequestFilter);
        RowFilter<Object, Object> textFilter = RowFilter.regexFilter("(?i)" + searchText, 0, 1, 2, 3);
        if (statusFilter != null) {
            requestSorter.setRowFilter(RowFilter.andFilter(Arrays.asList(statusFilter, textFilter)));
        } else {
            requestSorter.setRowFilter(textFilter);
        }
    }
}

private RowFilter<Object, Object> getStatusFilter(String status) {
    if ("ALL".equals(status)) return null;
    return RowFilter.regexFilter("(?i)" + status, 5);
}

private void filterByStatus(String status) {
    // Complete implementation (see new code for full method)
}

private void showAllRequests() {
    currentRequestFilter = "ALL";
    jTextField7.setText("");
    if (requestSorter != null) requestSorter.setRowFilter(null);
}

private void setupMechanicComboBox() {
    JComboBox<String> mechanicCombobox = new JComboBox<>();
    mechanicCombobox.addItem("Sherwin");
    mechanicCombobox.addItem("MAURING");
    mechanicCombobox.addItem("SEV");
    try {
        tablemechanic.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(mechanicCombobox));
    } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("Column 5 not found");
    }
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel17 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        gradientPanel1 = new Project_System.Design.GradientPanel();
        jLabel1 = new javax.swing.JLabel();
        AdminDbtn = new Project_System.Design.ModernButton();
        AdminRbtn = new Project_System.Design.ModernButton();
        AdminAbtn = new Project_System.Design.ModernButton();
        AdminIbtn = new Project_System.Design.ModernButton();
        AdminRpbtn = new Project_System.Design.ModernButton();
        jButton4 = new Project_System.Design.ModernButton();
        AdminRpbtn1 = new Project_System.Design.ModernButton();
        Tab = new javax.swing.JTabbedPane();
        DashboardPanel = new javax.swing.JPanel();
        DashboardLetter = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        Repairreports1 = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        paymentreports1 = new javax.swing.JTable();
        roundedPanel1 = new Project_System.Design.RoundedPanel();
        jLabel9 = new javax.swing.JLabel();
        totaljob1 = new javax.swing.JLabel();
        roundedPanel2 = new Project_System.Design.RoundedPanel();
        jLabel10 = new javax.swing.JLabel();
        completejob1 = new javax.swing.JLabel();
        roundedPanel3 = new Project_System.Design.RoundedPanel();
        jLabel11 = new javax.swing.JLabel();
        ongoingjob1 = new javax.swing.JLabel();
        roundedPanel4 = new Project_System.Design.RoundedPanel();
        jLabel12 = new javax.swing.JLabel();
        todayrevenue = new javax.swing.JLabel();
        roundedPanel5 = new Project_System.Design.RoundedPanel();
        jLabel13 = new javax.swing.JLabel();
        monthlyrevenue = new javax.swing.JLabel();
        Requests1 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        request = new javax.swing.JTable();
        jTextField7 = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        AssignMechanics = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablemechanic = new javax.swing.JTable();
        Inventory = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        setTreshholdButton = new javax.swing.JButton();
        addStockButton = new javax.swing.JButton();
        historyButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableInventory1 = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        addItemButton = new javax.swing.JButton();
        removeItemButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        AddItem = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtCategory = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtParts = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        txtTreshold = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableInventory2 = new javax.swing.JTable();
        confirmButton = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        Reports = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Repairreports = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        paymentreports = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        partsusagereports = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ongoingjob = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        totaljob = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        completejob = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        issue = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jTextField6 = new javax.swing.JTextField();
        deletebutton = new javax.swing.JButton();
        historyButton1 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        manageissues = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        addissuebottom = new javax.swing.JButton();
        searchButton1 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gradientPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Welcome Admin");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel1.setVerifyInputWhenFocusTarget(false);

        AdminDbtn.setBackground(new java.awt.Color(106, 141, 146));
        AdminDbtn.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        AdminDbtn.setForeground(new java.awt.Color(255, 255, 255));
        AdminDbtn.setText("Dashboard");
        AdminDbtn.setToolTipText("");
        AdminDbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AdminDbtn.setIconTextGap(20);
        AdminDbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminDbtnActionPerformed(evt);
            }
        });

        AdminRbtn.setBackground(new java.awt.Color(106, 141, 146));
        AdminRbtn.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        AdminRbtn.setForeground(new java.awt.Color(255, 255, 255));
        AdminRbtn.setText("Requests");
        AdminRbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AdminRbtn.setIconTextGap(20);
        AdminRbtn.setPreferredSize(new java.awt.Dimension(141, 33));
        AdminRbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminRbtnActionPerformed(evt);
            }
        });

        AdminAbtn.setBackground(new java.awt.Color(106, 141, 146));
        AdminAbtn.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        AdminAbtn.setForeground(new java.awt.Color(255, 255, 255));
        AdminAbtn.setText("Assign Mechanics");
        AdminAbtn.setBorderPainted(false);
        AdminAbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AdminAbtn.setIconTextGap(20);
        AdminAbtn.setPreferredSize(new java.awt.Dimension(141, 33));
        AdminAbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminAbtnActionPerformed(evt);
            }
        });

        AdminIbtn.setBackground(new java.awt.Color(106, 141, 146));
        AdminIbtn.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        AdminIbtn.setForeground(new java.awt.Color(255, 255, 255));
        AdminIbtn.setText("Inventory");
        AdminIbtn.setToolTipText("");
        AdminIbtn.setBorderPainted(false);
        AdminIbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AdminIbtn.setIconTextGap(20);
        AdminIbtn.setPreferredSize(new java.awt.Dimension(141, 33));
        AdminIbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminIbtnActionPerformed(evt);
            }
        });

        AdminRpbtn.setBackground(new java.awt.Color(106, 141, 146));
        AdminRpbtn.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        AdminRpbtn.setForeground(new java.awt.Color(255, 255, 255));
        AdminRpbtn.setText("Reports");
        AdminRpbtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AdminRpbtn.setIconTextGap(20);
        AdminRpbtn.setPreferredSize(new java.awt.Dimension(141, 33));
        AdminRpbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminRpbtnActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(106, 141, 146));
        jButton4.setFont(new java.awt.Font("Roboto", 1, 11)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Log out");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        AdminRpbtn1.setBackground(new java.awt.Color(106, 141, 146));
        AdminRpbtn1.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        AdminRpbtn1.setForeground(new java.awt.Color(255, 255, 255));
        AdminRpbtn1.setText("Manage Issues");
        AdminRpbtn1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AdminRpbtn1.setIconTextGap(20);
        AdminRpbtn1.setPreferredSize(new java.awt.Dimension(141, 33));
        AdminRpbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminRpbtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gradientPanel1Layout = new javax.swing.GroupLayout(gradientPanel1);
        gradientPanel1.setLayout(gradientPanel1Layout);
        gradientPanel1Layout.setHorizontalGroup(
            gradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gradientPanel1Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gradientPanel1Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(gradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(AdminIbtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AdminRpbtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AdminAbtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AdminDbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AdminRbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(AdminRpbtn1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );
        gradientPanel1Layout.setVerticalGroup(
            gradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gradientPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addGap(63, 63, 63)
                .addComponent(AdminDbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(AdminRbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(AdminAbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(AdminIbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(AdminRpbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(AdminRpbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );

        getContentPane().add(gradientPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 750));

        Tab.setEnabled(false);

        DashboardPanel.setBackground(new java.awt.Color(255, 255, 255));

        DashboardLetter.setFont(new java.awt.Font("Roboto", 1, 30)); // NOI18N
        DashboardLetter.setText("Dashboard");

        Repairreports1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        Repairreports1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Req Id", "Customer ", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Repairreports1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        Repairreports1.setAutoscrolls(false);
        Repairreports1.getTableHeader().setReorderingAllowed(false);
        jScrollPane8.setViewportView(Repairreports1);

        paymentreports1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Customer", "Total amount", "Payment status"
            }
        ));
        paymentreports1.getTableHeader().setReorderingAllowed(false);
        jScrollPane9.setViewportView(paymentreports1);

        roundedPanel1.setBackground(new java.awt.Color(0, 122, 122));

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("TOTAL JOB CREATED");

        totaljob1.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        totaljob1.setForeground(new java.awt.Color(255, 255, 255));
        totaljob1.setText("jLabel9");

        javax.swing.GroupLayout roundedPanel1Layout = new javax.swing.GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totaljob1)
                .addGap(108, 108, 108))
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel9)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totaljob1)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        roundedPanel2.setBackground(new java.awt.Color(0, 122, 122));

        jLabel10.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("COMPLETED JOBS");

        completejob1.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        completejob1.setForeground(new java.awt.Color(255, 255, 255));
        completejob1.setText("jLabel10");

        javax.swing.GroupLayout roundedPanel2Layout = new javax.swing.GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel2Layout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(completejob1)))
                .addGap(52, 52, 52))
        );
        roundedPanel2Layout.setVerticalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(completejob1)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        roundedPanel3.setBackground(new java.awt.Color(0, 122, 122));

        jLabel11.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("ONGOING JOBS");

        ongoingjob1.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        ongoingjob1.setForeground(new java.awt.Color(255, 255, 255));
        ongoingjob1.setText("jLabel11");

        javax.swing.GroupLayout roundedPanel3Layout = new javax.swing.GroupLayout(roundedPanel3);
        roundedPanel3.setLayout(roundedPanel3Layout);
        roundedPanel3Layout.setHorizontalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(73, 73, 73))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel3Layout.createSequentialGroup()
                        .addComponent(ongoingjob1)
                        .addGap(101, 101, 101))))
        );
        roundedPanel3Layout.setVerticalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ongoingjob1)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        roundedPanel4.setBackground(new java.awt.Color(0, 122, 122));

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("TODAYS REVENUE");

        todayrevenue.setFont(new java.awt.Font("Roboto", 0, 16)); // NOI18N
        todayrevenue.setForeground(new java.awt.Color(255, 255, 255));
        todayrevenue.setText("jLabel9");

        javax.swing.GroupLayout roundedPanel4Layout = new javax.swing.GroupLayout(roundedPanel4);
        roundedPanel4.setLayout(roundedPanel4Layout);
        roundedPanel4Layout.setHorizontalGroup(
            roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel4Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(roundedPanel4Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(todayrevenue)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        roundedPanel4Layout.setVerticalGroup(
            roundedPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(todayrevenue)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        roundedPanel5.setBackground(new java.awt.Color(0, 122, 122));

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("MONTHLY REVENUE");

        monthlyrevenue.setFont(new java.awt.Font("Roboto", 1, 16)); // NOI18N
        monthlyrevenue.setForeground(new java.awt.Color(255, 255, 255));
        monthlyrevenue.setText("jLabel9");

        javax.swing.GroupLayout roundedPanel5Layout = new javax.swing.GroupLayout(roundedPanel5);
        roundedPanel5.setLayout(roundedPanel5Layout);
        roundedPanel5Layout.setHorizontalGroup(
            roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel5Layout.createSequentialGroup()
                .addGroup(roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel5Layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(monthlyrevenue))
                    .addGroup(roundedPanel5Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel13)))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        roundedPanel5Layout.setVerticalGroup(
            roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(monthlyrevenue)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout DashboardPanelLayout = new javax.swing.GroupLayout(DashboardPanel);
        DashboardPanel.setLayout(DashboardPanelLayout);
        DashboardPanelLayout.setHorizontalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DashboardPanelLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(DashboardPanelLayout.createSequentialGroup()
                                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(DashboardLetter, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(50, 50, 50)
                                .addComponent(roundedPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)
                                .addComponent(roundedPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(DashboardPanelLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(DashboardPanelLayout.createSequentialGroup()
                        .addGap(181, 181, 181)
                        .addComponent(roundedPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(roundedPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(147, Short.MAX_VALUE))
        );
        DashboardPanelLayout.setVerticalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(DashboardLetter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(27, 27, 27)
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roundedPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roundedPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roundedPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roundedPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roundedPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(219, 219, 219))
        );

        Tab.addTab("Dashboard", DashboardPanel);

        Requests1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel28.setText("Requests");

        request.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        request.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "CUSTOMER", "MECHANIC", "VEHICLE", "DATE", "PROGRESS"
            }
        ));
        request.setRowHeight(30);
        jScrollPane4.setViewportView(request);

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jButton21.setText("PENDING");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton23.setText("DONE");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton22.setText("IN PROGRESS");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton24.setText("SEARCH");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(jButton21)))
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jButton23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton22)
                        .addGap(94, 94, 94))))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton23)
                    .addComponent(jButton22)
                    .addComponent(jButton21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout Requests1Layout = new javax.swing.GroupLayout(Requests1);
        Requests1.setLayout(Requests1Layout);
        Requests1Layout.setHorizontalGroup(
            Requests1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Requests1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(Requests1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(201, Short.MAX_VALUE))
        );
        Requests1Layout.setVerticalGroup(
            Requests1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Requests1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128))
        );

        Tab.addTab("Request", Requests1);

        AssignMechanics.setBackground(new java.awt.Color(255, 255, 255));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel29.setText("Assign Mechanic ");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel30.setText("Pending Service Requests ");

        tablemechanic.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tablemechanic.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "REQUEST ID", "CUSTOMER", "VEHICLE", "ISSUE", "DATE", "MECHANIC"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablemechanic.setRowHeight(20);
        jScrollPane5.setViewportView(tablemechanic);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 938, Short.MAX_VALUE)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel30)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AssignMechanicsLayout = new javax.swing.GroupLayout(AssignMechanics);
        AssignMechanics.setLayout(AssignMechanicsLayout);
        AssignMechanicsLayout.setHorizontalGroup(
            AssignMechanicsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        AssignMechanicsLayout.setVerticalGroup(
            AssignMechanicsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Tab.addTab("AssignMechanics", AssignMechanics);

        Inventory.setBackground(new java.awt.Color(255, 255, 255));

        jPanel16.setBackground(new java.awt.Color(153, 153, 153));

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        setTreshholdButton.setText("Set Threshold");
        setTreshholdButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTreshholdButtonActionPerformed(evt);
            }
        });

        addStockButton.setText("Add Stock");
        addStockButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStockButtonActionPerformed(evt);
            }
        });

        historyButton.setText("History");
        historyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historyButtonActionPerformed(evt);
            }
        });

        tableInventory1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tableInventory1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, "", null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, "", null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Category", "Parts", "Quantity", "Price", "Status", "Treshold", "Date Restocked", "Quantity Added"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableInventory1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableInventory1.setRowHeight(20);
        tableInventory1.getTableHeader().setReorderingAllowed(false);
        tableInventory1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableInventory1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableInventory1);
        if (tableInventory1.getColumnModel().getColumnCount() > 0) {
            tableInventory1.getColumnModel().getColumn(1).setHeaderValue("Parts");
            tableInventory1.getColumnModel().getColumn(2).setHeaderValue("Quantity");
            tableInventory1.getColumnModel().getColumn(3).setHeaderValue("Price");
            tableInventory1.getColumnModel().getColumn(4).setHeaderValue("Status");
            tableInventory1.getColumnModel().getColumn(5).setHeaderValue("Treshold");
        }

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel19.setText("Search and Filter ");

        addItemButton.setText("Add Item");
        addItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemButtonActionPerformed(evt);
            }
        });

        removeItemButton.setText("Remove Item");
        removeItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeItemButtonActionPerformed(evt);
            }
        });

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(521, 521, 521))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 463, Short.MAX_VALUE)
                        .addComponent(historyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                .addComponent(removeItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(addItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(setTreshholdButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(addStockButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 932, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(309, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(historyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(setTreshholdButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addStockButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel18.setText("Inventory");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout InventoryLayout = new javax.swing.GroupLayout(Inventory);
        Inventory.setLayout(InventoryLayout);
        InventoryLayout.setHorizontalGroup(
            InventoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        InventoryLayout.setVerticalGroup(
            InventoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Tab.addTab("Inventory", Inventory);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel22.setText("Add Item");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel23.setText("Category");

        txtCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCategoryActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel24.setText("Parts");

        txtParts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPartsActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel25.setText("Treshold");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel26.setText("Quantity");

        txtQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantityActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel27.setText("Price");

        txtTreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTresholdActionPerformed(evt);
            }
        });

        tableInventory2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Category", "Parts", "Quantity", "Price", "Status", "Treshold", "Date Restocked", "Quantity Added"
            }
        ));
        jScrollPane3.setViewportView(tableInventory2);

        confirmButton.setText("Confirm");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        jButton15.setText("Back");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtParts, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtTreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 823, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel22)
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtParts, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(196, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddItemLayout = new javax.swing.GroupLayout(AddItem);
        AddItem.setLayout(AddItemLayout);
        AddItemLayout.setHorizontalGroup(
            AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        AddItemLayout.setVerticalGroup(
            AddItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddItemLayout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        Tab.addTab("tab7", AddItem);

        Reports.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel2.setText("REPORTS");

        Repairreports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Req Id", "Customer ", "Status"
            }
        ));
        jScrollPane1.setViewportView(Repairreports);

        paymentreports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Customer", "Total amount", "Payment status"
            }
        ));
        jScrollPane6.setViewportView(paymentreports);

        partsusagereports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Category", "Partsname ", "Quantity used"
            }
        ));
        jScrollPane7.setViewportView(partsusagereports);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("ONGOING JOBS");

        ongoingjob.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        ongoingjob.setText("jLabel11");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(ongoingjob)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(ongoingjob)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("TOTAL JOB CREATED");

        totaljob.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        totaljob.setText("jLabel9");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(totaljob)))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(totaljob)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("COMPLETED JOBS");

        completejob.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        completejob.setText("jLabel10");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(61, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(completejob)
                .addGap(86, 86, 86))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(completejob)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("REPAIR REPORTS");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("PAYMENT REPORTS");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("PARTS USAGE REPORTS");

        javax.swing.GroupLayout ReportsLayout = new javax.swing.GroupLayout(Reports);
        Reports.setLayout(ReportsLayout);
        ReportsLayout.setHorizontalGroup(
            ReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsLayout.createSequentialGroup()
                .addGroup(ReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 142, Short.MAX_VALUE))
            .addGroup(ReportsLayout.createSequentialGroup()
                .addGroup(ReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReportsLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jLabel2))
                    .addGroup(ReportsLayout.createSequentialGroup()
                        .addGap(371, 371, 371)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(166, 166, 166)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(ReportsLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(153, 153, 153)
                .addComponent(jLabel8)
                .addGap(226, 226, 226))
        );
        ReportsLayout.setVerticalGroup(
            ReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportsLayout.createSequentialGroup()
                .addGroup(ReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReportsLayout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(ReportsLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(ReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(ReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(40, 40, 40)))
                .addGroup(ReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );

        Tab.addTab("Reports", Reports);

        issue.setBackground(new java.awt.Color(255, 255, 255));

        jPanel20.setBackground(new java.awt.Color(153, 153, 153));

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });

        deletebutton.setText("delete issues");
        deletebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebuttonActionPerformed(evt);
            }
        });

        historyButton1.setText("History");
        historyButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historyButton1ActionPerformed(evt);
            }
        });

        manageissues.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        manageissues.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id issue", "Category", "Problems"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        manageissues.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        manageissues.setRowHeight(20);
        manageissues.getTableHeader().setReorderingAllowed(false);
        manageissues.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageissuesMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(manageissues);

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel20.setText("ISSUES");

        addissuebottom.setText("Add Issue");
        addissuebottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addissuebottomActionPerformed(evt);
            }
        });

        searchButton1.setText("Search");
        searchButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(521, 521, 521))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 463, Short.MAX_VALUE)
                        .addComponent(historyButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                                .addComponent(addissuebottom, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(deletebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 932, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(309, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(historyButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addissuebottom, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletebutton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel21.setText("MANAGE ISSUES");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout issueLayout = new javax.swing.GroupLayout(issue);
        issue.setLayout(issueLayout);
        issueLayout.setHorizontalGroup(
            issueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        issueLayout.setVerticalGroup(
            issueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Tab.addTab("Inventory", issue);

        getContentPane().add(Tab, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, -26, 1140, 790));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    public void loadInventoryToTable() {
        DefaultTableModel model1 = (DefaultTableModel) tableInventory1.getModel();
        DefaultTableModel model2 = (DefaultTableModel) tableInventory2.getModel();
        model1.setRowCount(0);
        model2.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader("src\\INVENTORY.csv"))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] data = line.split(",", -1); // -1 keeps empty trailing cols
                model1.addRow(data); // ← was already there
                model2.addRow(data); // ← ADD THIS — was missing
            }
        } catch (IOException e) {
            System.out.println("Error: Could not read INVENTORY.csv");
        }
    }

    public void saveInventoryFromTable() {
        String filePath = "src\\INVENTORY.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            DefaultTableModel model = (DefaultTableModel) tableInventory1.getModel();

            bw.write("Category,Parts,Quantity,Price,Status,Treshold,Date Restocked,Quantity Added");
            bw.newLine();

            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);

                    // Prevents the "null" text from being saved
                    String text = (value == null) ? "" : value.toString().trim();

                    line.append(text);
                    if (j < model.getColumnCount() - 1) {
                        line.append(",");
                    }
                }
                bw.write(line.toString());
                bw.newLine();
            }
        } // This bracket closes the 'try'
        catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }
    
    
    
    /** Reads src\issues.csv and populates the manageissues table. */
    public void loadIssuesToTable() {
        DefaultTableModel model = (DefaultTableModel) manageissues.getModel();
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("src\\issues.csv"))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim(); // strip BOM
                if (line.isEmpty()) continue;
                if (isHeader) { isHeader = false; continue; }
                // CSV: Id, Category, IssueName, suggestedproduct
                String[] data = line.split(",", -1);
                if (data.length >= 3) {
                    model.addRow(new Object[]{
                        data[0].trim(),  // Id issue
                        data[1].trim(),  // Category
                        data[2].trim()   // Problems
                    });
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading issues.csv: " + e.getMessage());
        }
    }
 
    /** Writes the manageissues table back to src\issues.csv. */
    public void saveIssuesToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src\\issues.csv"))) {
            bw.write("Id,Category,IssueName,suggestedproduct");
            bw.newLine();
            DefaultTableModel model = (DefaultTableModel) manageissues.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                String id        = model.getValueAt(i, 0) != null ? model.getValueAt(i, 0).toString().trim() : "";
                String category  = model.getValueAt(i, 1) != null ? model.getValueAt(i, 1).toString().trim() : "";
                String issueName = model.getValueAt(i, 2) != null ? model.getValueAt(i, 2).toString().trim() : "";
                bw.write(id + "," + category + "," + issueName + ",");
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving issues.csv: " + e.getMessage());
        }
    }
 
    // ── END MANAGE ISSUES ─────────────────────────────────────────────────
 
     public void logHistory(String message) {
    // Use a DEDICATED file for inventory history only
    try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter("src\\INVENTORY_HISTORY.csv", true))) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

        String logEntry = dtf.format(now) + " - " + message;

        bw.write(logEntry);
        bw.newLine();
    } catch (java.io.IOException e) {
        System.out.println("Error writing inventory history: " + e.getMessage());
    }}
    
    

    public void updateStatus(int row) {
        try {
            DefaultTableModel model = (DefaultTableModel) tableInventory1.getModel();

            // Get values from the table (Quantity is col 2, Threshold is col 5)
            int currentQty = Integer.parseInt(model.getValueAt(row, 2).toString());
            int threshold = Integer.parseInt(model.getValueAt(row, 5).toString());

            String newStatus;
            if (currentQty <= 0) {
                newStatus = "Out of Stock";
            } else if (currentQty <= threshold) {
                newStatus = "Low Stock";
            } else {
                newStatus = "In Stock";
            }

            // Update the Status column (Column 4)
            model.setValueAt(newStatus, row, 4);

        } catch (NumberFormatException e) {
            // If a cell is empty or not a number, we set a default
            tableInventory1.setValueAt("Check Data", row, 4);
        }
    }

    public void assigntable() {
    BufferedReader reader = null;
    String line;
    boolean firstRow = true;

    try {
        reader = new BufferedReader(new FileReader(assigntable));
        DefaultTableModel model = (DefaultTableModel) tablemechanic.getModel();

        model.setRowCount(0); // CLEAR TABLE FIRST

        // Track which idreq we've already added
        String currentIdReq = "";
        String vehicle = "";
        String model_name = "";
        String date = "";
        String combinedProblems = "";
        String assignedMechanic = ""; // ADDED - tracks mechanic for current request

        while ((line = reader.readLine()) != null) {
            String[] row = line.split(",");
            
            if (firstRow) {
                firstRow = false;
                continue;
            }
            
            if (row.length >= 7) {
                String idReq = row[6].trim();      // idreq
                String username = row[2].trim();   // customer
                String car = row[3].trim();        // vehicle
                String problem = row[4].trim();    // issue
                String dateValue = row[5].trim();  // date
                String mechanic = row.length > 7 ? row[7].trim() : ""; // ADDED - get mechanic from CSV
            
                if (currentIdReq.equals(idReq)) {
                    // Same idreq, append problem
                    combinedProblems += ", " + problem;
                    // If mechanic was empty but now we have one, update it
                    if (!mechanic.isEmpty() && assignedMechanic.isEmpty()) { // ADDED
                        assignedMechanic = mechanic;
                    }
                } else {
                    // New idreq, add previous row if exists
                    if (!currentIdReq.isEmpty()) {
                        model.addRow(new Object[]{
                            currentIdReq,    // id
                            vehicle,         // customer
                            model_name,      // vehicle
                            combinedProblems,// combined issues
                            date,            // date
                            assignedMechanic // mechanic - ADDED
                        });
                    }
                    // Start new row
                    currentIdReq = idReq;
                    vehicle = username;
                    model_name = car;
                    combinedProblems = problem;
                    date = dateValue;
                    assignedMechanic = mechanic; // ADDED - store the mechanic value
                }
            }
        }

        // Add the last row
        if (!currentIdReq.isEmpty()) {
            model.addRow(new Object[]{
                currentIdReq,    // id
                vehicle,         // customer
                model_name,      // vehicle
                combinedProblems,// combined issues
                date,            // date
                assignedMechanic // mechanic - ADDED
            });
        }

        reader.close();
        
        // ADDED - Re-apply the mechanic combo box after loading data
        setupMechanicComboBox();

    } catch (FileNotFoundException ex) {
        Logger.getLogger(adminframe.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(adminframe.class.getName()).log(Level.SEVERE, null, ex);
    }
}

// ADD THIS NEW METHOD - sets up the mechanic dropdown in the table


    public void table(String combinedProblems) {
        tablemechanic.getTableHeader().setFont(new Font("Arial", Font.BOLD, 35));
        tablemechanic.setRowHeight(30);
        tablemechanic.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        JComboBox<String> comboBox = new JComboBox<>();
        JComboBox<String> mechaniccombobox = new JComboBox<>();

        String[] splitIssues = combinedProblems.split(", ");
        for (String p : splitIssues) {
            System.out.println(p);
            comboBox.addItem(p);

        }
        mechaniccombobox.addItem("Sherwin");
        mechaniccombobox.addItem("MAURING");
        mechaniccombobox.addItem("SEV");

        tablemechanic.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(mechaniccombobox));
        tablemechanic.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBox));

    }

    public void initListener() {
        DefaultTableModel model = (DefaultTableModel) tablemechanic.getModel();

        model.addTableModelListener(e -> {

            int row = e.getFirstRow();
            int col = e.getColumn();

            // column 5 only (repairman column)
            if (col == 5 && row >= 0) {

                Object val = model.getValueAt(row, 5);

                if (val != null) {
                    String selectedMechanic = val.toString();
                    System.out.println("Selected mechanic: " + selectedMechanic);

                    // Get the idReq from column 0 to identify which row to update
                    String idReq = model.getValueAt(row, 0).toString();

                    // UPDATE CSV FILE
                    updateRepairmanInCSV(idReq, selectedMechanic);
                }
            }
        });
    }

// NEW METHOD: Update the repairman in CSV file
    private void updateRepairmanInCSV(String idReq, String repairman) {
        try {
            // Read all lines from the CSV
            BufferedReader reader = new BufferedReader(new FileReader(assigntable));
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            int repairmanidissue = repairmanidissue();

            // Update the matching rows
            for (int i = 0; i < lines.size(); i++) {
                String currentLine = lines.get(i);
                String[] row = currentLine.split(",", -1); // -1 to keep empty trailing fields

                // Skip header
                if (i == 0) {
                    continue;
                }

                // Check if this row matches the idReq
                if (row.length >= 7 && row[6].trim().equals(idReq)) {
                    // Update the repairman column (index 7)
                    if (row.length >= 8) {
                        row[7] = repairman;
                        row[0] = String.valueOf(repairmanidissue);
                    } else {
                        // If column doesn't exist, add it
                        row = Arrays.copyOf(row, 8);
                        row[7] = repairman;
                    }

                    // Reconstruct the line
                    lines.set(i, String.join(",", row));
                }
            }

            // Write all lines back to the CSV
            BufferedWriter writer = new BufferedWriter(new FileWriter(assigntable));
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
            writer.close();

            System.out.println("CSV updated successfully! Assigned " + repairman + " to request ID: " + idReq);
            JOptionPane.showMessageDialog(this,
                    "Mechanic assigned successfully!\n" + repairman + " assigned to request #" + idReq,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            System.err.println("Error updating CSV: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error saving to CSV file",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(adminframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int repairmanidissue() {

        int nextId = 1; // default if none found
        BufferedReader reader = null;
        String line;
        boolean firstRow = true;

        try {
            reader = new BufferedReader(new FileReader(assigntable));

            int maxId = 1;

            while ((line = reader.readLine()) != null) {

                // skip header
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                String[] row = line.split(",");

                if (row.length >= 8) {

                    String repairman = row[7]; // repairman
                    String idStr = row[0];     // repairmanidissue

                    if (repairman.equalsIgnoreCase("Sherwin")) {

                        int id = Integer.parseInt(idStr);

                        if (id > maxId) {
                            maxId = id;
                        }
                    }
                }
            }

            nextId = maxId + 1;

            System.out.println("Next ID for Sherwin: " + nextId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nextId; // ✅ fixed
    }

    public void loadRequestsTable() {
        DefaultTableModel requestModel = (DefaultTableModel) request.getModel();
        requestModel.setRowCount(0); // Clear first

        try (BufferedReader reader = new BufferedReader(new FileReader(assigntable))) {
            String line;
            boolean firstRow = true;

            String currentIdReq = "";
            String customer = "";
            String mechanic = "";
            String vehicle = "";
            String date = "";

            while ((line = reader.readLine()) != null) {
                if (firstRow) {
                    firstRow = false;
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] row = line.split(",", -1);
                if (row.length < 7) {
                    continue;
                }

                String idReq = row[6].trim();
                String username = row[2].trim();
                String car = row[3].trim();
                String dateVal = row[5].trim();
                String repair = row.length > 7 ? row[7].trim() : "";

                if (currentIdReq.equals(idReq)) {
                    if (!repair.isEmpty()) {
                        mechanic = repair;
                    }
                } else {
                    if (!currentIdReq.isEmpty()) {
                        requestModel.addRow(new Object[]{
                            currentIdReq, customer, mechanic, vehicle, date, getRequestProgress(currentIdReq)
                        });
                    }
                    currentIdReq = idReq;
                    customer = username;
                    vehicle = car;
                    date = dateVal;
                    mechanic = repair;
                }
            }
            // Add the last group
            if (!currentIdReq.isEmpty()) {
                requestModel.addRow(new Object[]{
                    currentIdReq, customer, mechanic, vehicle, date, "Review"
                });
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading requests: " + ex.getMessage());
        }
    }

    public void initRequestsTableListener() {
    request.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = request.rowAtPoint(evt.getPoint());
            if (row >= 0) {
                DefaultTableModel m = (DefaultTableModel) request.getModel();
                int modelRow = request.convertRowIndexToModel(row);
                String id       = m.getValueAt(modelRow, 0).toString();
                String customer = m.getValueAt(modelRow, 1).toString();
                String mechanic = m.getValueAt(modelRow, 2).toString();
                String vehicle  = m.getValueAt(modelRow, 3).toString();

                RequestsPopUp popup = new RequestsPopUp();
                popup.setServiceDetails(id, customer, mechanic, vehicle);
                popup.setVisible(true);
            }
        }
    });
}

    private void AdminDbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminDbtnActionPerformed
        loadDashboard();
        Tab.setSelectedComponent(DashboardPanel);
    }//GEN-LAST:event_AdminDbtnActionPerformed

    private void AdminRbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminRbtnActionPerformed
        Tab.setSelectedIndex(1);
    }//GEN-LAST:event_AdminRbtnActionPerformed

    private void AdminAbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminAbtnActionPerformed
        Tab.setSelectedIndex(2);
    }//GEN-LAST:event_AdminAbtnActionPerformed

    private void AdminIbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminIbtnActionPerformed
        Tab.setSelectedIndex(3);
    }//GEN-LAST:event_AdminIbtnActionPerformed

    public void loadDashboard() {

        // ── COUNT STATS from problems.csv ──
        // Total jobs = unique idreq values
        java.util.Set<String> uniqueJobs = new java.util.HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src\\problems.csv"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] cols = line.split(",", -1);
                if (cols.length >= 7) {
                    uniqueJobs.add(cols[6].trim()); // idreq col
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int totalJobs = uniqueJobs.size();

        // Completed jobs = unique customer+idcustomer where latest status = READY FOR PICKUP
        java.util.Map<String, String> latestStatusMap = new java.util.LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src\\updatestatus.csv"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] cols = line.split(",", -1);
                if (cols.length < 6) {
                    continue;
                }
                String key = cols[1].trim() + "|" + cols[5].trim();
                latestStatusMap.put(key, cols[3].trim()); // overwrite = keeps latest
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int completedJobs = 0;
        for (String status : latestStatusMap.values()) {
            if (status.equalsIgnoreCase("READY FOR PICKUP")) {
                completedJobs++;
            }
        }
        int ongoingJobs = totalJobs - completedJobs;

        // ── SET BOTH SETS OF LABELS ──
        String total = String.valueOf(totalJobs);
        String completed = String.valueOf(completedJobs);
        String ongoing = String.valueOf(ongoingJobs);

        totaljob.setText(total);
        totaljob1.setText(total);
        completejob.setText(completed);
        completejob1.setText(completed);
        ongoingjob.setText(ongoing);
        ongoingjob1.setText(ongoing);

        // ── DASHBOARD TABLE 1: Repairreports1 — latest status per customer ──
        DefaultTableModel repairModel1 = (DefaultTableModel) Repairreports1.getModel();
        repairModel1.setRowCount(0);

        java.util.Map<String, String[]> latestStatus = new java.util.LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src\\updatestatus.csv"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] cols = line.split(",", -1);
                if (cols.length < 6) {
                    continue;
                }
                String key = cols[1].trim() + "|" + cols[5].trim();
                latestStatus.put(key, new String[]{cols[0].trim(), cols[1].trim(), cols[3].trim()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String[] row : latestStatus.values()) {
            repairModel1.addRow(new Object[]{row[0], row[1], row[2]});
        }

        // ── DASHBOARD TABLE 2: paymentreports1 — from reciept.csv ──
        DefaultTableModel payModel1 = (DefaultTableModel) paymentreports1.getModel();
        payModel1.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader("src\\reciept.csv"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                // cols: id(0),samecustomerid(1),customer(2),DATE(3),payment(4),totalcost(5)
                String[] cols = line.split(",", 7);
                if (cols.length < 6) {
                    continue;
                }
                payModel1.addRow(new Object[]{
                    cols[0].trim(),
                    cols[2].trim(),
                    "₱" + cols[5].trim(),
                    cols[4].trim()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ── TODAY'S and MONTHLY REVENUE from reciept.csv ──
        String today = new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date());
        String thisMonth = new java.text.SimpleDateFormat("MM/").format(new java.util.Date());
        String thisYear = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date());

        int dailyTotal = 0;
        int monthlyTotal = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("src\\reciept.csv"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] cols = line.split(",", 7);
                if (cols.length < 6) {
                    continue;
                }
                String date = cols[3].trim(); // MM/dd/yyyy HH:mm:ss
                int amount = 0;
                try {
                    amount = Integer.parseInt(cols[5].trim());
                } catch (Exception ignore) {
                }

                if (date.startsWith(today)) {
                    dailyTotal += amount;
                }
                if (date.startsWith(thisMonth) && date.contains(thisYear)) {
                    monthlyTotal += amount;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        todayrevenue.setText("₱" + String.format("%,d", dailyTotal));
        monthlyrevenue.setText("₱" + String.format("%,d", monthlyTotal));
    }

    public void loadReports() {

        // ── TABLE 1: Repairreports — latest status per customer from updatestatus.csv ──
        DefaultTableModel repairModel = (DefaultTableModel) Repairreports.getModel();
        repairModel.setRowCount(0);

        // key = "customer|idcustomer", value = [id, customer, status]
        java.util.Map<String, String[]> latestStatus = new java.util.LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src\\updatestatus.csv"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (first) {
                    first = false;
                    continue;
                }
                // cols: id(0),customer(1),repairman(2),diagnoses(3),customersame(4),idcustomer(5)
                String[] cols = line.split(",", -1);
                if (cols.length < 6) {
                    continue;
                }
                String key = cols[1].trim() + "|" + cols[5].trim();
                // overwrite so only latest status per customer+slot remains
                latestStatus.put(key, new String[]{
                    cols[0].trim(), // id = Req Id
                    cols[1].trim(), // customer
                    cols[3].trim() // diagnoses = status
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String[] row : latestStatus.values()) {
            repairModel.addRow(new Object[]{row[0], row[1], row[2]});
        }

        // ── TABLE 2: paymentreports — from reciept.csv ──
        DefaultTableModel payModel = (DefaultTableModel) paymentreports.getModel();
        payModel.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader("src\\reciept.csv"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (first) {
                    first = false;
                    continue;
                }
                // cols: id(0),samecustomerid(1),customer(2),DATE(3),payment(4),totalcost(5),reciept(6)
                String[] cols = line.split(",", 7);
                if (cols.length < 6) {
                    continue;
                }
                payModel.addRow(new Object[]{
                    cols[0].trim(), // ID
                    cols[2].trim(), // Customer
                    "₱" + cols[5].trim(), // Total amount
                    cols[4].trim() // Payment status (CASH/GCASH etc)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ── TABLE 3: partsusagereports — INVENTORY.csv + cartrequest.csv ──
        DefaultTableModel partsModel = (DefaultTableModel) partsusagereports.getModel();
        partsModel.setRowCount(0);

        // Step 1: Load all parts from INVENTORY.csv
        // key = partname lowercase, value = [category, partname, quantity(stock)]
        java.util.Map<String, String[]> inventoryMap = new java.util.LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src\\INVENTORY.csv"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (first) {
                    first = false;
                    continue;
                }
                String[] cols = line.split(",", -1);
                if (cols.length < 3) {
                    continue;
                }
                String category = cols[0].trim();
                String partName = cols[1].trim();
                String stock = cols[2].trim();
                inventoryMap.put(partName.toLowerCase(), new String[]{category, partName, stock});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Step 2: Sum qty used per part from deductinventory.csv
        java.util.Map<String, Integer> qtyUsed = new java.util.LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src\\usedparts.csv"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (first) {
                    first = false;
                    continue;
                }
                // cols: category(0), parts(1), qtyused(2)
                String[] cols = line.split(",", -1);
                if (cols.length < 3 || cols[1].trim().isEmpty()) {
                    continue;
                }
                String partName = cols[1].trim();
                int qty = 0;
                try {
                    qty = Integer.parseInt(cols[2].trim());
                } catch (Exception ignore) {
                }
                qtyUsed.merge(partName.toLowerCase(), qty, Integer::sum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Step 3: Fill table — only parts that were actually used
        for (java.util.Map.Entry<String, Integer> entry : qtyUsed.entrySet()) {
            String key = entry.getKey();
            int used = entry.getValue();
            String[] inv = inventoryMap.get(key);

            String category = inv != null ? inv[0] : "—";
            String partName = inv != null ? inv[1] : key;
            int stock = 0;
            if (inv != null) {
                try {
                    stock = Integer.parseInt(inv[2]);
                } catch (Exception ignore) {
                }
            }
            int remaining = stock - used;

            partsModel.addRow(new Object[]{category, partName, used, remaining});
        }
    }

    private void AdminRpbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminRpbtnActionPerformed
        loadReports();
    Tab.setSelectedComponent(Reports);    }//GEN-LAST:event_AdminRpbtnActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        DefaultTableModel obj = (DefaultTableModel) tableInventory1.getModel();
    }//GEN-LAST:event_jTextField5KeyReleased

    private void setTreshholdButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTreshholdButtonActionPerformed
        // 1. Get the row the user clicked on
        int row = tableInventory1.getSelectedRow();

        // 2. Check if a row was actually selected
        if (row != -1) {
            // 3. Create the popup and PASS 'this' and 'row'
            setThreshold thresholdFrame = new setThreshold(this, row);

            // Keep your existing WindowListener to refresh the table when closed
            thresholdFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    loadInventoryToTable(); // This refreshes your view
                }
            });

            thresholdFrame.setVisible(true);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an item from the table first!");
        }


    }//GEN-LAST:event_setTreshholdButtonActionPerformed

    private void addStockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStockButtonActionPerformed
        int row = tableInventory1.getSelectedRow();
        if (row != -1) {
            addStock popup = new addStock();
            popup.admin = this;       // Pass this frame to the popup
            popup.selectedRow = row;  // Pass the selected row index
            popup.setVisible(true);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an item first!");
        }
    }//GEN-LAST:event_addStockButtonActionPerformed

    private void txtCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCategoryActionPerformed

    private void txtTresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTresholdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTresholdActionPerformed

    private void addItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemButtonActionPerformed
        Tab.setSelectedIndex(4);
    }//GEN-LAST:event_addItemButtonActionPerformed

    private void txtPartsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPartsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartsActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        // 1. Get data from TextFields
        String cat = txtCategory.getText().trim();
        String part = txtParts.getText().trim();
        String qtyStr = txtQuantity.getText().trim();
        String price = txtPrice.getText().trim();
        String threshStr = txtTreshold.getText().trim();

        try {
            int qty = Integer.parseInt(qtyStr);
            int thresh = Integer.parseInt(threshStr);
            String status;

            // Status Logic
            if (qty <= 0) {
                status = "Out of Stock";
            } else if (qty <= thresh) {
                status = "Low Stock";
            } else if (qty >= 50) {
                status = "At Capacity";
            } else {
                status = "Available";
            }

            // 2. Add the row to the table
            DefaultTableModel model = (DefaultTableModel) tableInventory2.getModel();
            Object[] newRow = {cat, part, qty, price, status, thresh};
            model.addRow(newRow);

            // --- NEW SORTING LOGIC ---
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            tableInventory2.setRowSorter(sorter);

            // This sorts by the "Parts" column (Index 1) in Ascending order
            java.util.List<RowSorter.SortKey> sortKeys = new java.util.ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
            sorter.sort(); // This triggers the alphabetical update instantly
            // -------------------------

            // 3. Update the CSV file
            java.io.FileWriter fw = new java.io.FileWriter("src\\INVENTORY.csv", true);
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(fw)) {
                bw.write(cat + "," + part + "," + qty + "," + price + "," + status + "," + thresh);
                bw.newLine();
            }

            logHistory("New item added: " + part + " (Category: " + cat + ")");
            javax.swing.JOptionPane.showMessageDialog(this, "Item added and sorted!");

            // 4. Clear fields
            txtCategory.setText("");
            txtParts.setText("");
            txtQuantity.setText("");
            // ... (clear other fields)

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Check your number inputs!");
        } catch (java.io.IOException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "CSV Error: " + e.getMessage());
        }
    }

// Helper method to reset the UI
    private void clearFields() {
        txtCategory.setText("");
        txtParts.setText("");
        txtQuantity.setText("");
        txtPrice.setText("");
        txtTreshold.setText("");
        txtTreshold.setText("");

    }//GEN-LAST:event_confirmButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // 1. Get the text from your search field (make sure the variable name is correct)
        String query = jTextField5.getText().trim();

        // 2. Get the table model
        DefaultTableModel model = (DefaultTableModel) tableInventory1.getModel();

        // 3. Create the Sorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tableInventory1.setRowSorter(sorter);

        // 4. Apply the filter (Case-insensitive)
        if (query.length() == 0) {
            sorter.setRowFilter(null); // Shows everything if search is empty
        } else {
            // (?i) makes it ignore Capital/Small letter differences
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void txtQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantityActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        Tab.setSelectedIndex(3);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void tableInventory1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableInventory1MouseClicked

    }//GEN-LAST:event_tableInventory1MouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

    }//GEN-LAST:event_formWindowActivated

    private void removeItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeItemButtonActionPerformed
        int selectedRow = tableInventory1.getSelectedRow();

        if (selectedRow != -1) {
            // Ask for confirmation so you don't delete by mistake
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove this item?", "Confirm Deletion",
                    javax.swing.JOptionPane.YES_NO_OPTION);

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                // 1. Get the item name BEFORE removing it from the table
                String itemName = tableInventory1.getValueAt(selectedRow, 1).toString();

                DefaultTableModel model = (DefaultTableModel) tableInventory1.getModel();

                // 2. Remove from the UI table
                model.removeRow(selectedRow);

                // 3. Save the new table state to the CSV immediately
                saveInventoryFromTable();

                // 4. Log the removal to HISTORY.csv
                logHistory(itemName + " was removed from the inventory.");

                javax.swing.JOptionPane.showMessageDialog(this, "Item removed successfully.");
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an item to remove.");
        }
    }//GEN-LAST:event_removeItemButtonActionPerformed
    private String getRequestProgress(String idReq) {
    try (BufferedReader br = new BufferedReader(new FileReader("src\\updatestatus.csv"))) {
        String line;
        boolean first = true;
        while ((line = br.readLine()) != null) {
            if (first) {
                first = false;
                continue;
            }
            String[] cols = line.split(",", -1);
            if (cols.length >= 4 && cols[0].trim().equals(idReq)) {
                return cols[3].trim(); // Status column
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "PENDING"; // Default status
}
    private void loadPartsUsageReport() {
    DefaultTableModel model = (DefaultTableModel) partsusagereports.getModel();
    model.setRowCount(0);

    java.io.File file = new java.io.File("src\\deductinventory.csv");
    if (!file.exists()) return;

    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
        String line;
        boolean firstLine = true;
        while ((line = br.readLine()) != null) {
            if (firstLine) { firstLine = false; continue; } // skip header
            String[] cols = line.split(",", -1);
            if (cols.length >= 3) {
                model.addRow(new Object[]{ cols[0].trim(), cols[1].trim(), cols[2].trim() });
            }
        }
    } catch (java.io.IOException e) {
        e.printStackTrace();
    }
}
    private void historyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyButtonActionPerformed
        // 1. Create the history window
        historyFrame hf = new historyFrame();

        // 2. Make it visible
        hf.setVisible(true);

        // 3. IMPORTANT: Tell it to load the text from the CSV
        hf.loadHistoryData();
    }//GEN-LAST:event_historyButtonActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        currentRequestFilter = "PENDING";
    filterByStatus("PENDING");
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        currentRequestFilter = "DONE";
    filterByStatus("DONE");
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        currentRequestFilter = "IN PROGRESS";
    filterByStatus("IN PROGRESS");
    }//GEN-LAST:event_jButton22ActionPerformed

    private void AdminRpbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminRpbtn1ActionPerformed
        Tab.setSelectedComponent(issue);
    }//GEN-LAST:event_AdminRpbtn1ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6KeyReleased

    private void historyButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_historyButton1ActionPerformed

    private void manageissuesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageissuesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_manageissuesMouseClicked

    private void addissuebottomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addissuebottomActionPerformed
         AddIssueFrame popup = new AddIssueFrame(this);
        popup.setVisible(true);
    }//GEN-LAST:event_addissuebottomActionPerformed

    private void searchButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchButton1ActionPerformed

    private void deletebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebuttonActionPerformed
        int selectedRow = manageissues.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an issue to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this issue?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) manageissues.getModel();
            model.removeRow(selectedRow);
            saveIssuesToCSV();
            JOptionPane.showMessageDialog(this, "Issue deleted successfully.");
        }
    }//GEN-LAST:event_deletebuttonActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Open the Login window
        LOGIN loginWindow = new LOGIN();
        loginWindow.setVisible(true);

        // Close this frame
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

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
            java.util.logging.Logger.getLogger(adminframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(adminframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(adminframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(adminframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new adminframe(username).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AddItem;
    private javax.swing.JButton AdminAbtn;
    private javax.swing.JButton AdminDbtn;
    private javax.swing.JButton AdminIbtn;
    private javax.swing.JButton AdminRbtn;
    private javax.swing.JButton AdminRpbtn;
    private javax.swing.JButton AdminRpbtn1;
    private javax.swing.JPanel AssignMechanics;
    private javax.swing.JLabel DashboardLetter;
    private javax.swing.JPanel DashboardPanel;
    private javax.swing.JPanel Inventory;
    private javax.swing.JTable Repairreports;
    private javax.swing.JTable Repairreports1;
    private javax.swing.JPanel Reports;
    private javax.swing.JPanel Requests1;
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JButton addItemButton;
    private javax.swing.JButton addStockButton;
    private javax.swing.JButton addissuebottom;
    private javax.swing.JLabel completejob;
    private javax.swing.JLabel completejob1;
    private javax.swing.JButton confirmButton;
    private javax.swing.JButton deletebutton;
    private Project_System.Design.GradientPanel gradientPanel1;
    private javax.swing.JButton historyButton;
    private javax.swing.JButton historyButton1;
    private javax.swing.JPanel issue;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    public javax.swing.JTable manageissues;
    private javax.swing.JLabel monthlyrevenue;
    private javax.swing.JLabel ongoingjob;
    private javax.swing.JLabel ongoingjob1;
    private javax.swing.JTable partsusagereports;
    private javax.swing.JTable paymentreports;
    private javax.swing.JTable paymentreports1;
    private javax.swing.JButton removeItemButton;
    private javax.swing.JTable request;
    private Project_System.Design.RoundedPanel roundedPanel1;
    private Project_System.Design.RoundedPanel roundedPanel2;
    private Project_System.Design.RoundedPanel roundedPanel3;
    private Project_System.Design.RoundedPanel roundedPanel4;
    private Project_System.Design.RoundedPanel roundedPanel5;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton searchButton1;
    private javax.swing.JButton setTreshholdButton;
    public javax.swing.JTable tableInventory1;
    private javax.swing.JTable tableInventory2;
    private javax.swing.JTable tablemechanic;
    private javax.swing.JLabel todayrevenue;
    private javax.swing.JLabel totaljob;
    private javax.swing.JLabel totaljob1;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtParts;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtTreshold;
    // End of variables declaration//GEN-END:variables

    
    

}
