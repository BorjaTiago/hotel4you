package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Checks the text of the SQL script v16 that creates the card tables and the first master card. */
class CardScriptTest {

    private static final Path SCRIPT = Path.of("database", "v16-create-access-cards.sql");

    private static String script() throws IOException {
        return Files.readString(SCRIPT).toLowerCase(Locale.ROOT);
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onRun_createCardAndLogTables_whenScriptRuns() throws IOException {
        String sql = script();

        assertTrue(sql.contains("create table dbo.access_cards"));
        assertTrue(sql.contains("create table dbo.access_logs"));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onRun_keepOwnerRules_whenCardTableIsCreated() throws IOException {
        String sql = script();

        assertTrue(sql.contains("card_number varchar(20) not null"));
        assertTrue(sql.contains("uq_access_cards_number unique"));
        assertTrue(sql.contains("ck_access_cards_owner"));
        assertTrue(sql.contains("ck_access_cards_master"));
        assertTrue(sql.contains("ck_access_logs_place"));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onRun_skipExisting_whenScriptRunsTwice() throws IOException {
        String sql = script();

        assertTrue(sql.contains("if object_id('dbo.access_cards', 'u') is null"));
        assertTrue(sql.contains("if object_id('dbo.access_logs', 'u') is null"));
    }

    @Test
    @Tag("H4Y-109-AC-5")
    void onRun_insertMasterCard_whenManagerExists() throws IOException {
        String sql = script();

        assertTrue(sql.contains("insert into dbo.access_cards"));
        assertTrue(sql.contains("'manager'"));
        assertTrue(sql.contains("if not exists (select 1 from dbo.access_cards where is_master = 1)"));
    }

    @Test
    @Tag("H4Y-109-AC-5")
    void onRun_keepNoSecret_whenMasterCardIsSeeded() throws IOException {
        String sql = script();

        assertFalse(sql.contains("password"));
        assertFalse(sql.contains("$2a$"));
        assertFalse(sql.contains("secret"));
    }
}
