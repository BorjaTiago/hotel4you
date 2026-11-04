package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import pt.isep.hotel4you.domain.Client;

/** Stores and finds clients in the SQL Server table {@code customers}. */
public class JdbcClientRepository implements ClientRepository {

    private final Database database;

    /**
     * Creates the repository.
     *
     * @param database the database to read from and write to
     */
    public JdbcClientRepository(Database database) {
        this.database = database;
    }

    @Override
    public Optional<Client> findByDocument(String document) {
        String sql = "SELECT id, name, document FROM customers WHERE document = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, document);
                ResultSet row = statement.executeQuery()) {
            if (!row.next()) {
                return Optional.empty();
            }
            return Optional.of(new Client(row.getLong("id"), row.getString("name"),
                    row.getString("document")));
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public Client save(Client client) {
        String sql = """
                INSERT INTO customers (name, document)
                OUTPUT INSERTED.id
                VALUES (?, ?)
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, client.getName(),
                        client.getDocument());
                ResultSet row = statement.executeQuery()) {
            row.next();
            return new Client(row.getLong("id"), client.getName(), client.getDocument());
        } catch (SQLException e) {
            if (isDuplicateKey(e)) {
                throw new ClientExistsException();
            }
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    private static boolean isDuplicateKey(SQLException error) {
        return error.getErrorCode() == 2601 || error.getErrorCode() == 2627;
    }
}
