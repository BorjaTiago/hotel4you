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
                "db.url=jdbc:sqlserver://localhost:1433",
                "db.user=sa",
                "db.password=secret"));

        AppConfig config = AppConfig.load(configFile);

        assertEquals("jdbc:sqlserver://localhost:1433", config.dbUrl());
        assertEquals("sa", config.dbUser());
        assertEquals("secret", config.dbPassword());
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
