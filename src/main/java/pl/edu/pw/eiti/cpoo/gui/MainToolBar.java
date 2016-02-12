package pl.edu.pw.eiti.cpoo.gui;

import javax.swing.*;

public class MainToolBar extends JToolBar {
    private static MainToolBar instance = new MainToolBar();

    private JLabel status;

    private JButton grayScale;
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
        addComponents();
        resetState();
        setFloatable(false);
    }

    private void addComponents() {
        addGrayScale();
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
        medianPanel = new JPanel();

        median = new JButton("Mediana");
        median.addActionListener(e -> MainWindow.getInstance().createMedianImage());
        medianPanel.add(median);

        JLabel thresholdLabel = new JLabel("");
        medianPanel.add(thresholdLabel);
        binarizedThreshold = new JSlider(0, 255, 130);
        binarizedThreshold.addChangeListener(e -> {
            thresholdLabel.setText("Próg: [" + binarizedThreshold.getValue() + "]");
            MainWindow.getInstance().createBinarizedImage(binarizedThreshold.getValue());
        });
        medianPanel.add(binarizedThreshold);

        add(medianPanel);
    }

    private void addErosion() {
        erode = new JButton("Eroduj");
        erode.addActionListener(e -> MainWindow.getInstance().createErodedImage(erodeSteps.getValue()));
        add(erode);
    }

    private void addFillGaps() {
        fillGapsPanel = new JPanel();

        fillGaps = new JButton("Wypełnij puste obszary");
        fillGaps.addActionListener(e -> MainWindow.getInstance().createFilledGapsImage());
        fillGapsPanel.add(fillGaps);

        JLabel stepsLabel = new JLabel("");
        fillGapsPanel.add(stepsLabel);
        erodeSteps = new JSlider(0, 20, 3);
        erodeSteps.addChangeListener(e -> {
            stepsLabel.setText("Liczba iteracji: [" + erodeSteps.getValue() + "]");
            MainWindow.getInstance().createErodedImage(erodeSteps.getValue());
        });
        fillGapsPanel.add(erodeSteps);

        add(fillGapsPanel);
    }

    private void addDilation() {
        dilate = new JButton("Dylacja");
        dilate.addActionListener(e -> MainWindow.getInstance().createDilatedImage(dilateSteps.getValue()));
        add(dilate);
    }

    private void addDetectCorners() {
        detectCornersPanel = new JPanel();

        detectCorners = new JButton("Oznacz narożniki");
//        detectCorners.addActionListener(e -> MainWindow.getInstance().createDetectedCornersImage());
        detectCornersPanel.add(detectCorners);

        JLabel stepsLabel = new JLabel("");
        detectCornersPanel.add(stepsLabel);
        dilateSteps = new JSlider(0, 20, 5);
        dilateSteps.addChangeListener(e -> {
            stepsLabel.setText("Liczba iteracji: [" + dilateSteps.getValue() + "]");
            MainWindow.getInstance().createDilatedImage(dilateSteps.getValue());
        });
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
