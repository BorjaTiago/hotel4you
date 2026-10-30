package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.RoomSummary;

/** Tests the room list query without a real database. */
class JdbcRoomSummaryQueryTest {

    private final RowDatabase database = new RowDatabase();
    private final JdbcRoomRepository repository = new JdbcRoomRepository(database);

    @Test
    @Tag("H4Y-50-AC-1")
    void onFindSummaries_returnOneSummaryPerRow_whenRowsHaveTypeFloorAndPrice() {
        database.rows = List.of(
                row(201, 2, "single", new BigDecimal("80.00")),
                row(301, 3, "family", null));

        List<RoomSummary> rooms = repository.findSummaries();

        assertEquals(List.of(
                new RoomSummary(201, 2, "single", new BigDecimal("80.00")),
                new RoomSummary(301, 3, "family", null)), rooms);
        assertTrue(database.sql.contains("t.base_price"));
        assertTrue(database.sql.contains("ORDER BY r.number"));
        assertEquals(List.of("result", "statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-50-AC-3")
    void onFindSummaries_returnEmptyList_whenTableHasNoRooms() {
        assertEquals(List.of(), repository.findSummaries());
    }

    @Test
    @Tag("H4Y-50-AC-1")
    void onFindSummaries_throwIllegalStateWithSqlCause_whenQueryFails() {
        database.failure = new SQLException("Query failed");

        IllegalStateException error =
                assertThrows(IllegalStateException.class, repository::findSummaries);

        assertSame(database.failure, error.getCause());
    }

    private static Map<String, Object> row(int number, int floor, String type, BigDecimal price) {
        Map<String, Object> values = new HashMap<>();
        values.put("number", number);
        values.put("floor_number", floor);
        values.put("room_type_name", type);
        values.put("base_price", price);
        return values;
    }

    /** Returns fixed rows for any query and records what it closes. */
    private static class RowDatabase extends Database {
        private List<Map<String, Object>> rows = List.of();
        private final List<String> closed = new ArrayList<>();
        private String sql = "";
        private SQLException failure;

        RowDatabase() {
            super(null);
        }

        @Override
        public Connection connect() {
            return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class}, (proxy, method, args) -> {
                        switch (method.getName()) {
                            case "prepareStatement" -> {
                                sql = (String) args[0];
                                return statement();
                            }
                            case "close" -> closed.add("connection");
                            default -> { }
                        }
                        return null;
                    });
        }

        private PreparedStatement statement() {
            return (PreparedStatement) Proxy.newProxyInstance(PreparedStatement.class.getClassLoader(),
                    new Class<?>[] {PreparedStatement.class}, (proxy, method, args) -> {
                        switch (method.getName()) {
                            case "executeQuery" -> {
                                if (failure != null) {
                                    throw failure;
                                }
                                return result(rows.iterator());
                            }
                            case "close" -> closed.add("statement");
                            default -> { }
                        }
                        return null;
                    });
        }

        private ResultSet result(Iterator<Map<String, Object>> iterator) {
            Object[] current = {null};
            return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
                    new Class<?>[] {ResultSet.class}, (proxy, method, args) -> {
                        switch (method.getName()) {
                            case "next" -> {
                                current[0] = iterator.hasNext() ? iterator.next() : null;
                                return current[0] != null;
                            }
                            case "getInt", "getString", "getBigDecimal" -> {
                                return ((Map<?, ?>) current[0]).get(args[0]);
                            }
                            case "close" -> closed.add("result");
                            default -> { }
                        }
                        return null;
                    });
        }
    }
}
