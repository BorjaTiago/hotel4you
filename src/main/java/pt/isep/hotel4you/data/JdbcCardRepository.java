package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.CardType;
import pt.isep.hotel4you.domain.MagneticCard;

/** Stores and finds cards in the SQL Server table {@code access_cards}. */
public class JdbcCardRepository implements CardRepository {

    private static final String COLUMNS = "id, card_number, reservation_id, user_id, is_master, active, blocked_at, access_code_hash";

    private final Database database;

    /**
     * Creates the repository.
     *
     * @param database the database to read from and write to
     */
    public JdbcCardRepository(Database database) {
        this.database = database;
    }

    @Override
    public Optional<MagneticCard> findByNumber(String number) {
        String sql = "SELECT " + COLUMNS + " FROM access_cards WHERE card_number = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, number);
                ResultSet row = statement.executeQuery()) {
            return row.next() ? Optional.of(read(row)) : Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public boolean existsByNumber(String number) {
        String sql = "SELECT 1 FROM access_cards WHERE card_number = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, number);
                ResultSet row = statement.executeQuery()) {
            return row.next();
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public Optional<MagneticCard> findByReservationId(long reservationId) {
        String sql = "SELECT " + COLUMNS + " FROM access_cards WHERE reservation_id = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, reservationId);
                ResultSet row = statement.executeQuery()) {
            return row.next() ? Optional.of(read(row)) : Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public List<MagneticCard> findByUserId(long userId) {
        String sql = "SELECT " + COLUMNS + " FROM access_cards WHERE user_id = ? ORDER BY id";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, userId);
                ResultSet row = statement.executeQuery()) {
            List<MagneticCard> cards = new ArrayList<>();
            while (row.next()) {
                cards.add(read(row));
            }
            return cards;
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public List<MagneticCard> findMasters() {
        String sql = "SELECT " + COLUMNS + " FROM access_cards WHERE is_master = 1 ORDER BY id";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql);
                ResultSet row = statement.executeQuery()) {
            List<MagneticCard> cards = new ArrayList<>();
            while (row.next()) {
                cards.add(read(row));
            }
            return cards;
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public MagneticCard save(MagneticCard card) {
        String sql = """
                INSERT INTO access_cards (card_number, reservation_id, user_id, is_master, active, blocked_at,
                    access_code_hash)
                OUTPUT INSERTED.id
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, card.getNumber(),
                        card.getReservationId(), card.getUserId(),
                        bit(card.getType() == CardType.MASTER), bit(card.isActive()),
                        timestamp(card), card.getAccessCodeHash());
                ResultSet row = statement.executeQuery()) {
            row.next();
            return new MagneticCard(row.getLong("id"), card.getNumber(), card.getType(),
                    card.getReservationId(), card.getUserId(), card.isActive(), card.getBlockedAt(),
                    card.getAccessCodeHash());
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public boolean update(MagneticCard card) {
        String sql = "UPDATE access_cards SET active = ?, blocked_at = ? WHERE card_number = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, bit(card.isActive()),
                        timestamp(card), card.getNumber())) {
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    private static MagneticCard read(ResultSet row) throws SQLException {
        long reservationId = row.getLong("reservation_id");
        Long reservation = row.wasNull() ? null : reservationId;
        long userId = row.getLong("user_id");
        Long user = row.wasNull() ? null : userId;
        Timestamp blockedAt = row.getTimestamp("blocked_at");
        return new MagneticCard(row.getLong("id"), row.getString("card_number"),
                row.getBoolean("is_master") ? CardType.MASTER : CardType.CLIENT, reservation, user,
                row.getBoolean("active"), blockedAt == null ? null : blockedAt.toLocalDateTime(),
                row.getString("access_code_hash"));
    }

    private static String bit(boolean value) {
        return value ? "1" : "0";
    }

    private static Timestamp timestamp(MagneticCard card) {
        return card.getBlockedAt() == null ? null : Timestamp.valueOf(card.getBlockedAt());
    }
}
