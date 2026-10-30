package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public Room save(Room room, int floorLimit) {
        if (floorLimit < 1) {
            throw new IllegalArgumentException("O limite de quartos deve ser positivo.");
        }
        try (Connection connection = database.connect()) {
            return saveInTransaction(connection, room, floorLimit);
        } catch (SQLException e) {
            throw persistenceError(e);
        }
    }

    private static Room saveInTransaction(Connection connection, Room room, int floorLimit)
            throws SQLException {
        connection.setAutoCommit(false);
        try {
            long floorId = findFloorIdForUpdate(connection, room.getFloorNumber());
            long roomTypeId = findRoomTypeId(connection, room.getRoomTypeName());
            if (countByFloorId(connection, floorId) >= floorLimit) {
                throw new RoomLimitExceededException(floorLimit);
            }
            long roomId = insertRoom(connection, room, floorId, roomTypeId);
            connection.commit();
            return new Room(roomId, room.getNumber(), room.getFloorNumber(), room.getRoomTypeName());
        } catch (SQLException e) {
            rollback(connection, e);
            if (isDuplicateKey(e)) {
                throw new DuplicateRoomException();
            }
            throw e;
        } catch (RuntimeException e) {
            rollback(connection, e);
            throw e;
        }
    }

    private static long findFloorIdForUpdate(Connection connection, int floorNumber)
            throws SQLException {
        String sql = "SELECT id FROM floors WITH (UPDLOCK, HOLDLOCK) WHERE number = ?";
        try (PreparedStatement statement = prepareInt(connection, sql, floorNumber);
                ResultSet result = statement.executeQuery()) {
            if (!result.next()) {
                throw new IllegalArgumentException("O piso indicado não existe.");
            }
            return result.getLong("id");
        }
    }

    private static long findRoomTypeId(Connection connection, String roomTypeName)
            throws SQLException {
        try (PreparedStatement statement = Database.prepare(connection,
                "SELECT id FROM room_types WHERE name = ?", roomTypeName);
                ResultSet result = statement.executeQuery()) {
            if (!result.next()) {
                throw new IllegalArgumentException("O tipo de quarto indicado não existe.");
            }
            return result.getLong("id");
        }
    }

    private static int countByFloorId(Connection connection, long floorId) throws SQLException {
        try (PreparedStatement statement = prepareLong(connection,
                "SELECT COUNT(*) FROM rooms WHERE floor_id = ?", floorId);
                ResultSet result = statement.executeQuery()) {
            result.next();
            return result.getInt(1);
        }
    }

    private static long insertRoom(
            Connection connection, Room room, long floorId, long roomTypeId) throws SQLException {
        String sql = "INSERT INTO rooms (number, floor_id, room_type_id) "
                + "OUTPUT INSERTED.id VALUES (?, ?, ?)";
        try (PreparedStatement statement = prepareInsert(connection, sql, room, floorId, roomTypeId);
                ResultSet keys = statement.executeQuery()) {
            if (!keys.next()) {
                throw new SQLException("SQL Server did not return the room identifier.");
            }
            return keys.getLong(1);
        }
    }

    private static PreparedStatement prepareInt(Connection connection, String sql, int value)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, value);
        return statement;
    }

    private static PreparedStatement prepareLong(Connection connection, String sql, long value)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setLong(1, value);
        return statement;
    }

    private static PreparedStatement prepareInsert(
            Connection connection, String sql, Room room, long floorId, long roomTypeId)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, room.getNumber());
        statement.setLong(2, floorId);
        statement.setLong(3, roomTypeId);
        return statement;
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
        return new IllegalStateException(Database.NOT_AVAILABLE, error);
    }
}
