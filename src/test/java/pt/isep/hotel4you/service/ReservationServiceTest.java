package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import pt.isep.hotel4you.data.RoomNotAvailableException;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.DocumentType;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationQuote;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Acceptance tests for the reservation create (H4Y-56). */
class ReservationServiceTest {
    private static final LocalDate START = LocalDate.of(2026, 11, 10);
    private static final LocalDate END = LocalDate.of(2026, 11, 12);
    private static final User RECEPTIONIST = new Receptionist(4L, "Ana", "ana", "hash");
    private static final Client CLIENT = new Client(7L, "Maria Sousa", DocumentType.CC, "12345678",
            LocalDate.of(1990, 5, 1), "912345678", "PT", null, null);
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
        rooms.addRoomType("familiar");
        room = rooms.save(new Room(null, 201, 2, "duplo"), 10);
        InMemoryRoomTypeRepository types = new InMemoryRoomTypeRepository();
        types.add(DOUBLE);
        types.add(new RoomType(2L, "familiar", 4, null));
        reservations = new InMemoryReservationRepository();
        reservations.addRoomNumber(room.getId(), room.getNumber());
        roomService = new RoomService(rooms, types, reservations);
        service = new ReservationService(reservations, roomService, types);
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onCreate_saveReservedReservation_whenRoomIsFree() {
        Reservation saved = service.create(CLIENT, room, START, END, RECEPTIONIST);

        assertNotNull(saved.getId());
        assertEquals(ReservationStatus.RESERVED, saved.getStatus());
        assertEquals(CLIENT.getId(), saved.getClientId());
        assertEquals(room.getId(), saved.getRoomId());
        assertEquals(START, saved.getStartDate());
        assertEquals(END, saved.getEndDate());
        assertEquals(1, reservations.saved().size());
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onCreate_takeRoomOffFreeList_whenReservationIsSaved() {
        service.create(CLIENT, room, START, END, RECEPTIONIST);

        assertFalse(roomService.isAvailable(room, START, END));
        assertTrue(roomService.searchRooms(START, END, DOUBLE, RECEPTIONIST).isEmpty());
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onCreate_keepCreator_whenReservationIsSaved() {
        Reservation saved = service.create(CLIENT, room, START, END, RECEPTIONIST);

        assertEquals(RECEPTIONIST.getId(), saved.getCreatedByUserId());
    }

    @Test
    @Tag("H4Y-56-AC-2")
    void onCreate_throw_whenRoomHasOverlappingReservation() {
        service.create(CLIENT, room, START, END, RECEPTIONIST);

        RoomNotAvailableException error = assertThrows(RoomNotAvailableException.class,
                () -> service.create(CLIENT, room, START.plusDays(1), END.plusDays(1), RECEPTIONIST));
        assertEquals("O quarto já está reservado nessas datas.", error.getMessage());
        assertEquals(1, reservations.saved().size());
    }

    @Test
    @Tag("H4Y-56-AC-2")
    void onCreate_throw_whenAnotherSaveTakesTheRoomFirst() {
        reservations.loseNextSave = true;

        RoomNotAvailableException error = assertThrows(RoomNotAvailableException.class,
                () -> service.create(CLIENT, room, START, END, RECEPTIONIST));
        assertEquals("O quarto já está reservado nessas datas.", error.getMessage());
        assertTrue(reservations.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-56-AC-2")
    void onCreate_saveReservation_whenPreviousGuestLeavesOnArrival() {
        service.create(CLIENT, room, START, END, RECEPTIONIST);

        service.create(CLIENT, room, END, END.plusDays(2), RECEPTIONIST);

        assertEquals(2, reservations.saved().size());
    }

    @Test
    @Tag("H4Y-56-AC-2")
    void onCreate_saveReservation_whenOverlappingReservationIsCancelled() {
        reservations.add(new Reservation(1L, room.getId(), START, END, ReservationStatus.CANCELLED));

        Reservation saved = service.create(CLIENT, room, START, END, RECEPTIONIST);

        assertEquals(ReservationStatus.RESERVED, saved.getStatus());
    }

    @Test
    @Tag("H4Y-56-AC-3")
    void onCreate_throwWithClient_whenClientIsMissing() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.create(null, room, START, END, RECEPTIONIST));
        assertEquals("Faltam dados: cliente.", error.getMessage());
        assertTrue(reservations.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-56-AC-3")
    void onCreate_throwWithDates_whenPeriodIsMissing() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.create(CLIENT, room, null, null, RECEPTIONIST));
        assertEquals("Faltam dados: data de entrada, data de saída.", error.getMessage());
        assertTrue(reservations.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-56-AC-3")
    void onCreate_throwWithAllFields_whenEverythingIsMissing() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.create(null, null, null, null, RECEPTIONIST));
        assertEquals("Faltam dados: cliente, quarto, data de entrada, data de saída.",
                error.getMessage());
    }

    @Test
    @Tag("H4Y-56-AC-3")
    void onCreate_throwWithClient_whenClientIsNotSaved() {
        Client unsaved = new Client(null, "Rui", DocumentType.CC, "87654321",
                LocalDate.of(1980, 1, 1), "913333333", "PT", null, null);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.create(unsaved, room, START, END, RECEPTIONIST));
        assertEquals("Faltam dados: cliente.", error.getMessage());
    }

    @Test
    @Tag("H4Y-56-AC-3")
    void onCreate_throw_whenDepartureIsNotAfterArrival() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.create(CLIENT, room, END, START, RECEPTIONIST));
        assertEquals("A data de saída tem de ser depois da data de entrada.", error.getMessage());
        assertTrue(reservations.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onCreate_throw_whenActorIsNotReceptionistOrManager() {
        User staff = new RestaurantStaff(2L, "Joao", "joao", "hash");

        assertThrows(AccessDeniedException.class,
                () -> service.create(CLIENT, room, START, END, staff));
        assertThrows(AccessDeniedException.class,
                () -> service.create(CLIENT, room, START, END, null));
        assertTrue(reservations.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onCreate_saveReservation_whenActorIsManager() {
        User manager = new Manager(1L, "Rui", "rui", "hash");

        Reservation saved = service.create(CLIENT, room, START, END, manager);

        assertEquals(manager.getId(), saved.getCreatedByUserId());
    }

    @Test
    @Tag("H4Y-56-AC-4")
    void onQuote_returnBasePricePerNight_whenTypeHasPrice() {
        ReservationQuote quote = service.quote(room, START, END, RECEPTIONIST);

        assertEquals(new BigDecimal("80.00"), quote.pricePerNight());
        assertEquals(2, quote.nights());
    }

    @Test
    @Tag("H4Y-56-AC-4")
    void onQuote_returnNoPrice_whenTypeHasNoPrice() {
        Room family = new Room(9L, 301, 3, "familiar");

        ReservationQuote quote = service.quote(family, START, END, RECEPTIONIST);

        assertNull(quote.pricePerNight());
        assertEquals(2, quote.nights());
    }

    @Test
    @Tag("H4Y-56-AC-4")
    void onQuote_throw_whenRoomIsMissing() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.quote(null, START, END, RECEPTIONIST));
        assertEquals("Faltam dados: quarto.", error.getMessage());
    }

    @Test
    @Tag("H4Y-56-AC-4")
    void onQuote_throw_whenActorIsRestaurantStaff() {
        assertThrows(AccessDeniedException.class,
                () -> service.quote(room, START, END, new RestaurantStaff(2L, "Joao", "joao", "hash")));
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onListClientReservations_showNewReservation_whenReservationIsSaved() {
        Reservation saved = service.create(CLIENT, room, START, END, RECEPTIONIST);

        List<ClientReservation> rows = service.listClientReservations(CLIENT, RECEPTIONIST);

        assertEquals(List.of(new ClientReservation(saved.getId(), 201, START, END,
                ReservationStatus.RESERVED)), rows);
    }

    @Test
    @Tag("H4Y-56-AC-3")
    void onListClientReservations_throw_whenClientIsMissing() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.listClientReservations(null, RECEPTIONIST));
        assertEquals("Faltam dados: cliente.", error.getMessage());
    }

    @Test
    @Tag("H4Y-56-AC-1")
    void onListClientReservations_throw_whenActorIsMissing() {
        assertThrows(AccessDeniedException.class,
                () -> service.listClientReservations(CLIENT, null));
    }
}
