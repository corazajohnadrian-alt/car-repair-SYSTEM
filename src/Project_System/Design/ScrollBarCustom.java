/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project_System.Design;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author User
 */
public class ScrollBarCustom extends JScrollBar {
    public ScrollBarCustom() {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(8, 8)); // Thin width
        setForeground(new Color(180, 180, 180)); // Thumb color
        setBackground(new Color(240, 240, 240)); // Track color
    }

    private class ModernScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(150, 150, 150);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton(); // Removes top arrow
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton(); // Removes bottom arrow
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            return button;
        }
    }
}
