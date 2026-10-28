package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests that the floor repository keeps the SQL error as the cause. */
class JdbcFloorRepositoryTest {

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

    private final JdbcFloorRepository repository = new JdbcFloorRepository(new FailingDatabase());

    @Test
    @Tag("H4Y-28-AC-1")
    void onFindAll_keepSqlCause_whenSqlFails() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                repository::findAll);

        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    @Test
    @Tag("H4Y-28-AC-2")
    void onFindByNumber_keepSqlCause_whenSqlFails() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.findByNumber(4));

        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }
}
