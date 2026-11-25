package pt.isep.hotel4you.data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import pt.isep.hotel4you.domain.RoomServiceRequest;
import pt.isep.hotel4you.domain.RoomServiceType;

/** Saves the room service requests in the SQL Server table {@code reservation_services}. */
public class JdbcRoomServiceRequestRepository implements RoomServiceRequestRepository {
    private static final String FIND_EXTRA = "SELECT id, price FROM extra_prices WHERE name = ?";
    private static final String INSERT =
            "INSERT INTO reservation_services (reservation_id, extra_price_id, unit_price, used_at) "
            + "OUTPUT INSERTED.id, INSERTED.reservation_id, INSERTED.used_at "
            + "SELECT TOP (1) r.id, ?, ?, SYSDATETIME() FROM reservations r "
            + "JOIN rooms ro ON ro.id = r.room_id "
            + "WHERE ro.number = ? AND r.status = 'checked_in' "
            + "ORDER BY r.id";

    private final Database database;

    /**
     * Creates the repository.
     *
     * @param database the database to use
     */
    public JdbcRoomServiceRequestRepository(Database database) {
        this.database = database;
    }

    /**
     * Reads the catalogue entry of the type, then inserts the request with one SQL statement
     * that also checks the check-in of the room.
     *
     * @param roomNumber the room number
     * @param type the service type
     * @return the saved request, or empty if the room has no reservation with check-in
     * @throws IllegalStateException if the catalogue has no entry for the type or the database
     *         is not available
     */
    @Override
    public Optional<RoomServiceRequest> save(int roomNumber, RoomServiceType type) {
        try (Connection connection = database.connect()) {
            long extraId;
            BigDecimal price;
            try (PreparedStatement statement = connection.prepareStatement(FIND_EXTRA)) {
                statement.setString(1, type.catalogueName());
                try (ResultSet row = statement.executeQuery()) {
                    if (!row.next()) {
                        throw new IllegalStateException("O catálogo não tem o serviço "
                                + type.catalogueName() + ". Execute o script v14.");
                    }
                    extraId = row.getLong("id");
                    price = row.getBigDecimal("price");
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
                statement.setLong(1, extraId);
                statement.setBigDecimal(2, price);
                statement.setInt(3, roomNumber);
                try (ResultSet row = statement.executeQuery()) {
                    if (!row.next()) {
                        return Optional.empty();
                    }
                    return Optional.of(new RoomServiceRequest(row.getLong("id"),
                            row.getLong("reservation_id"), roomNumber, type, price,
                            row.getTimestamp("used_at").toLocalDateTime()));
                }
            }
        } catch (SQLException error) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, error);
        }
    }
}
