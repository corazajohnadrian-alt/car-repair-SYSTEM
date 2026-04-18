/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project_System.Design;

import Project_System.Design.*;
import javax.swing.JPanel;
import java.awt.*;

/**
 *
 * @author Administrator
 */
public class GradientPanel1 extends JPanel {
    
    @Override
    protected void paintComponent(Graphics g) {
        // 1. We DON'T call super.paintComponent(g) first if we want to 
        // control the background entirely ourselves.

        Graphics2D g2d = (Graphics2D) g.create(); // Use .create() to protect the original Graphics object
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        Color color1 = new Color(0, 102, 115);
        Color color2 = new Color(44, 83, 100);

        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        g2d.dispose(); // Always dispose what you create

        // 2. Call super LAST to paint the text fields, buttons, and labels on TOP of the gradient
        super.paintComponent(g);
    }
}
