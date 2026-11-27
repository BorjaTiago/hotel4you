package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;
import pt.isep.hotel4you.domain.ReservationExtra;

/** Stores reservation extras and their price snapshots in SQL Server. */
public class JdbcReservationExtrasRepository implements ReservationExtrasRepository {
    private final Database database;

    /**
     * Creates the repository.
     * @param database the connection provider
     */
    public JdbcReservationExtrasRepository(Database database) {
        this.database = database;
    }

    @Override
    public List<ReservationExtra> findByReservation(long reservationId, ExtraType type) {
        String sql = "SELECT x.extra_price_id, p.name, x.unit_price FROM " + tableFor(type) + " x "
                + "JOIN extra_prices p ON p.id = x.extra_price_id "
                + "WHERE x.reservation_id = ?" + includedOnly(type) + " ORDER BY x.extra_price_id";
        List<ReservationExtra> items = new ArrayList<>();
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, reservationId);
                ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                items.add(new ReservationExtra(rows.getLong("extra_price_id"),
                        rows.getString("name"), rows.getBigDecimal("unit_price")));
            }
            return items;
        } catch (SQLException error) {
            throw new IllegalStateException("Não foi possível consultar os extras da reserva.", error);
        }
    }

    @Override
    public boolean add(long reservationId, ExtraService extra, Long replacedExtraId) {
        ExtraType type = extra.getType();
        String table = tableFor(type);
        return edit(reservationId, connection -> {
            if (replacedExtraId != null) {
                deleteExtra(connection, reservationId, replacedExtraId, type);
            }
            String sql = "INSERT INTO " + table + " (reservation_id, extra_price_id, unit_price) "
                    + "SELECT ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM " + table + " x "
                    + "WHERE x.reservation_id = ? AND x.extra_price_id = ?" + includedOnly(type) + ")";
            try (PreparedStatement statement = Database.prepare(connection, sql,
                    reservationId, extra.getId(), extra.getPrice(), reservationId, extra.getId())) {
                statement.executeUpdate();
            }
        });
    }

    @Override
    public boolean remove(long reservationId, long extraPriceId, ExtraType type) {
        return edit(reservationId, connection -> deleteExtra(connection, reservationId, extraPriceId, type));
    }

    private static void deleteExtra(Connection connection, long reservationId, long extraPriceId, ExtraType type)
            throws SQLException {
        String sql = "DELETE x FROM " + tableFor(type) + " x WHERE x.reservation_id = ? "
                + "AND x.extra_price_id = ?" + includedOnly(type);
        try (PreparedStatement statement = Database.prepare(connection, sql, reservationId, extraPriceId)) {
            statement.executeUpdate();
        }
    }

    private static String tableFor(ExtraType type) {
        return switch (type) {
            case ROOM_EXTRA -> "reservation_extras";
            case PAID_SERVICE -> "reservation_services";
        };
    }

    private static String includedOnly(ExtraType type) {
        return type == ExtraType.PAID_SERVICE ? " AND x.used_at IS NULL" : "";
    }

    private boolean edit(long reservationId, Change change) {
        try (Connection connection = database.connect()) {
            connection.setAutoCommit(false);
            return edit(connection, reservationId, change);
        } catch (SQLException error) {
            throw new IllegalStateException("Não foi possível alterar os extras da reserva.", error);
        }
    }

    private static boolean edit(Connection connection, long reservationId, Change change) throws SQLException {
        try {
            if (!JdbcReservationRepository.lockReserved(connection, reservationId)) {
                connection.rollback();
                return false;
            }
            change.apply(connection);
            connection.commit();
            return true;
        } catch (SQLException error) {
            connection.rollback();
            throw error;
        }
    }

    private interface Change {
        void apply(Connection connection) throws SQLException;
    }
}
