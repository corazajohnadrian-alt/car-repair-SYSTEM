/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project_System.Design;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
/**
 *
 * @author Administrator
 */
public class Image extends JPanel {
    private java.awt.Image img;

    public Image(String path) {
    // This looks inside your JAR/Project folders
    java.net.URL imgUrl = getClass().getResource(path);
    
    if (imgUrl != null) {
        this.img = new javax.swing.ImageIcon(imgUrl).getImage();
    } else {
        // This will print to the output window if the path is wrong
        System.err.println("CANNOT FIND IMAGE AT: " + path);
    }
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (img != null) {
            // 1. Get the current size of the Panel
            int panelWidth = this.getWidth();
            int panelHeight = this.getHeight();

            // 2. Get the original image dimensions
            int imgWidth = img.getWidth(this);
            int imgHeight = img.getHeight(this);

            // 3. Calculate the scale ratio (to avoid stretching)
            double ratioX = (double) panelWidth / imgWidth;
            double ratioY = (double) panelHeight / imgHeight;
            double scale = Math.min(ratioX, ratioY);

            // 4. Calculate new dimensions based on the smaller scale
            int newWidth = (int) (imgWidth * scale);
            int newHeight = (int) (imgHeight * scale);

            // 5. Calculate coordinates to center the image
            int x = (panelWidth - newWidth) / 2;
            int y = (panelHeight - newHeight) / 2;

            // 6. Draw the scaled, non-distorted image
            g.drawImage(img, x, y, newWidth, newHeight, this);
        }
    }
}