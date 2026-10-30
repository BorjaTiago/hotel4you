package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.RoomType;

/** Tests the room type repository with a fake connection. */
class JdbcRoomTypeRepositoryTest {

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

    /**
     * A fake database. Its query returns one row that answers by column label, and an unknown label
     * fails as in a real driver. Its update returns the given number of changed rows, so 0 means no
     * row changed.
     */
    private static class FakeDatabase extends Database {

        private final List<String> calls = new ArrayList<>();
        private final int updatedRows;
        private final Map<Integer, Object> parameters = new HashMap<>();
        private BigDecimal price;
        private String sql;

        FakeDatabase(int updatedRows) {
            super(null);
            this.updatedRows = updatedRows;
        }

        @Override
        public Connection connect() {
            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("prepareStatement")) {
                            sql = (String) args[0];
                            return statement();
                        }
                        return defaultValue(method);
                    });
        }

        private PreparedStatement statement() {
            return (PreparedStatement) Proxy.newProxyInstance(
                    PreparedStatement.class.getClassLoader(),
                    new Class<?>[] {PreparedStatement.class},
                    (proxy, method, args) -> {
                        calls.add(method.getName());
                        if (method.getName().equals("setBigDecimal")
                                || method.getName().equals("setLong")) {
                            parameters.put((Integer) args[0], args[1]);
                        }
                        if (method.getName().equals("executeUpdate")) {
                            return updatedRows;
                        }
                        if (method.getName().equals("executeQuery")) {
                            return resultSet();
                        }
                        return defaultValue(method);
                    });
        }

        private ResultSet resultSet() {
            Map<String, Object> row = new HashMap<>();
            row.put("id", 7L);
            row.put("name", "duplo");
            row.put("capacity", 2);
            row.put("base_price", price);
            int[] rowsLeft = {1};
            return (ResultSet) Proxy.newProxyInstance(
                    ResultSet.class.getClassLoader(),
                    new Class<?>[] {ResultSet.class},
                    (proxy, method, args) -> {
                        switch (method.getName()) {
                            case "next":
                                return rowsLeft[0]-- > 0;
                            case "getLong":
                            case "getString":
                            case "getInt":
                            case "getBigDecimal":
                                return column(row, (String) args[0]);
                            default:
                                return method.getReturnType() == boolean.class
                                        ? false : defaultValue(method);
                        }
                    });
        }

        private static Object column(Map<String, Object> row, String label) throws SQLException {
            if (!row.containsKey(label)) {
                throw new SQLException("Invalid column name " + label);
            }
            return row.get(label);
        }

        private static Object defaultValue(Method method) {
            Class<?> type = method.getReturnType();
            if (type == boolean.class) {
                return false;
            }
            if (type == long.class) {
                return 1L;
            }
            if (type == int.class || type == short.class || type == byte.class) {
                return 1;
            }
            return null;
        }
    }

    private final JdbcRoomTypeRepository failing = new JdbcRoomTypeRepository(new FailingDatabase());

    private static void assertUpdate(FakeDatabase database, long id) {
        assertEquals("UPDATE room_types SET base_price = ? WHERE id = ?", database.sql);
        assertEquals(new BigDecimal("80.00"), database.parameters.get(1));
        assertEquals(id, database.parameters.get(2));
    }

    @Test
    @Tag("H4Y-26-AC-1")
    void onFindAll_keepSqlCause_whenSqlFails() {
        IllegalStateException error = assertThrows(IllegalStateException.class, failing::findAll);

        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    @Test
    @Tag("H4Y-26-AC-1")
    void onUpdateBasePrice_keepSqlCause_whenSqlFails() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> failing.updateBasePrice(1L, new BigDecimal("80.00")));

        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(SQLException.class, error.getCause());
    }

    @Test
    @Tag("H4Y-26-AC-1")
    void onFindAll_returnNullPrice_whenPriceIsNotSet() {
        List<RoomType> types = new JdbcRoomTypeRepository(new FakeDatabase(0)).findAll();

        assertEquals(1, types.size());
        assertEquals("duplo", types.get(0).getName());
        assertNull(types.get(0).getBasePrice());
    }

    @Test
    @Tag("H4Y-26-AC-2")
    void onFindAll_returnPrice_whenPriceIsSet() {
        FakeDatabase database = new FakeDatabase(0);
        database.price = new BigDecimal("80.00");

        RoomType type = new JdbcRoomTypeRepository(database).findAll().get(0);

        assertEquals(7L, type.getId());
        assertEquals(2, type.getCapacity());
        assertEquals(new BigDecimal("80.00"), type.getBasePrice());
    }

    @Test
    @Tag("H4Y-26-AC-3")
    void onUpdateBasePrice_returnFalse_whenNoRowChanges() {
        FakeDatabase database = new FakeDatabase(0);

        boolean updated = new JdbcRoomTypeRepository(database)
                .updateBasePrice(99L, new BigDecimal("80.00"));

        assertFalse(updated);
        assertTrue(database.calls.contains("setBigDecimal"));
        assertTrue(database.calls.contains("setLong"));
        assertUpdate(database, 99L);
    }

    @Test
    @Tag("H4Y-26-AC-1")
    void onUpdateBasePrice_returnTrue_whenRowChanges() {
        FakeDatabase database = new FakeDatabase(1);

        boolean updated = new JdbcRoomTypeRepository(database)
                .updateBasePrice(1L, new BigDecimal("80.00"));

        assertTrue(updated);
        assertUpdate(database, 1L);
    }
}
