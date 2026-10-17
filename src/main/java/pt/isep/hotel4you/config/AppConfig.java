package pt.isep.hotel4you.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Settings for the database and the first manager, read from the {@code application.properties} file.
 *
 * @param dbUrl the JDBC url of the database
 * @param dbUser the database user
 * @param dbPassword the database password
 * @param firstManagerUsername the login name of the first manager, can be null
 * @param firstManagerPassword the password of the first manager, can be null
 */
public record AppConfig(
        String dbUrl,
        String dbUser,
        String dbPassword,
        String firstManagerUsername,
        String firstManagerPassword) {

    /**
     * Reads the settings from an {@code application.properties} file with {@code KEY=value} lines.
     *
     * @param configFile the path of the {@code application.properties} file
     * @return the settings
     * @throws IllegalStateException if the file cannot be read or a database setting is missing
     */
    public static AppConfig load(Path configFile) {
        Properties values = new Properties();
        try (Reader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8)) {
            values.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read the application.properties file.");
        }
        return new AppConfig(
                required(values, "H4Y_DB_URL"),
                required(values, "H4Y_DB_USER"),
                required(values, "H4Y_DB_PASSWORD"),
                values.getProperty("H4Y_FIRST_MANAGER_USERNAME"),
                values.getProperty("H4Y_FIRST_MANAGER_PASSWORD"));
    }

    /**
     * Returns the settings without the passwords.
     *
     * @return the settings, with {@code ***} for each password
     */
    @Override
    public String toString() {
        return "AppConfig[dbUrl=" + dbUrl
                + ", dbUser=" + dbUser
                + ", dbPassword=***"
                + ", firstManagerUsername=" + firstManagerUsername
                + ", firstManagerPassword=***]";
    }

    private static String required(Properties values, String name) {
        String value = values.getProperty(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing setting: " + name);
        }
        return value;
    }
}
