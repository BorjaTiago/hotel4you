package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.DuplicateRoomException;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.RoomLimitExceededException;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomSummary;
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

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 5})
    @Tag("H4Y-25-AC-3")
    void onSaveRoom_throw_whenFloorIsNotForRooms(int floorNumber) {
        assertThrows(IllegalArgumentException.class,
                () -> roomService.saveRoom(room(101, floorNumber, "single"), manager()));

        assertEquals(0, repository.findAll().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    @Tag("H4Y-25-AC-4")
    void onSaveRoom_throw_whenFloorIsFull(int floorNumber) {
        seedRooms(floorNumber, 301, FLOOR_LIMIT);

        assertThrows(RoomLimitExceededException.class,
                () -> roomService.saveRoom(room(311, floorNumber, "double"), manager()));

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
                () -> roomService.saveRoom(room(202, 2, "double"), null));
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
                () -> {
                    RoomService second = new RoomService(repository);
                    try {
                        second.saveRoom(room(211, 2, "double"), manager());
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
    @Tag("H4Y-50-AC-1")
    void listRoomSummaries_receptionistAsksWithoutFilter_returnsEveryRoomWithTypeFloorAndPrice() {
        seedPricedRooms();

        List<RoomSummary> rooms = roomService.listRoomSummaries(null, receptionist());

        assertEquals(List.of(
                new RoomSummary(201, 2, "single", new BigDecimal("80.00")),
                new RoomSummary(202, 2, "double", new BigDecimal("120.00")),
                new RoomSummary(301, 3, "family", new BigDecimal("180.00"))), rooms);
    }

    @Test
    @Tag("H4Y-50-AC-1")
    void listRoomSummaries_managerAsks_returnsEveryRoom() {
        seedPricedRooms();

        assertEquals(3, roomService.listRoomSummaries(null, manager()).size());
    }

    @Test
    @Tag("H4Y-50-AC-1")
    void listRoomSummaries_roomTypeHasNoBasePrice_returnsRoomWithNullPrice() {
        repository.save(room(203, 2, "double"), FLOOR_LIMIT);

        RoomSummary summary = roomService.listRoomSummaries(null, receptionist()).getFirst();

        assertEquals(203, summary.number());
        assertNull(summary.basePrice());
    }

    @Test
    @Tag("H4Y-50-AC-2")
    void listRoomSummaries_filterByFamilyType_returnsOnlyFamilyRooms() {
        seedPricedRooms();
        repository.save(room(302, 3, "family"), FLOOR_LIMIT);

        List<RoomSummary> rooms = roomService.listRoomSummaries("family", receptionist());

        assertEquals(List.of(301, 302), rooms.stream().map(RoomSummary::number).toList());
        assertTrue(rooms.stream().allMatch(room -> room.roomTypeName().equals("family")));
    }

    @Test
    @Tag("H4Y-50-AC-2")
    void listRoomSummaries_filterHasOtherCaseAndSpaces_returnsMatchingRooms() {
        seedPricedRooms();

        List<RoomSummary> rooms = roomService.listRoomSummaries("  FAMILY ", receptionist());

        assertEquals(List.of(301), rooms.stream().map(RoomSummary::number).toList());
    }

    @Test
    @Tag("H4Y-50-AC-2")
    void listRoomSummaries_filterIsBlank_returnsEveryRoom() {
        seedPricedRooms();

        assertEquals(3, roomService.listRoomSummaries("  ", receptionist()).size());
    }

    @Test
    @Tag("H4Y-50-AC-3")
    void listRoomSummaries_noRoomsRegistered_returnsEmptyList() {
        assertEquals(List.of(), roomService.listRoomSummaries(null, receptionist()));
    }

    @Test
    @Tag("H4Y-50-AC-3")
    void listRoomSummaries_filterMatchesNoRoom_returnsEmptyList() {
        seedPricedRooms();

        assertEquals(List.of(), roomService.listRoomSummaries("suite", receptionist()));
    }

    @Test
    @Tag("H4Y-50-AC-4")
    void listRoomSummaries_actorIsNotLoggedIn_throwsAccessDenied() {
        seedPricedRooms();

        assertThrows(AccessDeniedException.class, () -> roomService.listRoomSummaries(null, null));
    }

    @Test
    @Tag("H4Y-50-AC-4")
    void listRoomSummaries_actorIsRestaurantStaff_throwsAccessDenied() {
        RestaurantStaff joao = new RestaurantStaff(3L, "Joao Lima", "joao", HASH);

        assertThrows(AccessDeniedException.class, () -> roomService.listRoomSummaries(null, joao));
    }

    private void seedPricedRooms() {
        repository.setBasePrice("single", new BigDecimal("80.00"));
        repository.setBasePrice("double", new BigDecimal("120.00"));
        repository.setBasePrice("family", new BigDecimal("180.00"));
        repository.save(room(202, 2, "double"), FLOOR_LIMIT);
        repository.save(room(301, 3, "family"), FLOOR_LIMIT);
        repository.save(room(201, 2, "single"), FLOOR_LIMIT);
    }

    private static Receptionist receptionist() {
        return new Receptionist(2L, "Ana Silva", "ana", HASH);
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
