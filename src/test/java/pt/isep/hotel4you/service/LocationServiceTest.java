package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryFloorRepository;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.User;

/** Tests that the manager can view the hotel floors and their zones. */
class LocationServiceTest {

    private static final String NOT_FOUND_MESSAGE = "O piso não existe.";
    private static final String DENIED_MESSAGE = "Só o gestor pode consultar os pisos.";

    private LocationService service;

    @BeforeEach
    void setUp() {
        InMemoryFloorRepository repository = new InMemoryFloorRepository();
        repository.add(floor(3, "Piso 3"));
        repository.add(floor(0, "Piso 0", "Receção", "Bar", "Salas de conferência"));
        repository.add(floor(-1, "Piso -1", "Garagem", "Arrecadação"));
        repository.add(floor(2, "Piso 2"));
        repository.add(floor(1, "Piso 1", "Restaurante", "Piscina"));
        service = new LocationService(repository);
    }

    private static Floor floor(int number, String description, String... zones) {
        List<Location> locations = Arrays.stream(zones)
                .map(name -> new Location(null, name, false, false))
                .toList();
        return new Floor(number, description, locations);
    }

    private static List<String> zoneNames(Floor floor) {
        return floor.locations().stream().map(Location::name).toList();
    }

    private User manager() {
        return new Manager(1L, "Rui Costa", "rui", "hash");
    }

    private User receptionist() {
        return new Receptionist(2L, "Ana Silva", "ana", "hash");
    }

    @Test
    @Tag("H4Y-28-AC-1")
    void onListFloors_returnFloorsWithZones_whenManager() {
        List<Floor> floors = service.listFloors(manager());

        assertEquals(List.of(-1, 0, 1, 2, 3), floors.stream().map(Floor::number).toList());
        assertEquals(List.of("Garagem", "Arrecadação"), zoneNames(floors.get(0)));
        assertEquals(List.of("Receção", "Bar", "Salas de conferência"), zoneNames(floors.get(1)));
        assertEquals(List.of("Restaurante", "Piscina"), zoneNames(floors.get(2)));
        assertTrue(floors.get(3).locations().isEmpty());
        assertTrue(floors.get(4).locations().isEmpty());
    }

    @Test
    @Tag("H4Y-28-AC-1")
    void onFindFloor_returnFloor_whenFloorExists() {
        Floor floor = service.findFloor(1, manager());

        assertEquals(1, floor.number());
        assertEquals(List.of("Restaurante", "Piscina"), zoneNames(floor));
    }

    @Test
    @Tag("H4Y-28-AC-2")
    void onFindFloor_throwFloorNotFound_whenFloorMissing() {
        FloorNotFoundException error = assertThrows(FloorNotFoundException.class,
                () -> service.findFloor(4, manager()));

        assertEquals(NOT_FOUND_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-28-AC-1")
    void onListFloors_throwAccessDenied_whenNotManager() {
        User actor = receptionist();

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.listFloors(actor));

        assertEquals(DENIED_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-28-AC-2")
    void onFindFloor_throwAccessDenied_whenNotManager() {
        User actor = receptionist();

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.findFloor(4, actor));

        assertEquals(DENIED_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-28-AC-1")
    void onCanViewFloors_returnTrue_whenManager() {
        assertTrue(service.canViewFloors(manager()));
    }

    @Test
    @Tag("H4Y-28-AC-1")
    void onCanViewFloors_returnFalse_whenNotManager() {
        assertFalse(service.canViewFloors(receptionist()));
    }

    @Test
    @Tag("H4Y-28-AC-1")
    void onCanViewFloors_returnFalse_whenActorNull() {
        assertFalse(service.canViewFloors(null));
    }
}
