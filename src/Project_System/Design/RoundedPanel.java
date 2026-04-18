package Project_System.Design;

import javax.swing.*;
import java.awt.*;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Administrator
 */
public class RoundedPanel extends JPanel {
    private int cornerRadius = 30;

    public RoundedPanel() {
        setOpaque(false); // Important: Allows the background to show through the corners
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int shadowSize = 5; // Thickness of the shadow

        // 1. Draw the Shadow
        g2.setColor(new Color(0, 0, 0, 50)); // Black with low alpha (transparency)
        g2.fillRoundRect(shadowSize, shadowSize, width - shadowSize - 1, height - shadowSize - 1, 30, 30);

        // 2. Draw the actual Panel on top, offset slightly
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width - shadowSize - 1, height - shadowSize - 1, 30, 30);

        g2.dispose();
        super.paintComponent(g);
    }
}
