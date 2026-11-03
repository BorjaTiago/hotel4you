package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.StockItem;

/** Stores and finds stock items in the SQL Server table {@code items}. */
public class JdbcItemRepository implements ItemRepository {

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
        return query(sql, String.valueOf(id)).stream().findFirst();
    }

    @Override
    public Optional<StockItem> findByName(String name) {
        String sql = "SELECT id, name, unit, stock_quantity FROM items WHERE name = ?";
        return query(sql, name).stream().findFirst();
    }

    @Override
    public List<StockItem> findAll() {
        return query("SELECT id, name, unit, stock_quantity FROM items ORDER BY name");
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
                        item.getUnit(), String.valueOf(item.getStockQuantity()));
                ResultSet row = statement.executeQuery()) {
            row.next();
            return new StockItem(row.getLong("id"), item.getName(), item.getUnit(),
                    item.getStockQuantity());
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public void update(StockItem item) {
        String sql = "UPDATE items SET name = ?, unit = ? WHERE id = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, item.getName(),
                        item.getUnit(), String.valueOf(item.getId()))) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    private List<StockItem> query(String sql, String... values) {
        List<StockItem> items = new ArrayList<>();
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, values);
                ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                items.add(new StockItem(rows.getLong("id"), rows.getString("name"),
                        rows.getString("unit"), rows.getInt("stock_quantity")));
            }
            return items;
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }
}
