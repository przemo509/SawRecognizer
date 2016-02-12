package pl.edu.pw.eiti.cpoo.gui.panel;

import java.awt.*;

public class BinaryImagePanel extends ImagePanel {
    private final boolean[][] image;

    public BinaryImagePanel(boolean[][] image) {
        super(image.length, image[0].length);
        this.image = image;
    }

    @Override
    public Color getColor(int i, int j) {
        return image[i][j] ? Color.BLACK : Color.WHITE;
    }

    public boolean[][] getImage() {
        return image;
    }
}
