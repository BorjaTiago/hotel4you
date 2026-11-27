package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Checks the text of the SQL script v17 that adds the hashed card code to the cards. */
class CardCodeScriptTest {

    private static final Path SCRIPT = Path.of("database", "v17-alter-access-cards-access-code.sql");

    private static String script() throws IOException {
        return Files.readString(SCRIPT).toLowerCase(Locale.ROOT);
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onRun_addNullableHashColumn_whenScriptRuns() throws IOException {
        String sql = script();

        assertTrue(sql.contains("alter table dbo.access_cards add access_code_hash varchar(255) null"));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onRun_skipExisting_whenScriptRunsTwice() throws IOException {
        assertTrue(script().contains("if col_length('dbo.access_cards', 'access_code_hash') is null"));
    }

    @Test
    @Tag("H4Y-109-AC-5")
    void onRun_keepNoSecret_whenColumnIsAdded() throws IOException {
        String sql = script();

        assertFalse(sql.contains("insert"));
        assertFalse(sql.contains("$2a$"));
    }
}
