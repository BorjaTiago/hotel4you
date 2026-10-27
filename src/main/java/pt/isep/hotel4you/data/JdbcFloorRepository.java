package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Location;

/** Finds floors and zones in the SQL Server tables {@code floors} and {@code locations}. */
public class JdbcFloorRepository implements FloorRepository {

    private static final String SELECT = """
            SELECT f.number, f.description, l.id, l.name, l.guest_card_access, l.requires_reservation
            FROM floors f LEFT JOIN locations l ON l.floor_id = f.id
            """;
    private static final String ORDER = " ORDER BY f.number, l.name";

    private final Database database;

    /**
     * Creates the repository.
     *
     * @param database the database to read from
     */
    public JdbcFloorRepository(Database database) {
        this.database = database;
    }

    /**
     * Finds all floors with their zones, in one query.
     *
     * @return the floors ordered by number, or an empty list if none exists
     * @throws IllegalStateException if the database is not available
     */
    @Override
    public List<Floor> findAll() {
        return query(SELECT + ORDER);
    }

    /**
     * Finds one floor with its zones, in one query.
     *
     * @param number the floor number
     * @return the floor, or empty if it does not exist
     * @throws IllegalStateException if the database is not available
     */
    @Override
    public Optional<Floor> findByNumber(int number) {
        List<Floor> floors = query(SELECT + " WHERE f.number = ?" + ORDER, String.valueOf(number));
        return floors.stream().findFirst();
    }

    private List<Floor> query(String sql, String... values) {
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, values);
                ResultSet row = statement.executeQuery()) {
            return toFloors(row);
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    /** Groups the rows (ordered by floor) into floors. A floor without zones has an empty list. */
    private static List<Floor> toFloors(ResultSet row) throws SQLException {
        List<Floor> floors = new ArrayList<>();
        int currentNumber = 0;
        String currentDescription = null;
        List<Location> locations = new ArrayList<>();
        boolean open = false;
        while (row.next()) {
            int number = row.getInt("number");
            if (open && number != currentNumber) {
                floors.add(new Floor(currentNumber, currentDescription, locations));
                locations = new ArrayList<>();
            }
            open = true;
            currentNumber = number;
            currentDescription = row.getString("description");
            long id = row.getLong("id");
            if (!row.wasNull()) {
                locations.add(new Location(id, row.getString("name"),
                        row.getBoolean("guest_card_access"), row.getBoolean("requires_reservation")));
            }
        }
        if (open) {
            floors.add(new Floor(currentNumber, currentDescription, locations));
        }
        return floors;
    }
}
