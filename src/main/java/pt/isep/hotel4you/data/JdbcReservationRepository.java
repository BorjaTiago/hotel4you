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
        String sql = """
                INSERT INTO reservations
                    (customer_id, room_id, start_date, end_date, status, created_by_user_id)
                OUTPUT INSERTED.id
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql,
                        reservation.getClientId(), reservation.getRoomId(),
                        Date.valueOf(reservation.getStartDate()), Date.valueOf(reservation.getEndDate()),
                        reservation.getStatus().name().toLowerCase(Locale.ROOT),
                        reservation.getCreatedByUserId());
                ResultSet row = statement.executeQuery()) {
            row.next();
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
}
