package Project_System;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Popup frame for adding a new issue to issues.csv and the manageissues table.
 * Opened by the "Add Issue" button in adminframe.
 */
public class AddIssueFrame extends javax.swing.JFrame {

    private final adminframe parent;

    // ── UI components ──────────────────────────────────────────────────────────
    private JLabel lblTitle;
    private JLabel lblCategory;
    private JLabel lblIssueName;
    private JTextField txtCategory;
    private JTextField txtIssueName;
    private JButton btnAdd;
    private JButton btnCancel;

    public AddIssueFrame(adminframe parent) {
        this.parent = parent;
        initComponents();
        setTitle("Add New Issue");
        setSize(380, 260);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        // ── Layout ──────────────────────────────────────────────────────────
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        setContentPane(panel);

        // Title
        lblTitle = new JLabel("Add New Issue");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(80, 15, 220, 35);
        panel.add(lblTitle);

        // Category label + field
        lblCategory = new JLabel("Category:");
        lblCategory.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCategory.setBounds(30, 65, 100, 25);
        panel.add(lblCategory);

        txtCategory = new JTextField();
        txtCategory.setBounds(130, 65, 210, 28);
        panel.add(txtCategory);

        // Issue Name label + field
        lblIssueName = new JLabel("Issue Name:");
        lblIssueName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblIssueName.setBounds(30, 110, 100, 25);
        panel.add(lblIssueName);

        txtIssueName = new JTextField();
        txtIssueName.setBounds(130, 110, 210, 28);
        panel.add(txtIssueName);

        // Add button
        btnAdd = new JButton("Add Issue");
        btnAdd.setBounds(80, 165, 110, 35);
        btnAdd.setBackground(new Color(0, 122, 122));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> handleAdd());
        panel.add(btnAdd);

        // Cancel button
        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(210, 165, 110, 35);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());
        panel.add(btnCancel);
    }

    private void handleAdd() {
        String category  = txtCategory.getText().trim();
        String issueName = txtIssueName.getText().trim();

        if (category.isEmpty() || issueName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in both Category and Issue Name.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Generate next ID
        int nextId = getNextId();

        // 1. Add row to the manageissues table in adminframe
        DefaultTableModel model = (DefaultTableModel) parent.manageissues.getModel();
        model.addRow(new Object[]{String.valueOf(nextId), category, issueName});

        // 2. Persist all rows back to issues.csv
        parent.saveIssuesToCSV();

        JOptionPane.showMessageDialog(this,
                "Issue \"" + issueName + "\" added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    /** Reads issues.csv to find the current maximum ID, then returns max+1. */
    private int getNextId() {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("src\\issues.csv"))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;
                if (isHeader) { isHeader = false; continue; }
                String[] data = line.split(",", -1);
                if (data.length >= 1) {
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        if (id > maxId) maxId = id;
                    } catch (NumberFormatException ignore) {}
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read issues.csv for ID: " + e.getMessage());
        }
        return maxId + 1;
    }
}
