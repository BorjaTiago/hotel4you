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

    @Test
    @Tag("H4Y-28-AC-1")
    void onAllows_returnFalse_whenReceptionistManagesFloors() {
        assertFalse(Permission.MANAGE_FLOORS.allows(RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onAllows_returnFalse_whenReceptionistManagesRooms() {
        assertFalse(Permission.MANAGE_ROOMS.allows(RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-26-AC-4")
    void onAllows_returnFalse_whenReceptionistManagesPrices() {
        assertFalse(Permission.MANAGE_PRICES.allows(RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onAllows_returnTrue_whenReceptionistManagesClients() {
        assertTrue(Permission.MANAGE_CLIENTS.allows(RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-50-AC-1")
    void onAllows_returnTrue_whenReceptionistListsRooms() {
        assertTrue(Permission.LIST_ROOMS.allows(RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-51-AC-4")
    void onAllows_returnFalse_whenReceptionistManagesStock() {
        assertFalse(Permission.MANAGE_STOCK.allows(RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-51-AC-4")
    void onAllows_returnFalse_whenRestaurantStaffManagesStock() {
        assertFalse(Permission.MANAGE_STOCK.allows(RESTAURANT_STAFF));
    }

    @Test
    @Tag("H4Y-51-AC-4")
    void onAllows_returnFalse_whenNullManagesStock() {
        assertFalse(Permission.MANAGE_STOCK.allows(null));
    }

    @Test
    @Tag("H4Y-51-AC-1")
    void onAllows_returnTrue_whenManagerManagesStock() {
        assertTrue(Permission.MANAGE_STOCK.allows(MANAGER));
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onAllows_returnTrue_whenManagerManagesClients() {
        assertTrue(Permission.MANAGE_CLIENTS.allows(MANAGER));
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onAllows_returnFalse_whenRestaurantStaffManagesClients() {
        assertFalse(Permission.MANAGE_CLIENTS.allows(RESTAURANT_STAFF));
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onAllows_returnFalse_whenNullManagesClients() {
        assertFalse(Permission.MANAGE_CLIENTS.allows(null));
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onAllows_returnTrue_whenReceptionistManagesReservations() {
        assertTrue(Permission.MANAGE_RESERVATIONS.allows(RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onAllows_returnFalse_whenManagerManagesReservations() {
        assertFalse(Permission.MANAGE_RESERVATIONS.allows(MANAGER));
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
