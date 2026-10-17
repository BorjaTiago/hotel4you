package pt.isep.hotel4you.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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

/** Tests the reading of the configuration from the environment and the .env file. */
class AppConfigTest {

    @TempDir Path tempDir;

    private static Map<String, String> dbEnvironment() {
        return Map.of(
                "H4Y_DB_URL", "jdbc:sqlserver://localhost:1433",
                "H4Y_DB_USER", "sa",
                "H4Y_DB_PASSWORD", "pw");
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onParseEnvFile_readValue_whenLineValid() {
        Map<String, String> values = AppConfig.parseEnvFile(List.of("H4Y_DB_USER=sa"));

        assertEquals("sa", values.get("H4Y_DB_USER"));
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onParseEnvFile_skipLine_whenCommentOrBlank() {
        Map<String, String> values =
                AppConfig.parseEnvFile(List.of("# H4Y_DB_USER=sa", "", "   ", "no equals sign"));

        assertTrue(values.isEmpty());
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onParseEnvFile_keepEqualsSign_whenValueHasEquals() {
        Map<String, String> values = AppConfig.parseEnvFile(List.of("H4Y_DB_PASSWORD=a=b"));

        assertEquals("a=b", values.get("H4Y_DB_PASSWORD"));
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onParseEnvFile_removeQuotes_whenValueQuoted() {
        Map<String, String> values =
                AppConfig.parseEnvFile(List.of("H4Y_DB_USER=\"sa\"", "H4Y_DB_URL='jdbc:sqlserver://localhost'"));

        assertEquals("sa", values.get("H4Y_DB_USER"));
        assertEquals("jdbc:sqlserver://localhost", values.get("H4Y_DB_URL"));
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onParseEnvFile_ignoreCarriageReturn_whenWindowsLine() {
        Map<String, String> values = AppConfig.parseEnvFile(List.of("H4Y_DB_USER=sa\r"));

        assertEquals("sa", values.get("H4Y_DB_USER"));
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onLoad_useEnvFile_whenEnvironmentEmpty() throws IOException {
        Path envFile = tempDir.resolve(".env");
        Files.write(
                envFile,
                List.of("H4Y_DB_URL=jdbc:sqlserver://filehost:1433", "H4Y_DB_USER=fileuser", "H4Y_DB_PASSWORD=filepw"));

        AppConfig config = AppConfig.load(Map.of(), envFile);

        assertEquals("jdbc:sqlserver://filehost:1433", config.dbUrl());
        assertEquals("fileuser", config.dbUser());
        assertEquals("filepw", config.dbPassword());
        assertNull(config.firstManagerUsername());
        assertNull(config.firstManagerPassword());
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onLoad_preferEnvironment_whenBothSet() throws IOException {
        Path envFile = tempDir.resolve(".env");
        Files.write(envFile, List.of("H4Y_DB_USER=fileuser"));

        AppConfig config = AppConfig.load(dbEnvironment(), envFile);

        assertEquals("sa", config.dbUser());
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onLoad_work_whenEnvFileMissing() {
        AppConfig config = AppConfig.load(dbEnvironment(), tempDir.resolve("missing.env"));

        assertEquals("jdbc:sqlserver://localhost:1433", config.dbUrl());
        assertEquals("sa", config.dbUser());
        assertEquals("pw", config.dbPassword());
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onLoad_throw_whenDbUrlMissing() {
        Map<String, String> environment =
                Map.of("H4Y_DB_USER", "sa", "H4Y_DB_PASSWORD", "pw");
        Path envFile = tempDir.resolve("missing.env");

        IllegalStateException error =
                assertThrows(IllegalStateException.class, () -> AppConfig.load(environment, envFile));

        assertTrue(error.getMessage().contains("H4Y_DB_URL"));
    }

    @Test
    @Tag("H4Y-32-AC-5")
    void onToString_hidePasswords_always() {
        AppConfig config =
                new AppConfig("jdbc:sqlserver://localhost:1433", "sa", "secret-db", "boss", "secret-manager");

        String text = config.toString();

        assertFalse(text.contains("secret-db"));
        assertFalse(text.contains("secret-manager"));
    }
}
