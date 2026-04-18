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
public class RoundedTextField extends JTextField {
    private int cornerRadius = 15;

    public RoundedTextField() {
        setOpaque(false); 
        // Using the foreground color for the text and background for the panel
        setBorder(null); 
    }    
    
    @Override
    public Insets getInsets() {
        // The 15px left/right padding keeps the text away from the curves
        return new Insets(5, 15, 5, 15); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint the background
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        
        g2d.dispose();
        super.paintComponent(g);
    }
}