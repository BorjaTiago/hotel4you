package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.Location;

/** Tests that the zone repository keeps the SQL error as the cause. */
class JdbcLocationRepositoryTest {

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

    /** A database whose statements fail on execute with the given SQL error. */
    private static class RefusingDatabase extends Database {

        private final SQLException error;

        RefusingDatabase(SQLException error) {
            super(null);
            this.error = error;
        }

        @Override
        public Connection connect() {
            PreparedStatement statement = (PreparedStatement) Proxy.newProxyInstance(
                    PreparedStatement.class.getClassLoader(),
                    new Class<?>[] {PreparedStatement.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("executeUpdate")) {
                            throw error;
                        }
                        return null;
                    });
            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("prepareStatement")) {
                            return statement;
                        }
                        return null;
                    });
        }
    }

    private final JdbcLocationRepository repository =
            new JdbcLocationRepository(new FailingDatabase());
    private final Location zone = new Location(1L, "Bar", false, true);

    private static void assertSqlCause(IllegalStateException error) {
        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    @Test
    @Tag("H4Y-28-AC-11")
    void onFindById_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.findById(1L)));
    }

    @Test
    @Tag("H4Y-28-AC-5")
    void onFindByFloorAndName_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class,
                () -> repository.findByFloorAndName(0, "Bar")));
    }

    @Test
    @Tag("H4Y-28-AC-3")
    void onSave_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class,
                () -> repository.save(0, new Location(null, "Bar", false, true))));
    }

    @Test
    @Tag("H4Y-28-AC-9")
    void onUpdate_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class,
                () -> repository.update(0, zone)));
    }

    @Test
    @Tag("H4Y-28-AC-14")
    void onDelete_returnFalse_whenForeignKeyRefuses() {
        JdbcLocationRepository refusing = new JdbcLocationRepository(
                new RefusingDatabase(new SQLException("fk", "23000", 547)));

        assertFalse(refusing.delete(1L));
    }

    @Test
    @Tag("H4Y-28-AC-13")
    void onDelete_keepSqlCause_whenOtherSqlError() {
        JdbcLocationRepository refusing = new JdbcLocationRepository(
                new RefusingDatabase(new SQLException("down", "08001", 100)));

        assertSqlCause(assertThrows(IllegalStateException.class, () -> refusing.delete(1L)));
    }
}
