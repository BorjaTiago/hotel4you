package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.ReservationStatus;

/** Checks SQL mapping, parameter binding and resource closure without stored test data. */
class JdbcReservationRepositoryTest {
    private final RecordingDatabase database = new RecordingDatabase();
    private final JdbcReservationRepository repository = new JdbcReservationRepository(database);

    @Test
    @Tag("H4Y-54-AC-1")
    @Tag("H4Y-54-AC-3")
    void onFindByRoom_mapAllStates_whenRowsIncludeCancelledBooking() {
        var rows = repository.findByRoom(8L);
        assertEquals(8L, database.boundRoomId);
        assertTrue(database.sql.contains("WHERE room_id = ?"));
        assertEquals(2, rows.size());
        assertEquals(ReservationStatus.RESERVED, rows.get(0).getStatus());
        assertEquals(ReservationStatus.CANCELLED, rows.get(1).getStatus());
        assertEquals(LocalDate.of(2026, 11, 10), rows.get(0).getStartDate());
        assertEquals(LocalDate.of(2026, 11, 12), rows.get(0).getEndDate());
        assertEquals(8L, rows.get(0).getRoomId());
        assertThrows(UnsupportedOperationException.class, () -> rows.clear());
        assertEquals(List.of("result", "statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-54-AC-1")
    void onFindByRoom_returnEmpty_whenRoomHasNoReservations() {
        database.count = 0;
        assertTrue(repository.findByRoom(8L).isEmpty());
        assertEquals(List.of("result", "statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-54-AC-2")
    void onFindByRoom_throwAndClose_whenDatabaseReadFails() {
        database.failure = new SQLException("Read failed");
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.findByRoom(8L));
        assertSame(database.failure, error.getCause());
        assertEquals(List.of("statement", "connection"), database.closed);
    }

    private static class RecordingDatabase extends Database {
        private final List<String> closed = new ArrayList<>();
        private int count = 2;
        private long boundRoomId;
        private String sql;
        private SQLException failure;

        RecordingDatabase() { super(null); }

        @Override
        public Connection connect() {
            return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class}, (proxy, method, args) -> {
                        if (method.getName().equals("close")) { closed.add("connection"); return null; }
                        if (method.getName().equals("prepareStatement")) {
                            sql = (String) args[0];
                            return statement();
                        }
                        throw new UnsupportedOperationException(method.getName());
                    });
        }

        private PreparedStatement statement() {
            return (PreparedStatement) Proxy.newProxyInstance(PreparedStatement.class.getClassLoader(),
                    new Class<?>[] {PreparedStatement.class}, (proxy, method, args) -> {
                        return switch (method.getName()) {
                            case "setLong" -> { boundRoomId = (Long) args[1]; yield null; }
                            case "executeQuery" -> {
                                if (failure != null) { throw failure; }
                                yield result();
                            }
                            case "close" -> { closed.add("statement"); yield null; }
                            default -> throw new UnsupportedOperationException(method.getName());
                        };
                    });
        }

        private ResultSet result() {
            int[] row = {0};
            return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
                    new Class<?>[] {ResultSet.class}, (proxy, method, args) -> {
                        return switch (method.getName()) {
                            case "next" -> ++row[0] <= count;
                            case "getLong" -> "room_id".equals(args[0]) ? 8L : (long) row[0];
                            case "getDate" -> Date.valueOf("start_date".equals(args[0])
                                    ? "2026-11-10" : "2026-11-12");
                            case "getString" -> row[0] == 1 ? "reserved" : "cancelled";
                            case "close" -> { closed.add("result"); yield null; }
                            default -> throw new UnsupportedOperationException(method.getName());
                        };
                    });
        }
    }
}
