package pl.edu.pw.eiti.cpoo.gui;


import pl.edu.pw.eiti.cpoo.gui.panel.BinaryImagePanel;
import pl.edu.pw.eiti.cpoo.gui.panel.RgbImagePanel;
import pl.edu.pw.eiti.cpoo.processor.ImageProcessor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindow extends JFrame {
    private static final Logger logger = Logger.getLogger(MainWindow.class.getName());
    private static MainWindow instance = new MainWindow();

    private JTabbedPane tabbedPane;
    private RgbImagePanel originalImage;
    private BinaryImagePanel binarizedImage;

    public static MainWindow getInstance() {
        return instance;
    }

    private MainWindow() {
        super("SawRecognizer");
        setSize(800, 600);
        setLocationRelativeTo(null);

        addComponents();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setBackgroundImage(MainMenuBar.getInstance().showImageChooser());
    }

    private void addComponents() {
        setJMenuBar(MainMenuBar.getInstance());

        setLayout(new BorderLayout());
        add(MainToolBar.getInstance(), BorderLayout.PAGE_START);
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
    }

    void setBackgroundImage(String imageFilePath) {
        try {
            originalImage = null;
            binarizedImage = null;
            tabbedPane.removeAll();

            File imageFile = new File(imageFilePath);
            BufferedImage image = ImageIO.read(imageFile);
            originalImage = new RgbImagePanel(image);
            tabbedPane.addTab("Oryginał", originalImage);
            MainToolBar.getInstance().setStatus(imageFile.getName() + " [" + image.getWidth() + "x" + image.getHeight() + "]");
            MainToolBar.getInstance().allowBinarize();
            repaint();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0}", e);
        }
    }

    public void binarizeOriginalImage() {
        binarizedImage = new BinaryImagePanel(ImageProcessor.binarize(originalImage.getImage()));
        tabbedPane.addTab("Binarny", binarizedImage);
    }
}

