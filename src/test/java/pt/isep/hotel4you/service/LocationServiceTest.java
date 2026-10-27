package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.InMemoryFloorRepository;
import pt.isep.hotel4you.data.InMemoryLocationRepository;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.User;

/** Tests that the manager can view the hotel floors and manage their zones. */
class LocationServiceTest {

    private static final String NOT_FOUND_MESSAGE = "O piso não existe.";
    private static final String DENIED_MESSAGE = "Só o gestor pode consultar os pisos.";

    private static final String MANAGE_DENIED_MESSAGE = "Só o gestor pode gerir as zonas.";
    private static final String NAME_BLANK_MESSAGE = "Escreva o nome da zona.";
    private static final String NAME_LONG_MESSAGE = "O nome da zona tem no máximo 50 caracteres.";
    private static final String LOCATION_MISSING_MESSAGE = "A zona não existe.";
    private static final String NAME_TAKEN_MESSAGE = "Já existe uma zona com este nome neste piso.";
    private static final String IN_USE_MESSAGE = "A zona tem associações e não pode ser removida.";

    private LocationService service;
    private InMemoryLocationRepository locationRepository;

    @BeforeEach
    void setUp() {
        InMemoryFloorRepository repository = new InMemoryFloorRepository();
        repository.add(floor(3, "Piso 3"));
        repository.add(floor(0, "Piso 0", "Receção", "Bar", "Salas de conferência"));
        repository.add(floor(-1, "Piso -1", "Garagem", "Arrecadação"));
        repository.add(floor(2, "Piso 2"));
        repository.add(floor(1, "Piso 1", "Restaurante", "Piscina"));
        locationRepository = new InMemoryLocationRepository();
        service = new LocationService(repository, locationRepository);
    }

    private static Floor floor(int number, String description, String... zones) {
        List<Location> locations = Arrays.stream(zones)
                .map(name -> new Location(null, name, false, false))
                .toList();
        return new Floor(number, description, locations);
    }

    private static List<String> zoneNames(Floor floor) {
        return floor.locations().stream().map(Location::getName).toList();
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

    private Location createBar() {
        return service.createLocation(1, "Bar", false, false, manager());
    }

    @Test
    @Tag("H4Y-28-AC-3")
    void onCreateLocation_saveWithReservationFlag_whenManager() {
        Location zone = service.createLocation(1, "Spa", true, true, manager());

        assertNotNull(zone.getId());
        Location saved = locationRepository.findById(zone.getId()).get();
        assertEquals("Spa", saved.getName());
        assertTrue(saved.isGuestCardAccess());
        assertTrue(saved.isRequiresReservation());
        assertEquals(1, locationRepository.floorOf(zone.getId()));
    }

    @Test
    @Tag("H4Y-28-AC-4")
    void onCreateLocation_saveWithoutReservationFlag_whenFlagOff() {
        Location zone = service.createLocation(1, "Jardim", false, false, manager());

        assertFalse(locationRepository.findById(zone.getId()).get().isRequiresReservation());
    }

    @Test
    @Tag("H4Y-28-AC-3")
    void onCreateLocation_trimName_whenNameHasSpaces() {
        Location zone = service.createLocation(1, "  Spa  ", false, true, manager());

        assertEquals("Spa", zone.getName());
        assertEquals("Spa", locationRepository.findById(zone.getId()).get().getName());
    }

    @Test
    @Tag("H4Y-28-AC-5")
    void onCreateLocation_throwNameTaken_whenNameUsedOnFloor() {
        createBar();

        LocationNameTakenException error = assertThrows(LocationNameTakenException.class,
                () -> service.createLocation(1, "Bar", false, true, manager()));

        assertEquals(NAME_TAKEN_MESSAGE, error.getMessage());
        assertEquals(1, locationRepository.count());
    }

    @Test
    @Tag("H4Y-28-AC-5")
    void onCreateLocation_throwNameTaken_whenNameDiffersOnlyInCase() {
        createBar();

        assertThrows(LocationNameTakenException.class,
                () -> service.createLocation(1, "BAR", false, true, manager()));
        assertEquals(1, locationRepository.count());
    }

    @Test
    @Tag("H4Y-28-AC-5")
    void onCreateLocation_allowSameName_whenOtherFloor() {
        createBar();

        Location zone = service.createLocation(2, "Bar", false, false, manager());

        assertEquals(2, locationRepository.floorOf(zone.getId()));
        assertEquals(2, locationRepository.count());
    }

    @Test
    @Tag("H4Y-28-AC-6")
    void onCreateLocation_throwFloorNotFound_whenFloorMissing() {
        FloorNotFoundException error = assertThrows(FloorNotFoundException.class,
                () -> service.createLocation(4, "Spa", false, true, manager()));

        assertEquals(NOT_FOUND_MESSAGE, error.getMessage());
        assertEquals(0, locationRepository.count());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    @Tag("H4Y-28-AC-3")
    void onCreateLocation_throw_whenNameBlank(String name) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.createLocation(1, name, false, true, manager()));

        assertEquals(NAME_BLANK_MESSAGE, error.getMessage());
        assertEquals(0, locationRepository.count());
    }

