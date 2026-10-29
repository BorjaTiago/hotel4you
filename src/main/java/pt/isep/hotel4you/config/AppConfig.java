package pt.isep.hotel4you.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.HashMap;
import java.util.Map;

/**
 * Settings read from the {@code application.properties} file.
 *
 * @param dbUrl the JDBC url of the database
 * @param dbUser the database user
 * @param dbPassword the database password
 * @param roomFloorLimits the configured room floors and their maximum room counts
 */
public record AppConfig(String dbUrl, String dbUser, String dbPassword,
        Map<Integer, Integer> roomFloorLimits) {

    /** Copies the room settings so callers cannot change them. */
    public AppConfig {
        roomFloorLimits = Map.copyOf(roomFloorLimits);
        if (roomFloorLimits.values().stream().anyMatch(limit -> limit < 1)) {
            throw new IllegalArgumentException("O limite de quartos deve ser positivo.");
        }
    }

    /**
     * Creates database settings with no room floors configured.
     *
     * @param dbUrl the JDBC url
     * @param dbUser the database user
     * @param dbPassword the database password
     */
    public AppConfig(String dbUrl, String dbUser, String dbPassword) {
        this(dbUrl, dbUser, dbPassword, Map.of());
    }

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
                parseRoomFloorLimits(properties.getProperty("rooms.floor-limits", "")));
    }

    /**
     * Shows the settings without the passwords.
     *
     * @return the settings, with {@code ***} for each password
     */
    @Override
    public String toString() {
        return "AppConfig[dbUrl=" + dbUrl + ", dbUser=" + dbUser + ", dbPassword=***]";
    }

    private static String required(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Falta a configuração: " + key);
        }
        return value;
    }

    private static Map<Integer, Integer> parseRoomFloorLimits(String value) {
        Map<Integer, Integer> limits = new HashMap<>();
        if (value.isBlank()) {
            return Map.of();
        }
        try {
            for (String entry : value.split(",", -1)) {
                String[] parts = entry.trim().split(":", -1);
                if (parts.length != 2) {
                    throw new IllegalArgumentException();
                }
                int floor = Integer.parseInt(parts[0].trim());
                int limit = Integer.parseInt(parts[1].trim());
                if (limit < 1 || limits.putIfAbsent(floor, limit) != null) {
                    throw new IllegalArgumentException();
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Configuração inválida: rooms.floor-limits. Use piso:limite, sem pisos repetidos.");
        }
        return Map.copyOf(limits);
    }
}
