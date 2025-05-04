package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/movie_rental_system";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private static Properties props = new Properties();

    static {
        try {
            // Try to load from config file if exists
            props.load(new FileInputStream("config/database.properties"));
        } catch (IOException e) {
            // Use defaults if config file not found
            props.setProperty("db.url", DEFAULT_URL);
            props.setProperty("db.user", DEFAULT_USER);
            props.setProperty("db.password", DEFAULT_PASSWORD);
        }
    }

    public static String getUrl() {
        return props.getProperty("db.url", DEFAULT_URL);
    }

    public static String getUser() {
        return props.getProperty("db.user", DEFAULT_USER);
    }

    public static String getPassword() {
        return props.getProperty("db.password", DEFAULT_PASSWORD);
    }

    public static void setCredentials(String url, String user, String password) {
        props.setProperty("db.url", url);
        props.setProperty("db.user", user);
        props.setProperty("db.password", password);
    }
}
