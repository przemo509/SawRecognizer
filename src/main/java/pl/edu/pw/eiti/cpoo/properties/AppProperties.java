package pl.edu.pw.eiti.cpoo.properties;

import pl.edu.pw.eiti.cpoo.utils.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppProperties {
    private static final String APP_PROPERTIES_PATH = AppProperties.class.getResource("/app.properties").getPath();
    private static final String PROPERTY_LAST_OPENED_PATH = "last.opened.path";

    private static Logger logger = Logger.getLogger(AppProperties.class.getName());
    private static AppProperties instance = new AppProperties();

    private final Properties properties;
    private String lastOpenedPath;

    public static AppProperties getInstance() {
        return instance;
    }

    private AppProperties() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(APP_PROPERTIES_PATH));
            loadAll();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0}", e);
        }
    }

    private void loadAll() {
        lastOpenedPath = getFilePathProperty(PROPERTY_LAST_OPENED_PATH);
    }

    private String getFilePathProperty(String propertyName) {
        String filePath = getStringProperty(propertyName);
        Assert.directoryExists(new File(filePath));
        return filePath;
    }

    private String getStringProperty(String propertyName) {
        String property = properties.getProperty(propertyName);
        Assert.notNull(property, MessageFormat.format("Brak właściwości [{0}] w pliku [{1}].", propertyName, APP_PROPERTIES_PATH));
        return property;
    }

    public String getLastOpenedPath() {
        return lastOpenedPath;
    }

    public void setLastOpenedPath(String path) {
        lastOpenedPath = path;
        saveProperty(PROPERTY_LAST_OPENED_PATH, path);
    }

    private void saveProperty(String key, String value) {
        properties.setProperty(key, value);
        try {
            properties.store(new FileOutputStream(APP_PROPERTIES_PATH), "PartyMusicVoter config");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0}", e);
        }
    }
}
