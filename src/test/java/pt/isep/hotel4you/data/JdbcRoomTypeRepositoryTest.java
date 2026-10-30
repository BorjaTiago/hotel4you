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
import java.util.List;
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

    /** A database whose statement answers like one row with a NULL price, or like no row changed. */
    private static class FakeDatabase extends Database {

        private final List<String> calls = new ArrayList<>();

        FakeDatabase() {
            super(null);
        }

        @Override
        public Connection connect() {
            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class},
                    (proxy, method, args) -> method.getName().equals("prepareStatement")
                            ? statement() : defaultValue(method));
        }

        private PreparedStatement statement() {
            return (PreparedStatement) Proxy.newProxyInstance(
                    PreparedStatement.class.getClassLoader(),
                    new Class<?>[] {PreparedStatement.class},
                    (proxy, method, args) -> {
                        calls.add(method.getName());
                        return method.getName().equals("executeQuery")
                                ? resultSet() : defaultValue(method);
                    });
        }

        private ResultSet resultSet() {
            int[] rowsLeft = {1};
            return (ResultSet) Proxy.newProxyInstance(
                    ResultSet.class.getClassLoader(),
                    new Class<?>[] {ResultSet.class},
                    (proxy, method, args) -> {
                        switch (method.getName()) {
                            case "next":
                                return rowsLeft[0]-- > 0;
                            case "getString":
                                return "duplo";
                            case "getBigDecimal":
                                return null;
                            default:
                                return method.getReturnType() == boolean.class
                                        ? false : defaultValue(method);
                        }
                    });
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
        List<RoomType> types = new JdbcRoomTypeRepository(new FakeDatabase()).findAll();

        assertEquals(1, types.size());
        assertEquals("duplo", types.get(0).getName());
        assertNull(types.get(0).getBasePrice());
    }

    @Test
    @Tag("H4Y-26-AC-3")
    void onUpdateBasePrice_returnFalse_whenNoRowChanges() {
        FakeDatabase database = new FakeDatabase();

        boolean updated = new JdbcRoomTypeRepository(database)
                .updateBasePrice(99L, new BigDecimal("80.00"));

        assertFalse(updated);
        assertTrue(database.calls.contains("setBigDecimal"));
        assertTrue(database.calls.contains("setLong"));
    }
}
