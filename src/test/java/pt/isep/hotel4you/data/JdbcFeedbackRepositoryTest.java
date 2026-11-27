package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;
import pt.isep.hotel4you.domain.Feedback;
import pt.isep.hotel4you.domain.FeedbackType;

/** Checks the feedback SQL with JDBC doubles (H4Y-111). */
class JdbcFeedbackRepositoryTest {
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 27, 10, 5);
    private static final Client CLIENT = new Client(7L, "Maria Sousa", DocumentType.CC, "12345678",
            LocalDate.of(1990, 5, 1), "912345678", "PT", null, null);

    private final RecordingDatabase database = new RecordingDatabase();
    private final JdbcFeedbackRepository repository = new JdbcFeedbackRepository(database);

    @Test
    @Tag("H4Y-111-AC-1")
    void onSave_insertSuggestionWithClient_whenClientIsGiven() {
        Feedback feedback = repository.save(CLIENT, FeedbackType.SUGGESTION, "Mais toalhas.");

        String sql = database.sql.replaceAll("\\s+", " ");
        assertTrue(sql.contains("INSERT INTO feedback (customer_id, type, description, created_at)"));
        assertTrue(sql.contains("OUTPUT INSERTED.id, INSERTED.created_at"));
        assertTrue(sql.contains("VALUES (?, ?, ?, SYSDATETIME())"));
        assertEquals(List.of(7L, "suggestion", "Mais toalhas."), database.bound);
        assertEquals(12L, feedback.getId());
        assertSame(CLIENT, feedback.getClient());
        assertEquals(FeedbackType.SUGGESTION, feedback.getType());
        assertEquals("Mais toalhas.", feedback.getDescription());
        assertEquals(NOW, feedback.getCreatedAt());
        assertEquals(List.of("result", "statement", "connection"), database.closed);
    }

    @Test
    @Tag("H4Y-111-AC-2")
    void onSave_insertComplaintWithNullClient_whenNoClientIsGiven() {
        Feedback feedback = repository.save(null, FeedbackType.COMPLAINT, "Barulho.");

        assertEquals(List.of("null:" + Types.INTEGER, "complaint", "Barulho."), database.bound);
        assertNull(feedback.getClient());
        assertEquals(NOW, feedback.getCreatedAt());
    }

    @Test
    @Tag("H4Y-111-AC-1")
    void onSave_throwClientGone_whenClientWasRemoved() {
        database.failure = new SQLException("fk", "23000", 547);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> repository.save(CLIENT, FeedbackType.SUGGESTION, "Mais toalhas."));

        assertEquals("O cliente já não existe. Escolha outro cliente ou registe sem cliente.",
                error.getMessage());
    }

    @Test
    @Tag("H4Y-111-AC-1")
    void onSave_throwNotAvailable_whenDatabaseFails() {
        SQLException failure = new SQLException("down");
        database.failure = failure;

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> repository.save(null, FeedbackType.SUGGESTION, "Mais toalhas."));

        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertSame(failure, error.getCause());
    }

    private static class RecordingDatabase extends Database {
        private final List<String> closed = new ArrayList<>();
        private final List<Object> bound = new ArrayList<>();
        private String sql;
        private SQLException failure;

        RecordingDatabase() {
            super(null);
        }

        @Override
        public Connection connect() {
            return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                    new Class<?>[] {Connection.class}, (proxy, method, args) -> {
                        if (method.getName().equals("close")) {
                            closed.add("connection");
                            return null;
                        }
                        if (method.getName().equals("prepareStatement")) {
                            sql = (String) args[0];
                            return statement();
                        }
                        throw new UnsupportedOperationException(method.getName());
                    });
        }

        private PreparedStatement statement() {
            return (PreparedStatement) Proxy.newProxyInstance(
                    PreparedStatement.class.getClassLoader(), new Class<?>[] {PreparedStatement.class},
                    (proxy, method, args) -> switch (method.getName()) {
                        case "setLong", "setString" -> {
                            bound.add(args[1]);
                            yield null;
                        }
                        case "setNull" -> {
                            bound.add("null:" + args[1]);
                            yield null;
                        }
                        case "executeQuery" -> {
                            if (failure != null) {
                                throw failure;
                            }
                            yield result();
                        }
                        case "close" -> {
                            closed.add("statement");
                            yield null;
                        }
                        default -> throw new UnsupportedOperationException(method.getName());
                    });
        }

        private ResultSet result() {
            return (ResultSet) Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
                    new Class<?>[] {ResultSet.class}, (proxy, method, args) -> switch (method.getName()) {
                        case "next" -> true;
                        case "getLong" -> 12L;
                        case "getTimestamp" -> Timestamp.valueOf(NOW);
                        case "close" -> {
                            closed.add("result");
                            yield null;
                        }
                        default -> throw new UnsupportedOperationException(method.getName());
                    });
        }
    }
}
