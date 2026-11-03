package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.StockItem;

/** Tests that the stock item repository keeps the SQL error as the cause. */
class JdbcItemRepositoryTest {

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

    private final JdbcItemRepository repository = new JdbcItemRepository(new FailingDatabase());

    private static void assertSqlCause(IllegalStateException error) {
        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    @Test
    @Tag("H4Y-51-AC-1")
    void onFindAll_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.findAll()));
    }

    @Test
    @Tag("H4Y-51-AC-3")
    void onFindByName_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class,
                () -> repository.findByName("toalhas")));
    }

    @Test
    @Tag("H4Y-51-AC-1")
    void onSave_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class,
                () -> repository.save(new StockItem(null, "toalhas", "unidade", 0))));
    }

    @Test
    @Tag("H4Y-51-AC-5")
    void onUpdate_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class,
                () -> repository.update(new StockItem(1L, "toalhas", "unidade", 0))));
    }
}
