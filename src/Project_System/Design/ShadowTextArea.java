/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project_System.Design;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
/**
 *
 * @author Administrator
 */
public class ShadowTextArea extends JTextArea {
    public ShadowTextArea() {
        setOpaque(false); // Removes the square white background
        setBackground(new Color(0, 0, 0, 0));
        // Keeping your padding for the text
        setBorder(new EmptyBorder(20, 25, 20, 25)); 
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int shadowSize = 5;
        int width = getWidth();
        int height = getHeight();

        // 1. Paint the shadow (Black with transparency)
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRect(shadowSize, shadowSize, width - shadowSize, height - shadowSize);

        // 2. Paint the main area (Sharp corners, no rounding)
        // Using MAGENTA as per your screenshot, or change to your Teal color
        g2.setColor(Color.WHITE); 
        g2.fillRect(0, 0, width - shadowSize, height - shadowSize);

        g2.dispose();

        // 3. Let the superclass paint ONLY the text on top
        super.paintComponent(g);
    }

    @Override
    public void paint(Graphics g) {
        setOpaque(false);
        super.paint(g);
    }
}