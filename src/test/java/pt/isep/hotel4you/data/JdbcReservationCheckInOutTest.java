package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import java.util.Optional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.ReservationSummary;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;

/** Checks the check-in, check-out and active list SQL with JDBC doubles (H4Y-82). */
class JdbcReservationCheckInOutTest {
    private static final LocalDate START = LocalDate.of(2026, 11, 10);
    private static final LocalDate END = LocalDate.of(2026, 11, 12);

    private final RecordingDatabase database = new RecordingDatabase();
    private final JdbcReservationRepository repository = new JdbcReservationRepository(database);

    private String sql() {
        return database.sql.replaceAll("\\s+", " ");
    }

    @Test
    @Tag("H4Y-82-AC-1")
    void onCheckIn_updateOnlyReservedRow_whenCalled() {
        assertTrue(repository.checkIn(5L));

        assertTrue(sql().contains(
                "UPDATE reservations SET status = 'checked_in', checked_in_at = SYSDATETIME()"));
        assertTrue(sql().contains("WHERE id = ? AND status = 'reserved'"));
        assertEquals(List.of(5L), database.bound);
        assertEquals(List.of("statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-82-AC-2")
    void onCheckIn_returnFalse_whenNoRowChanges() {
        database.updated = 0;

        assertFalse(repository.checkIn(5L));
    }

    @Test
    @Tag("H4Y-82-AC-1")
    void onCheckIn_throwAndClose_whenUpdateFails() {
        database.failure = new SQLException("Update failed");

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.checkIn(5L));
        assertEquals("Não foi possível guardar o check-in.", error.getMessage());
        assertSame(database.failure, error.getCause());
        assertEquals(List.of("statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-82-AC-5")
    void onCheckOut_updateOnlyCheckedInRow_whenCalled() {
        assertTrue(repository.checkOut(5L));

        assertTrue(sql().contains(
                "UPDATE reservations SET status = 'checked_out', checked_out_at = SYSDATETIME()"));
        assertTrue(sql().contains("WHERE id = ? AND status = 'checked_in'"));
        assertEquals(List.of(5L), database.bound);
        assertEquals(List.of("statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-82-AC-6")
    void onCheckOut_returnFalse_whenNoRowChanges() {
        database.updated = 0;

        assertFalse(repository.checkOut(5L));
    }

    @Test
    @Tag("H4Y-82-AC-5")
    void onCheckOut_throwAndClose_whenUpdateFails() {
        database.failure = new SQLException("Update failed");

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.checkOut(5L));
        assertEquals("Não foi possível guardar o check-out.", error.getMessage());
        assertSame(database.failure, error.getCause());
        assertEquals(List.of("statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-82-AC-4")
    void onFindById_mapRow_whenReservationExists() {
        database.rows = 1;

        Optional<Reservation> found = repository.findById(5L);

        assertTrue(sql().contains("FROM reservations WHERE id = ?"));
        assertEquals(List.of(5L), database.bound);
        Reservation reservation = found.orElseThrow();
        assertEquals(1L, reservation.getId());
        assertEquals(7L, reservation.getClientId());
        assertEquals(8L, reservation.getRoomId());
        assertEquals(START, reservation.getStartDate());
        assertEquals(END, reservation.getEndDate());
        assertEquals(ReservationStatus.RESERVED, reservation.getStatus());
        assertEquals(4L, reservation.getCreatedByUserId());
        assertEquals(List.of("result", "statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-82-AC-4")
    void onFindById_returnEmpty_whenReservationDoesNotExist() {
        database.rows = 0;

        assertTrue(repository.findById(5L).isEmpty());
        assertEquals(List.of("result", "statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-82-AC-12")
    @Tag("H4Y-82-AC-13")
    void onFindActive_readReservedAndCheckedInWithRoomNumber_whenCalled() {
        database.rows = 2;

        List<ReservationSummary> rows = repository.findActive();

        assertTrue(sql().contains("JOIN rooms rm ON rm.id = r.room_id"));
        assertTrue(sql().contains("WHERE r.status IN ('reserved', 'checked_in')"));
        assertTrue(sql().contains("ORDER BY r.start_date, r.id"));
        assertEquals(List.of(
                new ReservationSummary(1L, "Maria Sousa", "12345678", 201, START, END,
                        ReservationStatus.RESERVED),
                new ReservationSummary(2L, "Maria Sousa", "12345678", 201, START, END,
                        ReservationStatus.CHECKED_IN)), rows);
        assertThrows(UnsupportedOperationException.class, rows::clear);
        assertEquals(List.of("result", "statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-81-AC-1")
    @Tag("H4Y-81-AC-2")
    void onFindActive_joinCustomersAndReadNameAndDocument_whenCalled() {
        database.rows = 1;

        ReservationSummary row = repository.findActive().get(0);

        assertTrue(sql().contains("JOIN customers c ON c.id = r.customer_id"));
        assertTrue(sql().contains("c.name"));
        assertTrue(sql().contains("c.document"));
        assertTrue(sql().contains("WHERE r.status IN ('reserved', 'checked_in')"));
        assertEquals("Maria Sousa", row.clientName());
        assertEquals("12345678", row.document());
    }

    @Test
    @Tag("H4Y-82-AC-12")
    void onFindActive_throwAndClose_whenReadFails() {
        database.failure = new SQLException("Read failed");

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.findActive());
        assertEquals("Não foi possível consultar as reservas.", error.getMessage());
        assertSame(database.failure, error.getCause());
        assertEquals(List.of("statement", "connection"), database.closed);
    }

    private static class RecordingDatabase extends Database {
        private final List<String> closed = new ArrayList<>();
        private final List<Object> bound = new ArrayList<>();
        private int updated = 1;
        private int rows;
        private String sql;
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
                            sql = (String) args[0];
                            return statement();
                        }
                        throw new UnsupportedOperationException(method.getName());
                    });
        }

        private PreparedStatement statement() {
            return (PreparedStatement) Proxy.newProxyInstance(
                    PreparedStatement.class.getClassLoader(), new Class<?>[] {PreparedStatement.class},
                    (proxy, method, args) -> switch (method.getName()) {
                        case "setLong", "setString", "setObject", "setInt" -> {
                            bound.add(args[1]);
                            yield null;
                        }
                        case "executeUpdate" -> {
                            if (failure != null) {
                                throw failure;
                            }
                            yield updated;
                        }
                        case "executeQuery" -> {
                            if (failure != null) {
                                throw failure;
                            }
                            yield result();
                        }
                        case "close" -> {
                            closed.add("statement");
                            yield null;
                        }
                        default -> throw new UnsupportedOperationException(method.getName());
                    });
        }

        private ResultSet result() {
            int[] row = {0};
            return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
                    new Class<?>[] {ResultSet.class}, (proxy, method, args) -> switch (method.getName()) {
                        case "next" -> ++row[0] <= rows;
                        case "getLong" -> switch ((String) args[0]) {
                            case "customer_id" -> 7L;
                            case "room_id" -> 8L;
                            case "created_by_user_id" -> 4L;
                            default -> (long) row[0];
                        };
                        case "getInt" -> 201;
                        case "getDate" -> Date.valueOf("start_date".equals(args[0]) ? START : END);
                        case "getString" -> switch ((String) args[0]) {
                            case "name" -> "Maria Sousa";
                            case "document" -> "12345678";
                            default -> row[0] == 1 ? "reserved" : "checked_in";
                        };
                        case "close" -> {
                            closed.add("result");
                            yield null;
                        }
                        default -> throw new UnsupportedOperationException(method.getName());
                    });
        }
    }
}
