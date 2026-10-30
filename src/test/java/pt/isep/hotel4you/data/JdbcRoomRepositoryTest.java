package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.Room;

/** Tests transaction completion and resource closure without a real database. */
class JdbcRoomRepositoryTest {

    private final RecordingDatabase database = new RecordingDatabase();
    private final JdbcRoomRepository repository = new JdbcRoomRepository(database);
    private final Room room = new Room(null, 202, 2, "duplo");

    @Test
    @Tag("H4Y-25-AC-1")
    void onSave_bindNumbersWithJdbcTypes_whenRoomIsValid() {
        repository.save(room, 10);

        assertEquals(List.of(
                new Binding("setInt", 1, 2),
                new Binding("setString", 1, "duplo"),
                new Binding("setLong", 1, 1L),
                new Binding("setInt", 1, 202),
                new Binding("setLong", 2, 1L),
                new Binding("setLong", 3, 2L)), database.bindings);
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSave_commitAndClose_whenRoomIsValid() {
        Room saved = repository.save(room, 10);

        assertEquals(42L, saved.getId());
        assertEquals(202, saved.getNumber());
        assertEquals(2, saved.getFloorNumber());
        assertEquals("duplo", saved.getRoomTypeName());
        assertEquals(List.of("begin", "lock floor", "find type", "count", "insert", "commit", "close"),
                database.events);
        assertEquals(4, database.closedStatements);
        assertEquals(4, database.closedResults);
    }

    @Test
    @Tag("H4Y-25-AC-4")
    void onSave_rollbackWithoutInsert_whenFloorIsFull() {
        database.roomCount = 10;

        assertThrows(RoomLimitExceededException.class, () -> repository.save(room, 10));

        assertFalse(database.events.contains("insert"));
        assertFalse(database.events.contains("commit"));
        assertTrue(database.events.contains("rollback"));
        assertEquals("close", database.events.getLast());
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSave_keepSqlCauseAndRollback_whenInsertFails() {
        database.insertFailure = new SQLException("Insert failed", "08000", 100);
        database.rollbackFailure = new SQLException("Rollback failed");

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.save(room, 10));

        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertSame(database.insertFailure, error.getCause());
        assertSame(database.rollbackFailure, error.getCause().getSuppressed()[0]);
        assertFalse(database.events.contains("commit"));
        assertTrue(database.events.contains("rollback"));
        assertEquals("close", database.events.getLast());
        assertEquals(4, database.closedStatements);
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSave_throwDuplicateAndRollback_whenSqlRejectsDuplicate() {
        database.insertFailure = new SQLException("Duplicate", "23000", 2627);

        assertThrows(DuplicateRoomException.class, () -> repository.save(room, 10));

        assertTrue(database.events.contains("rollback"));
        assertFalse(database.events.contains("commit"));
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSave_rollback_whenSqlReturnsNoIdentifier() {
        database.identifierReturned = false;

        assertThrows(IllegalStateException.class, () -> repository.save(room, 10));

        assertTrue(database.events.contains("rollback"));
        assertFalse(database.events.contains("commit"));
        assertEquals(4, database.closedResults);
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSave_rollback_whenFloorIsUnknown() {
        database.floorFound = false;

        assertThrows(IllegalArgumentException.class, () -> repository.save(room, 10));

        assertEquals(List.of("begin", "lock floor", "rollback", "close"), database.events);
        assertEquals(1, database.closedStatements);
        assertEquals(1, database.closedResults);
    }

    private record Binding(String method, int position, Object value) { }

    /** Records JDBC calls and provides one row per successful query. */
    private static class RecordingDatabase extends Database {
        private final List<String> events = new ArrayList<>();
        private final List<Binding> bindings = new ArrayList<>();
        private int roomCount;
        private int closedStatements;
        private int closedResults;
        private boolean identifierReturned = true;
        private boolean floorFound = true;
        private SQLException insertFailure;
        private SQLException rollbackFailure;

        RecordingDatabase() {
            super(null);
        }

        @Override
        public Connection connect() {
            return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class}, (proxy, method, args) -> {
                        switch (method.getName()) {
                            case "setAutoCommit" -> {
                                assertEquals(false, args[0]);
                                events.add("begin");
                            }
                            case "prepareStatement" -> { return statement((String) args[0]); }
                            case "commit", "close" -> events.add(method.getName());
                            case "rollback" -> {
                                events.add("rollback");
                                if (rollbackFailure != null) {
                                    throw rollbackFailure;
                                }
                            }
                            default -> { }
                        }
                        return null;
                    });
        }

        private PreparedStatement statement(String sql) {
            return (PreparedStatement) Proxy.newProxyInstance(PreparedStatement.class.getClassLoader(),
                    new Class<?>[] {PreparedStatement.class}, (proxy, method, args) -> {
                        if (method.getName().startsWith("set")) {
                            bindings.add(new Binding(method.getName(), (int) args[0], args[1]));
                        }
                        if (method.getName().equals("executeQuery")) {
                            if (sql.startsWith("SELECT 1")) {
                                return result(1, true);
                            }
                            if (sql.contains("UPDLOCK")) {
                                assertTrue(sql.contains("HOLDLOCK"));
                                events.add("lock floor");
                                return result(1, floorFound);
                            }
                            if (sql.contains("FROM room_types")) {
                                events.add("find type");
                                return result(2, true);
                            }
                            if (sql.contains("COUNT(*)")) {
                                events.add("count");
                                return result(roomCount, true);
                            }
                            assertTrue(sql.contains("OUTPUT INSERTED.id"));
                            events.add("insert");
                            if (insertFailure != null) {
                                throw insertFailure;
                            }
                            return result(42, identifierReturned);
                        }
                        if (method.getName().equals("close")) {
                            closedStatements++;
                        }
                        return null;
                    });
        }

        private ResultSet result(int value, boolean hasRow) {
            boolean[] first = {hasRow};
            return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
                    new Class<?>[] {ResultSet.class}, (proxy, method, args) -> {
                        return switch (method.getName()) {
                            case "next" -> {
                                boolean available = first[0];
                                first[0] = false;
                                yield available;
                            }
                            case "getLong" -> (long) value;
                            case "getInt" -> value;
                            case "close" -> { closedResults++; yield null; }
                            default -> null;
                        };
                    });
        }
    }
}
