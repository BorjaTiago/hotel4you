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
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Acceptance tests for the delete of a reservation before the check-in (H4Y-57). */
class ReservationDeleteTest {
    private static final LocalDate START = LocalDate.now().plusDays(3);
    private static final LocalDate END = START.plusDays(2);
    private static final User MANAGER = new Manager(1L, "Rui", "rui", "hash");
    private static final User RECEPTIONIST = new Receptionist(4L, "Ana", "ana", "hash");
    private static final RoomType DOUBLE = new RoomType(1L, "duplo", 2, new BigDecimal("80.00"));

    private InMemoryReservationRepository reservations;
    private RoomService roomService;
    private ReservationService service;

    @BeforeEach
    void setUp() {
        InMemoryRoomRepository rooms = new InMemoryRoomRepository();
        rooms.addFloor(2);
        rooms.addRoomType("duplo");
        Room room = rooms.save(new Room(null, 201, 2, "duplo"), 10);
        InMemoryRoomTypeRepository types = new InMemoryRoomTypeRepository();
        types.add(DOUBLE);
        reservations = new InMemoryReservationRepository();
        reservations.addRoomNumber(room.getId(), room.getNumber());
        roomService = new RoomService(rooms, types, reservations);
        service = new ReservationService(reservations, roomService, types);
        reservations.add(new Reservation(1L, 7L, room.getId(), START, END,
                ReservationStatus.RESERVED, 4L));
    }

    private static Client client() {
        return new Client(7L, "Maria Sousa", DocumentType.CC, "12345678",
                LocalDate.of(1990, 1, 1), "912345678", "PT", null, null);
    }

    @Test
    @Tag("H4Y-57-AC-1")
    void onDelete_removeReservation_whenReservationHasNoCheckIn() {
        service.delete(1L, MANAGER);

        assertTrue(reservations.findById(1L).isEmpty());
        assertTrue(service.listActive(MANAGER).isEmpty());
        assertTrue(service.listClientReservations(client(), MANAGER).isEmpty());
    }

    @Test
    @Tag("H4Y-57-AC-4")
    void onDelete_throwAccessDenied_whenUserIsNotManager() {
        assertThrows(AccessDeniedException.class, () -> service.delete(1L, RECEPTIONIST));
        assertFalse(reservations.findById(1L).isEmpty());
    }

    @Test
    @Tag("H4Y-57-AC-1")
    void onDelete_throwAndKeepReservation_whenStateChangesBeforeDelete() {
        reservations.loseNextUpdate = true;

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.delete(1L, MANAGER));
        assertEquals("A reserva mudou de estado. Atualize a lista.", error.getMessage());
        assertFalse(reservations.findById(1L).isEmpty());
    }

    @Test
    @Tag("H4Y-57-AC-1")
    void onDelete_throwAndKeepReservation_whenReservationIsCancelled() {
        assertKeptWithoutDelete(ReservationStatus.CANCELLED);
    }

    @Test
    @Tag("H4Y-57-AC-1")
    void onDelete_throwAndKeepReservation_whenReservationIsCheckedIn() {
        assertKeptWithoutDelete(ReservationStatus.CHECKED_IN);
    }

    @Test
    @Tag("H4Y-57-AC-1")
    void onDelete_throwAndKeepReservation_whenReservationIsCheckedOut() {
        assertKeptWithoutDelete(ReservationStatus.CHECKED_OUT);
    }

    private void assertKeptWithoutDelete(ReservationStatus status) {
        reservations.add(new Reservation(2L, 7L, 1L, START, END, status, 4L));

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> service.delete(2L, MANAGER));
        assertEquals("A reserva mudou de estado. Atualize a lista.", error.getMessage());
        assertFalse(reservations.findById(2L).isEmpty());
        assertEquals(0, reservations.deleteCalls);
    }

    @Test
    @Tag("H4Y-57-AC-2")
    void onDelete_offerRoomAgain_whenReservationIsDeleted() {
        assertTrue(roomService.searchRooms(START, END, DOUBLE, MANAGER).isEmpty());

        service.delete(1L, MANAGER);

        assertEquals(List.of(201), roomService.searchRooms(START, END, DOUBLE, MANAGER).stream()
                .map(Room::getNumber).toList());
    }

    @Test
    @Tag("H4Y-57-AC-3")
    void onDelete_throwNotFound_whenReservationDoesNotExist() {
        ReservationNotFoundException error = assertThrows(ReservationNotFoundException.class,
                () -> service.delete(99L, MANAGER));
        assertEquals("A reserva não existe.", error.getMessage());
        assertFalse(reservations.findById(1L).isEmpty());
    }
}
