package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import pt.isep.hotel4you.domain.Location;

/** Stores and finds zones in the SQL Server table {@code locations}. */
public class JdbcLocationRepository implements LocationRepository {

    /** The SQL Server error code when a foreign key blocks a delete. */
    private static final int FOREIGN_KEY_ERROR = 547;

    private final Database database;

    /**
     * Creates the repository.
     *
     * @param database the database to read from and write to
     */
    public JdbcLocationRepository(Database database) {
        this.database = database;
    }

    @Override
    public Optional<Location> findById(long id) {
        String sql = """
                SELECT id, name, guest_card_access, requires_reservation
                FROM locations WHERE id = ?
                """;
        return findOne(sql, String.valueOf(id));
    }

    @Override
    public Optional<Location> findByFloorAndName(int floorNumber, String name) {
        String sql = """
                SELECT l.id, l.name, l.guest_card_access, l.requires_reservation
                FROM locations l JOIN floors f ON f.id = l.floor_id
                WHERE f.number = ? AND l.name = ?
                """;
        return findOne(sql, String.valueOf(floorNumber), name);
    }

    @Override
    public Location save(int floorNumber, Location location) {
        String sql = """
                INSERT INTO locations (name, floor_id, guest_card_access, requires_reservation)
                OUTPUT INSERTED.id
                SELECT ?, id, ?, ? FROM floors WHERE number = ?
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, location.getName(),
                        bit(location.isGuestCardAccess()), bit(location.isRequiresReservation()),
                        String.valueOf(floorNumber));
                ResultSet row = statement.executeQuery()) {
            if (!row.next()) {
                throw new IllegalStateException("Piso não encontrado. Execute os scripts da base de dados.");
            }
            return new Location(row.getLong("id"), location.getName(),
                    location.isGuestCardAccess(), location.isRequiresReservation());
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public void update(int floorNumber, Location location) {
        String sql = """
                UPDATE locations
                SET name = ?, floor_id = (SELECT id FROM floors WHERE number = ?),
                    guest_card_access = ?, requires_reservation = ?
                WHERE id = ?
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, location.getName(),
                        String.valueOf(floorNumber), bit(location.isGuestCardAccess()),
                        bit(location.isRequiresReservation()), String.valueOf(location.getId()))) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection,
                        "DELETE FROM locations WHERE id = ?", String.valueOf(id))) {
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == FOREIGN_KEY_ERROR) {
                return false;
            }
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    private Optional<Location> findOne(String sql, String... values) {
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, values);
                ResultSet row = statement.executeQuery()) {
            if (!row.next()) {
                return Optional.empty();
            }
            return Optional.of(new Location(row.getLong("id"), row.getString("name"),
                    row.getBoolean("guest_card_access"), row.getBoolean("requires_reservation")));
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    private static String bit(boolean value) {
        return value ? "1" : "0";
    }
}
