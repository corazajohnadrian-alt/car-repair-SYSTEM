/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project_System.Design;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
/**
 *
 * @author Administrator
 */
public class ModernProgressBarUI extends BasicProgressBarUI {

    @Override
    protected void paintDeterminate(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = c.getWidth();
        int height = c.getHeight();
        int arc = height; // Set arc to height for a fully pill-shaped bar

        // 1. Clear the area to ensure transparency works
        // This removes that square background you see in your screenshot
        c.setOpaque(false);

        // 2. Draw the Background (The Track)
        g2.setColor(new Color(230, 230, 230)); 
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        // 3. Draw the Progress (The Filler)
        double percent = progressBar.getPercentComplete();
        int progressWidth = (int) (width * percent);

        if (progressWidth > 0) {
            g2.setColor(new Color(0, 102, 115)); // Your FIXO Teal
            g2.fillRoundRect(0, 0, progressWidth, height, arc, arc);
        }

        g2.dispose();
    }
}
