package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;

/** Reads booking periods from the existing SQL Server reservation schema. */
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
}
