package pl.edu.pw.eiti.cpoo.gui;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image image;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(image != null) {
            int smallerSize = Math.min(getWidth(), getHeight());
            Image scaledImage = image.getScaledInstance(smallerSize, smallerSize, Image.SCALE_DEFAULT);
            g.drawImage(scaledImage, 0, 0, null);
        }
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
