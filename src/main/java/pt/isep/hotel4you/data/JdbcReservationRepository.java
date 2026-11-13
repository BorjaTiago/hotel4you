package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import pt.isep.hotel4you.domain.ReservationSummary;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;

/** Reads and saves bookings in the SQL Server reservations table. */
public class JdbcReservationRepository implements ReservationRepository {
    private final Database database;

    /** @param database the connection factory */
    public JdbcReservationRepository(Database database) {
        this.database = database;
    }

    @Override
    public List<Reservation> findByRoom(long roomId) {
        String sql = "SELECT id, room_id, start_date, end_date, status "
                + "FROM reservations WHERE room_id = ? ORDER BY start_date, id";
        return query(sql, roomId);
    }

    @Override
    public List<Reservation> findByRooms(List<Long> roomIds) {
        if (roomIds.isEmpty()) {
            return List.of();
        }
        String sql = "SELECT id, room_id, start_date, end_date, status FROM reservations "
                + "WHERE room_id IN (" + String.join(", ", Collections.nCopies(roomIds.size(), "?"))
                + ") ORDER BY room_id, start_date, id";
        return query(sql, roomIds.toArray());
    }

    private List<Reservation> query(String sql, Object... values) {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, values);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                reservations.add(new Reservation(result.getLong("id"), result.getLong("room_id"),
                        result.getDate("start_date").toLocalDate(),
                        result.getDate("end_date").toLocalDate(),
                        ReservationStatus.fromDatabase(result.getString("status"))));
            }
            return List.copyOf(reservations);
        } catch (SQLException error) {
            throw new IllegalStateException("Não foi possível consultar as reservas.", error);
        }
    }

    @Override
    public Reservation save(Reservation reservation) {
        // The lock hints keep the checked rows and range locked until the insert ends.
        String sql = """
                INSERT INTO reservations
                    (customer_id, room_id, start_date, end_date, status, created_by_user_id)
                OUTPUT INSERTED.id
                SELECT ?, ?, ?, ?, ?, ?
                WHERE NOT EXISTS (
                    SELECT 1 FROM reservations WITH (UPDLOCK, HOLDLOCK)
                    WHERE room_id = ? AND status <> 'cancelled' AND start_date < ? AND end_date > ?)
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql,
                        reservation.getClientId(), reservation.getRoomId(),
                        Date.valueOf(reservation.getStartDate()), Date.valueOf(reservation.getEndDate()),
                        reservation.getStatus().name().toLowerCase(Locale.ROOT),
                        reservation.getCreatedByUserId(), reservation.getRoomId(),
                        Date.valueOf(reservation.getEndDate()), Date.valueOf(reservation.getStartDate()));
                ResultSet row = statement.executeQuery()) {
            if (!row.next()) {
                throw new RoomNotAvailableException();
            }
            return new Reservation(row.getLong("id"), reservation.getClientId(),
                    reservation.getRoomId(), reservation.getStartDate(), reservation.getEndDate(),
                    reservation.getStatus(), reservation.getCreatedByUserId());
        } catch (SQLException error) {
            throw new IllegalStateException("Não foi possível guardar a reserva.", error);
        }
    }

    @Override
    public List<ClientReservation> findByClient(long clientId) {
        String sql = "SELECT r.id, rm.number, r.start_date, r.end_date, r.status "
                + "FROM reservations r JOIN rooms rm ON rm.id = r.room_id "
                + "WHERE r.customer_id = ? ORDER BY r.start_date DESC, r.id DESC";
        List<ClientReservation> reservations = new ArrayList<>();
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, clientId);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                reservations.add(new ClientReservation(result.getLong("id"), result.getInt("number"),
                        result.getDate("start_date").toLocalDate(),
                        result.getDate("end_date").toLocalDate(),
                        ReservationStatus.fromDatabase(result.getString("status"))));
            }
            return List.copyOf(reservations);
        } catch (SQLException error) {
            throw new IllegalStateException("Não foi possível consultar as reservas.", error);
        }
    }

    @Override
    public Optional<Reservation> findById(long id) {
        String sql = "SELECT id, customer_id, room_id, start_date, end_date, status, created_by_user_id "
                + "FROM reservations WHERE id = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, id);
                ResultSet result = statement.executeQuery()) {
            if (!result.next()) {
                return Optional.empty();
            }
            return Optional.of(new Reservation(result.getLong("id"), result.getLong("customer_id"),
                    result.getLong("room_id"), result.getDate("start_date").toLocalDate(),
                    result.getDate("end_date").toLocalDate(),
                    ReservationStatus.fromDatabase(result.getString("status")),
                    result.getLong("created_by_user_id")));
        } catch (SQLException error) {
            throw new IllegalStateException("Não foi possível consultar as reservas.", error);
        }
    }

    @Override
    public List<ReservationSummary> findActive() {
        String sql = "SELECT r.id, c.name, c.document, rm.number, r.start_date, r.end_date, r.status "
                + "FROM reservations r JOIN rooms rm ON rm.id = r.room_id "
                + "JOIN customers c ON c.id = r.customer_id "
                + "WHERE r.status IN ('reserved', 'checked_in') ORDER BY r.start_date, r.id";
        List<ReservationSummary> reservations = new ArrayList<>();
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                reservations.add(new ReservationSummary(result.getLong("id"),
                        result.getString("name"), result.getString("document"),
                        result.getInt("number"),
                        result.getDate("start_date").toLocalDate(),
                        result.getDate("end_date").toLocalDate(),
                        ReservationStatus.fromDatabase(result.getString("status"))));
            }
            return List.copyOf(reservations);
        } catch (SQLException error) {
            throw new IllegalStateException("Não foi possível consultar as reservas.", error);
        }
    }

    @Override
    public boolean checkIn(long id) {
        String sql = "UPDATE reservations SET status = 'checked_in', checked_in_at = SYSDATETIME() "
                + "WHERE id = ? AND status = 'reserved'";
        return update(sql, id, "Não foi possível guardar o check-in.");
    }

    @Override
    public boolean checkOut(long id) {
        String sql = "UPDATE reservations SET status = 'checked_out', checked_out_at = SYSDATETIME() "
                + "WHERE id = ? AND status = 'checked_in'";
        return update(sql, id, "Não foi possível guardar o check-out.");
    }

    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM reservations WHERE id = ? AND status = 'reserved'";
        return update(sql, id, "Não foi possível apagar a reserva.");
    }

    private boolean update(String sql, long id, String errorMessage) {
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, id)) {
            return statement.executeUpdate() == 1;
        } catch (SQLException error) {
            throw new IllegalStateException(errorMessage, error);
        }
    }
}
