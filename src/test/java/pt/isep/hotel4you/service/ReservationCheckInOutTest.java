package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryReservationRepository;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.InMemoryRoomTypeRepository;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Acceptance tests for the check-in and the check-out of a reservation (H4Y-82). */
class ReservationCheckInOutTest {
    private static final LocalDate START = LocalDate.now();
    private static final LocalDate END = START.plusDays(2);
    private static final User RECEPTIONIST = new Receptionist(4L, "Ana", "ana", "hash");
    private static final RoomType DOUBLE = new RoomType(1L, "duplo", 2, new BigDecimal("80.00"));

    private InMemoryReservationRepository reservations;
    private RoomService roomService;
    private ReservationService service;
    private Room room;

    @BeforeEach
    void setUp() {
        InMemoryRoomRepository rooms = new InMemoryRoomRepository();
        rooms.addFloor(2);
        rooms.addRoomType("duplo");
        room = rooms.save(new Room(null, 201, 2, "duplo"), 10);
        InMemoryRoomTypeRepository types = new InMemoryRoomTypeRepository();
        types.add(DOUBLE);
        reservations = new InMemoryReservationRepository();
        reservations.addRoomNumber(room.getId(), room.getNumber());
        roomService = new RoomService(rooms, types, reservations);
        service = new ReservationService(reservations, roomService, types);
    }

    private void addReservation(long id, ReservationStatus status) {
        reservations.add(new Reservation(id, 7L, room.getId(), START, END, status, 4L));
    }

    private ReservationStatus statusOf(long id) {
        return reservations.findById(id).orElseThrow().getStatus();
    }

    @Test
    @Tag("H4Y-82-AC-1")
    void onCheckIn_saveCheckedIn_whenReservationIsReserved() {
        addReservation(1L, ReservationStatus.RESERVED);

        service.checkIn(1L, RECEPTIONIST);

        assertEquals(ReservationStatus.CHECKED_IN, statusOf(1L));
    }

    @Test
    @Tag("H4Y-82-AC-1")
    void onCheckIn_throwAccessDenied_whenUserMayNotManageReservations() {
        addReservation(1L, ReservationStatus.RESERVED);
        User cook = new RestaurantStaff(5L, "Rui", "rui", "hash");

        assertThrows(AccessDeniedException.class, () -> service.checkIn(1L, cook));
        assertEquals(ReservationStatus.RESERVED, statusOf(1L));
    }

