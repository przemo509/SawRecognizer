package pl.edu.pw.eiti.cpoo.gui;


import pl.edu.pw.eiti.cpoo.gui.panel.BinaryImagePanel;
import pl.edu.pw.eiti.cpoo.gui.panel.GrayScaleImagePanel;
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
    private GrayScaleImagePanel grayScaleImage;
    private GrayScaleImagePanel equalHistogramImage;
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
        tabbedPane.addChangeListener(e -> onTabSwitch());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void onTabSwitch() {
        Component selectedTab = tabbedPane.getSelectedComponent();
        if (selectedTab == null) {
            return;
        } else if (selectedTab == originalImage) {
            MainToolBar.getInstance().enableGrayScaleButton();
        } else if (selectedTab == grayScaleImage) {
            MainToolBar.getInstance().enableEqualizedButton();
        } else if (selectedTab == equalHistogramImage) {
            MainToolBar.getInstance().enableBinarizeButton();
        } else if (selectedTab == binarizedImage) {
            MainToolBar.getInstance().enableTodoButton();
        } else {
            MainToolBar.getInstance().disableAllButtons();
        }
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
            repaint();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0}", e);
        }
    }

    public void createGrayScaleImage() {
        clearGrayScaleTab();
        clearEqualizedHistogramTab();
        clearBinarizedTab();

        grayScaleImage = new GrayScaleImagePanel(ImageProcessor.makeGrayScale(originalImage.getRgbImage()));
        tabbedPane.addTab("Skala szarości", grayScaleImage);
        tabbedPane.setSelectedComponent(grayScaleImage);
    }

    public void createEqualHistogramImage() {
        clearEqualizedHistogramTab();
        clearBinarizedTab();

        equalHistogramImage = new GrayScaleImagePanel(ImageProcessor.equalizeHistogram(grayScaleImage.getImage()));
        tabbedPane.addTab("Wyrównany histogram", equalHistogramImage);
        tabbedPane.setSelectedComponent(equalHistogramImage);
    }

    public void createBinarizedImage(int threshold, double tailFactor) {
        clearBinarizedTab();

        binarizedImage = new BinaryImagePanel(ImageProcessor.binarize(equalHistogramImage.getImage(), threshold, tailFactor));
        tabbedPane.addTab("Binarny", binarizedImage);
        tabbedPane.setSelectedComponent(binarizedImage);
    }

    private void clearGrayScaleTab() {
        tabbedPane.remove(grayScaleImage);
        grayScaleImage = null;
    }

    private void clearEqualizedHistogramTab() {
        tabbedPane.remove(equalHistogramImage);
        equalHistogramImage = null;
    }

    private void clearBinarizedTab() {
        tabbedPane.remove(binarizedImage);
        binarizedImage = null;
    }
}