    @Test
    @Tag("H4Y-28-AC-3")
    void onCreateLocation_throw_whenNameTooLong() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.createLocation(1, "a".repeat(51), false, true, manager()));

        assertEquals(NAME_LONG_MESSAGE, error.getMessage());
        assertEquals(0, locationRepository.count());
    }

    @Test
    @Tag("H4Y-28-AC-3")
    void onCreateLocation_saveZone_whenNameHas50Chars() {
        Location zone = service.createLocation(1, "a".repeat(50), false, true, manager());

        assertEquals(50, zone.getName().length());
    }

    @Test
    @Tag("H4Y-28-AC-7")
    void onCreateLocation_throwAccessDenied_whenNotManager() {
        User actor = receptionist();

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.createLocation(4, "Spa", false, true, actor));

        assertEquals(MANAGE_DENIED_MESSAGE, error.getMessage());
        assertEquals(0, locationRepository.count());
    }

    @Test
    @Tag("H4Y-28-AC-8")
    void onEditLocation_changeReservationFlag_whenManager() {
        Location zone = createBar();

        service.editLocation(zone.getId(), 1, "Bar", false, true, manager());

        assertTrue(locationRepository.findById(zone.getId()).get().isRequiresReservation());
    }

    @Test
    @Tag("H4Y-28-AC-9")
    void onEditLocation_changeNameAndFloor_whenManager() {
        Location zone = createBar();

        service.editLocation(zone.getId(), 2, " Cafe ", false, false, manager());

        assertEquals("Cafe", locationRepository.findById(zone.getId()).get().getName());
        assertEquals(2, locationRepository.floorOf(zone.getId()));
    }

    @Test
    @Tag("H4Y-28-AC-9")
    void onEditLocation_keepSameName_whenOnlyFlagChanges() {
        Location zone = createBar();

        Location edited = service.editLocation(zone.getId(), 1, "Bar", true, true, manager());

        assertEquals("Bar", edited.getName());
        assertTrue(edited.isGuestCardAccess());
    }

    @Test
    @Tag("H4Y-28-AC-10")
    void onEditLocation_throwNameTaken_whenNameUsedByOtherZone() {
        Location bar = createBar();
        service.createLocation(1, "Piscina", false, false, manager());

        LocationNameTakenException error = assertThrows(LocationNameTakenException.class,
                () -> service.editLocation(bar.getId(), 1, "piscina", false, false, manager()));

        assertEquals(NAME_TAKEN_MESSAGE, error.getMessage());
        assertEquals("Bar", locationRepository.findById(bar.getId()).get().getName());
    }

    @Test
    @Tag("H4Y-28-AC-11")
    void onEditLocation_throwLocationNotFound_whenZoneMissing() {
        LocationNotFoundException error = assertThrows(LocationNotFoundException.class,
                () -> service.editLocation(99L, 1, "Bar", false, false, manager()));

        assertEquals(LOCATION_MISSING_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-28-AC-9")
    void onEditLocation_throwFloorNotFound_whenFloorMissing() {
        Location zone = createBar();

        FloorNotFoundException error = assertThrows(FloorNotFoundException.class,
                () -> service.editLocation(zone.getId(), 4, "Bar", false, false, manager()));

        assertEquals(NOT_FOUND_MESSAGE, error.getMessage());
        assertEquals(1, locationRepository.floorOf(zone.getId()));
    }

    @Test
    @Tag("H4Y-28-AC-12")
    void onEditLocation_throwAccessDenied_whenNotManager() {
        Location zone = createBar();
        User actor = receptionist();

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.editLocation(zone.getId(), 4, "Cafe", true, true, actor));

        assertEquals(MANAGE_DENIED_MESSAGE, error.getMessage());
        assertEquals("Bar", locationRepository.findById(zone.getId()).get().getName());
    }

    @Test
    @Tag("H4Y-28-AC-13")
    void onRemoveLocation_deleteZone_whenNoAssociations() {
        Location zone = createBar();

        service.removeLocation(zone.getId(), manager());

        assertTrue(locationRepository.findById(zone.getId()).isEmpty());
    }

    @Test
    @Tag("H4Y-28-AC-14")
    void onRemoveLocation_throwInUse_whenZoneHasAssociations() {
        Location zone = createBar();
        locationRepository.markInUse(zone.getId());

        LocationInUseException error = assertThrows(LocationInUseException.class,
                () -> service.removeLocation(zone.getId(), manager()));

        assertEquals(IN_USE_MESSAGE, error.getMessage());
        assertTrue(locationRepository.findById(zone.getId()).isPresent());
    }

    @Test
    @Tag("H4Y-28-AC-15")
    void onRemoveLocation_throwLocationNotFound_whenZoneMissing() {
        LocationNotFoundException error = assertThrows(LocationNotFoundException.class,
                () -> service.removeLocation(99L, manager()));

        assertEquals(LOCATION_MISSING_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-28-AC-16")
    void onRemoveLocation_throwAccessDenied_whenNotManager() {
        Location zone = createBar();
        User actor = receptionist();

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.removeLocation(zone.getId(), actor));

        assertEquals(MANAGE_DENIED_MESSAGE, error.getMessage());
        assertTrue(locationRepository.findById(zone.getId()).isPresent());
    }
}
