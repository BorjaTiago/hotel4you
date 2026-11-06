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
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;

/** Checks the reservation insert and the client reservation query with JDBC doubles. */
class JdbcReservationSaveTest {
    private static final LocalDate START = LocalDate.of(2026, 11, 10);
    private static final LocalDate END = LocalDate.of(2026, 11, 12);

    private final RecordingDatabase database = new RecordingDatabase();
    private final JdbcReservationRepository repository = new JdbcReservationRepository(database);

    @Test
    @Tag("H4Y-56-AC-1")
    void onSave_insertReservedRowWithCreator_whenReservationIsNew() {
        Reservation saved = repository.save(
                new Reservation(null, 7L, 8L, START, END, ReservationStatus.RESERVED, 4L));

        assertTrue(database.sql.contains("INSERT INTO reservations"));
        assertTrue(database.sql.contains(
                "(customer_id, room_id, start_date, end_date, status, created_by_user_id)"));
        assertTrue(database.sql.contains("OUTPUT INSERTED.id"));
        assertEquals(List.of(7L, 8L, Date.valueOf(START), Date.valueOf(END), "reserved", 4L),
                database.bound);
        assertEquals(55L, saved.getId());
        assertEquals(7L, saved.getClientId());
        assertEquals(8L, saved.getRoomId());
        assertEquals(4L, saved.getCreatedByUserId());
        assertEquals(ReservationStatus.RESERVED, saved.getStatus());
        assertEquals(List.of("result", "statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onSave_throwAndClose_whenInsertFails() {
        database.failure = new SQLException("Insert failed");

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.save(
                        new Reservation(null, 7L, 8L, START, END, ReservationStatus.RESERVED, 4L)));
        assertEquals("Não foi possível guardar a reserva.", error.getMessage());
        assertSame(database.failure, error.getCause());
        assertEquals(List.of("statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onFindByClient_mapRowsWithRoomNumber_whenClientHasReservations() {
        database.clientRows = true;

        List<ClientReservation> rows = repository.findByClient(7L);

        assertTrue(database.sql.contains("JOIN rooms"));
        assertTrue(database.sql.contains("WHERE r.customer_id = ?"));
        assertEquals(List.of(7L), database.bound);
        assertEquals(List.of(
                new ClientReservation(1L, 201, START, END, ReservationStatus.RESERVED),
                new ClientReservation(2L, 201, START, END, ReservationStatus.CANCELLED)), rows);
        assertThrows(UnsupportedOperationException.class, rows::clear);
        assertEquals(List.of("result", "statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onFindByClient_throwAndClose_whenReadFails() {
        database.failure = new SQLException("Read failed");

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.findByClient(7L));
        assertEquals("Não foi possível consultar as reservas.", error.getMessage());
        assertSame(database.failure, error.getCause());
        assertEquals(List.of("statement", "connection"), database.closed);
    }

    private static class RecordingDatabase extends Database {
        private final List<String> closed = new ArrayList<>();
        private final List<Object> bound = new ArrayList<>();
        private boolean clientRows;
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
                        case "executeQuery" -> {
                            if (failure != null) {
                                throw failure;
                            }
                            yield clientRows ? clientResult() : idResult();
                        }
                        case "close" -> {
                            closed.add("statement");
                            yield null;
                        }
                        default -> throw new UnsupportedOperationException(method.getName());
                    });
        }

        private ResultSet idResult() {
            return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
                    new Class<?>[] {ResultSet.class}, (proxy, method, args) -> switch (method.getName()) {
                        case "next" -> true;
                        case "getLong" -> 55L;
                        case "close" -> {
                            closed.add("result");
                            yield null;
                        }
                        default -> throw new UnsupportedOperationException(method.getName());
                    });
        }

        private ResultSet clientResult() {
            int[] row = {0};
            return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
                    new Class<?>[] {ResultSet.class}, (proxy, method, args) -> switch (method.getName()) {
                        case "next" -> ++row[0] <= 2;
                        case "getLong" -> (long) row[0];
                        case "getInt" -> 201;
                        case "getDate" -> Date.valueOf("start_date".equals(args[0]) ? START : END);
                        case "getString" -> row[0] == 1 ? "reserved" : "cancelled";
                        case "close" -> {
                            closed.add("result");
                            yield null;
                        }
                        default -> throw new UnsupportedOperationException(method.getName());
                    });
        }
    }
}
