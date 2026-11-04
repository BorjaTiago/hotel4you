package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    /** A database whose statements fail with a given error or give a given update count. */
    private static class StatementDatabase extends Database {

        private final SQLException error;
        private final int updateCount;

        StatementDatabase(SQLException error, int updateCount) {
            super(null);
            this.error = error;
            this.updateCount = updateCount;
        }

        @Override
        public Connection connect() {
            PreparedStatement statement = (PreparedStatement) Proxy.newProxyInstance(
                    PreparedStatement.class.getClassLoader(),
                    new Class<?>[] {PreparedStatement.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("executeQuery")
                                || method.getName().equals("executeUpdate")) {
                            return run();
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

        private Object run() throws SQLException {
            if (error != null) {
                throw error;
            }
            return updateCount;
        }
    }

    private static JdbcItemRepository failingWith(int errorCode) {
        return new JdbcItemRepository(
                new StatementDatabase(new SQLException("dup", "23000", errorCode), 0));
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

    @Test
    @Tag("H4Y-51-AC-3")
    void onSave_throwDuplicate_whenSqlErrorIs2627() {
        JdbcItemRepository duplicate = failingWith(2627);

        assertThrows(DuplicateItemException.class,
                () -> duplicate.save(new StockItem(null, "toalhas", "unidade", 0)));
    }

    @Test
    @Tag("H4Y-51-AC-3")
    void onSave_throwDuplicate_whenSqlErrorIs2601() {
        JdbcItemRepository duplicate = failingWith(2601);

        assertThrows(DuplicateItemException.class,
                () -> duplicate.save(new StockItem(null, "toalhas", "unidade", 0)));
    }

    @Test
    @Tag("H4Y-51-AC-7")
    void onUpdate_throwDuplicate_whenSqlErrorIs2601() {
        JdbcItemRepository duplicate = failingWith(2601);

        assertThrows(DuplicateItemException.class,
                () -> duplicate.update(new StockItem(1L, "toalhas", "unidade", 0)));
    }

    @Test
    @Tag("H4Y-51-AC-7")
    void onUpdate_throwDuplicate_whenSqlErrorIs2627() {
        JdbcItemRepository duplicate = failingWith(2627);

        assertThrows(DuplicateItemException.class,
                () -> duplicate.update(new StockItem(1L, "toalhas", "unidade", 0)));
    }

    @Test
    @Tag("H4Y-51-AC-1")
    void onSave_keepSqlCause_whenSqlErrorIsOther() {
        JdbcItemRepository other = failingWith(208);

        assertSqlCause(assertThrows(IllegalStateException.class,
                () -> other.save(new StockItem(null, "toalhas", "unidade", 0))));
    }

    @Test
    @Tag("H4Y-51-AC-8")
    void onUpdate_returnFalse_whenNoRowChanged() {
        JdbcItemRepository none = new JdbcItemRepository(new StatementDatabase(null, 0));

        assertFalse(none.update(new StockItem(99L, "toalhas", "unidade", 0)));
    }

    @Test
    @Tag("H4Y-51-AC-5")
    void onUpdate_keepSqlCause_whenSqlErrorIsOther() {
        JdbcItemRepository other = failingWith(208);

        assertSqlCause(assertThrows(IllegalStateException.class,
                () -> other.update(new StockItem(1L, "toalhas", "unidade", 0))));
    }

    @Test
    @Tag("H4Y-51-AC-5")
    void onUpdate_returnTrue_whenOneRowChanged() {
        JdbcItemRepository one = new JdbcItemRepository(new StatementDatabase(null, 1));

        assertTrue(one.update(new StockItem(1L, "toalhas", "unidade", 0)));
    }
}
