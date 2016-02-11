package pl.edu.pw.eiti.cpoo.gui.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class ImagePanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
    public static final int SCALING_SPEED = 1;

    protected final int imageWidth;
    protected final int imageHeight;
    private int pixelSize;
    private int currentDragStartX;
    private int currentDragStartY;
    private int currentTranslateX;
    private int currentTranslateY;
    private int accumulatedTranslateX;
    private int accumulatedTranslateY;

    public ImagePanel(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        resetValues();
        addMouseWheelListener(this);
        addMouseListener(this);
    }

    private void resetValues() {
        pixelSize = 1;
        currentDragStartX = 0;
        currentDragStartY = 0;
        currentTranslateX = 0;
        currentTranslateY = 0;
        accumulatedTranslateX = 0;
        accumulatedTranslateY = 0;
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
                g.setColor(getColor(i, j));
                g.fillRect(pixelSize*i, pixelSize*j, pixelSize, pixelSize);
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
        // do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // do nothing
    }
}
