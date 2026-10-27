package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Tests which roles may use each function. */
class PermissionTest {

    private static final String HASH =
            "$2a$12$abcdefghijklmnopqrstuuAbCdEfGhIjKlMnOpQrStUvWxYz01234";

    private static final User MANAGER = new Manager(1L, "Rui Costa", "rui", HASH);
    private static final User RECEPTIONIST = new Receptionist(2L, "Ana Silva", "ana", HASH);
    private static final User RESTAURANT_STAFF =
            new RestaurantStaff(3L, "Joao Lima", "joao", HASH);

    @Test
    @Tag("H4Y-23-AC-2")
    void onAllows_returnFalse_whenReceptionistManagesStaff() {
        assertFalse(Permission.MANAGE_STAFF.allows(RECEPTIONIST));
    }

    @ParameterizedTest
    @EnumSource(Permission.class)
    @Tag("H4Y-23-AC-1")
    void onAllows_returnTrue_whenActorIsManager(Permission permission) {
        assertTrue(permission.allows(MANAGER));
    }

    @ParameterizedTest
    @EnumSource(Permission.class)
    @Tag("H4Y-23-AC-1")
    void onAllows_returnFalse_whenActorIsRestaurantStaff(Permission permission) {
        assertFalse(permission.allows(RESTAURANT_STAFF));
    }

    @ParameterizedTest
    @EnumSource(Permission.class)
    @Tag("H4Y-23-AC-3")
    void onAllows_returnFalse_whenActorIsNull(Permission permission) {
        assertFalse(permission.allows(null));
    }
}
