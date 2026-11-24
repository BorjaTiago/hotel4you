package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.CardType;
import pt.isep.hotel4you.domain.MagneticCard;

/** Tests that the card repository keeps the SQL error as the cause. */
class JdbcCardRepositoryTest {

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

    private final JdbcCardRepository repository = new JdbcCardRepository(new FailingDatabase());
    private final MagneticCard card = new MagneticCard(1L, "12345", CardType.CLIENT, 3L, null, true, null);

    private static void assertSqlCause(IllegalStateException error) {
        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onFindByNumber_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.findByNumber("12345")));
    }

    @Test
    @Tag("H4Y-109-AC-2")
    void onExistsByNumber_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.existsByNumber("12345")));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onSave_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.save(card)));
    }

    @Test
    @Tag("H4Y-109-AC-1")
    void onUpdate_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.update(card)));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onFindByReservationId_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.findByReservationId(3L)));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onFindByUserId_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.findByUserId(1L)));
    }
}
