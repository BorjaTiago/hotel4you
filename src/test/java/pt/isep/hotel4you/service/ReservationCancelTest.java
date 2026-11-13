package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.DocumentType;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Acceptance tests for the cancel of a reservation that keeps the history (H4Y-55). */
class ReservationCancelTest {
    private static final LocalDate START = LocalDate.now().plusDays(3);
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

    private void addReservation(ReservationStatus status) {
        reservations.add(new Reservation(1L, 7L, room.getId(), START, END, status, 4L));
    }

    private ReservationStatus status() {
        return reservations.findById(1L).orElseThrow().getStatus();
    }

    private static Client client() {
        return new Client(7L, "Maria Sousa", DocumentType.CC, "12345678",
                LocalDate.of(1990, 1, 1), "912345678", "PT", null, null);
    }

    @Test
    @Tag("H4Y-55-AC-1")
    void onCancel_saveCancelledAndKeepHistory_whenReservationHasNoCheckIn() {
        addReservation(ReservationStatus.RESERVED);

        service.cancel(1L, RECEPTIONIST);

        assertEquals(ReservationStatus.CANCELLED, status());
        List<ClientReservation> history = service.listClientReservations(client(), RECEPTIONIST);
        assertEquals(List.of(ReservationStatus.CANCELLED),
                history.stream().map(ClientReservation::status).toList());
        assertTrue(service.listActive(RECEPTIONIST).isEmpty());
    }

    @Test
    @Tag("H4Y-55-AC-1")
    void onCancel_throwAccessDenied_whenUserMayNotManageReservations() {
        addReservation(ReservationStatus.RESERVED);
        User cook = new RestaurantStaff(5L, "Rui", "rui", "hash");

        assertThrows(AccessDeniedException.class, () -> service.cancel(1L, cook));
        assertEquals(ReservationStatus.RESERVED, status());
    }

    @Test
    @Tag("H4Y-55-AC-1")
    void onCancel_throwNotFound_whenReservationDoesNotExist() {
        assertThrows(ReservationNotFoundException.class, () -> service.cancel(99L, RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-55-AC-1")
    void onCancel_throwAndKeepState_whenStateChangesBeforeCancel() {
        addReservation(ReservationStatus.RESERVED);
        reservations.loseNextUpdate = true;

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.cancel(1L, RECEPTIONIST));
        assertEquals("A reserva mudou de estado. Atualize a lista.", error.getMessage());
        assertEquals(ReservationStatus.RESERVED, status());
    }

    @Test
    @Tag("H4Y-55-AC-2")
    void onCancel_offerRoomAgain_whenReservationIsCancelled() {
        addReservation(ReservationStatus.RESERVED);
        assertTrue(roomService.searchRooms(START, END, DOUBLE, RECEPTIONIST).isEmpty());

        service.cancel(1L, RECEPTIONIST);

        assertEquals(List.of(201), roomService.searchRooms(START, END, DOUBLE, RECEPTIONIST)
                .stream().map(Room::getNumber).toList());
    }

    @Test
    @Tag("H4Y-55-AC-3")
    void onCancel_throwAndKeepCancelled_whenReservationIsCancelled() {
        addReservation(ReservationStatus.CANCELLED);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.cancel(1L, RECEPTIONIST));
        assertEquals("A reserva já está cancelada.", error.getMessage());
        assertEquals(ReservationStatus.CANCELLED, status());
    }

    @Test
    @Tag("H4Y-55-AC-4")
    void onCancel_throwAndKeepState_whenReservationHasCheckIn() {
        addReservation(ReservationStatus.CHECKED_IN);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.cancel(1L, RECEPTIONIST));
        assertEquals("A reserva já tem check-in.", error.getMessage());
        assertEquals(ReservationStatus.CHECKED_IN, status());
    }

    @Test
    @Tag("H4Y-55-AC-4")
    void onCancel_throwAndKeepState_whenReservationHasCheckOut() {
        addReservation(ReservationStatus.CHECKED_OUT);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.cancel(1L, RECEPTIONIST));
        assertEquals("A reserva já tem check-out.", error.getMessage());
        assertEquals(ReservationStatus.CHECKED_OUT, status());
    }
}