    @Test
    @Tag("H4Y-82-AC-2")
    void onCheckIn_throwAndKeepState_whenReservationHasCheckIn() {
        addReservation(1L, ReservationStatus.CHECKED_IN);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.checkIn(1L, RECEPTIONIST));
        assertEquals("A reserva já tem check-in.", error.getMessage());
        assertEquals(ReservationStatus.CHECKED_IN, statusOf(1L));
    }

    @Test
    @Tag("H4Y-82-AC-2")
    void onCheckIn_throwAndKeepState_whenStateChangesBeforeSave() {
        addReservation(1L, ReservationStatus.RESERVED);
        reservations.loseNextUpdate = true;

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.checkIn(1L, RECEPTIONIST));
        assertEquals("A reserva mudou de estado. Atualize a lista.", error.getMessage());
        assertEquals(ReservationStatus.RESERVED, statusOf(1L));
    }

    @Test
    @Tag("H4Y-82-AC-3")
    void onCheckIn_throwAndKeepState_whenReservationIsCancelled() {
        addReservation(1L, ReservationStatus.CANCELLED);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.checkIn(1L, RECEPTIONIST));
        assertEquals("A reserva está cancelada.", error.getMessage());
        assertEquals(ReservationStatus.CANCELLED, statusOf(1L));
    }

    @Test
    @Tag("H4Y-82-AC-4")
    void onCheckIn_throwNotFound_whenReservationDoesNotExist() {
        ReservationNotFoundException error = assertThrows(ReservationNotFoundException.class,
                () -> service.checkIn(99L, RECEPTIONIST));
        assertEquals("A reserva não existe.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-4")
    void onCheckOut_throwNotFound_whenReservationDoesNotExist() {
        ReservationNotFoundException error = assertThrows(ReservationNotFoundException.class,
                () -> service.checkOut(99L, RECEPTIONIST));
        assertEquals("A reserva não existe.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-5")
    void onCheckOut_saveCheckedOut_whenReservationIsCheckedIn() {
        addReservation(1L, ReservationStatus.CHECKED_IN);

        service.checkOut(1L, RECEPTIONIST);

        assertEquals(ReservationStatus.CHECKED_OUT, statusOf(1L));
    }

    @Test
    @Tag("H4Y-82-AC-5")
    void onCheckOut_throwAccessDenied_whenUserMayNotManageReservations() {
        addReservation(1L, ReservationStatus.CHECKED_IN);
        User cook = new RestaurantStaff(5L, "Rui", "rui", "hash");

        assertThrows(AccessDeniedException.class, () -> service.checkOut(1L, cook));
        assertEquals(ReservationStatus.CHECKED_IN, statusOf(1L));
    }

    @Test
    @Tag("H4Y-82-AC-6")
    void onCheckOut_throwAndKeepState_whenReservationHasNoCheckIn() {
        addReservation(1L, ReservationStatus.RESERVED);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.checkOut(1L, RECEPTIONIST));
        assertEquals("A reserva não tem check-in.", error.getMessage());
        assertEquals(ReservationStatus.RESERVED, statusOf(1L));
    }

    @Test
    @Tag("H4Y-82-AC-6")
    void onCheckOut_throwAndKeepState_whenStateChangesBeforeSave() {
        addReservation(1L, ReservationStatus.CHECKED_IN);
        reservations.loseNextUpdate = true;

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.checkOut(1L, RECEPTIONIST));
        assertEquals("A reserva mudou de estado. Atualize a lista.", error.getMessage());
        assertEquals(ReservationStatus.CHECKED_IN, statusOf(1L));
    }

    @Test
    @Tag("H4Y-82-AC-7")
    void onCheckOut_throwAndKeepState_whenReservationHasCheckOut() {
        addReservation(1L, ReservationStatus.CHECKED_OUT);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.checkOut(1L, RECEPTIONIST));
        assertEquals("A reserva já tem check-out.", error.getMessage());
        assertEquals(ReservationStatus.CHECKED_OUT, statusOf(1L));
    }

    @Test
    @Tag("H4Y-82-AC-6")
    void onCheckOut_throwAndKeepState_whenReservationIsCancelled() {
        addReservation(1L, ReservationStatus.CANCELLED);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.checkOut(1L, RECEPTIONIST));
        assertEquals("A reserva está cancelada.", error.getMessage());
        assertEquals(ReservationStatus.CANCELLED, statusOf(1L));
    }

    @Test
    @Tag("H4Y-82-AC-8")
    void onSearchRooms_offerRoom_whenStayHasCheckOutAndNightsAreAfterDeparture() {
        addReservation(1L, ReservationStatus.RESERVED);
        service.checkIn(1L, RECEPTIONIST);
        service.checkOut(1L, RECEPTIONIST);

        List<Room> free = roomService.searchRooms(END, END.plusDays(2), DOUBLE, RECEPTIONIST);

        assertEquals(ReservationStatus.CHECKED_OUT, statusOf(1L));
        assertTrue(roomService.isAvailable(room, END, END.plusDays(2)));
        assertEquals(List.of(room.getId()), free.stream().map(Room::getId).toList());
        assertFalse(roomService.isAvailable(room, START, END));
    }

    @Test
    @Tag("H4Y-82-AC-12")
    @Tag("H4Y-82-AC-13")
    void onListActive_showReservedAndCheckedIn_whenOtherStatesExist() {
        addReservation(1L, ReservationStatus.RESERVED);
        addReservation(2L, ReservationStatus.CHECKED_IN);
        addReservation(3L, ReservationStatus.CANCELLED);
        addReservation(4L, ReservationStatus.CHECKED_OUT);

        List<ClientReservation> active = service.listActive(RECEPTIONIST);

        assertEquals(List.of(1L, 2L), active.stream().map(ClientReservation::id).toList());
        assertEquals(List.of(ReservationStatus.RESERVED, ReservationStatus.CHECKED_IN),
                active.stream().map(ClientReservation::status).toList());
    }

    @Test
    @Tag("H4Y-82-AC-12")
    void onListActive_throwAccessDenied_whenUserMayNotManageReservations() {
        User cook = new RestaurantStaff(5L, "Rui", "rui", "hash");

        assertThrows(AccessDeniedException.class, () -> service.listActive(cook));
    }
}
