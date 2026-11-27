package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.Feedback;
import pt.isep.hotel4you.domain.FeedbackType;

/** Saves the client suggestions and complaints in the SQL Server table {@code feedback}. */
public class JdbcFeedbackRepository implements FeedbackRepository {
    private static final int FOREIGN_KEY_ERROR = 547;
    private static final String INSERT =
            "INSERT INTO feedback (customer_id, type, description, created_at) "
            + "OUTPUT INSERTED.id, INSERTED.created_at "
            + "VALUES (?, ?, ?, SYSDATETIME())";

    private final Database database;

    /**
     * Creates the repository.
     *
     * @param database the database to use
     */
    public JdbcFeedbackRepository(Database database) {
        this.database = database;
    }

    /**
     * Inserts one row. The database gives the identifier and the date and time.
     *
     * @param client the saved client, or null if no client is known
     * @param type the feedback type
     * @param description the checked text
     * @return the saved feedback
     * @throws IllegalArgumentException if the client no longer exists (SQL error 547)
     * @throws IllegalStateException if the database is not available
     */
    @Override
    public Feedback save(Client client, FeedbackType type, String description) {
        try (Connection connection = database.connect();
                PreparedStatement statement = connection.prepareStatement(INSERT)) {
            if (client == null) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setLong(1, client.getId());
            }
            statement.setString(2, type.databaseValue());
            statement.setString(3, description);
            try (ResultSet row = statement.executeQuery()) {
                row.next();
                return new Feedback(row.getLong("id"), client, type, description,
                        row.getTimestamp("created_at").toLocalDateTime());
            }
        } catch (SQLException error) {
            if (error.getErrorCode() == FOREIGN_KEY_ERROR) {
                throw new IllegalArgumentException(
                        "O cliente já não existe. Escolha outro cliente ou registe sem cliente.", error);
            }
            throw new IllegalStateException(Database.NOT_AVAILABLE, error);
        }
    }
}
