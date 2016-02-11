package pl.edu.pw.eiti.cpoo.gui.panel;

import java.awt.*;
import java.lang.reflect.Array;

public class GrayScaleImagePanel extends ImagePanel {

    private final int[][] image;

    public GrayScaleImagePanel(Color[][] image) {
        super(image.length, image[0].length);
        this.image = (int[][]) Array.newInstance(int.class, imageWidth, imageHeight);
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                Color c = image[i][j];
                this.image[i][j] = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
            }
        }
    }

    public GrayScaleImagePanel(int[][] image) {
        super(image.length, image[0].length);
        this.image = image;
    }

    @Override
    public Color getColor(int i, int j) {
        return new Color(image[i][j], image[i][j], image[i][j]);
    }

    public int[][] getImage() {
        return image;
    }
}
