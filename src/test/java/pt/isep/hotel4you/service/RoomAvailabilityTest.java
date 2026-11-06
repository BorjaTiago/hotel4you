package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import pt.isep.hotel4you.data.InMemoryReservationRepository;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.InMemoryRoomTypeRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Acceptance tests for free rooms by type and nights. */
class RoomAvailabilityTest {
    private static final LocalDate START = LocalDate.of(2026, 11, 10);
    private static final LocalDate END = LocalDate.of(2026, 11, 12);
    private static final String CHOOSE = "Escolha a data de entrada e a data de saída.";
    private static final String AFTER = "A data de saída tem de ser depois da data de entrada.";
    private static final User RECEPTIONIST = new Receptionist(1L, "Ana", "ana", "hash");
    private static final RoomType TYPE = new RoomType(1L, "duplo", 2, null);
    private InMemoryReservationRepository reservations;
    private InMemoryRoomTypeRepository types;
    private RoomService service;
    private Room first;
    private Room second;

    @BeforeEach
    void setUp() {
        InMemoryRoomRepository rooms = new InMemoryRoomRepository();
        rooms.addFloor(2);
        rooms.addRoomType("duplo");
        rooms.addRoomType("familiar");
        first = rooms.save(new Room(null, 201, 2, "duplo"), 10);
        second = rooms.save(new Room(null, 202, 2, "duplo"), 10);
        rooms.save(new Room(null, 203, 2, "familiar"), 10);
        types = new InMemoryRoomTypeRepository();
        types.add(TYPE);
        types.add(new RoomType(2L, "familiar", 4, null));
        reservations = new InMemoryReservationRepository();
        service = new RoomService(rooms, types, reservations);
    }

    @Test
    @Tag("H4Y-54-AC-1")
    void onSearchRooms_returnMatchingRooms_whenNightsAreFree() {
        assertEquals(List.of(first, second), service.searchRooms(START, END, TYPE, RECEPTIONIST));
        assertTrue(service.isAvailable(first, START, END));
    }

    @Test
    @Tag("H4Y-54-AC-2")
    void onSearchRooms_excludeRoom_whenActiveReservationOverlaps() {
        reservations.add(new Reservation(1L, first.getId(), START, END, ReservationStatus.RESERVED));
        assertEquals(List.of(second), service.searchRooms(START.plusDays(1), END.plusDays(1),
                TYPE, RECEPTIONIST));
        assertFalse(service.isAvailable(first, START, END));
    }

