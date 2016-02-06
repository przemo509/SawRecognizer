package pl.edu.pw.eiti.cpoo;

import pl.edu.pw.eiti.cpoo.gui.MainWindow;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.LogManager;

public class SawRecognizer {
    public static void main(String[] args) throws IOException {
        LogManager.getLogManager().readConfiguration(SawRecognizer.class.getResourceAsStream("/logging.properties"));

        SwingUtilities.invokeLater(() -> MainWindow.getInstance().setVisible(true));
    }
}
