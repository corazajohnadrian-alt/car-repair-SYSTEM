/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project_System.Design;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
/**
 *
 * @author Administrator
 */
public class ImageMax extends JPanel {
    private java.awt.Image img;

    public ImageMax(String path) {
        java.net.URL imgUrl = getClass().getResource(path);
        if (imgUrl != null) {
            this.img = new javax.swing.ImageIcon(imgUrl).getImage();
        }
        setOpaque(false); // Keeps the background clean
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            Graphics2D g2 = (Graphics2D) g;
            // High quality rendering
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imgWidth = img.getWidth(this);
            int imgHeight = img.getHeight(this);

            // OPTION B MATH: Fill and Crop
            double scale = Math.max((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);

            int newWidth = (int) (imgWidth * scale);
            int newHeight = (int) (imgHeight * scale);

            // Center the image within the panel
            int x = (panelWidth - newWidth) / 2;
            int y = (panelHeight - newHeight) / 2;

            g2.drawImage(img, x, y, newWidth, newHeight, this);
        }
    }
}
