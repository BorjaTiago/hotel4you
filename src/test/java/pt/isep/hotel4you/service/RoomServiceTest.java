package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;
import pt.isep.hotel4you.data.DuplicateRoomException;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.RoomLimitExceededException;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.User;

/** Tests room registration rules. */
class RoomServiceTest {

    private static final int FLOOR_LIMIT = 10;
    private static final String HASH = "$2a$12$abcdefghijklmnopqrstuuAbCdEfGhIjKlMnOpQrStUvWxYz01234";

    private InMemoryRoomRepository repository;
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRoomRepository();
        for (int floor : List.of(-1, 0, 1, 2, 3, 5)) {
            repository.addFloor(floor);
        }
        for (String roomType : List.of("individual", "duplo", "familiar")) {
            repository.addRoomType(roomType);
        }
        roomService = new RoomService(repository, new pt.isep.hotel4you.data.InMemoryRoomTypeRepository());
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSaveRoom_saveRoom_whenFloorAndTypeAreValid() {
        Room saved = roomService.saveRoom(room(202, 2, "duplo"), manager());

        assertEquals(1, repository.findAll().size());
        assertEquals(202, saved.number());
        assertEquals(2, saved.floorNumber());
        assertEquals("duplo", saved.roomTypeName());
    }

    @Test
    @Tag("H4Y-25-AC-2")
    void onSaveRoom_registerFullDistribution_whenCapacityAllows() {
        for (int i = 0; i < 7; i++) {
            roomService.saveRoom(room(201 + i, 2, "individual"), manager());
        }
        for (int i = 0; i < 3; i++) {
            roomService.saveRoom(room(208 + i, 2, "duplo"), manager());
        }
        for (int i = 0; i < 7; i++) {
            roomService.saveRoom(room(301 + i, 3, "duplo"), manager());
        }
        for (int i = 0; i < 3; i++) {
            roomService.saveRoom(room(308 + i, 3, "familiar"), manager());
        }

        List<Room> rooms = roomService.listRooms(manager());
        assertEquals(20, rooms.size());
        assertEquals(10, rooms.stream().filter(room -> room.floorNumber() == 2).count());
        assertEquals(10, rooms.stream().filter(room -> room.floorNumber() == 3).count());
        assertEquals(7, rooms.stream().filter(room -> room.roomTypeName().equals("individual")).count());
        assertEquals(10, rooms.stream().filter(room -> room.roomTypeName().equals("duplo")).count());
        assertEquals(3, rooms.stream().filter(room -> room.roomTypeName().equals("familiar")).count());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 5})
    @Tag("H4Y-25-AC-3")
    void onSaveRoom_throw_whenFloorIsNotForRooms(int floorNumber) {
        assertThrows(IllegalArgumentException.class,
                () -> roomService.saveRoom(room(101, floorNumber, "individual"), manager()));

        assertEquals(0, repository.findAll().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    @Tag("H4Y-25-AC-4")
    void onSaveRoom_throw_whenFloorIsFull(int floorNumber) {
        seedRooms(floorNumber, 301, FLOOR_LIMIT);

        assertThrows(RoomLimitExceededException.class,
                () -> roomService.saveRoom(room(311, floorNumber, "duplo"), manager()));

        assertEquals(FLOOR_LIMIT, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSaveRoom_throw_whenRoomNumberExists() {
        seedRooms(2, 202, 1);

        assertThrows(DuplicateRoomException.class,
                () -> roomService.saveRoom(room(202, 3, "duplo"), manager()));

        assertEquals(1, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSaveRoom_throw_whenRoomTypeIsUnknown() {
        assertThrows(IllegalArgumentException.class,
                () -> roomService.saveRoom(room(202, 2, "penthouse"), manager()));

        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSaveRoom_throw_whenFloorDoesNotExist() {
        assertThrows(IllegalArgumentException.class,
                () -> roomService.saveRoom(room(402, 4, "duplo"), manager()));

        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-4")
    void onRoomFloorLimits_returnImmutableRules_whenScreenReadsLimits() {
        assertEquals(Map.of(2, 10, 3, 10), roomService.roomFloorLimits());
        assertThrows(UnsupportedOperationException.class,
                () -> roomService.roomFloorLimits().put(5, 10));
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSaveRoom_throwAccessDenied_whenActorIsNull() {
        assertThrows(AccessDeniedException.class,
                () -> roomService.saveRoom(room(202, 2, "duplo"), null));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSaveRoom_throwAccessDenied_whenActorIsNotManager() {
        User receptionist = new Receptionist(2L, "Ana Silva", "ana", HASH);

        assertThrows(AccessDeniedException.class,
                () -> roomService.saveRoom(room(202, 2, "duplo"), receptionist));

        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-4")
    void onSaveRoom_saveOnlyOne_whenConcurrentRequestsReachFloorLimit()
            throws InterruptedException, ExecutionException {
        seedRooms(2, 201, FLOOR_LIMIT - 1);
        List<Callable<Boolean>> attempts = List.of(
                () -> trySave(room(210, 2, "duplo")),
                () -> {
                    RoomService second = new RoomService(repository, new pt.isep.hotel4you.data.InMemoryRoomTypeRepository());
                    try {
                        second.saveRoom(room(211, 2, "duplo"), manager());
                        return true;
                    } catch (RoomLimitExceededException e) {
                        return false;
                    }
                });

        try (var executor = Executors.newFixedThreadPool(2)) {
            var results = executor.invokeAll(attempts);
            int saved = 0;
            int rejected = 0;
            for (var result : results) {
                if (result.get()) {
                    saved++;
                } else {
                    rejected++;
                }
            }
            assertEquals(1, saved);
            assertEquals(1, rejected);
        }

        assertEquals(FLOOR_LIMIT, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onListRooms_returnSavedRooms_whenActorIsManager() {
        Room saved = roomService.saveRoom(room(202, 2, "duplo"), manager());

        assertEquals(List.of(saved), roomService.listRooms(manager()));
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onListRoomTypes_returnCatalogue_whenActorIsManager() {
        assertEquals(List.of("duplo", "familiar", "individual"), roomService.listRoomTypes(manager()));
    }

    @ParameterizedTest
    @MethodSource("deniedActors")
    @Tag("H4Y-25-AC-1")
    void onListRooms_throwAccessDenied_whenActorIsNotManager(User actor) {
        assertThrows(AccessDeniedException.class, () -> roomService.listRooms(actor));
    }

    @ParameterizedTest
    @MethodSource("deniedActors")
    @Tag("H4Y-25-AC-1")
    void onListRoomTypes_throwAccessDenied_whenActorIsNotManager(User actor) {
        assertThrows(AccessDeniedException.class, () -> roomService.listRoomTypes(actor));
    }

    private static Stream<User> deniedActors() {
        return Stream.of(null, new Receptionist(2L, "Ana", "ana", HASH),
                new RestaurantStaff(3L, "João", "joao", HASH));
    }

    private boolean trySave(Room room) {
        try {
            roomService.saveRoom(room, manager());
            return true;
        } catch (RoomLimitExceededException e) {
            return false;
        }
    }

    private void seedRooms(int floorNumber, int firstRoomNumber, int count) {
        for (int i = 0; i < count; i++) {
            repository.save(
                    room(firstRoomNumber + i, floorNumber, "duplo"), FLOOR_LIMIT);
        }
    }

    private static Room room(int number, int floorNumber, String roomTypeName) {
        return new Room(null, number, floorNumber, roomTypeName);
    }

    private static Manager manager() {
        return new Manager(1L, "Rui Costa", "rui", HASH);
    }
}
