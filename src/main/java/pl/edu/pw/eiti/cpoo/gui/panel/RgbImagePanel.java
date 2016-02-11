package pl.edu.pw.eiti.cpoo.gui.panel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;

public class RgbImagePanel extends ImagePanel {

    private final int[][] rgb;

    public RgbImagePanel(BufferedImage image) {
        super(image.getWidth(), image.getHeight());
        this.rgb = (int[][]) Array.newInstance(int.class, imageWidth, imageHeight);
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                this.rgb[i][j] = image.getRGB(i, j);
            }
        }
    }

    @Override
    public Color getColor(int i, int j) {
        return new Color(rgb[i][j]);
    }

    public int[][] getRgbImage() {
        return rgb;
    }
}
