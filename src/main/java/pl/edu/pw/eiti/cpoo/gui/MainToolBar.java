package pl.edu.pw.eiti.cpoo.gui;

import javax.swing.*;

public class MainToolBar extends JToolBar {
    private static MainToolBar instance = new MainToolBar();

    private JButton binarize;
    private JLabel status;

    public static MainToolBar getInstance() {
        return instance;
    }

    private MainToolBar() {
        addComponents();
        resetState();
        setFloatable(false);
    }

    private void addComponents() {
        addBinarize();
        addStatus();
    }

    private void resetState() {
        status.setText("brak obrazu");
        disableButtons();
    }

    private void disableButtons() {
        binarize.setEnabled(false);
    }

    private void addBinarize() {
        binarize = new JButton("Binaryzuj");
        binarize.addActionListener(e -> MainWindow.getInstance().binarizeOriginalImage());
        add(binarize);
    }

    private void addStatus() {
        addSeparator();
        status = new JLabel();
        add(status);
    }

    public void setStatus(String status) {
        this.status.setText(status);
    }

    public void allowBinarize() {
        disableButtons();
        binarize.setEnabled(true);
    }
}
