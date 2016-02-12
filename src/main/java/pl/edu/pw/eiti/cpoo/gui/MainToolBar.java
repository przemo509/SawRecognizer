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

    private JPanel todoPanel;
    private JButton todo;
    private JSlider erodeSteps;

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
        addTodo();
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
        todoPanel.setVisible(todo == buttonToEnable);
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

    public void enableTodoButton() {
        disableButtonsButOne(todo);
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

    private void addTodo() {
        todoPanel = new JPanel();

        todo = new JButton("TODO");
//        todo.addActionListener(e -> MainWindow.getInstance().createBinarizedImage(binarizeThreshold.getValue(), binarizeTailFactor.getValue() / 100.0));
        todoPanel.add(todo);

        JLabel stepsLabel = new JLabel("");
        todoPanel.add(stepsLabel);
        erodeSteps = new JSlider(0, 20, 3);
        erodeSteps.addChangeListener(e -> {
            stepsLabel.setText("Liczba iteracji: [" + erodeSteps.getValue() + "]");
            MainWindow.getInstance().createErodedImage(erodeSteps.getValue());
        });
        todoPanel.add(erodeSteps);

        add(todoPanel);
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
