package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.config.AppConfig;

/** Tests the message when the database is down. */
class DatabaseTest {

    @Test
    @Tag("H4Y-32-AC-9")
    void onConnect_throwFixedMessage_whenServerDown() {
        AppConfig config = new AppConfig(
                "jdbc:sqlserver://localhost:1;databaseName=hotel4you;loginTimeout=1",
                "secret-user", "secret-password", null, null);
        Database database = new Database(config);

        IllegalStateException error = assertThrows(IllegalStateException.class, database::connect);

        assertEquals("Database not available. Check that SQL Server is running.", error.getMessage());
    }

    @Test
    @Tag("H4Y-32-AC-9")
    void onConnect_hideConnectionDetails_whenServerDown() {
        AppConfig config = new AppConfig(
                "jdbc:sqlserver://localhost:1;databaseName=hotel4you;loginTimeout=1",
                "secret-user", "secret-password", null, null);
        Database database = new Database(config);

        IllegalStateException error = assertThrows(IllegalStateException.class, database::connect);

        assertFalse(error.getMessage().contains("localhost"));
        assertFalse(error.getMessage().contains("secret-user"));
        assertFalse(error.getMessage().contains("secret-password"));
        assertFalse(error.getMessage().contains("hotel4you"));
    }

    @Test
    @Tag("H4Y-32-AC-9")
    void onConnect_keepSqlCause_whenServerDown() {
        AppConfig config = new AppConfig(
                "jdbc:sqlserver://localhost:1;databaseName=hotel4you;loginTimeout=1",
                "secret-user", "secret-password", null, null);
        Database database = new Database(config);

        IllegalStateException error = assertThrows(IllegalStateException.class, database::connect);

        assertInstanceOf(SQLException.class, error.getCause());
    }
}
