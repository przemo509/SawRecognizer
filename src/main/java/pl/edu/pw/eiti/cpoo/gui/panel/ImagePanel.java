package pl.edu.pw.eiti.cpoo.gui.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;

public abstract class ImagePanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
    public static final int SCALING_SPEED = 1;

    protected final int imageWidth;
    protected final int imageHeight;
    private static int pixelSize = 1;
    private int currentDragStartX;
    private int currentDragStartY;
    private int currentTranslateX;
    private int currentTranslateY;
    private static int accumulatedTranslateX = 0;
    private static int accumulatedTranslateY = 0;
    protected static boolean[][] markedPixels;

    public ImagePanel(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        resetValues();
        addMouseWheelListener(this);
        addMouseListener(this);
    }

    private void resetValues() {
        currentDragStartX = 0;
        currentDragStartY = 0;
        currentTranslateX = 0;
        currentTranslateY = 0;
        markedPixels = (boolean[][]) Array.newInstance(boolean.class, imageWidth, imageHeight);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.translate(currentTranslateX + accumulatedTranslateX, currentTranslateY + accumulatedTranslateY);
        drawImage(g);
    }

    private void drawImage(Graphics g) {
        for (int i = 0; i < imageWidth; i++) {
            for (int j = 0; j < imageHeight; j++) {
                Color c = getColor(i, j);
                if(markedPixels[i][j]) {
                    c = new Color(255 - c.getRed(), c.getGreen(), c.getBlue());
                }
                g.setColor(c);
                g.fillRect(pixelSize * i, pixelSize * j, pixelSize, pixelSize);
            }
        }

    }

    public abstract Color getColor(int i, int j);

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int pivotOldX = ensureWithinInterval(e.getX(), accumulatedTranslateX, accumulatedTranslateX + pixelSize * imageWidth);
        int pivotOldY = ensureWithinInterval(e.getY(), accumulatedTranslateY, accumulatedTranslateY + pixelSize * imageHeight);

        pixelSize += e.getWheelRotation() < 0 ? SCALING_SPEED : -SCALING_SPEED;
        if (pixelSize < 1) {
            pixelSize = 1;
        }

        accumulatedTranslateX = (int) (pivotOldX * (1f - pixelSize));
        accumulatedTranslateY = (int) (pivotOldY * (1f - pixelSize));

        repaint();
    }

    private int ensureWithinInterval(int n, int start, int end) {
        if (n < start) {
            return start;
        } else if (n > end) {
            return end;
        } else {
            return n;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentDragStartX = e.getX();
        currentDragStartY = e.getY();
        addMouseMotionListener(this);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        accumulatedTranslateX += currentTranslateX;
        accumulatedTranslateY += currentTranslateY;
        currentTranslateX = 0;
        currentTranslateY = 0;
        removeMouseMotionListener(this);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        removeMouseMotionListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentTranslateX = e.getX() - currentDragStartX;
        currentTranslateY = e.getY() - currentDragStartY;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int imageX = (e.getX() - accumulatedTranslateX) / pixelSize;
        int imageY = (e.getY() - accumulatedTranslateY) / pixelSize;
        markedPixels[imageX][imageY] ^= true;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // do nothing
    }
}
