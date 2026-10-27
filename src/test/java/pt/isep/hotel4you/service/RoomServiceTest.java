package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.DuplicateRoomException;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.RoomLimitExceededException;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
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
        for (int floor : List.of(-1, 0, 1, 2, 3)) {
            repository.addFloor(floor);
        }
        for (String roomType : List.of("single", "double", "family")) {
            repository.addRoomType(roomType);
        }
        roomService = new RoomService(repository);
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSaveRoom_saveRoom_whenFloorAndTypeAreValid() {
        Room saved = roomService.saveRoom(room(202, 2, "double"), manager());

        assertEquals(1, repository.findAll().size());
        assertEquals(202, saved.number());
        assertEquals(2, saved.floorNumber());
        assertEquals("double", saved.roomTypeName());
    }

    @Test
    @Tag("H4Y-25-AC-2")
    void onSaveRoom_registerFullDistribution_whenCapacityAllows() {
        for (int i = 0; i < 7; i++) {
            roomService.saveRoom(room(201 + i, 2, "single"), manager());
        }
        for (int i = 0; i < 3; i++) {
            roomService.saveRoom(room(208 + i, 2, "double"), manager());
        }
        for (int i = 0; i < 7; i++) {
            roomService.saveRoom(room(301 + i, 3, "double"), manager());
        }
        for (int i = 0; i < 3; i++) {
            roomService.saveRoom(room(308 + i, 3, "family"), manager());
        }

        List<Room> rooms = roomService.listRooms();
        assertEquals(20, rooms.size());
        assertEquals(10, rooms.stream().filter(room -> room.floorNumber() == 2).count());
        assertEquals(10, rooms.stream().filter(room -> room.floorNumber() == 3).count());
        assertEquals(7, rooms.stream().filter(room -> room.roomTypeName().equals("single")).count());
        assertEquals(10, rooms.stream().filter(room -> room.roomTypeName().equals("double")).count());
        assertEquals(3, rooms.stream().filter(room -> room.roomTypeName().equals("family")).count());
    }

    @Test
    @Tag("H4Y-25-AC-3")
    void onSaveRoom_throw_whenFloorIsNotForRooms() {
        assertThrows(IllegalArgumentException.class,
                () -> roomService.saveRoom(room(101, 1, "single"), manager()));

        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-4")
    void onSaveRoom_throw_whenFloorIsFull() {
        seedRooms(3, 301, FLOOR_LIMIT);

        assertThrows(RoomLimitExceededException.class,
                () -> roomService.saveRoom(room(311, 3, "double"), manager()));

        assertEquals(FLOOR_LIMIT, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSaveRoom_throw_whenRoomNumberExists() {
        seedRooms(2, 202, 1);

        assertThrows(DuplicateRoomException.class,
                () -> roomService.saveRoom(room(202, 3, "double"), manager()));

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
                () -> roomService.saveRoom(room(402, 4, "double"), manager()));

        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSaveRoom_throwAccessDenied_whenActorIsNotManager() {
        User receptionist = new Receptionist(2L, "Ana Silva", "ana", HASH);

        assertThrows(AccessDeniedException.class,
                () -> roomService.saveRoom(room(202, 2, "double"), receptionist));

        assertEquals(0, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-4")
    void onSaveRoom_saveOnlyOne_whenConcurrentRequestsReachFloorLimit()
            throws InterruptedException, ExecutionException {
        seedRooms(2, 201, FLOOR_LIMIT - 1);
        List<Callable<Boolean>> attempts = List.of(
                () -> trySave(room(210, 2, "double")),
                () -> trySave(room(211, 2, "double")));

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
            repository.saveWithinFloorLimit(
                    room(firstRoomNumber + i, floorNumber, "double"), FLOOR_LIMIT);
        }
    }

    private static Room room(int number, int floorNumber, String roomTypeName) {
        return new Room(null, number, floorNumber, roomTypeName);
    }

    private static Manager manager() {
        return new Manager(1L, "Rui Costa", "rui", HASH);
    }
}
