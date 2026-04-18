/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project_System.Design;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Administrator
 */
public class RoundedLogo extends javax.swing.JLabel {
    public RoundedLogo() {
        setOpaque(false);
        setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    try {
            // 1. Load the original 300x300 image
            java.net.URL imgURL = getClass().getResource("/Project_System/resources/logo.png");
            if (imgURL != null) {
                javax.swing.ImageIcon original = new javax.swing.ImageIcon(imgURL);
                // 2. Scale it to 80x80 immediately
                java.awt.Image scaled = original.getImage().getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
                // 3. Apply it to this label
                setIcon(new javax.swing.ImageIcon(scaled));
            }
        } catch (Exception e) {
            System.err.println("Logo scaling failed: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the white circle
        g2d.setColor(java.awt.Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        
        super.paintComponent(g);
    }
}
