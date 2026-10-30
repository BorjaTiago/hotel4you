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
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;
import pt.isep.hotel4you.data.DuplicateRoomException;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.InMemoryRoomTypeRepository;
import pt.isep.hotel4you.data.RoomLimitExceededException;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomSummary;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Tests room registration rules. */
class RoomServiceTest {

    private static final int FLOOR_LIMIT = 10;
    private static final String HASH = "$2a$12$abcdefghijklmnopqrstuuAbCdEfGhIjKlMnOpQrStUvWxYz01234";

    private InMemoryRoomRepository repository;
    private InMemoryRoomTypeRepository roomTypeRepository;
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
        roomTypeRepository = new InMemoryRoomTypeRepository();
        roomTypeRepository.add(new RoomType(1L, "duplo", 2, null));
        roomTypeRepository.add(new RoomType(2L, "familiar", 4, null));
        roomTypeRepository.add(new RoomType(3L, "individual", 1, null));
        roomService = new RoomService(repository, roomTypeRepository);
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onSaveRoom_saveRoom_whenFloorAndTypeAreValid() {
        Room saved = roomService.saveRoom(room(202, 2, "duplo"), manager());

        assertEquals(1, repository.findAll().size());
        assertEquals(202, saved.getNumber());
        assertEquals(2, saved.getFloorNumber());
        assertEquals("duplo", saved.getRoomTypeName());
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
        assertEquals(10, rooms.stream().filter(room -> room.getFloorNumber() == 2).count());
        assertEquals(10, rooms.stream().filter(room -> room.getFloorNumber() == 3).count());
        assertEquals(7, rooms.stream().filter(room -> room.getRoomTypeName().equals("individual")).count());
        assertEquals(10, rooms.stream().filter(room -> room.getRoomTypeName().equals("duplo")).count());
        assertEquals(3, rooms.stream().filter(room -> room.getRoomTypeName().equals("familiar")).count());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 5})
    @Tag("H4Y-25-AC-3")
    void onSaveRoom_throw_whenFloorIsNotForRooms(int floorNumber) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> roomService.saveRoom(room(101, floorNumber, "individual"), manager()));
        assertEquals("Só os pisos 2 e 3 têm quartos.", error.getMessage());

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
    @Tag("H4Y-25-AC-4")
    void onSaveRoom_throwLimit_whenFloorIsFullAndNumberExists() {
        seedRooms(2, 301, FLOOR_LIMIT);

        assertThrows(RoomLimitExceededException.class,
                () -> roomService.saveRoom(room(301, 2, "duplo"), manager()));

        assertEquals(FLOOR_LIMIT, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onListRoomTypes_returnRoomTypeRepositoryNames_whenRoomRepositoryHasNone() {
        RoomService service = new RoomService(new InMemoryRoomRepository(), roomTypeRepository);

        assertEquals(List.of("duplo", "familiar", "individual"), service.listRoomTypes(manager()));
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
                    RoomService second = new RoomService(repository, roomTypeRepository);
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

    @Test
    @Tag("H4Y-25-AC-2")
    void onRoomTypeTargets_returnImmutableSummaryData_whenScreenReadsTargets() {
        var targets = roomService.roomTypeTargets();

        assertEquals(List.of("individual", "duplo", "familiar"),
                targets.stream().map(RoomService.RoomTypeTarget::name).toList());
        assertEquals(List.of("Individuais", "Duplos", "Familiares"),
                targets.stream().map(RoomService.RoomTypeTarget::label).toList());
        assertEquals(List.of(7, 10, 3),
                targets.stream().map(RoomService.RoomTypeTarget::total).toList());
        assertThrows(UnsupportedOperationException.class, () -> targets.clear());
    }

    @Test
    @Tag("H4Y-25-AC-2")
    void onSaveRoom_allowMoreThanTypeTarget_whenFloorHasCapacity() {
        for (int i = 0; i < 8; i++) {
            roomService.saveRoom(room(201 + i, 2, "individual"), manager());
        }

        assertEquals(8, repository.findAll().size());
    }

    private static Stream<User> deniedActors() {
        return Stream.of(null, new Receptionist(2L, "Ana", "ana", HASH),
                new RestaurantStaff(3L, "João", "joao", HASH));
    }

    @Test
    @Tag("H4Y-50-AC-1")
    void onListRoomSummaries_returnEveryRoomWithTypeFloorAndPrice_whenReceptionistAsksWithoutFilter() {
        seedPricedRooms();

        List<RoomSummary> rooms = roomService.listRoomSummaries(null, receptionist());

        assertEquals(List.of(
                new RoomSummary(3L, 201, 2, "Quartos", "individual", new BigDecimal("80.00")),
                new RoomSummary(1L, 202, 2, "Quartos", "duplo", new BigDecimal("120.00")),
                new RoomSummary(2L, 301, 3, "Quartos", "familiar", new BigDecimal("180.00"))), rooms);
    }

    @Test
    @Tag("H4Y-50-AC-1")
    void onListRoomSummaries_returnEveryRoom_whenManagerAsks() {
        seedPricedRooms();

        assertEquals(3, roomService.listRoomSummaries(null, manager()).size());
    }

    @Test
    @Tag("H4Y-50-AC-1")
    void onListRoomSummaries_returnNullPrice_whenRoomTypeHasNoBasePrice() {
        repository.save(room(203, 2, "duplo"), FLOOR_LIMIT);

        RoomSummary summary = roomService.listRoomSummaries(null, receptionist()).getFirst();

        assertEquals(203, summary.number());
        assertNull(summary.basePrice());
    }

    @Test
    @Tag("H4Y-50-AC-2")
    void onListRoomSummaries_returnOnlyFamilyRooms_whenFilterIsFamily() {
        seedPricedRooms();
        repository.save(room(302, 3, "familiar"), FLOOR_LIMIT);

        List<RoomSummary> rooms = roomService.listRoomSummaries("familiar", receptionist());

        assertEquals(List.of(301, 302), rooms.stream().map(RoomSummary::number).toList());
        assertTrue(rooms.stream().allMatch(room -> room.roomTypeName().equals("familiar")));
    }

    @Test
    @Tag("H4Y-50-AC-2")
    void onListRoomSummaries_returnMatchingRooms_whenFilterHasOtherCaseAndSpaces() {
        seedPricedRooms();

        List<RoomSummary> rooms = roomService.listRoomSummaries("  FAMILIAR ", receptionist());

        assertEquals(List.of(301), rooms.stream().map(RoomSummary::number).toList());
    }

    @Test
    @Tag("H4Y-50-AC-2")
    void onListRoomSummaries_returnEveryRoom_whenFilterIsBlank() {
        seedPricedRooms();

        assertEquals(3, roomService.listRoomSummaries("  ", receptionist()).size());
    }

    @Test
    @Tag("H4Y-50-AC-3")
    void onListRoomSummaries_returnEmptyList_whenNoRoomsRegistered() {
        assertEquals(List.of(), roomService.listRoomSummaries(null, receptionist()));
    }

    @Test
    @Tag("H4Y-50-AC-3")
    void onListRoomSummaries_returnEmptyList_whenFilterMatchesNoRoom() {
        seedPricedRooms();

        assertEquals(List.of(), roomService.listRoomSummaries("suite", receptionist()));
    }

    @Test
    @Tag("H4Y-50-AC-4")
    void onListRoomSummaries_throwAccessDenied_whenActorIsNotLoggedIn() {
        seedPricedRooms();

        assertThrows(AccessDeniedException.class, () -> roomService.listRoomSummaries(null, null));
    }

    @Test
    @Tag("H4Y-50-AC-4")
    void onListRoomSummaries_throwAccessDenied_whenActorIsRestaurantStaff() {
        RestaurantStaff joao = new RestaurantStaff(3L, "Joao Lima", "joao", HASH);

        assertThrows(AccessDeniedException.class, () -> roomService.listRoomSummaries(null, joao));
    }

    @Test
    @Tag("H4Y-25-AC-5")
    void onEditRoom_changeNumberFloorAndType_whenDataIsValid() {
        Room saved = repository.save(room(202, 2, "duplo"), FLOOR_LIMIT);

        Room edited = roomService.editRoom(new Room(saved.getId(), 305, 3, "familiar"), manager());

        assertEquals(saved.getId(), edited.getId());
        assertEquals(305, edited.getNumber());
        assertEquals(3, edited.getFloorNumber());
        assertEquals("familiar", edited.getRoomTypeName());
        List<Room> listed = roomService.listRooms(manager());
        assertEquals(1, listed.size());
        assertRoom(listed.getFirst(), saved.getId(), 305, 3, "familiar");
    }

    @Test
    @Tag("H4Y-25-AC-5")
    void onEditRoom_keepOwnNumber_whenOnlyTypeChanges() {
        Room saved = repository.save(room(202, 2, "duplo"), FLOOR_LIMIT);

        Room edited = roomService.editRoom(new Room(saved.getId(), 202, 2, "familiar"), manager());

        assertEquals(202, edited.getNumber());
        assertEquals("familiar", roomService.listRooms(manager()).getFirst().getRoomTypeName());
    }

    @Test
    @Tag("H4Y-25-AC-6")
    void onEditRoom_throwDuplicate_whenNumberBelongsToAnotherRoom() {
        Room first = repository.save(room(201, 2, "duplo"), FLOOR_LIMIT);
        repository.save(room(202, 2, "duplo"), FLOOR_LIMIT);

        assertThrows(DuplicateRoomException.class,
                () -> roomService.editRoom(new Room(first.getId(), 202, 2, "duplo"), manager()));

        assertRoom(repository.findAll().getFirst(), first.getId(), 201, 2, "duplo");
    }

    @Test
    @Tag("H4Y-25-AC-7")
    void onEditRoom_throwLimitExceeded_whenTargetFloorIsFull() {
        seedRooms(3, 301, FLOOR_LIMIT);
        Room moving = repository.save(room(201, 2, "duplo"), FLOOR_LIMIT);

        assertThrows(RoomLimitExceededException.class,
                () -> roomService.editRoom(new Room(moving.getId(), 201, 3, "duplo"), manager()));

        Room stored = repository.findAll().stream()
                .filter(room -> room.getId().equals(moving.getId())).findFirst().orElseThrow();
        assertRoom(stored, moving.getId(), 201, 2, "duplo");
    }

    @Test
    @Tag("H4Y-25-AC-7")
    void onEditRoom_saveChange_whenRoomStaysOnFullFloor() {
        seedRooms(2, 201, FLOOR_LIMIT);
        Room stays = repository.findAll().getFirst();

        Room edited = roomService.editRoom(new Room(stays.getId(), 220, 2, "familiar"), manager());

        assertEquals(220, edited.getNumber());
        assertEquals(FLOOR_LIMIT, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-7")
    void onEditRoom_throw_whenFloorHasNoRooms() {
        Room saved = repository.save(room(202, 2, "duplo"), FLOOR_LIMIT);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> roomService.editRoom(new Room(saved.getId(), 101, 1, "duplo"), manager()));

        assertEquals("Só os pisos 2 e 3 têm quartos.", error.getMessage());
        assertRoom(repository.findAll().getFirst(), saved.getId(), 202, 2, "duplo");
    }

    @ParameterizedTest
    @MethodSource("deniedActors")
    @Tag("H4Y-25-AC-8")
    void onEditRoom_throwAccessDenied_whenActorIsNotManager(User actor) {
        Room saved = repository.save(room(202, 2, "duplo"), FLOOR_LIMIT);

        assertThrows(AccessDeniedException.class,
                () -> roomService.editRoom(new Room(saved.getId(), 203, 2, "duplo"), actor));

        assertRoom(repository.findAll().getFirst(), saved.getId(), 202, 2, "duplo");
    }

    @Test
    @Tag("H4Y-25-AC-9")
    void onEditRoom_throw_whenRoomIdIsUnknownOrNull() {
        repository.save(room(202, 2, "duplo"), FLOOR_LIMIT);

        assertThrows(IllegalArgumentException.class,
                () -> roomService.editRoom(new Room(99L, 203, 2, "duplo"), manager()));
        IllegalArgumentException nullId = assertThrows(IllegalArgumentException.class,
                () -> roomService.editRoom(room(203, 2, "duplo"), manager()));
        assertEquals("Escolha um quarto na tabela.", nullId.getMessage());
        assertThrows(IllegalArgumentException.class, () -> roomService.editRoom(null, manager()));
    }

    @Test
    @Tag("H4Y-25-AC-10")
    void onRemoveRoom_deleteRoom_whenNoAssociations() {
        Room first = repository.save(room(202, 2, "duplo"), FLOOR_LIMIT);
        Room second = repository.save(room(203, 2, "duplo"), FLOOR_LIMIT);

        roomService.removeRoom(first.getId(), manager());

        List<Room> rooms = roomService.listRooms(manager());
        assertEquals(1, rooms.size());
        assertEquals(second.getId(), rooms.getFirst().getId());
    }

    @Test
    @Tag("H4Y-25-AC-11")
    void onRemoveRoom_throwInUse_whenRoomHasAssociations() {
        Room saved = repository.save(room(202, 2, "duplo"), FLOOR_LIMIT);
        repository.markInUse(saved.getId());

        RoomInUseException error = assertThrows(RoomInUseException.class,
                () -> roomService.removeRoom(saved.getId(), manager()));

        assertEquals("O quarto tem associações e não pode ser removido.", error.getMessage());
        assertEquals(1, repository.findAll().size());
    }

    @ParameterizedTest
    @MethodSource("deniedActors")
    @Tag("H4Y-25-AC-12")
    void onRemoveRoom_throwAccessDenied_whenActorIsNotManager(User actor) {
        Room saved = repository.save(room(202, 2, "duplo"), FLOOR_LIMIT);

        assertThrows(AccessDeniedException.class, () -> roomService.removeRoom(saved.getId(), actor));

        assertEquals(1, repository.findAll().size());
    }

    @Test
    @Tag("H4Y-25-AC-13")
    void onRemoveRoom_throw_whenRoomIdIsUnknown() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> roomService.removeRoom(99L, manager()));

        assertEquals("O quarto indicado não existe.", error.getMessage());
    }

    private static void assertRoom(Room room, Long id, int number, int floor, String type) {
        assertEquals(id, room.getId());
        assertEquals(number, room.getNumber());
        assertEquals(floor, room.getFloorNumber());
        assertEquals(type, room.getRoomTypeName());
    }

    private void seedPricedRooms() {
        repository.setBasePrice("individual", new BigDecimal("80.00"));
        repository.setBasePrice("duplo", new BigDecimal("120.00"));
        repository.setBasePrice("familiar", new BigDecimal("180.00"));
        repository.save(room(202, 2, "duplo"), FLOOR_LIMIT);
        repository.save(room(301, 3, "familiar"), FLOOR_LIMIT);
        repository.save(room(201, 2, "individual"), FLOOR_LIMIT);
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
