/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project_System.Design;

import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Administrator
 */
public class RoundedPanelNS extends JPanel {
    
    @Override
    protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int width = getWidth();
    int height = getHeight();

    // Draw the actual Panel (No shadow logic)
    g2.setColor(getBackground());
    // Use width-1 and height-1 to ensure the edges stay within the panel bounds
    g2.fillRoundRect(0, 0, width - 1, height - 1, 30, 30);

    g2.dispose();
    super.paintComponent(g);
    }
}

