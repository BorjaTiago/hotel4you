package pt.isep.hotel4you.data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;

/** Reads and updates the SQL Server table {@code extra_prices}. */
public class JdbcExtraPriceRepository implements ExtraPriceRepository {
    private static final String SELECT = "SELECT id, name, type, price FROM extra_prices ORDER BY id";
    private static final String UPDATE = "UPDATE extra_prices SET price = ? WHERE id = ?";
    private final Database database;

    /**
     * Creates the repository.
     * @param database the database to use
     */
    public JdbcExtraPriceRepository(Database database) {
        this.database = database;
    }

    /**
     * Finds all catalogue entries, including entries without prices.
     * @return the entries ordered by identifier
     * @throws IllegalStateException if the database is not available or a stored type is invalid
     */
    @Override
    public List<ExtraService> findAll() {
        try (Connection connection = database.connect();
                PreparedStatement statement = connection.prepareStatement(SELECT);
                ResultSet row = statement.executeQuery()) {
            List<ExtraService> extras = new ArrayList<>();
            while (row.next()) {
                extras.add(new ExtraService(row.getLong("id"), row.getString("name"),
                        ExtraType.valueOf(row.getString("type")), row.getBigDecimal("price")));
            }
            return extras;
        } catch (SQLException error) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, error);
        } catch (IllegalArgumentException error) {
            throw new IllegalStateException("O catálogo de preços extra contém um tipo inválido.", error);
        }
    }

    /**
     * Updates one catalogue price with a single SQL statement.
     * @param id the catalogue identifier
     * @param price the validated price per night
     * @return true if the entry was updated, false if it does not exist
     * @throws IllegalStateException if the database is not available
     */
    @Override
    public boolean updatePrice(long id, BigDecimal price) {
        try (Connection connection = database.connect();
                PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setBigDecimal(1, price);
            statement.setLong(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException error) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, error);
        }
    }
}
