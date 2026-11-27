package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.CardAccessRecord;

/** Tests that the card entry repository keeps the SQL error as the cause. */
class JdbcCardAccessRecordRepositoryTest {

    /** A database that gives a connection where each SQL statement fails. */
    private static class FailingDatabase extends Database {

        FailingDatabase() {
            super(null);
        }

        @Override
        public Connection connect() {
            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("prepareStatement")) {
                            throw new SQLException("Table not found");
                        }
                        return null;
                    });
        }
    }

    private final JdbcCardAccessRecordRepository repository =
            new JdbcCardAccessRecordRepository(new FailingDatabase());

    private static void assertSqlCause(IllegalStateException error) {
        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onSave_keepSqlCause_whenSqlFails() {
        CardAccessRecord record = new CardAccessRecord(null, 7L, 12L, null, LocalDateTime.of(2026, 11, 20, 9, 0));

        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.save(record)));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onFindByCardId_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.findByCardId(7L)));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onFindByRoomId_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.findByRoomId(12L)));
    }
}
