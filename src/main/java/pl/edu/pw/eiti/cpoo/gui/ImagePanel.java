package pl.edu.pw.eiti.cpoo.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImagePanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
    public static final float SCALING_SPEED = 1.3f;

    private Image image;
    private float scale;
    private int currentDragStartX;
    private int currentDragStartY;
    private int currentTranslateX;
    private int currentTranslateY;
    private int accumulatedTranslateX;
    private int accumulatedTranslateY;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            g.drawImage(
                    image,
                    currentTranslateX + accumulatedTranslateX,
                    currentTranslateY + accumulatedTranslateY,
                    getScaledImageWidth(),
                    getScaledImageHeight(),
                    null);
        }
    }

    private int getScaledImageWidth() {
        return (int) (scale * image.getWidth(null));
    }

    private int getScaledImageHeight() {
        return (int) (scale * image.getHeight(null));
    }

    public void setImage(Image image) {
        removeMouseListener(this);
        removeMouseWheelListener(this);
        this.image = image;
        resetValues();
        addMouseWheelListener(this);
        addMouseListener(this);
    }

    private void resetValues() {
        scale = 1.0f;
        currentDragStartX = 0;
        currentDragStartY = 0;
        currentTranslateX = 0;
        currentTranslateY = 0;
        accumulatedTranslateX = 0;
        accumulatedTranslateY = 0;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int pivotOldX = ensureWithinInterval(e.getX(), accumulatedTranslateX, accumulatedTranslateX + getScaledImageWidth());
        int pivotOldY = ensureWithinInterval(e.getY(), accumulatedTranslateY, accumulatedTranslateY + getScaledImageHeight());

        scale = e.getWheelRotation() < 0 ? scale * SCALING_SPEED : scale / SCALING_SPEED;
        if (scaleToSmall()) {
            scale *= SCALING_SPEED;
        }

        accumulatedTranslateX = (int) (pivotOldX * (1f - scale));
        accumulatedTranslateY = (int) (pivotOldY * (1f - scale));

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

    private boolean scaleToSmall() {
        return getScaledImageWidth() < 10 || getScaledImageHeight() < 10;
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
