package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pt.isep.hotel4you.domain.Room;

/** Stores rooms in SQL Server. */
public class JdbcRoomRepository implements RoomRepository {

    private final Database database;

    /**
     * Creates the repository.
     *
     * @param database the database connection provider
     */
    public JdbcRoomRepository(Database database) {
        this.database = database;
    }

    @Override
    public boolean floorExists(int floorNumber) {
        return exists("SELECT 1 FROM floors WHERE number = ?", floorNumber);
    }

    @Override
    public boolean roomTypeExists(String roomTypeName) {
        try (Connection connection = database.connect();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT 1 FROM room_types WHERE name = ?")) {
            statement.setString(1, roomTypeName);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw persistenceError(e);
        }
    }

    @Override
    public List<String> findRoomTypeNames() {
        List<String> names = new ArrayList<>();
        try (Connection connection = database.connect();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT name FROM room_types ORDER BY name");
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                names.add(result.getString("name"));
            }
            return List.copyOf(names);
        } catch (SQLException e) {
            throw persistenceError(e);
        }
    }

    @Override
    public boolean existsByNumber(int roomNumber) {
        return exists("SELECT 1 FROM rooms WHERE number = ?", roomNumber);
    }

    @Override
    public int countByFloor(int floorNumber) {
        String sql = "SELECT COUNT(*) FROM rooms r JOIN floors f ON f.id = r.floor_id "
                + "WHERE f.number = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, floorNumber);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getInt(1);
            }
        } catch (SQLException e) {
            throw persistenceError(e);
        }
    }

    @Override
    public List<Room> findAll() {
        String sql = "SELECT r.id, r.number, f.number AS floor_number, t.name AS room_type_name "
                + "FROM rooms r JOIN floors f ON f.id = r.floor_id "
                + "JOIN room_types t ON t.id = r.room_type_id ORDER BY r.number";
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = database.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                rooms.add(new Room(
                        result.getLong("id"),
                        result.getInt("number"),
                        result.getInt("floor_number"),
                        result.getString("room_type_name")));
            }
            return List.copyOf(rooms);
        } catch (SQLException e) {
            throw persistenceError(e);
        }
    }

    @Override
    public Room saveWithinFloorLimit(Room room, int floorLimit) {
        if (floorLimit < 1) {
            throw new IllegalArgumentException("O limite de quartos deve ser positivo.");
        }
        try (Connection connection = database.connect()) {
            connection.setAutoCommit(false);
            try {
                long floorId = findFloorIdForUpdate(connection, room.floorNumber());
                long roomTypeId = findRoomTypeId(connection, room.roomTypeName());
                if (countByFloorId(connection, floorId) >= floorLimit) {
                    throw new RoomLimitExceededException(floorLimit);
                }
                long roomId = insertRoom(connection, room, floorId, roomTypeId);
                connection.commit();
                return new Room(roomId, room.number(), room.floorNumber(), room.roomTypeName());
            } catch (RoomLimitExceededException | IllegalArgumentException e) {
                rollback(connection, e);
                throw e;
            } catch (SQLException e) {
                rollback(connection, e);
                if (isDuplicateKey(e)) {
                    throw new DuplicateRoomException();
                }
                throw persistenceError(e);
            }
        } catch (SQLException e) {
            throw persistenceError(e);
        }
    }

    private boolean exists(String sql, int value) {
        try (Connection connection = database.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, value);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw persistenceError(e);
        }
    }

    private static long findFloorIdForUpdate(Connection connection, int floorNumber)
            throws SQLException {
        String sql = "SELECT id FROM floors WITH (UPDLOCK, HOLDLOCK) WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, floorNumber);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new IllegalArgumentException("O piso indicado não existe.");
                }
                return result.getLong("id");
            }
        }
    }

    private static long findRoomTypeId(Connection connection, String roomTypeName)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id FROM room_types WHERE name = ?")) {
            statement.setString(1, roomTypeName);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new IllegalArgumentException("O tipo de quarto indicado não existe.");
                }
                return result.getLong("id");
            }
        }
    }

    private static int countByFloorId(Connection connection, long floorId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM rooms WHERE floor_id = ?")) {
            statement.setLong(1, floorId);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getInt(1);
            }
        }
    }

    private static long insertRoom(
            Connection connection, Room room, long floorId, long roomTypeId) throws SQLException {
        String sql = "INSERT INTO rooms (number, floor_id, room_type_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, room.number());
            statement.setLong(2, floorId);
            statement.setLong(3, roomTypeId);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("SQL Server did not return the room identifier.");
                }
                return keys.getLong(1);
            }
        }
    }

    private static boolean isDuplicateKey(SQLException error) {
        return error.getErrorCode() == 2601 || error.getErrorCode() == 2627;
    }

    private static void rollback(Connection connection, Exception cause) {
        try {
            connection.rollback();
        } catch (SQLException rollbackError) {
            cause.addSuppressed(rollbackError);
        }
    }

    private static IllegalStateException persistenceError(SQLException error) {
        return new IllegalStateException("Não foi possível guardar os quartos.", error);
    }
}
