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

    public void markPixels(int pixelSize) {
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                if(image[i][j]) {
                    markPixel(i, j, pixelSize);
                }
            }
        }
        repaint();
    }

    private void markPixel(int x, int y, int pixelSize) {
        for (int i = -pixelSize/2; i <= pixelSize/2; i++) {
            int xi = x + i;
            for (int j = -pixelSize/2; j <= pixelSize/2; j++) {
                int yj = y + j;
                if (xi >= 0 && xi < imageWidth && yj >= 0 && yj < imageHeight) {
                    markedPixels[xi][yj] = true;
                }
            }
        }
    }
}
