package pl.edu.pw.eiti.cpoo.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImagePanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
    private static final Logger logger = Logger.getLogger(ImagePanel.class.getName());

    private Image image;
    private float scale = 1.0f;
    private int currentDragStartX = 0;
    private int currentDragStartY = 0;
    private int currentTranslateX = 0;
    private int currentTranslateY = 0;
    private int accumulatedTranslateX = 0;
    private int accumulatedTranslateY = 0;

    public ImagePanel() {
        addMouseWheelListener(this);
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            int smallerSize = (int) (Math.min(getWidth(), getHeight()) * scale);
            Image scaledImage = image.getScaledInstance(smallerSize, smallerSize, Image.SCALE_DEFAULT);
            g.translate(currentTranslateX + accumulatedTranslateX, currentTranslateY + accumulatedTranslateY);
            g.drawImage(scaledImage, 0, 0, null);
        }
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scale += -0.1f * e.getWheelRotation();
        if (scale < 0.1f) {
            scale = 0.1f;
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentDragStartX = e.getX();
        currentDragStartY = e.getY();
        addMouseMotionListener(this);
        logger.log(Level.FINE, "Start moving at ({0}, {1})", new Object[]{currentDragStartX, currentDragStartY});
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        accumulatedTranslateX += currentTranslateX;
        accumulatedTranslateY += currentTranslateY;
        currentTranslateX = 0;
        currentTranslateY = 0;
        removeMouseMotionListener(this);
        logger.log(Level.FINE, "Stop moving (mouse release).");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        removeMouseMotionListener(this);
        logger.log(Level.FINE, "Stop moving (mouse exit).");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentTranslateX = e.getX() - currentDragStartX;
        currentTranslateY = e.getY() - currentDragStartY;
        repaint();
        logger.log(Level.FINE, "Moving to ({0}, {1})", new Object[]{e.getX(), e.getY()});
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
