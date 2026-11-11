package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryReservationRepository;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.InMemoryRoomTypeRepository;
import pt.isep.hotel4you.domain.ReservationSummary;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Acceptance tests for the list of active reservations of the Reservas screen (H4Y-81). */
class ReservationListActiveTest {
    private static final LocalDate START = LocalDate.of(2026, 11, 10);
    private static final LocalDate END = LocalDate.of(2026, 11, 12);
    private static final User RECEPTIONIST = new Receptionist(4L, "Ana", "ana", "hash");
    private static final RoomType DOUBLE = new RoomType(1L, "duplo", 2, new BigDecimal("80.00"));

    private InMemoryReservationRepository reservations;
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
        reservations.addClient(7L, "Maria Sousa", "12345678");
        RoomService roomService = new RoomService(rooms, types, reservations);
        service = new ReservationService(reservations, roomService, types);
    }

    private void addReservation(long id, ReservationStatus status) {
        reservations.add(new Reservation(id, 7L, room.getId(), START, END, status, 4L));
    }

    @Test
    @Tag("H4Y-81-AC-1")
    void onListActive_showClientRoomDatesAndStatus_whenReservationIsReserved() {
        addReservation(1L, ReservationStatus.RESERVED);

        List<ReservationSummary> rows = service.listActive(RECEPTIONIST);

        assertEquals(List.of(new ReservationSummary(1L, "Maria Sousa", "12345678", 201, START, END,
                ReservationStatus.RESERVED)), rows);
    }

    @Test
    @Tag("H4Y-81-AC-1")
    void onListActive_showReservedAndCheckedIn_whenBothExist() {
        addReservation(1L, ReservationStatus.RESERVED);
        addReservation(2L, ReservationStatus.CHECKED_IN);

        List<ReservationSummary> rows = service.listActive(RECEPTIONIST);

        assertEquals(List.of(ReservationStatus.RESERVED, ReservationStatus.CHECKED_IN),
                rows.stream().map(ReservationSummary::status).toList());
    }

    @Test
    @Tag("H4Y-81-AC-2")
    void onListActive_hideCancelledAndCheckedOut_whenOtherStatesExist() {
        addReservation(1L, ReservationStatus.CANCELLED);
        addReservation(2L, ReservationStatus.CHECKED_OUT);
        addReservation(3L, ReservationStatus.RESERVED);

        List<ReservationSummary> rows = service.listActive(RECEPTIONIST);

        assertEquals(List.of(3L), rows.stream().map(ReservationSummary::id).toList());
    }

    @Test
    @Tag("H4Y-81-AC-10")
    void onListActive_returnSameRows_whenUserIsManager() {
        addReservation(1L, ReservationStatus.RESERVED);
        addReservation(2L, ReservationStatus.CHECKED_IN);
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        assertEquals(service.listActive(RECEPTIONIST), service.listActive(manager));
        assertEquals(2, service.listActive(manager).size());
    }

    @Test
    @Tag("H4Y-81-AC-11")
    void onListActive_throwAccessDenied_whenUserIsRestaurantStaff() {
        addReservation(1L, ReservationStatus.RESERVED);
        User cook = new RestaurantStaff(5L, "Rui", "rui", "hash");

        assertThrows(AccessDeniedException.class, () -> service.listActive(cook));
    }
}
