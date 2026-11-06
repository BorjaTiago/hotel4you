package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;

/**
 * Stores and finds clients in the SQL Server table {@code customers}. Text matches are not case
 * sensitive because v1 creates the database with the CI_AS collation.
 */
public class JdbcClientRepository implements ClientRepository {

    private static final int FOREIGN_KEY_ERROR = 547;
    private static final String COLUMNS =
            "id, name, document_type, document, birth_date, phone, nationality, email, tax_number";

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
    public Optional<Client> findById(long id) {
        String sql = "SELECT " + COLUMNS + " FROM customers WHERE id = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, id);
                ResultSet row = statement.executeQuery()) {
            if (!row.next()) {
                return Optional.empty();
            }
            return Optional.of(toClient(row));
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public Optional<Client> findByDocument(DocumentType documentType, String document) {
        String sql = "SELECT " + COLUMNS + " FROM customers WHERE document_type = ? AND document = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, documentType.name(),
                        document);
                ResultSet row = statement.executeQuery()) {
            if (!row.next()) {
                return Optional.empty();
            }
            return Optional.of(toClient(row));
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public List<Client> findAll() {
        String sql = "SELECT " + COLUMNS + " FROM customers ORDER BY name";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql);
                ResultSet rows = statement.executeQuery()) {
            List<Client> clients = new ArrayList<>();
            while (rows.next()) {
                clients.add(toClient(rows));
            }
            return clients;
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public List<Client> search(String text) {
        String sql = "SELECT " + COLUMNS + " FROM customers"
                + " WHERE name LIKE ? ESCAPE '\\' OR document LIKE ? ESCAPE '\\' ORDER BY name";
        String pattern = "%" + escapeLike(text) + "%";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, pattern, pattern);
                ResultSet rows = statement.executeQuery()) {
            List<Client> clients = new ArrayList<>();
            while (rows.next()) {
                clients.add(toClient(rows));
            }
            return clients;
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public Client save(Client client) {
        String sql = """
                INSERT INTO customers
                    (name, document_type, document, birth_date, phone, nationality, email, tax_number)
                OUTPUT INSERTED.id
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql,
                        client.getName(), client.getDocumentType().name(), client.getDocument(),
                        Date.valueOf(client.getBirthDate()), client.getPhone(),
                        client.getNationality(), client.getEmail(), client.getTaxNumber());
                ResultSet row = statement.executeQuery()) {
            row.next();
            return new Client(row.getLong("id"), client.getName(), client.getDocumentType(),
                    client.getDocument(), client.getBirthDate(), client.getPhone(),
                    client.getNationality(), client.getEmail(), client.getTaxNumber());
        } catch (SQLException e) {
            if (isDuplicateKey(e)) {
                throw new ClientExistsException();
            }
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public boolean update(Client client) {
        String sql = """
                UPDATE customers
                SET name = ?, document_type = ?, document = ?, birth_date = ?, phone = ?,
                    nationality = ?, email = ?, tax_number = ?
                WHERE id = ?
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql,
                        client.getName(), client.getDocumentType().name(), client.getDocument(),
                        Date.valueOf(client.getBirthDate()), client.getPhone(),
                        client.getNationality(), client.getEmail(), client.getTaxNumber(),
                        client.getId())) {
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            if (isDuplicateKey(e)) {
                throw new ClientExistsException();
            }
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection,
                        "DELETE FROM customers WHERE id = ?", id)) {
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == FOREIGN_KEY_ERROR) {
                throw new ClientInUseException();
            }
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    /** Escapes the LIKE wildcards, so the text matches as plain text. */
    private static String escapeLike(String text) {
        return text.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_").replace("[", "\\[");
    }

    private static boolean isDuplicateKey(SQLException error) {
        return error.getErrorCode() == 2601 || error.getErrorCode() == 2627;
    }

    private static Client toClient(ResultSet row) throws SQLException {
        return new Client(row.getLong("id"), row.getString("name"),
                DocumentType.fromDatabase(row.getString("document_type")), row.getString("document"),
                row.getDate("birth_date").toLocalDate(), row.getString("phone"),
                row.getString("nationality"), row.getString("email"), row.getString("tax_number"));
    }
}
