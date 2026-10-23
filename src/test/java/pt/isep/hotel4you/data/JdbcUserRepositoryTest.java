package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Role;

/** Tests that the repository keeps the SQL error as the cause. */
class JdbcUserRepositoryTest {

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

    private final JdbcUserRepository repository = new JdbcUserRepository(new FailingDatabase());

    @Test
    @Tag("H4Y-32-AC-9")
    void onFindByUsername_keepSqlCause_whenSqlFails() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.findByUsername("manager"));

        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    @Test
    @Tag("H4Y-32-AC-9")
    void onExistsByUsername_keepSqlCause_whenSqlFails() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.existsByUsername("manager"));

        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    @Test
    @Tag("H4Y-32-AC-9")
    void onExistsByRole_keepSqlCause_whenSqlFails() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.existsByRole(Role.MANAGER));

        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    @Test
    @Tag("H4Y-32-AC-9")
    void onSave_keepSqlCause_whenSqlFails() {
        Manager manager = new Manager(null, "Ana Silva", "ana", "hash");

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.save(manager));

        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }
}
