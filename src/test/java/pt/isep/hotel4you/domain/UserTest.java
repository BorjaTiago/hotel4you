package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests the rules of the User base class. */
class UserTest {

    private static final String HASH =
            "$2a$12$abcdefghijklmnopqrstuuAbCdEfGhIjKlMnOpQrStUvWxYz01234";

    @Test
    @Tag("H4Y-32-AC-2")
    void onCreateUser_keepAllFields_whenValuesValid() {
        User user = new Receptionist(7L, "Ana Silva", "ana", HASH);

        assertEquals(7L, user.getId());
        assertEquals("Ana Silva", user.getName());
        assertEquals("ana", user.getUsername());
        assertEquals(HASH, user.getPasswordHash());
        assertEquals(Role.RECEPTIONIST, user.getRole());
    }

    @Test
    @Tag("H4Y-32-AC-2")
    void onCreateUser_allowNullId_whenNotSaved() {
        User user = new Manager(null, "Rui Costa", "rui", HASH);

        assertNull(user.getId());
    }

    @Test
    @Tag("H4Y-32-AC-2")
    void onCreateUser_trimUsername_whenUsernameHasSpaces() {
        User user = new Receptionist(1L, "Ana Silva", "  ana  ", HASH);

        assertEquals("ana", user.getUsername());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-32-AC-2")
    void onCreateUser_throw_whenNameBlank(String name) {
        assertThrows(IllegalArgumentException.class,
                () -> new Receptionist(1L, name, "ana", HASH));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-32-AC-2")
    void onCreateUser_throw_whenUsernameBlank(String username) {
        assertThrows(IllegalArgumentException.class,
                () -> new Receptionist(1L, "Ana Silva", username, HASH));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-32-AC-2")
    void onCreateUser_throw_whenPasswordHashBlank(String passwordHash) {
        assertThrows(IllegalArgumentException.class,
                () -> new Receptionist(1L, "Ana Silva", "ana", passwordHash));
    }
}
