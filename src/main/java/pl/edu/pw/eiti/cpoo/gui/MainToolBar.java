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
        disableAllButtons();
    }

    private void disableButtonsButOne(JButton buttonToEnable) {
        grayScale.setVisible(grayScale == buttonToEnable);
        equalizedHistogram.setVisible(equalizedHistogram == buttonToEnable);
        binarized.setVisible(binarized == buttonToEnable);
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
        disableButtonsButOne(binarized);
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

    private void addBinarized() {
        binarized = new JButton("Na binarny");
        binarized.addActionListener(e -> MainWindow.getInstance().createBinarizedImage());
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
