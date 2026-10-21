package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import pt.isep.hotel4you.config.AppConfig;

/** Opens connections to the SQL Server database. */
public class Database {

    /** The message of the error when the database cannot be reached. */
    public static final String NOT_AVAILABLE = "Database not available. Check that SQL Server is running.";

    private final AppConfig config;

    /**
     * Creates a database object.
     *
     * @param config the settings with the url, user and password
     */
    public Database(AppConfig config) {
        this.config = config;
    }

    /**
     * Opens a new connection. The caller must close it.
     *
     * @return the open connection
     * @throws IllegalStateException if the database cannot be reached
     */
    public Connection connect() {
        try {
            return DriverManager.getConnection(config.dbUrl(), config.dbUser(), config.dbPassword());
        } catch (SQLException e) {
            throw new IllegalStateException(NOT_AVAILABLE, e);
        }
    }

    /**
     * Checks that the database can be reached. It opens a connection and closes it.
     *
     * @throws IllegalStateException if the database cannot be reached
     */
    public void check() {
        try (Connection connection = connect()) {
            // The connection opened, so the database works. It closes at the end of the block.
        } catch (SQLException e) {
            throw new IllegalStateException(NOT_AVAILABLE, e);
        }
    }

    /**
     * Prepares a SQL statement and fills its {@code ?} values in order.
     *
     * @param connection the open connection
     * @param sql the SQL text with {@code ?} for each value
     * @param values the values for the {@code ?} marks, in order
     * @return the statement, ready to run
     * @throws SQLException if the SQL is not valid
     */
    public static PreparedStatement prepare(Connection connection, String sql, String... values)
            throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            statement.setString(i + 1, values[i]);
        }
        return statement;
    }
}
