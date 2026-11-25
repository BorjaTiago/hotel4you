package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.ExitReason;
import pt.isep.hotel4you.domain.MovementType;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.StockMovement;

/** Stores and finds stock items in the SQL Server tables {@code items} and {@code stock_movements}. */
public class JdbcItemRepository implements ItemRepository {

    /**
     * The SQL Server error of a failed CHECK or FOREIGN KEY constraint. In the stock UPDATE
     * only the CHECK {@code stock_quantity >= 0} can fail.
     */
    private static final int CONSTRAINT_FAILED = 547;

    private final Database database;

    /**
     * Creates the repository.
     *
     * @param database the database to read from and write to
     */
    public JdbcItemRepository(Database database) {
        this.database = database;
    }

    @Override
    public Optional<StockItem> findById(long id) {
        String sql = "SELECT id, name, unit, stock_quantity FROM items WHERE id = ?";
        return findOne(sql, id);
    }

    @Override
    public Optional<StockItem> findByName(String name) {
        String sql = "SELECT id, name, unit, stock_quantity FROM items WHERE name = ?";
        return findOne(sql, name);
    }

    @Override
    public List<StockItem> findAll() {
        String sql = "SELECT id, name, unit, stock_quantity FROM items ORDER BY name";
        List<StockItem> items = new ArrayList<>();
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql);
                ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                items.add(toItem(rows));
            }
            return items;
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public StockItem save(StockItem item) {
        String sql = """
                INSERT INTO items (name, unit, stock_quantity)
                OUTPUT INSERTED.id
                VALUES (?, ?, ?)
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, item.getName(),
                        item.getUnit(), item.getStockQuantity());
                ResultSet row = statement.executeQuery()) {
            row.next();
            return new StockItem(row.getLong("id"), item.getName(), item.getUnit(),
                    item.getStockQuantity());
        } catch (SQLException e) {
            if (isDuplicateKey(e)) {
                throw new DuplicateItemException();
            }
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public boolean update(StockItem item) {
        String sql = "UPDATE items SET name = ?, unit = ? WHERE id = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, item.getName(),
                        item.getUnit(), item.getId())) {
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            if (isDuplicateKey(e)) {
                throw new DuplicateItemException();
            }
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>When a statement fails, the method calls a rollback, so the stock does not change.
     * The CHECK {@code stock_quantity >= 0} refuses a stock below zero (SQL error 547), also when
     * another user lowered the stock at the same time. Every other SQL error shows the
     * "not available" message.
     */
    @Override
    public boolean addMovement(StockMovement movement) {
        try (Connection connection = database.connect()) {
            connection.setAutoCommit(false);
            return saveMovement(connection, movement);
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    private static boolean saveMovement(Connection connection, StockMovement movement)
            throws SQLException {
        try {
            if (!updateStock(connection, movement)) {
                connection.rollback();
                return false;
            }
            insertMovement(connection, movement);
            connection.commit();
            return true;
        } catch (SQLException | RuntimeException e) {
            connection.rollback();
            throw e;
        }
    }

    private static boolean updateStock(Connection connection, StockMovement movement)
            throws SQLException {
        String sql = "UPDATE items SET stock_quantity = stock_quantity + ? WHERE id = ?";
        try (PreparedStatement statement = Database.prepare(connection, sql,
                stockChange(movement), movement.getItem().getId())) {
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == CONSTRAINT_FAILED) {
                throw new NotEnoughStockException();
            }
            throw e;
        }
    }

    /** Gives the quantity to add to the stock: positive for an entry, negative for a use or an exit. */
    private static int stockChange(StockMovement movement) {
        return movement.getType() == MovementType.ENTRY
                ? movement.getQuantity() : -movement.getQuantity();
    }

    private static void insertMovement(Connection connection, StockMovement movement)
            throws SQLException {
        String sql = """
                INSERT INTO stock_movements (item_id, type, exit_reason, quantity, moved_at, user_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = Database.prepare(connection, sql,
                movement.getItem().getId(), typeCode(movement.getType()),
                reasonCode(movement.getExitReason()), movement.getQuantity(),
                movement.getDateTime(), movement.getUser().getId())) {
            statement.executeUpdate();
        }
    }

    /** Gives the code of the type in the {@code stock_movements.type} column. */
    private static String typeCode(MovementType type) {
        return switch (type) {
            case ENTRY -> "in";
            case USE -> "use";
            case EXIT -> "out";
        };
    }

    /** Gives the code of the reason in the {@code stock_movements.exit_reason} column, or null. */
    private static String reasonCode(ExitReason reason) {
        if (reason == null) {
            return null;
        }
        return switch (reason) {
            case DISCARD -> "discard";
            case RETURN -> "return";
            case LOSS -> "loss";
        };
    }

    private Optional<StockItem> findOne(String sql, Object... values) {
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, values);
                ResultSet rows = statement.executeQuery()) {
            if (!rows.next()) {
                return Optional.empty();
            }
            return Optional.of(toItem(rows));
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    private static boolean isDuplicateKey(SQLException error) {
        return error.getErrorCode() == 2601 || error.getErrorCode() == 2627;
    }

    private static StockItem toItem(ResultSet row) throws SQLException {
        return new StockItem(row.getLong("id"), row.getString("name"), row.getString("unit"),
                row.getInt("stock_quantity"));
    }
}
