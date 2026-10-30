package pt.isep.hotel4you.data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pt.isep.hotel4you.domain.RoomType;

/** Finds room types in the SQL Server table {@code room_types} and saves their base price. */
public class JdbcRoomTypeRepository implements RoomTypeRepository {

    private static final String SELECT =
            "SELECT id, name, capacity, base_price FROM room_types ORDER BY id";
    private static final String UPDATE = "UPDATE room_types SET base_price = ? WHERE id = ?";

    private final Database database;

    /**
     * Creates the repository.
     *
     * @param database the database to use
     */
    public JdbcRoomTypeRepository(Database database) {
        this.database = database;
    }

    /**
     * Finds all room types.
     *
     * @return the room types ordered by id, or an empty list if none exists
     * @throws IllegalStateException if the database is not available
     */
    @Override
    public List<RoomType> findAll() {
        try (Connection connection = database.connect();
                PreparedStatement statement = connection.prepareStatement(SELECT);
                ResultSet row = statement.executeQuery()) {
            List<RoomType> types = new ArrayList<>();
            while (row.next()) {
                types.add(new RoomType(row.getLong("id"), row.getString("name"),
                        row.getInt("capacity"), row.getBigDecimal("base_price")));
            }
            return types;
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    /**
     * Saves the base price of a room type.
     *
     * @param id the room type id
     * @param price the new base price
     * @return true if the room type was updated, false if no room type has this id
     * @throws IllegalStateException if the database is not available
     */
    @Override
    public boolean updateBasePrice(long id, BigDecimal price) {
        try (Connection connection = database.connect();
                PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setBigDecimal(1, price);
            statement.setLong(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }
}
