package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.RoomServiceRequest;
import pt.isep.hotel4you.domain.RoomServiceType;

/** Checks the room service request SQL with JDBC doubles (H4Y-110). */
class JdbcRoomServiceRequestRepositoryTest {
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 24, 21, 0);

    private final RecordingDatabase database = new RecordingDatabase();
    private final JdbcRoomServiceRequestRepository repository =
            new JdbcRoomServiceRequestRepository(database);

    private String sql(int index) {
        return database.sql.get(index).replaceAll("\\s+", " ");
    }

    @Test
    @Tag("H4Y-110-AC-1")
    void onSave_insertServiceRow_whenRoomHasCheckIn() {
        Optional<RoomServiceRequest> saved = repository.save(202, RoomServiceType.DRINK);

        assertTrue(sql(0).contains("SELECT id, price FROM extra_prices WHERE name = ?"));
        assertTrue(sql(1).contains(
                "INSERT INTO reservation_services (reservation_id, extra_price_id, unit_price, used_at)"));
        assertTrue(sql(1).contains("OUTPUT INSERTED.id, INSERTED.reservation_id, INSERTED.used_at"));
        assertTrue(sql(1).contains("SELECT r.id, ?, ?, SYSDATETIME() FROM reservations r"));
        assertTrue(sql(1).contains("JOIN rooms ro ON ro.id = r.room_id"));
        assertTrue(sql(1).contains("WHERE ro.number = ? AND r.status = 'checked_in'"));
        assertEquals(List.of("Bebidas", 3L, new BigDecimal("4.50"), 202), database.bound);
        RoomServiceRequest request = saved.orElseThrow();
        assertEquals(11L, request.getId());
        assertEquals(9L, request.getReservationId());
        assertEquals(202, request.getRoomNumber());
        assertEquals(RoomServiceType.DRINK, request.getType());
        assertEquals(new BigDecimal("4.50"), request.getUnitPrice());
        assertEquals(NOW, request.getRequestedAt());
        assertEquals(List.of("result", "statement", "result", "statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-110-AC-1")
    void onSave_keepNullPrice_whenCatalogueHasNoPrice() {
        database.price = null;

        RoomServiceRequest request = repository.save(202, RoomServiceType.FOOD).orElseThrow();

        assertNull(request.getUnitPrice());
        assertEquals(Arrays.asList("Comida", 3L, null, 202), database.bound);
    }

    @Test
    @Tag("H4Y-110-AC-2")
    void onSave_returnEmpty_whenRoomHasNoCheckIn() {
        database.insertedRows = 0;

        assertTrue(repository.save(203, RoomServiceType.LAUNDRY).isEmpty());
    }

    @Test
    @Tag("H4Y-110-AC-3")
    void onSave_throwAndInsertNothing_whenCatalogueHasNoEntry() {
        database.catalogueRows = 0;

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.save(202, RoomServiceType.LAUNDRY));

        assertEquals("O catálogo não tem o serviço Lavandaria. Execute o script v14.", error.getMessage());
        assertEquals(1, database.sql.size());
    }

    @Test
    @Tag("H4Y-110-AC-1")
    void onSave_throwNotAvailable_whenDatabaseFails() {
        SQLException failure = new SQLException("down");
        database.failure = failure;

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.save(202, RoomServiceType.FOOD));

        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertSame(failure, error.getCause());
    }

    private static class RecordingDatabase extends Database {
        private final List<String> closed = new ArrayList<>();
        private final List<Object> bound = new ArrayList<>();
        private final List<String> sql = new ArrayList<>();
        private BigDecimal price = new BigDecimal("4.50");
        private int catalogueRows = 1;
        private int insertedRows = 1;
        private SQLException failure;

        RecordingDatabase() {
            super(null);
        }

        @Override
        public Connection connect() {
            return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class}, (proxy, method, args) -> {
                        if (method.getName().equals("close")) {
                            closed.add("connection");
                            return null;
                        }
                        if (method.getName().equals("prepareStatement")) {
                            sql.add((String) args[0]);
                            return statement(sql.size() == 1 ? catalogueRows : insertedRows);
                        }
                        throw new UnsupportedOperationException(method.getName());
                    });
        }

        private PreparedStatement statement(int rows) {
            return (PreparedStatement) Proxy.newProxyInstance(
                    PreparedStatement.class.getClassLoader(), new Class<?>[] {PreparedStatement.class},
                    (proxy, method, args) -> switch (method.getName()) {
                        case "setLong", "setString", "setInt", "setBigDecimal" -> {
                            bound.add(args[1]);
                            yield null;
                        }
                        case "executeQuery" -> {
                            if (failure != null) {
                                throw failure;
                            }
                            yield result(rows);
                        }
                        case "close" -> {
                            closed.add("statement");
                            yield null;
                        }
                        default -> throw new UnsupportedOperationException(method.getName());
                    });
        }

        private ResultSet result(int rows) {
            int[] row = {0};
            return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
                    new Class<?>[] {ResultSet.class}, (proxy, method, args) -> switch (method.getName()) {
                        case "next" -> ++row[0] <= rows;
                        case "getLong" -> switch ((String) args[0]) {
                            case "id" -> sql.size() == 1 ? 3L : 11L;
                            case "reservation_id" -> 9L;
                            default -> throw new UnsupportedOperationException((String) args[0]);
                        };
                        case "getBigDecimal" -> price;
                        case "getTimestamp" -> Timestamp.valueOf(NOW);
                        case "close" -> {
                            closed.add("result");
                            yield null;
                        }
                        default -> throw new UnsupportedOperationException(method.getName());
                    });
        }
    }
}
