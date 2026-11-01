package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.Client;

/** Tests that the client repository keeps the SQL error as the cause. */
class JdbcClientRepositoryTest {

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

    private final JdbcClientRepository repository = new JdbcClientRepository(new FailingDatabase());

    private static void assertSqlCause(IllegalStateException error) {
        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    @Test
    @Tag("H4Y-52-AC-3")
    void onFindByDocument_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class,
                () -> repository.findByDocument("12345678")));
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onSave_keepSqlCause_whenSqlFails() {
        Client client = new Client(null, "Maria Santos", "12345678");

        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.save(client)));
    }
}