    @Test
    @Tag("H4Y-54-AC-2")
    void onSearchRooms_offerRoom_whenPreviousGuestLeavesOnArrival() {
        reservations.add(new Reservation(1L, first.getId(), START.minusDays(2), START,
                ReservationStatus.RESERVED));
        assertEquals(List.of(first, second), service.searchRooms(START, END, TYPE, RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-54-AC-2")
    void onSearchRooms_offerRoom_whenNextGuestArrivesOnDeparture() {
        reservations.add(new Reservation(1L, first.getId(), END, END.plusDays(2),
                ReservationStatus.RESERVED));
        assertEquals(List.of(first, second), service.searchRooms(START, END, TYPE, RECEPTIONIST));
    }

    @ParameterizedTest
    @CsvSource({"0,0", "-1,-1", "1,1", "-1,1"})
    @Tag("H4Y-54-AC-2")
    void onSearchRooms_excludeRoom_whenPeriodOverlapsReservation(int startShift, int endShift) {
        reservations.add(new Reservation(1L, first.getId(), START, END, ReservationStatus.RESERVED));
        assertEquals(List.of(second), service.searchRooms(START.plusDays(startShift),
                END.plusDays(endShift), TYPE, RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-54-AC-2")
    void onSearchRooms_excludeRoom_whenGuestIsCheckedIn() {
        reservations.add(new Reservation(1L, first.getId(), START, END, ReservationStatus.CHECKED_IN));
        assertEquals(List.of(second), service.searchRooms(START, END, TYPE, RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-54-AC-3")
    void onSearchRooms_offerRoom_whenOnlyReservationIsCancelled() {
        reservations.add(new Reservation(1L, first.getId(), START, END, ReservationStatus.CANCELLED));
        assertEquals(List.of(first, second), service.searchRooms(START, END, TYPE, RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-54-AC-3")
    @Tag("H4Y-54-AC-2")
    void onSearchRooms_excludeRoom_whenCancelledAndActiveReservationsBothExist() {
        reservations.add(new Reservation(1L, first.getId(), START, END, ReservationStatus.CANCELLED));
        reservations.add(new Reservation(2L, first.getId(), START, END, ReservationStatus.RESERVED));
        assertEquals(List.of(second), service.searchRooms(START, END, TYPE, RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-54-AC-2")
    void onSearchRooms_queryOnce_whenTypeHasSeveralRooms() {
        reservations.add(new Reservation(1L, first.getId(), START, END, ReservationStatus.RESERVED));
        assertEquals(List.of(second), service.searchRooms(START, END, TYPE, RECEPTIONIST));
        assertEquals(1, reservations.findByRoomsCalls);
        assertEquals(0, reservations.findByRoomCalls);
    }

    @ParameterizedTest
    @MethodSource("invalidPeriods")
    @Tag("H4Y-54-AC-4")
    void onSearchRooms_throw_whenPeriodIsInvalid(LocalDate start, LocalDate end, String message) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.searchRooms(start, end, TYPE, RECEPTIONIST));
        assertEquals(message, error.getMessage());
    }

    @Test
    @Tag("H4Y-54-AC-5")
    void onSearchRooms_returnEmptyList_whenAllMatchingRoomsAreOccupied() {
        reservations.add(new Reservation(1L, first.getId(), START, END, ReservationStatus.RESERVED));
        reservations.add(new Reservation(2L, second.getId(), START, END, ReservationStatus.RESERVED));
        assertTrue(service.searchRooms(START, END, TYPE, RECEPTIONIST).isEmpty());
    }

    @Test
    @Tag("H4Y-54-AC-5")
    void onSearchRoomsWithMessage_returnEmptyResultAndMessage_whenAllRoomsAreOccupied() {
        reservations.add(new Reservation(1L, first.getId(), START, END, ReservationStatus.RESERVED));
        reservations.add(new Reservation(2L, second.getId(), START, END, ReservationStatus.RESERVED));

        var result = service.searchRoomsWithMessage(START, END, TYPE, RECEPTIONIST);
        assertTrue(result.rooms().isEmpty());
        assertEquals("Não há quartos livres para estas datas.", result.message());
    }

    @Test
    @Tag("H4Y-54-AC-5")
    void onSearchRoomsWithMessage_returnEmptyResultAndMessage_whenTypeHasNoRooms() {
        RoomType single = new RoomType(3L, "individual", 1, null);
        types.add(single);

        var result = service.searchRoomsWithMessage(START, END, single, RECEPTIONIST);
        assertTrue(result.rooms().isEmpty());
        assertEquals("Não há quartos livres para estas datas.", result.message());
        assertEquals(1, reservations.findByRoomsCalls);
        assertEquals(0, reservations.findByRoomCalls);
    }

    @Test
    @Tag("H4Y-54-AC-1")
    void onListSearchRoomTypes_returnCatalogue_whenActorIsReceptionist() {
        assertEquals(List.of(TYPE, new RoomType(2L, "familiar", 4, null)).stream()
                .map(RoomType::getName).toList(), service.listSearchRoomTypes(RECEPTIONIST)
                .stream().map(RoomType::getName).toList());
        assertThrows(AccessDeniedException.class, () -> service.listSearchRoomTypes(null));
    }

    @Test
    @Tag("H4Y-54-AC-1")
    void onSearchRooms_allowManager_whenActorHasPermission() {
        User manager = new Manager(2L, "Rui", "rui", "hash");
        assertEquals(2, service.searchRooms(START, END, TYPE, manager).size());
    }

    @Test
    @Tag("H4Y-54-AC-1")
    void onSearchRooms_throw_whenActorIsNotAllowed() {
        assertThrows(AccessDeniedException.class, () -> service.searchRooms(START, END, TYPE, null));
        User restaurant = new RestaurantStaff(3L, "João", "joao", "hash");
        assertThrows(AccessDeniedException.class,
                () -> service.searchRooms(START, END, TYPE, restaurant));
    }

    @Test
    @Tag("H4Y-54-AC-1")
    void onSearchRooms_throw_whenTypeIsMissingOrUnknown() {
        assertThrows(IllegalArgumentException.class,
                () -> service.searchRooms(START, END, null, RECEPTIONIST));
        assertThrows(IllegalArgumentException.class, () -> service.searchRooms(START, END,
                new RoomType(99L, "suite", 2, null), RECEPTIONIST));
    }

    @ParameterizedTest
    @MethodSource("invalidTypes")
    @Tag("H4Y-54-AC-1")
    void onSearchRooms_throwTypeMessage_whenTypeIsInvalid(RoomType type) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.searchRooms(START, END, type, RECEPTIONIST));
        assertEquals("Escolha um tipo de quarto válido.", error.getMessage());
    }

    private static Stream<RoomType> invalidTypes() {
        return Stream.of(new RoomType(null, "duplo", 2, null),
                new RoomType(1L, "other", 2, null));
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> invalidPeriods() {
        return Stream.of(org.junit.jupiter.params.provider.Arguments.of(START, START, AFTER),
                org.junit.jupiter.params.provider.Arguments.of(END, START, AFTER),
                org.junit.jupiter.params.provider.Arguments.of(null, END, CHOOSE),
                org.junit.jupiter.params.provider.Arguments.of(START, null, CHOOSE));
    }
}
