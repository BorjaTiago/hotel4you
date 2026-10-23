package pt.isep.hotel4you.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

/** Stores and finds users in the SQL Server tables {@code users} and {@code roles}. */
public class JdbcUserRepository implements UserRepository {

    private final Database database;

    /**
     * Creates the repository.
     *
     * @param database the database to read from and write to
     */
    public JdbcUserRepository(Database database) {
        this.database = database;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = """
                SELECT u.id, u.name, u.username, u.password_hash, r.name AS role
                FROM users u JOIN roles r ON r.id = u.role_id
                WHERE u.username = ?
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, username);
                ResultSet row = statement.executeQuery()) {
            if (!row.next()) {
                return Optional.empty();
            }
            return Optional.ofNullable(toUser(row));
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, username);
                ResultSet row = statement.executeQuery()) {
            return row.next();
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public boolean existsByRole(Role role) {
        String sql = """
                SELECT 1
                FROM users u JOIN roles r ON r.id = u.role_id
                WHERE r.name = ?
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, role.name());
                ResultSet row = statement.executeQuery()) {
            return row.next();
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    @Override
    public User save(User user) {
        String sql = """
                INSERT INTO users (name, username, password_hash, role_id)
                OUTPUT INSERTED.id
                SELECT ?, ?, ?, id FROM roles WHERE name = ?
                """;
        try (Connection connection = database.connect();
                PreparedStatement statement = Database.prepare(connection, sql, user.getName(),
                        user.getUsername(), user.getPasswordHash(), user.getRole().name());
                ResultSet row = statement.executeQuery()) {
            if (!row.next()) {
                throw new IllegalStateException("Papel não encontrado. Execute os scripts da base de dados.");
            }
            return createUser(row.getLong("id"), user.getName(), user.getUsername(),
                    user.getPasswordHash(), user.getRole());
        } catch (SQLException e) {
            throw new IllegalStateException(Database.NOT_AVAILABLE, e);
        }
    }

    private static User toUser(ResultSet row) throws SQLException {
        return createUser(row.getLong("id"), row.getString("name"), row.getString("username"),
                row.getString("password_hash"), Role.valueOf(row.getString("role")));
    }

    private static User createUser(Long id, String name, String username, String passwordHash, Role role) {
        return switch (role) {
            case MANAGER -> new Manager(id, name, username, passwordHash);
            case RECEPTIONIST -> new Receptionist(id, name, username, passwordHash);
            case RESTAURANT -> new RestaurantStaff(id, name, username, passwordHash);
            // Clients and suppliers have no account, so there is no user object for them.
            default -> null;
        };
    }
}
