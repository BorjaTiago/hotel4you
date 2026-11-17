package pt.isep.hotel4you.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests that invalid catalogue data does not expose Java enum error text to the user. */
class JdbcExtraPriceRepositoryTest {
    @Test
    @Tag("H4Y-93-AC-3")
    void onFindAll_wrapMappingError_whenStoredTypeIsUnknown() {
        int[] rowsLeft = {1};
        Database database = new Database(null) {
            @Override public Connection connect() {
                return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                        new Class<?>[] {Connection.class}, (connection, method, arguments) -> {
                            if (!method.getName().equals("prepareStatement")) return null;
                            return Proxy.newProxyInstance(PreparedStatement.class.getClassLoader(),
                                    new Class<?>[] {PreparedStatement.class}, (statement, operation, values) -> {
                                        if (!operation.getName().equals("executeQuery")) return null;
                                        return Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
                                                new Class<?>[] {ResultSet.class}, (row, getter, column) ->
                                                    switch (getter.getName()) {
                                                        case "next" -> rowsLeft[0]-- > 0;
                                                        case "getLong" -> 1L;
                                                        case "getString" -> "type".equals(column[0]) ? "UNKNOWN" : "Vista mar";
                                                        default -> null;
                                                    });
                                    });
                        });
            }
        };
        var repository = new JdbcExtraPriceRepository(database);
        var error = assertThrows(IllegalStateException.class, repository::findAll);
        assertEquals(Database.NOT_AVAILABLE, error.getMessage());
        assertInstanceOf(IllegalArgumentException.class, error.getCause());
    }
}
