package pl.edu.pw.eiti.cpoo.gui;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindow extends JFrame {
    private static final Logger logger = Logger.getLogger(MainWindow.class.getName());
    private static MainWindow instance = new MainWindow();

    private JLabel backgroundImage;

    public static MainWindow getInstance() {
        return instance;
    }

    private MainWindow() {
        super("SawRecognizer");
        setSize(800, 600);
        setLocationRelativeTo(null);

        addComponents();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void addComponents() {
        setJMenuBar(MainMenuBar.getInstance());

        setLayout(new BorderLayout());
        add(MainToolBar.getInstance(), BorderLayout.PAGE_START);
        backgroundImage = new JLabel();
        add(backgroundImage, BorderLayout.CENTER);
    }

    void setBackgroundImage(String imageFilePath) {
        try {
            File imageFile = new File(imageFilePath);
            backgroundImage.setIcon(new ImageIcon(ImageIO.read(imageFile)));
            MainToolBar.getInstance().setImageName(imageFile.getName());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0}", e);
        }
    }
}

