package pl.edu.pw.eiti.cpoo.gui;


import pl.edu.pw.eiti.cpoo.gui.panel.BinaryImagePanel;
import pl.edu.pw.eiti.cpoo.gui.panel.GrayScaleImagePanel;
import pl.edu.pw.eiti.cpoo.gui.panel.ImagePanel;
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
    private GrayScaleImagePanel preMedianImage;
    private GrayScaleImagePanel equalHistogramImage;
    private BinaryImagePanel binarizedImage;
    private BinaryImagePanel medianImage;
    private BinaryImagePanel erodedImage;
    private BinaryImagePanel filledGapsImage;
    private BinaryImagePanel dilatedImage;
    private BinaryImagePanel cornersImage;

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
            MainToolBar.getInstance().enablePreMedianButton();
        } else if (selectedTab == preMedianImage) {
            MainToolBar.getInstance().enableEqualizedButton();
        } else if (selectedTab == equalHistogramImage) {
            MainToolBar.getInstance().enableBinarizeButton();
        } else if (selectedTab == binarizedImage) {
            MainToolBar.getInstance().enableMedianButton();
        } else if (selectedTab == medianImage) {
            MainToolBar.getInstance().enableErodeButton();
        } else if (selectedTab == erodedImage) {
            MainToolBar.getInstance().enableFillGapsButton();
        } else if (selectedTab == filledGapsImage) {
            MainToolBar.getInstance().enableDilateButton();
        } else if (selectedTab == dilatedImage) {
            MainToolBar.getInstance().enableDetectCorners();
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
        clearTabsFromStep(grayScaleImage);

        grayScaleImage = new GrayScaleImagePanel(ImageProcessor.makeGrayScale(originalImage.getRgbImage()));
        tabbedPane.addTab("Skala szarości", grayScaleImage);
        tabbedPane.setSelectedComponent(grayScaleImage);
    }

    public void createPreMedianImage() {
        clearTabsFromStep(preMedianImage);

        preMedianImage = new GrayScaleImagePanel(ImageProcessor.median(grayScaleImage.getImage()));
        tabbedPane.addTab("Mediana #1", preMedianImage);
        tabbedPane.setSelectedComponent(preMedianImage);
    }

    public void createEqualHistogramImage() {
        clearTabsFromStep(equalHistogramImage);

        equalHistogramImage = new GrayScaleImagePanel(ImageProcessor.equalizeHistogram(preMedianImage.getImage()));
        tabbedPane.addTab("Wyrównany histogram", equalHistogramImage);
        tabbedPane.setSelectedComponent(equalHistogramImage);
    }

    public void createBinarizedImage(int threshold) {
        clearTabsFromStep(binarizedImage);

        binarizedImage = new BinaryImagePanel(ImageProcessor.binarize(equalHistogramImage.getImage(), threshold));
        tabbedPane.addTab("Binarny", binarizedImage);
        tabbedPane.setSelectedComponent(binarizedImage);
    }

    public void createMedianImage() {
        clearTabsFromStep(medianImage);

        medianImage = new BinaryImagePanel(ImageProcessor.median(binarizedImage.getImage()));
        tabbedPane.addTab("Po medianie", medianImage);
        tabbedPane.setSelectedComponent(medianImage);
    }

    public void createErodedImage(int erosionSteps) {
        clearTabsFromStep(erodedImage);

        erodedImage = new BinaryImagePanel(ImageProcessor.erode(medianImage.getImage(), erosionSteps));
        tabbedPane.addTab("Po erozji", erodedImage);
        tabbedPane.setSelectedComponent(erodedImage);
    }

    public void createFilledGapsImage() {
        clearTabsFromStep(filledGapsImage);

        filledGapsImage = new BinaryImagePanel(ImageProcessor.fillGaps(erodedImage.getImage()));
        tabbedPane.addTab("Po wypełnieniu", filledGapsImage);
        tabbedPane.setSelectedComponent(filledGapsImage);
    }

    public void createDilatedImage(int dilationSteps) {
        clearTabsFromStep(dilatedImage);

        dilatedImage = new BinaryImagePanel(ImageProcessor.dilate(filledGapsImage.getImage(), dilationSteps));
        tabbedPane.addTab("Po dylacji", dilatedImage);
        tabbedPane.setSelectedComponent(dilatedImage);
    }

    public void createDetectedCornersImage() {
        clearTabsFromStep(cornersImage);

        cornersImage = new BinaryImagePanel(ImageProcessor.detectCorners(dilatedImage.getImage()));
        cornersImage.markPixels(5);
        tabbedPane.addTab("Narożniki", cornersImage);
        tabbedPane.setSelectedComponent(cornersImage);
    }

    private void clearTabsFromStep(ImagePanel imagePanel) {
        if(imagePanel == cornersImage) {
            clearCornersTab();
            return;
        }
        clearCornersTab();

        if(imagePanel == dilatedImage) {
            clearDilatedTab();
            return;
        }
        clearDilatedTab();

        if(imagePanel == filledGapsImage) {
            clearFillGapsTab();
            return;
        }
        clearFillGapsTab();

        if(imagePanel == erodedImage) {
            clearErodedTab();
            return;
        }
        clearErodedTab();

        if(imagePanel == medianImage) {
            clearMedianImage();
            return;
        }
        clearMedianImage();

        if(imagePanel == binarizedImage) {
            clearBinarizedTab();
            return;
        }
        clearBinarizedTab();

        if(imagePanel == equalHistogramImage) {
            clearEqualizedHistogramTab();
            return;
        }
        clearEqualizedHistogramTab();

        if(imagePanel == preMedianImage) {
            clearPreMedianTab();
            return;
        }
        clearPreMedianTab();

        if(imagePanel == grayScaleImage) {
            clearGrayScaleTab();
            return;
        }
        clearGrayScaleTab();
    }

    private void clearGrayScaleTab() {
        tabbedPane.remove(grayScaleImage);
        grayScaleImage = null;
    }

    private void clearPreMedianTab() {
        tabbedPane.remove(preMedianImage);
        preMedianImage = null;
    }

    private void clearEqualizedHistogramTab() {
        tabbedPane.remove(equalHistogramImage);
        equalHistogramImage = null;
    }

    private void clearBinarizedTab() {
        tabbedPane.remove(binarizedImage);
        binarizedImage = null;
    }

    public void clearMedianImage() {
        tabbedPane.remove(medianImage);
        medianImage = null;
    }

    private void clearErodedTab() {
        tabbedPane.remove(erodedImage);
        erodedImage = null;
    }

    private void clearFillGapsTab() {
        tabbedPane.remove(filledGapsImage);
        filledGapsImage = null;
    }

    private void clearDilatedTab() {
        tabbedPane.remove(dilatedImage);
        dilatedImage = null;
    }

    private void clearCornersTab() {
        tabbedPane.remove(cornersImage);
        cornersImage = null;
    }
}

