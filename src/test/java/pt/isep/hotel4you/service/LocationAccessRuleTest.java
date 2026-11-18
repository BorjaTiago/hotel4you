package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.InMemoryFloorRepository;
import pt.isep.hotel4you.data.InMemoryLocationRepository;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

/**
 * Tests the access rule by zone: reception only on floors 0 and 1, restaurant staff only in the
 * kitchen and the restaurant, manager with no limit (RF-24, RF-25, RF-01).
 */
class LocationAccessRuleTest {

    /** Test zones of each floor. The kitchen is a test zone: the statement has no floor for it. */
    private static final List<Floor> FLOORS = List.of(
            floor(-1, zone(1, "Garagem"), zone(2, "Arrecadação"), zone(3, "Cozinha")),
            floor(0, zone(4, "Receção"), zone(5, "Bar"), zone(6, "Salas de conferência")),
            floor(1, zone(7, "Restaurante"), zone(8, "Piscina")),
            floor(2, zone(9, "Quarto 201")),
            floor(3, zone(10, "Quarto 301")));

    private LocationService service;

    @BeforeEach
    void setUp() {
        InMemoryFloorRepository floorRepository = new InMemoryFloorRepository();
        FLOORS.forEach(floorRepository::add);
        service = new LocationService(floorRepository, new InMemoryLocationRepository());
    }

    private static Floor floor(int number, Location... zones) {
        return new Floor(number, "Piso " + number, List.of(zones));
    }

    private static Location zone(long id, String name) {
        return new Location(id, name, false, false);
    }

    /** Finds a test zone by floor and name, so that each table row names its floor. */
    private static Location zoneOn(int floorNumber, String name) {
        return FLOORS.stream()
                .filter(floor -> floor.number() == floorNumber)
                .flatMap(floor -> floor.locations().stream())
                .filter(location -> location.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No test zone " + name));
    }

    private static User receptionist() {
        return new Receptionist(2L, "Ana Silva", "ana", "hash");
    }

    private static User restaurantStaff() {
        return new RestaurantStaff(3L, "Joana Lima", "joana", "hash");
    }

    private static User manager() {
        return new Manager(1L, "Rui Costa", "rui", "hash");
    }

    /** A user with a role that does not log in (client or supplier, RF-01). */
    private static User userWithRole(Role role) {
        return new User(9L, "Outra Pessoa", "outra", "hash", role) { };
    }

    @ParameterizedTest(name = "{0} on floor {1} -> {2}")
    @CsvSource({
        "Receção,              0, true",
        "Bar,                  0, true",
        "Salas de conferência, 0, true",
        "Restaurante,          1, true",
        "Piscina,              1, true",
    })
    @Tag("H4Y-87-AC-1")
    void onCanAccess_allow_whenReceptionistOnFloorZeroOrOne(String name, int floor,
            boolean expected) {
        assertEquals(expected, service.canAccess(zoneOn(floor, name), receptionist()));
    }

    @ParameterizedTest(name = "{0} on floor {1} -> {2}")
    @CsvSource({
        "Garagem,     -1, false",
        "Arrecadação, -1, false",
        "Cozinha,     -1, false",
        "Quarto 201,   2, false",
        "Quarto 301,   3, false",
    })
    @Tag("H4Y-87-AC-2")
    void onCanAccess_deny_whenReceptionistOnOtherFloor(String name, int floor, boolean expected) {
        assertEquals(expected, service.canAccess(zoneOn(floor, name), receptionist()));
    }

    @Test
    @Tag("H4Y-87-AC-2")
    void onCanAccess_deny_whenReceptionistZoneIsOnNoFloor() {
        Location unknown = zone(99, "Receção");

        assertFalse(service.canAccess(unknown, receptionist()));
    }

    @ParameterizedTest(name = "{0} on floor {1} -> {2}")
    @CsvSource({
        "Cozinha,     -1, true",
        "Restaurante,  1, true",
    })
    @Tag("H4Y-87-AC-3")
    void onCanAccess_allow_whenRestaurantStaffInKitchenOrRestaurant(String name, int floor,
            boolean expected) {
        assertEquals(expected, service.canAccess(zoneOn(floor, name), restaurantStaff()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"cozinha", "RESTAURANTE"})
    @Tag("H4Y-87-AC-3")
    void onCanAccess_allow_whenRestaurantZoneNameHasOtherCase(String name) {
        assertTrue(service.canAccess(zone(50, name), restaurantStaff()));
    }

    @ParameterizedTest(name = "{0} on floor {1} -> {2}")
    @CsvSource({
        "Garagem,              -1, false",
        "Arrecadação,          -1, false",
        "Receção,               0, false",
        "Bar,                   0, false",
        "Salas de conferência,  0, false",
        "Piscina,               1, false",
        "Quarto 201,            2, false",
        "Quarto 301,            3, false",
    })
    @Tag("H4Y-87-AC-4")
    void onCanAccess_deny_whenRestaurantStaffInOtherZone(String name, int floor,
            boolean expected) {
        assertEquals(expected, service.canAccess(zoneOn(floor, name), restaurantStaff()));
    }

    @ParameterizedTest(name = "{0} on floor {1} -> {2}")
    @CsvSource({
        "Garagem,              -1, true",
        "Arrecadação,          -1, true",
        "Cozinha,              -1, true",
        "Receção,               0, true",
        "Bar,                   0, true",
        "Salas de conferência,  0, true",
        "Restaurante,           1, true",
        "Piscina,               1, true",
        "Quarto 201,            2, true",
        "Quarto 301,            3, true",
    })
    @Tag("H4Y-87-AC-5")
    void onCanAccess_allow_whenManagerOnAnyFloor(String name, int floor, boolean expected) {
        assertEquals(expected, service.canAccess(zoneOn(floor, name), manager()));
    }

    @Test
    @Tag("H4Y-87-AC-2")
    @Tag("H4Y-87-AC-4")
    void onCanAccess_deny_whenActorIsNull() {
        assertFalse(service.canAccess(zoneOn(0, "Receção"), null));
    }

    @Test
    @Tag("H4Y-87-AC-2")
    @Tag("H4Y-87-AC-4")
    void onCanAccess_deny_whenLocationIsNull() {
        assertFalse(service.canAccess(null, manager()));
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"CLIENT", "SUPPLIER"})
    @Tag("H4Y-87-AC-2")
    @Tag("H4Y-87-AC-4")
    void onCanAccess_deny_whenRoleDoesNotLogIn(Role role) {
        assertFalse(service.canAccess(zoneOn(0, "Receção"), userWithRole(role)));
    }
}
