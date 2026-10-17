package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests the fixed role of each User subclass. */
class UserRoleTest {

    private static final String HASH =
            "$2a$12$abcdefghijklmnopqrstuuAbCdEfGhIjKlMnOpQrStUvWxYz01234";

    @Test
    @Tag("H4Y-32-AC-2")
    void onCreateManager_setRoleManager_always() {
        assertEquals(Role.MANAGER, new Manager(1L, "Rui Costa", "rui", HASH).getRole());
    }

    @Test
    @Tag("H4Y-32-AC-2")
    void onCreateReceptionist_setRoleReceptionist_always() {
        assertEquals(Role.RECEPTIONIST,
                new Receptionist(2L, "Ana Silva", "ana", HASH).getRole());
    }

    @Test
    @Tag("H4Y-32-AC-2")
    void onCreateRestaurantStaff_setRoleRestaurant_always() {
        assertEquals(Role.RESTAURANT,
                new RestaurantStaff(3L, "Joao Dias", "joao", HASH).getRole());
    }
}
