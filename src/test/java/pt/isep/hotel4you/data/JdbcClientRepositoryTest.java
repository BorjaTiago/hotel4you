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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;

/** Tests how the client repository handles SQL errors, row counts and search text. */
class JdbcClientRepositoryTest {

    /** A database that gives a connection where each SQL statement fails with the given error. */
    private static class FailingDatabase extends Database {

        private final SQLException error;

        FailingDatabase(SQLException error) {
            super(null);
            this.error = error;
        }

        @Override
        public Connection connect() {
            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("prepareStatement")) {
                            throw error;
                        }
                        return null;
                    });
        }
    }

    /**
     * A database whose statements fail with a given error when they run, or give a given update
     * count. It keeps the SQL and the values that the repository binds.
     */
    private static class StatementDatabase extends Database {

        private final SQLException error;
        private final int updateCount;
        private final List<Object> boundValues = new ArrayList<>();
        private String sql;

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
                        String name = method.getName();
                        if (name.startsWith("set") && args != null && args.length == 2) {
                            boundValues.add(args[1]);
                            return null;
                        }
                        if (name.equals("executeQuery") || name.equals("executeUpdate")) {
                            if (error != null) {
                                throw error;
                            }
                            return name.equals("executeUpdate") ? updateCount : null;
                        }
                        return null;
                    });
            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("prepareStatement")) {
                            sql = (String) args[0];
                            return statement;
                        }
                        return null;
                    });
        }
    }

    private static final Client NEW_CLIENT = new Client(null, "Maria Santos", DocumentType.CC, "12345678",
            LocalDate.of(1990, 5, 17), "+351 912 345 678", "PT", null, null);
    private static final Client SAVED_CLIENT = new Client(1L, "Maria Santos", DocumentType.CC, "12345678",
            LocalDate.of(1990, 5, 17), "+351 912 345 678", "PT", null, null);

    private final JdbcClientRepository repository = new JdbcClientRepository(
            new FailingDatabase(new SQLException("Table not found")));

    private static void assertSqlCause(IllegalStateException error) {
        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    private static JdbcClientRepository failingWith(int errorCode) {
        return new JdbcClientRepository(
                new StatementDatabase(new SQLException("error", "23000", errorCode), 0));
    }

    // ---------- SQL error keeps the cause ----------

    @Test
    @Tag("H4Y-52-AC-5")
    void onFindById_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.findById(1L)));
    }

    @Test
    @Tag("H4Y-52-AC-3")
    void onFindByDocument_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class,
                () -> repository.findByDocument(DocumentType.CC, "12345678")));
    }

    @Test
    @Tag("H4Y-52-AC-4")
    void onFindAll_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.findAll()));
    }

    @Test
    @Tag("H4Y-52-AC-4")
    void onSearch_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.search("Maria")));
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onSave_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.save(NEW_CLIENT)));
    }

    @Test
    @Tag("H4Y-52-AC-5")
    void onUpdate_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.update(SAVED_CLIENT)));
    }

    @Test
    @Tag("H4Y-52-AC-6")
    void onDelete_keepSqlCause_whenSqlFails() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> repository.delete(1L)));
    }

    // ---------- Duplicate document ----------

    @Test
    @Tag("H4Y-52-AC-3")
    void onSave_throwClientExists_whenDatabaseErrorIs2601() {
        assertThrows(ClientExistsException.class, () -> failingWith(2601).save(NEW_CLIENT));
    }

    @Test
    @Tag("H4Y-52-AC-3")
    void onSave_throwClientExists_whenDatabaseErrorIs2627() {
        ClientExistsException error = assertThrows(ClientExistsException.class,
                () -> failingWith(2627).save(NEW_CLIENT));

        assertEquals("O cliente já existe.", error.getMessage());
    }

    @Test
    @Tag("H4Y-52-AC-5")
    void onUpdate_throwClientExists_whenDatabaseErrorIs2601() {
        assertThrows(ClientExistsException.class, () -> failingWith(2601).update(SAVED_CLIENT));
    }

    @Test
    @Tag("H4Y-52-AC-5")
    void onUpdate_throwClientExists_whenDatabaseErrorIs2627() {
        assertThrows(ClientExistsException.class, () -> failingWith(2627).update(SAVED_CLIENT));
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onSave_keepSqlCause_whenSqlErrorIsOther() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> failingWith(208).save(NEW_CLIENT)));
    }

    // ---------- Update and delete row counts ----------

    @Test
    @Tag("H4Y-52-AC-5")
    void onUpdate_returnTrue_whenOneRowChanged() {
        assertTrue(new JdbcClientRepository(new StatementDatabase(null, 1)).update(SAVED_CLIENT));
    }

    @Test
    @Tag("H4Y-52-AC-5")
    void onUpdate_returnFalse_whenNoRowChanged() {
        assertFalse(new JdbcClientRepository(new StatementDatabase(null, 0)).update(SAVED_CLIENT));
    }

    @Test
    @Tag("H4Y-52-AC-6")
    void onDelete_returnTrue_whenOneRowDeleted() {
        assertTrue(new JdbcClientRepository(new StatementDatabase(null, 1)).delete(1L));
    }

    @Test
    @Tag("H4Y-52-AC-6")
    void onDelete_returnFalse_whenNoRowDeleted() {
        assertFalse(new JdbcClientRepository(new StatementDatabase(null, 0)).delete(99L));
    }

    @Test
    @Tag("H4Y-52-AC-7")
    void onDelete_throwClientInUse_whenDatabaseErrorIs547() {
        ClientInUseException error = assertThrows(ClientInUseException.class,
                () -> failingWith(547).delete(1L));

        assertEquals("O cliente tem reservas e não pode ser removido.", error.getMessage());
    }

    @Test
    @Tag("H4Y-52-AC-6")
    void onDelete_keepSqlCause_whenSqlErrorIsOther() {
        assertSqlCause(assertThrows(IllegalStateException.class, () -> failingWith(208).delete(1L)));
    }

    // ---------- Search text ----------

    @Test
    @Tag("H4Y-52-AC-4")
    void onSearch_bindTextAsParameter_whenTextGiven() {
        StatementDatabase database = new StatementDatabase(new SQLException("stop"), 0);

        assertThrows(IllegalStateException.class,
                () -> new JdbcClientRepository(database).search("Maria"));

        assertTrue(database.sql.contains("LIKE ?"));
        assertFalse(database.sql.contains("Maria"));
        assertEquals(List.of("%Maria%", "%Maria%"), database.boundValues);
    }

    @Test
    @Tag("H4Y-52-AC-4")
    void onSearch_escapeWildcards_whenTextHasLikeCharacters() {
        StatementDatabase database = new StatementDatabase(new SQLException("stop"), 0);

        assertThrows(IllegalStateException.class,
                () -> new JdbcClientRepository(database).search("50%_[a]\\"));

        assertTrue(database.sql.contains("ESCAPE '\\'"));
        assertEquals("%50\\%\\_\\[a]\\\\%", database.boundValues.get(0));
    }
}
