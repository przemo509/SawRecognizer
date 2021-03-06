package pl.edu.pw.eiti.cpoo.gui;

import javax.swing.*;
import java.awt.*;

public class MainToolBar extends JToolBar {
    private static MainToolBar instance = new MainToolBar();

    private JLabel status;

    private JButton grayScale;

    private JButton preMedian;
    private JButton equalizedHistogram;
    private JButton binarize;

    private JPanel medianPanel;
    private JButton median;
    private JSlider binarizedThreshold;

    private JButton erode;

    private JPanel fillGapsPanel;
    private JButton fillGaps;
    private JSlider erodeSteps;

    private JButton dilate;

    private JPanel detectCornersPanel;
    private JButton detectCorners;
    private JSlider dilateSteps;

    public static MainToolBar getInstance() {
        return instance;
    }

    private MainToolBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        addComponents();
        resetState();
    }

    private void addComponents() {
        addGrayScale();
        addPreMedian();
        addEqualizedHistogram();
        addBinarize();
        addMedian();
        addErosion();
        addFillGaps();
        addDilation();
        addDetectCorners();
        addStatus();
    }

    private void resetState() {
        status.setText("brak obrazu");
        disableAllButtons();
    }

    private void disableButtonsButOne(JButton buttonToEnable) {
        grayScale.setVisible(grayScale == buttonToEnable);
        preMedian.setVisible(preMedian == buttonToEnable);
        equalizedHistogram.setVisible(equalizedHistogram == buttonToEnable);
        binarize.setVisible(binarize == buttonToEnable);
        medianPanel.setVisible(median == buttonToEnable);
        erode.setVisible(erode == buttonToEnable);
        fillGapsPanel.setVisible(fillGaps == buttonToEnable);
        dilate.setVisible(dilate == buttonToEnable);
        detectCornersPanel.setVisible(detectCorners == buttonToEnable);
    }

    public void disableAllButtons() {
        disableButtonsButOne(null);
    }

    public void enableGrayScaleButton() {
        disableButtonsButOne(grayScale);
    }

    public void enablePreMedianButton() {
        disableButtonsButOne(preMedian);
    }

    public void enableEqualizedButton() {
        disableButtonsButOne(equalizedHistogram);
    }

    public void enableBinarizeButton() {
        disableButtonsButOne(binarize);
    }

    public void enableMedianButton() {
        disableButtonsButOne(median);
    }

    public void enableErodeButton() {
        disableButtonsButOne(erode);
    }

    public void enableFillGapsButton() {
        disableButtonsButOne(fillGaps);
    }

    public void enableDilateButton() {
        disableButtonsButOne(dilate);
    }

    public void enableDetectCorners() {
        disableButtonsButOne(detectCorners);
    }

    private void addGrayScale() {
        grayScale = new JButton("Do skali szarości");
        grayScale.addActionListener(e -> MainWindow.getInstance().createGrayScaleImage());
        add(grayScale);
    }

    private void addPreMedian() {
        preMedian = new JButton("Mediana");
        preMedian.addActionListener(e -> MainWindow.getInstance().createPreMedianImage());
        add(preMedian);
    }

    private void addEqualizedHistogram() {
        equalizedHistogram = new JButton("Wyrównaj histogram");
        equalizedHistogram.addActionListener(e -> MainWindow.getInstance().createEqualHistogramImage());
        add(equalizedHistogram);
    }

    private void addBinarize() {
        binarize = new JButton("Na binarny");
        binarize.addActionListener(e -> MainWindow.getInstance().createBinarizedImage(binarizedThreshold.getValue()));
        add(binarize);
    }

    private void addMedian() {
        medianPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        median = new JButton("Mediana");
        median.addActionListener(e -> MainWindow.getInstance().createMedianImage());
        medianPanel.add(median);

        JLabel thresholdLabel = new JLabel("");
        medianPanel.add(thresholdLabel);
        binarizedThreshold = new JSlider(0, 255);
        binarizedThreshold.addChangeListener(e -> {
            thresholdLabel.setText("Próg: [" + binarizedThreshold.getValue() + "]");
            if (MainWindow.getInstance() != null) {
                MainWindow.getInstance().createBinarizedImage(binarizedThreshold.getValue());
            }
        });
        binarizedThreshold.setValue(130);
        medianPanel.add(binarizedThreshold);

        add(medianPanel);
    }

    private void addErosion() {
        erode = new JButton("Eroduj");
        erode.addActionListener(e -> MainWindow.getInstance().createErodedImage(erodeSteps.getValue()));
        add(erode);
    }

    private void addFillGaps() {
        fillGapsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        fillGaps = new JButton("Wypełnij puste obszary");
        fillGaps.addActionListener(e -> MainWindow.getInstance().createFilledGapsImage());
        fillGapsPanel.add(fillGaps);

        JLabel stepsLabel = new JLabel("");
        fillGapsPanel.add(stepsLabel);
        erodeSteps = new JSlider(0, 20);
        erodeSteps.addChangeListener(e -> {
            stepsLabel.setText("Liczba iteracji: [" + erodeSteps.getValue() + "]");
            if (MainWindow.getInstance() != null) {
                MainWindow.getInstance().createErodedImage(erodeSteps.getValue());
            }
        });
        erodeSteps.setValue(3);
        fillGapsPanel.add(erodeSteps);

        add(fillGapsPanel);
    }

    private void addDilation() {
        dilate = new JButton("Dylacja");
        dilate.addActionListener(e -> MainWindow.getInstance().createDilatedImage(dilateSteps.getValue()));
        add(dilate);
    }

    private void addDetectCorners() {
        detectCornersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        detectCorners = new JButton("Oznacz narożniki");
        detectCorners.addActionListener(e -> MainWindow.getInstance().createDetectedCornersImage());
        detectCornersPanel.add(detectCorners);

        JLabel stepsLabel = new JLabel("");
        detectCornersPanel.add(stepsLabel);
        dilateSteps = new JSlider(0, 20);
        dilateSteps.addChangeListener(e -> {
            stepsLabel.setText("Liczba iteracji: [" + dilateSteps.getValue() + "]");
            if (MainWindow.getInstance() != null) {
                MainWindow.getInstance().createDilatedImage(dilateSteps.getValue());
            }
        });
        dilateSteps.setValue(3);
        detectCornersPanel.add(dilateSteps);

        add(detectCornersPanel);
    }

    private void addStatus() {
        addSeparator();
        status = new JLabel();
        add(status);
    }

    public void setStatus(String status) {
        this.status.setText(status);
    }
}
