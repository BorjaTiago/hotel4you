package pt.isep.hotel4you.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests the reading of the configuration from the application.properties file. */
class AppConfigTest {

    @TempDir Path tempDir;

    @Test
    @Tag("H4Y-32-AC-5")
    void onLoad_readSettings_whenFileValid() throws IOException {
        Path configFile = tempDir.resolve("application.properties");
        Files.write(configFile, List.of(
                "# database",
                "H4Y_DB_URL=jdbc:sqlserver://localhost:1433",
                "H4Y_DB_USER=sa",
                "H4Y_DB_PASSWORD=secret",
                "H4Y_FIRST_MANAGER_USERNAME=rui",
                "H4Y_FIRST_MANAGER_PASSWORD=manager-secret"));

        AppConfig config = AppConfig.load(configFile);

        assertEquals("jdbc:sqlserver://localhost:1433", config.dbUrl());
        assertEquals("sa", config.dbUser());
        assertEquals("secret", config.dbPassword());
        assertEquals("rui", config.firstManagerUsername());
        assertEquals("manager-secret", config.firstManagerPassword());
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onLoad_throw_whenDbUrlMissing() throws IOException {
        Path configFile = tempDir.resolve("application.properties");
        Files.write(configFile, List.of("H4Y_DB_USER=sa", "H4Y_DB_PASSWORD=secret"));

        IllegalStateException error =
                assertThrows(IllegalStateException.class, () -> AppConfig.load(configFile));

        assertTrue(error.getMessage().contains("H4Y_DB_URL"));
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onLoad_throw_whenFileMissing() {
        assertThrows(IllegalStateException.class,
                () -> AppConfig.load(tempDir.resolve("missing.properties")));
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onToString_hidePasswords_always() {
        AppConfig config = new AppConfig(
                "jdbc:sqlserver://localhost:1433", "sa", "secret-db", "rui", "secret-manager");

        String text = config.toString();

        assertFalse(text.contains("secret-db"));
        assertFalse(text.contains("secret-manager"));
    }
}
