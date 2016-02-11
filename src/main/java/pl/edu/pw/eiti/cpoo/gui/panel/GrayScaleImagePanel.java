package pl.edu.pw.eiti.cpoo.gui.panel;

import java.awt.*;

public class GrayScaleImagePanel extends ImagePanel {

    private final int[][] image;

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
