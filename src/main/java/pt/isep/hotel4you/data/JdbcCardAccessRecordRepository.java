package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import pt.isep.hotel4you.domain.CardAccessRecord;

/** Stores and finds card entries in the SQL Server table {@code access_logs}. */
public class JdbcCardAccessRecordRepository implements CardAccessRecordRepository {

    private static final String COLUMNS = "id, access_card_id, location_id, room_id, entered_at";

    private final Database database;

    /**
     * Creates the repository.
     *
     * @param database the database to read from and write to
     */
    public JdbcCardAccessRecordRepository(Database database) {
        this.database = database;
    }

    @Override
    public CardAccessRecord save(CardAccessRecord record) {
        String sql = """
                INSERT INTO access_logs (access_card_id, location_id, room_id, entered_at)
                OUTPUT INSERTED.id
                VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, record.getCardId(),
                        record.getLocationId(), record.getRoomId(),
                        Timestamp.valueOf(record.getEntryDateTime()));
                ResultSet row = statement.executeQuery()) {
            row.next();
            return new CardAccessRecord(row.getLong("id"), record.getCardId(), record.getRoomId(),
                    record.getLocationId(), record.getEntryDateTime());
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public List<CardAccessRecord> findByCardId(long cardId) {
        return find("access_card_id", cardId);
    }

    @Override
    public List<CardAccessRecord> findByRoomId(long roomId) {
        return find("room_id", roomId);
    }

    private List<CardAccessRecord> find(String column, long value) {
        String sql = "SELECT " + COLUMNS + " FROM access_logs WHERE " + column + " = ? ORDER BY entered_at, id";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, value);
                ResultSet rows = statement.executeQuery()) {
            List<CardAccessRecord> records = new ArrayList<>();
            while (rows.next()) {
                records.add(read(rows));
            }
            return records;
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    private static CardAccessRecord read(ResultSet row) throws SQLException {
        long locationId = row.getLong("location_id");
        Long location = row.wasNull() ? null : locationId;
        long roomId = row.getLong("room_id");
        Long room = row.wasNull() ? null : roomId;
        return new CardAccessRecord(row.getLong("id"), row.getLong("access_card_id"), room, location,
                row.getTimestamp("entered_at").toLocalDateTime());
    }
}
