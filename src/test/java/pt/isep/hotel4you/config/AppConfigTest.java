package pt.isep.hotel4you.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests the reading of the configuration from the application.properties file. */
class AppConfigTest {

    @TempDir Path tempDir;

    @Test
    @Tag("H4Y-32-AC-5")
    void onLoad_readSettings_whenFileValid() throws IOException {
        Path configFile = tempDir.resolve("application.properties");
        Files.write(configFile, List.of(
                "# database",
                "db.url=jdbc:sqlserver://localhost:1433",
                "db.user=sa",
                "db.password=secret"));

        AppConfig config = AppConfig.load(configFile);

        assertEquals("jdbc:sqlserver://localhost:1433", config.dbUrl());
        assertEquals("sa", config.dbUser());
        assertEquals("secret", config.dbPassword());
    }

    @Test
    @Tag("H4Y-25-AC-4")
    void onLoad_readRoomFloorLimits_whenConfigured() throws IOException {
        Path file = roomConfig("2:10, 3:8, 5:2");

        AppConfig config = AppConfig.load(file);

        assertEquals(Map.of(2, 10, 3, 8, 5, 2), config.roomFloorLimits());
        assertThrows(UnsupportedOperationException.class,
                () -> config.roomFloorLimits().put(2, 20));
    }

    @Test
    @Tag("H4Y-25-AC-3")
    void onLoad_useNoRoomFloors_whenRoomConfigurationAbsent() throws IOException {
        Path file = tempDir.resolve("application.properties");
        Files.write(file, List.of("db.url=jdbc:sqlserver://localhost:1433", "db.user=sa", "db.password=secret"));

        assertEquals(Map.of(), AppConfig.load(file).roomFloorLimits());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2:0", "2:-1", "2:10,2:8", "two:10", "2:ten", "2", "2:10:3", "2:10,", "2:10,,3:10"})
    @Tag("H4Y-25-AC-4")
    void onLoad_throw_whenRoomFloorLimitsInvalid(String value) throws IOException {
        Path file = roomConfig(value);

        IllegalStateException error = assertThrows(IllegalStateException.class, () -> AppConfig.load(file));
        assertTrue(error.getMessage().contains("rooms.floor-limits"));
        assertFalse(error.getMessage().contains("secret"));
    }

    private Path roomConfig(String value) throws IOException {
        Path file = tempDir.resolve("application.properties");
        Files.write(file, List.of("db.url=jdbc:sqlserver://localhost:1433", "db.user=sa", "db.password=secret",
                "rooms.floor-limits=" + value));
        return file;
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onLoad_throw_whenDbUrlMissing() throws IOException {
        Path configFile = tempDir.resolve("application.properties");
        Files.write(configFile, List.of("db.user=sa", "db.password=secret"));

        IllegalStateException error =
                assertThrows(IllegalStateException.class, () -> AppConfig.load(configFile));

        assertEquals("Falta a configuração: db.url", error.getMessage());
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onLoad_throw_whenFileMissing() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> AppConfig.load(tempDir.resolve("missing.properties")));

        assertEquals("Não foi possível ler o ficheiro application.properties.",
                error.getMessage());
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onToString_hidePasswords_always() {
        AppConfig config = new AppConfig(
                "jdbc:sqlserver://localhost:1433", "sa", "secret-db");

        String text = config.toString();

        assertTrue(text.contains("jdbc:sqlserver://localhost:1433"));
        assertFalse(text.contains("secret-db"));
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onLoad_throw_whenDbPasswordBlank() throws IOException {
        Path configFile = tempDir.resolve("application.properties");
        Files.write(configFile, List.of(
                "db.url=jdbc:sqlserver://localhost:1433", "db.user=sa", "db.password=   "));

        IllegalStateException error =
                assertThrows(IllegalStateException.class, () -> AppConfig.load(configFile));

        assertTrue(error.getMessage().contains("db.password"));
    }
}
