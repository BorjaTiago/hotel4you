package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryItemRepository;
import pt.isep.hotel4you.data.InMemoryReservationRepository;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.InMemoryRoomTypeRepository;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Acceptance tests for the restaurant and the pool in a reservation (H4Y-92). */
class ReservationLocationsTest {
    private static final LocalDate START = LocalDate.now().plusDays(10);
    private static final LocalDate END = START.plusDays(2);
    private static final User RECEPTIONIST = new Receptionist(4L, "Ana", "ana", "hash");
    private static final long RESTAURANT_ID = 3L;
    private static final long POOL_ID = 4L;
    private static final Client CLIENT = new Client(7L, "Maria Sousa", DocumentType.CC, "12345678",
            LocalDate.of(1990, 5, 1), "912345678", "PT", null, null);

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
        types.add(new RoomType(1L, "duplo", 2, new BigDecimal("80.00")));
        reservations = new InMemoryReservationRepository();
        reservations.addRoomNumber(room.getId(), room.getNumber());
        reservations.addClient(CLIENT.getId(), CLIENT.getName(), CLIENT.getDocument());
        reservations.addLocation(RESTAURANT_ID, "Restaurante");
        reservations.addLocation(POOL_ID, "Piscina");
        service = new ReservationService(reservations, new RoomService(rooms, types, reservations,
                new InMemoryItemRepository()),
                types);
    }

    @Test
    @Tag("H4Y-92-AC-1")
    void onCreate_includeRestaurantAndPool_whenBothAreChosen() {
        Reservation saved = service.create(CLIENT, room, START, END,
                List.of(RESTAURANT_ID, POOL_ID), RECEPTIONIST);

        assertEquals(List.of(RESTAURANT_ID, POOL_ID), saved.getLocationIds());
        assertEquals(List.of(RESTAURANT_ID, POOL_ID), reservations.saved().get(0).getLocationIds());
        assertEquals("Restaurante, Piscina", service.listActive(RECEPTIONIST).get(0).locations());
    }

    @Test
    @Tag("H4Y-92-AC-2")
    void onCreate_includeOnlyPool_whenOnlyPoolIsChosen() {
        Reservation saved = service.create(CLIENT, room, START, END, List.of(POOL_ID), RECEPTIONIST);

        assertEquals(List.of(POOL_ID), saved.getLocationIds());
        assertEquals("Piscina", service.listActive(RECEPTIONIST).get(0).locations());
    }

    @Test
    @Tag("H4Y-92-AC-3")
    void onCreate_includeNoLocation_whenNoneIsChosen() {
        Reservation saved = service.create(CLIENT, room, START, END, List.of(), RECEPTIONIST);

        assertTrue(saved.getLocationIds().isEmpty());
        assertEquals("", service.listActive(RECEPTIONIST).get(0).locations());
    }

    @Test
    @Tag("H4Y-92-AC-1")
    void onCreate_showNewName_whenZoneIsRenamedAfterReservation() {
        service.create(CLIENT, room, START, END, List.of(RESTAURANT_ID), RECEPTIONIST);
        reservations.addLocation(RESTAURANT_ID, "Restaurante Mar");

        assertEquals("Restaurante Mar", service.listActive(RECEPTIONIST).get(0).locations());
    }
}
