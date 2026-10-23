package pt.isep.hotel4you.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Settings read from the {@code application.properties} file.
 *
 * @param dbUrl the JDBC url of the database
 * @param dbUser the database user
 * @param dbPassword the database password
 * @param managerUsername the login name of the first manager, can be null
 * @param managerPassword the password of the first manager, can be null
 */
public record AppConfig(String dbUrl, String dbUser, String dbPassword,
                        String managerUsername, String managerPassword) {

    /**
     * Reads the settings from a file with {@code key=value} lines.
     *
     * @param file the path of the {@code application.properties} file
     * @return the settings
     * @throws IllegalStateException if the file cannot be read or a database setting is missing
     */
    public static AppConfig load(Path file) {
        // 1. Read every "key=value" line of the file.
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(file)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível ler o ficheiro application.properties.");
        }

        // 2. Take each value by its key. The database values must exist.
        return new AppConfig(
                required(properties, "db.url"),
                required(properties, "db.user"),
                required(properties, "db.password"),
                properties.getProperty("manager.username"),
                properties.getProperty("manager.password"));
    }

    /**
     * Shows the settings without the passwords.
     *
     * @return the settings, with {@code ***} for each password
     */
    @Override
    public String toString() {
        return "AppConfig[dbUrl=" + dbUrl + ", dbUser=" + dbUser + ", dbPassword=***"
                + ", managerUsername=" + managerUsername + ", managerPassword=***]";
    }

    private static String required(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Falta a configuração: " + key);
        }
        return value;
    }
}
