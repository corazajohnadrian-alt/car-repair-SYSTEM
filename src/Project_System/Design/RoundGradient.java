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
public class RoundGradient extends JPanel {
    private Color color1 = new Color(0, 102, 115); // Teal top
    private Color color2 = Color.BLACK;            // Black bottom
    private int cornerRadius = 30;                 // Roundness

    public RoundGradient() {
        setOpaque(false); // Crucial for rounded corners to look right
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Quality settings
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // 1. Create the Gradient Paint
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gp);

        // 2. Draw the Rounded Rectangle (Fill)
        // We use width-1 and height-1 to ensure the edges aren't clipped
        g2d.fillRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);

        g2d.dispose();
        
        // Paint child components (buttons, labels) on top
        super.paintComponent(g);
    }
}

