/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project_System;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Edmer
 */
public class RequestsPopUp extends javax.swing.JFrame {
    boolean isEditAllowed = false;
    DefaultTableModel model;
 
    
    // Stored for approve / save actions
    private String currentId       = "";
    private String currentCustomer = "";
    private String currentMechanic = "";
    private String currentVehicle  = "";
    private String mediaFilePath = "";
 
    // CSV paths — match what the rest of the project uses
    private static final String PROBLEMS_CSV    = "src\\problems.csv";
    private static final String CARTREQUEST_CSV = "src\\cartrequest.csv";
    private static final String LABOURFEE_CSV   = "src\\labourfee.csv";
 
    public RequestsPopUp() {
        initComponents();
        // FIX #1: DISPOSE instead of EXIT so closing popup doesn't kill the app
        setupTableModelExpenses();
        this.setLocationRelativeTo(null);
    }
 
    private void setupTableModelExpenses() {
    String[] columns = {"Expenses Breakdown", "Amount (₱)"};
    model = new DefaultTableModel(new Object[][]{}, columns) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    expensesTable.setModel(model);
}
    
     private void loadPartsAndExpenses(String idreq, String customer) {
 
        // ── 1. Labour fees map ──
        Map<String, Integer> labourMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LABOURFEE_CSV))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] c = line.split(",", -1);
                if (c.length >= 2) {
                    try {
                        labourMap.put(c[0].trim().toLowerCase(), Integer.parseInt(c[1].trim()));
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (Exception e) {
            System.err.println("Could not load labourfee.csv: " + e.getMessage());
        }
 
        // ── 2. Issues for this idreq (to calculate labour) ──
        List<String> issues = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PROBLEMS_CSV))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] c = line.split(",", -1);
                if (c.length < 7) continue;
                if (!c[6].trim().equals(idreq)) continue;
                if (!c[2].trim().equalsIgnoreCase(customer)) continue;
                issues.add(c[4].trim());
            }
        } catch (Exception e) {
            System.err.println("Could not re-read problems.csv: " + e.getMessage());
        }
 
        // ── 3. Parts from cartrequest.csv ──
        List<Object[]> partRows = new ArrayList<>(); // [partName, qty, price, total]
        int partsTotal = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(CARTREQUEST_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (line.toLowerCase().startsWith("orderid")) continue;
                if (line.startsWith("---") || line.contains("GRAND TOTAL")) continue;
 
                String[] c = line.split(",", -1);
                if (c.length < 10) continue;
                if (!c[6].trim().equalsIgnoreCase(customer)) continue;
                if (!c[9].trim().equals(idreq)) continue;

                String partName = c[2].trim();
                String qty      = c[3].trim();
                String price    = c[4].trim();
                int    rowTotal = 0;
                try { rowTotal = Integer.parseInt(c[5].trim()); } catch (Exception ignored) {}
                partsTotal += rowTotal;
                int unitPrice = 0;
                try { unitPrice = Integer.parseInt(price.isEmpty() ? "0" : price); } catch (Exception ignored) {}
                partRows.add(new Object[]{partName, qty,
                 "₱" + String.format("%,d", unitPrice),
                 "₱" + String.format("%,d", rowTotal)});
            }
        } catch (Exception e) {
            System.err.println("Could not load cartrequest.csv: " + e.getMessage());
        }
 
        // ── 4. Fill jTable1: Parts & Materials ──
        DefaultTableModel partsModel = new DefaultTableModel(
            new String[]{"Parts & Materials", "Qty", "Unit Price", "Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Object[] row : partRows) partsModel.addRow(row);
        jTable1.setModel(partsModel);
 
        // ── 5. Fill expensesTable: Expenses Breakdown ──
        model.setRowCount(0);
 
        int labourTotal = 0;
 
        // Labour section header
        model.addRow(new Object[]{"── LABOUR FEES ──", ""});
        for (String issue : issues) {
            int fee = labourMap.getOrDefault(issue.toLowerCase(), 0);
            labourTotal += fee;
            model.addRow(new Object[]{issue, String.format("₱%,d", fee)});
        }
        model.addRow(new Object[]{"Labour Subtotal", String.format("₱%,d", labourTotal)});
 
        // Parts section header
        if (!partRows.isEmpty()) {
            model.addRow(new Object[]{"── PARTS ──", ""});
            for (Object[] row : partRows) {
                model.addRow(new Object[]{row[0] + " x" + row[1], row[3]});
            }
            model.addRow(new Object[]{"Parts Subtotal", String.format("₱%,d", partsTotal)});
        }
 
        // Grand total
        int grandTotal = labourTotal + partsTotal;
        model.addRow(new Object[]{"══ GRAND TOTAL ══", String.format("₱%,d", grandTotal)});
    }

 public void setServiceDetails(String id, String customer, String mechanic, String vehicle) {
    this.currentId       = id;
    this.currentCustomer = customer;
    this.currentMechanic = mechanic;
    this.currentVehicle  = vehicle;

    serviceIdLabel.setText("Service # " + id);
    ownerLabel.setText("Owner's Name: " + customer);
    mechanicLabel.setText("Mechanic Name: " + (mechanic.isEmpty() ? "Not yet assigned" : mechanic));
    vehicleLabel.setText("Vehicle: " + vehicle);

    loadCarDetails(customer, id);
    loadProblems(id, customer);
    loadPartsAndExpenses(id, customer);
    loadMediaPath(id, customer);

    this.setVisible(true);
    this.pack();
    this.setLocationRelativeTo(null);
}

 private void loadCarDetails(String customer, String idreq) {
    // ── Step 1: Find ALL matching car slots for this customer + idreq ──
    // problems.csv: repairmanissuenumber(0), issuenumber(1), username(2),
    //               car(3), problems(4), date(5), idreq(6), repairman(7)
    String carSlot = "";

    try (java.io.BufferedReader br = new java.io.BufferedReader(
            new java.io.FileReader(PROBLEMS_CSV))) {
        String line;
        boolean first = true;
        while ((line = br.readLine()) != null) {
            if (first) { first = false; continue; }
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] c = line.split(",", -1);
            if (c.length < 7) continue;

            String rowIdreq    = c[6].trim();
            String rowCustomer = c[2].trim();

            if (rowIdreq.equals(idreq) &&
                rowCustomer.equalsIgnoreCase(customer)) {
                carSlot = c[0].trim(); // repairmanissuenumber = car slot (1,2,3)
                break;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    if (carSlot.isEmpty()) {
        plateNumberLabel.setText("Plate Number: N/A");
        showNoImageLabel("No car slot found for this request");
        return;
    }

    // ── Step 2: Look up car in CAR REPAIRS.csv by slot + customer ──
    // cols: customerid(0), ownername(1), plateNum(2), model(3), brand(4),
    //       year(5), color(6), mileage(7), link(8)
    String plateNum  = "N/A";
    String model     = "N/A";
    String brand     = "N/A";
    String year      = "N/A";
    String color     = "N/A";
    String imagePath = "";

    try (java.io.BufferedReader br = new java.io.BufferedReader(
            new java.io.FileReader("src\\CAR REPAIRS.csv"))) {
        String line;
        boolean first = true;
        while ((line = br.readLine()) != null) {
            if (first) { first = false; continue; }
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] c = line.split(",", -1);
            if (c.length < 9) continue;

            String rowSlot     = c[0].trim();
            String rowCustomer = c[1].trim();

            // Match by BOTH slot number AND customer name
            if (rowSlot.equals(carSlot) &&
                rowCustomer.equalsIgnoreCase(customer)) {
                plateNum  = c[2].trim();
                model     = c[3].trim();
                brand     = c.length > 4 ? c[4].trim() : "N/A";
                year      = c.length > 5 ? c[5].trim() : "N/A";
                color     = c.length > 6 ? c[6].trim() : "N/A";
                imagePath = c.length > 8 ? c[8].trim() : "";
                break;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    

    // ── Step 3: Update all info labels ──
    plateNumberLabel.setText("Plate Number: " + plateNum);
    vehicleLabel.setText("Vehicle: " + brand + " " + model + " (" + year + ") — " + color);
    mechanicLabel.setText("Mechanic Name: " +
        (currentMechanic == null || currentMechanic.isEmpty()
            ? "Not yet assigned" : currentMechanic));
    ownerLabel.setText("Owner's Name: " + customer);

    // ── Step 4: Load image with full fallback ──
    loadImageIntoCarpicture(imagePath);
}
 
 private void loadImageIntoCarpicture(String imagePath) {
    // Always clear first
    Carpicture.removeAll();
    Carpicture.setLayout(new java.awt.BorderLayout());

    boolean loaded = false;

    // ── Try loading the file if path is given ──
    if (imagePath != null && !imagePath.trim().isEmpty()) {
        java.io.File imgFile = new java.io.File(imagePath.trim());

        if (imgFile.exists() && imgFile.isFile()) {
            try {
                java.awt.image.BufferedImage bimg = javax.imageio.ImageIO.read(imgFile);

                if (bimg != null) {
                    // Scale to fit panel — any image type works (jpg, png, gif, bmp)
                    int panelW = Math.max(Carpicture.getWidth(),  315);
                    int panelH = Math.max(Carpicture.getHeight(), 181);

                    java.awt.Image scaled = bimg.getScaledInstance(
                        panelW, panelH, java.awt.Image.SCALE_SMOOTH);

                    javax.swing.JLabel imgLabel = new javax.swing.JLabel(
                        new javax.swing.ImageIcon(scaled));
                    imgLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    imgLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);

                    Carpicture.add(imgLabel, java.awt.BorderLayout.CENTER);
                    loaded = true;
                }
            } catch (Exception e) {
                // Image exists but couldn't be read — fall through to placeholder
                System.err.println("Could not read image: " + imagePath + " — " + e.getMessage());
            }
        }
    }

    // ── Fallback: always show something even if image fails ──
    if (!loaded) {
        javax.swing.JLabel placeholder = new javax.swing.JLabel(
            "<html><center>🚗<br>No Image Available</center></html>",
            javax.swing.SwingConstants.CENTER);
        placeholder.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 14));
        placeholder.setForeground(java.awt.Color.GRAY);
        Carpicture.add(placeholder, java.awt.BorderLayout.CENTER);
    }

    Carpicture.revalidate();
    Carpicture.repaint();
}
 
 private void showNoImageLabel(String msg) {
    Carpicture.removeAll();
    Carpicture.setLayout(new java.awt.BorderLayout());

    javax.swing.JLabel placeholder = new javax.swing.JLabel(
        "<html><center>🚗<br>" + msg + "</center></html>",
        javax.swing.SwingConstants.CENTER);
    placeholder.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 13));
    placeholder.setForeground(java.awt.Color.GRAY);

    Carpicture.add(placeholder, java.awt.BorderLayout.CENTER);
    Carpicture.revalidate();
    Carpicture.repaint();
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        requestsPopUp = new javax.swing.JPanel();
        serviceIdLabel = new javax.swing.JLabel();
        Details = new javax.swing.JLabel();
        Carpicture = new javax.swing.JPanel();
        vehicleLabel = new javax.swing.JLabel();
        plateNumberLabel = new javax.swing.JLabel();
        mechanicLabel = new javax.swing.JLabel();
        ownerLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        problemTextArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        expensesTable = new javax.swing.JTable();
        backbutton = new javax.swing.JButton();
        viewphotoorvideo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Service Request");
        setName("requestPopUp"); // NOI18N
        setType(java.awt.Window.Type.POPUP);

        requestsPopUp.setBackground(new java.awt.Color(255, 255, 255));

        serviceIdLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        serviceIdLabel.setText("Service #");
        serviceIdLabel.setToolTipText("");

        Details.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Details.setText("Details");

        Carpicture.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        javax.swing.GroupLayout CarpictureLayout = new javax.swing.GroupLayout(Carpicture);
        Carpicture.setLayout(CarpictureLayout);
        CarpictureLayout.setHorizontalGroup(
            CarpictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 315, Short.MAX_VALUE)
        );
        CarpictureLayout.setVerticalGroup(
            CarpictureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );

        vehicleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        vehicleLabel.setText("Vehicle:");
        vehicleLabel.setToolTipText("");

        plateNumberLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        plateNumberLabel.setText("Plate Number:");

        mechanicLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        mechanicLabel.setText("Mechanic Name:");

        ownerLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        ownerLabel.setText("Owner's Name:");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTable1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Parts and Materials", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setCellSelectionEnabled(true);
        jTable1.setDropMode(javax.swing.DropMode.ON);
        jTable1.setSelectionBackground(new java.awt.Color(120, 120, 120));
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable1);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        problemTextArea.setColumns(20);
        problemTextArea.setRows(5);
        problemTextArea.setText("Car Issues\nReported Problem\n   \"Brakes not working properly.\" \n\n- temporary rani kani diri kay mo read\nna siya sa customer or mecaniko.");
        problemTextArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(problemTextArea);

        expensesTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        expensesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Expenses Breakdown", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        expensesTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        expensesTable.setSelectionBackground(new java.awt.Color(120, 120, 120));
        expensesTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(expensesTable);

        backbutton.setText("Back");
        backbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbuttonActionPerformed(evt);
            }
        });

        viewphotoorvideo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        viewphotoorvideo.setText("VIEW videos/photo");
        viewphotoorvideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewphotoorvideoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout requestsPopUpLayout = new javax.swing.GroupLayout(requestsPopUp);
        requestsPopUp.setLayout(requestsPopUpLayout);
        requestsPopUpLayout.setHorizontalGroup(
            requestsPopUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(requestsPopUpLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(requestsPopUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, requestsPopUpLayout.createSequentialGroup()
                        .addGroup(requestsPopUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3)
                            .addGroup(requestsPopUpLayout.createSequentialGroup()
                                .addGroup(requestsPopUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                                    .addComponent(viewphotoorvideo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addGap(30, 30, 30))
                    .addGroup(requestsPopUpLayout.createSequentialGroup()
                        .addGroup(requestsPopUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(requestsPopUpLayout.createSequentialGroup()
                                .addComponent(Carpicture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addGroup(requestsPopUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Details, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(vehicleLabel)
                                    .addComponent(plateNumberLabel)
                                    .addComponent(mechanicLabel)
                                    .addComponent(ownerLabel)))
                            .addComponent(serviceIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(backbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(302, Short.MAX_VALUE))))
        );
        requestsPopUpLayout.setVerticalGroup(
            requestsPopUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(requestsPopUpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serviceIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(requestsPopUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(requestsPopUpLayout.createSequentialGroup()
                        .addComponent(Details, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(vehicleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(plateNumberLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(mechanicLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ownerLabel))
                    .addComponent(Carpicture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14)
                .addGroup(requestsPopUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(requestsPopUpLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewphotoorvideo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        Carpicture.getAccessibleContext().setAccessibleName("CarPicture");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(requestsPopUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(requestsPopUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbuttonActionPerformed
        dispose();
    }//GEN-LAST:event_backbuttonActionPerformed

    private void viewphotoorvideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewphotoorvideoActionPerformed
       if (mediaFilePath.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "No photo or video uploaded for this request.");
        return;
    }

    java.io.File mediaFile = new java.io.File(mediaFilePath);
    if (!mediaFile.exists()) {
        javax.swing.JOptionPane.showMessageDialog(this, "File not found:\n" + mediaFilePath);
        return;
    }

    String path = mediaFilePath.toLowerCase();

    if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")) {
        // Show image in a popup dialog
        javax.swing.ImageIcon icon = new javax.swing.ImageIcon(mediaFilePath);
        java.awt.Image scaled = icon.getImage().getScaledInstance(600, 400, java.awt.Image.SCALE_SMOOTH);
        javax.swing.JLabel imgLabel = new javax.swing.JLabel(new javax.swing.ImageIcon(scaled));

        javax.swing.JDialog imgDialog = new javax.swing.JDialog(this, "Photo", true);
        imgDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
        imgDialog.add(imgLabel);
        imgDialog.pack();
        imgDialog.setLocationRelativeTo(this);
        imgDialog.setVisible(true);

    } else if (path.endsWith(".mp4")) {
        // Open video with default system player
        try {
            java.awt.Desktop.getDesktop().open(mediaFile);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Cannot open video: " + e.getMessage());
        }
    } else {
        // Unknown type — try opening with default app
        try {
            java.awt.Desktop.getDesktop().open(mediaFile);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Cannot open file: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_viewphotoorvideoActionPerformed

     private void loadProblems(String idreq, String customer) {
        StringBuilder sb = new StringBuilder();
        sb.append("Car Issues\n");
        sb.append("Reported Problems:\n\n");
 
        try (BufferedReader br = new BufferedReader(new FileReader(PROBLEMS_CSV))) {
            String line;
            boolean first = true;
            int num = 1;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; } // skip header
                String[] c = line.split(",", -1);
                if (c.length < 7) continue;
                if (!c[6].trim().equals(idreq)) continue;
                if (!c[2].trim().equalsIgnoreCase(customer)) continue;
                sb.append("  ").append(num++).append(". ").append(c[4].trim()).append("\n");
            }
        } catch (Exception e) {
            sb.append("  (Could not load issues — check problems.csv)");
        }
 
        problemTextArea.setText(sb.toString().trim());
        problemTextArea.setCaretPosition(0);
    }
     
     private void loadMediaPath(String idreq, String customer) {
    mediaFilePath = "";
    java.io.File file = new java.io.File("src\\pictures.csv");
    if (!file.exists()) {
        System.out.println("pictures.csv not found!");
        viewphotoorvideo.setEnabled(false);
        return;
    }

    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
        String line;
        boolean first = true;
        while ((line = br.readLine()) != null) {
            if (first) { first = false; continue; }
            if (line.trim().isEmpty()) continue;

            String[] c = line.split(",", -1);
            if (c.length < 5) continue;

            String rowCustomer = c[2].trim();
            String rowPath     = c[4].trim();

            // Debug print — remove after fixing
            System.out.println("Checking row: customer=" + rowCustomer 
                + " | idreq col=" + c[1].trim() 
                + " | path=" + rowPath);

            // Match by customer + non-empty path
            if (rowCustomer.equalsIgnoreCase(customer) && !rowPath.isEmpty()) {
                mediaFilePath = rowPath;
                System.out.println("Found media: " + mediaFilePath);
                break;
            }
        }
    } catch (Exception e) { e.printStackTrace(); }

    viewphotoorvideo.setEnabled(!mediaFilePath.isEmpty());
    viewphotoorvideo.setToolTipText(mediaFilePath.isEmpty() 
        ? "No media uploaded" 
        : mediaFilePath);

    System.out.println("Final mediaFilePath: '" + mediaFilePath + "'");
    System.out.println("Button enabled: " + !mediaFilePath.isEmpty());
}
    
    

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
            java.util.logging.Logger.getLogger(RequestsPopUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RequestsPopUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RequestsPopUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RequestsPopUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(() -> new RequestsPopUp().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel Carpicture;
    public javax.swing.JLabel Details;
    public javax.swing.JButton backbutton;
    public javax.swing.JTable expensesTable;
    public javax.swing.JPopupMenu jPopupMenu1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JTable jTable1;
    public javax.swing.JLabel mechanicLabel;
    public javax.swing.JLabel ownerLabel;
    public javax.swing.JLabel plateNumberLabel;
    public javax.swing.JTextArea problemTextArea;
    public javax.swing.JPanel requestsPopUp;
    public javax.swing.JLabel serviceIdLabel;
    public javax.swing.JLabel vehicleLabel;
    public javax.swing.JButton viewphotoorvideo;
    // End of variables declaration//GEN-END:variables
}
