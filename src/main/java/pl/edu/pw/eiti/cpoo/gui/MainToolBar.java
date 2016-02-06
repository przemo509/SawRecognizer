package pl.edu.pw.eiti.cpoo.gui;

import javax.swing.*;

public class MainToolBar extends JToolBar {
    private static MainToolBar instance = new MainToolBar();

    private JLabel status;

    public static MainToolBar getInstance() {
        return instance;
    }

    private MainToolBar() {
        addStatus();
        setFloatable(false);
    }

    private void addStatus() {
        addSeparator();
        status = new JLabel("brak obrazu");
        add(status);
    }

    public void setStatus(String status) {
        this.status.setText(status);
    }
}
