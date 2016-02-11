package pl.edu.pw.eiti.cpoo.gui.panel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;

public class RgbImagePanel extends ImagePanel {

    private final Color[][] image;

    public RgbImagePanel(BufferedImage image) {
        super(image.getWidth(), image.getHeight());
        this.image = (Color[][]) Array.newInstance(Color.class, imageWidth, imageHeight);
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                this.image[i][j] = new Color(image.getRGB(i, j));
            }
        }
    }

    @Override
    public Color getColor(int i, int j) {
        return image[i][j];
    }

    public Color[][] getImage() {
        return image;
    }
}
