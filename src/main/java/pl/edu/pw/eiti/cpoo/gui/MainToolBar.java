package pl.edu.pw.eiti.cpoo.gui;

import javax.swing.*;

public class MainToolBar extends JToolBar {
    private static MainToolBar instance = new MainToolBar();

    private JLabel status;

    private JButton grayScale;
    private JButton equalizedHistogram;
    private JButton binarized;

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
        addBinarized();
        addStatus();
    }

    private void resetState() {
        status.setText("brak obrazu");
        disableButtonsButOne(grayScale);
    }

    private void disableButtonsButOne(JButton buttonToEnable) {
        grayScale.setEnabled(grayScale == buttonToEnable);
        equalizedHistogram.setEnabled(equalizedHistogram == buttonToEnable);
        binarized.setEnabled(binarized == buttonToEnable);
    }

    private void addGrayScale() {
        grayScale = new JButton("Do skali szarości");
        grayScale.addActionListener(e -> {
            MainWindow.getInstance().createGrayScaleImage();
            disableButtonsButOne(equalizedHistogram);
        });
        add(grayScale);
    }

    private void addEqualizedHistogram() {
        equalizedHistogram = new JButton("Wyrównaj histogram");
        equalizedHistogram.addActionListener(e -> {
            MainWindow.getInstance().createEqualHistogramImage();
            disableButtonsButOne(binarized);
        });
        add(equalizedHistogram);
    }

    private void addBinarized() {
        binarized = new JButton("Na binarny");
        binarized.addActionListener(e -> {
            MainWindow.getInstance().createBinarizedImage();
            disableButtonsButOne(null);
        });
        add(binarized);
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
