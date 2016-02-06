package pl.edu.pw.eiti.cpoo.gui;

import javax.swing.*;

public class MainToolBar extends JToolBar {
    private static MainToolBar instance = new MainToolBar();

    private JLabel imageName;

    public static MainToolBar getInstance() {
        return instance;
    }

    private MainToolBar() {
        addImageName();
    }

    private void addImageName() {
        add(new JLabel("Obraz: "));
        imageName = new JLabel("-");
        add(imageName);
        addSeparator();
    }

    public void setImageName(String imageName) {
        this.imageName.setText(imageName);
    }
}
