package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryCardAccessRecordRepository;
import pt.isep.hotel4you.data.InMemoryCardRepository;
import pt.isep.hotel4you.data.InMemoryFloorRepository;
import pt.isep.hotel4you.data.InMemoryItemRepository;
import pt.isep.hotel4you.data.InMemoryLocationRepository;
import pt.isep.hotel4you.data.InMemoryReservationRepository;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.InMemoryRoomTypeRepository;
import pt.isep.hotel4you.data.InMemoryUserRepository;
import pt.isep.hotel4you.domain.IssuedCard;
import pt.isep.hotel4you.domain.MagneticCard;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Tests the card that the check-in issues (H4Y-134). */
class CardCheckInFacadeTest {

    private static final LocalDate START = LocalDate.now();
    private static final LocalDate END = START.plusDays(2);
    private static final User RECEPTIONIST = new Receptionist(4L, "Ana", "ana", "hash");
    private static final RoomType DOUBLE = new RoomType(1L, "duplo", 2, new BigDecimal("80.00"));

    private final InMemoryCardRepository cards = new InMemoryCardRepository();
    private final InMemoryReservationRepository reservations = new InMemoryReservationRepository();
    private final InMemoryUserRepository users = new InMemoryUserRepository();
    private final AuthService authService = new AuthService(users);
    private CardAccessService cardAccessService;
    private HotelFacade facade;
    private Room room;

    @BeforeEach
    void setUp() {
        InMemoryRoomRepository rooms = new InMemoryRoomRepository();
        rooms.addFloor(2);
        rooms.addRoomType("duplo");
        room = rooms.save(new Room(null, 201, 2, "duplo"), 10);
        InMemoryRoomTypeRepository types = new InMemoryRoomTypeRepository();
        types.add(DOUBLE);
        reservations.addRoomNumber(room.getId(), room.getNumber());
        RoomService roomService = new RoomService(rooms, types, reservations, new InMemoryItemRepository());
        ReservationService reservationService = new ReservationService(reservations, roomService, types);
        InMemoryFloorRepository floors = new InMemoryFloorRepository();
        cardAccessService = new CardAccessService(cards, new InMemoryCardAccessRecordRepository(),
                reservations, users, rooms, floors, new LocationService(floors,
                        new InMemoryLocationRepository()),
                new CardNumberGenerator(cards), new AccessCodeGenerator(), authService,
                Clock.systemDefaultZone());
        facade = new HotelFacade(authService, null, null, roomService, null, null,
                reservationService, null, cardAccessService, null);
    }

    private void addReservation(long id, ReservationStatus status) {
        reservations.add(new Reservation(id, 7L, room.getId(), START, END, status, 4L));
    }

    private ReservationStatus statusOf(long id) {
        return reservations.findById(id).orElseThrow().getStatus();
    }

    @Test
    @Tag("H4Y-134-AC-1")
    void onCheckIn_issueCardOfReservation_whenReservationIsActiveWithoutCard() {
        addReservation(1L, ReservationStatus.RESERVED);

        IssuedCard issued = facade.checkIn(1L, RECEPTIONIST);

        assertEquals(ReservationStatus.CHECKED_IN, statusOf(1L));
        MagneticCard saved = cards.findByReservationId(1L).orElseThrow();
        assertEquals(saved.getNumber(), issued.card().getNumber());
        assertEquals(1L, saved.getReservationId());
        assertTrue(saved.isActive());
    }

    @Test
    @Tag("H4Y-134-AC-2")
    void onCheckIn_giveDifferentNumber_whenAnotherReservationHasCard() {
        addReservation(1L, ReservationStatus.RESERVED);
        addReservation(2L, ReservationStatus.RESERVED);
        String first = facade.checkIn(1L, RECEPTIONIST).card().getNumber();

        String second = facade.checkIn(2L, RECEPTIONIST).card().getNumber();

        assertNotEquals(first, second);
    }

    @Test
    @Tag("H4Y-134-AC-3")
    void onCheckIn_throwAndKeepReservation_whenReservationAlreadyHasCard() {
        addReservation(1L, ReservationStatus.RESERVED);
        String number = cardAccessService.issueCard(reservations.findById(1L).orElseThrow(), RECEPTIONIST)
                .card().getNumber();

        CardAlreadyIssuedException error = assertThrows(CardAlreadyIssuedException.class,
                () -> facade.checkIn(1L, RECEPTIONIST));

        assertEquals("A reserva já tem cartão.", error.getMessage());
        assertEquals(ReservationStatus.RESERVED, statusOf(1L));
        assertEquals(number, cards.findByReservationId(1L).orElseThrow().getNumber());
    }

    @Test
    @Tag("H4Y-134-AC-4")
    void onCheckIn_throwAndIssueNoCard_whenReservationIsCancelled() {
        addReservation(1L, ReservationStatus.CANCELLED);

        assertThrows(IllegalStateException.class, () -> facade.checkIn(1L, RECEPTIONIST));

        assertEquals(ReservationStatus.CANCELLED, statusOf(1L));
        assertTrue(cards.findByReservationId(1L).isEmpty());
    }

    @Test
    @Tag("H4Y-134-AC-4")
    void onCheckIn_throwAndIssueNoCard_whenReservationDoesNotExist() {
        assertThrows(ReservationNotFoundException.class, () -> facade.checkIn(99L, RECEPTIONIST));

        assertTrue(cards.findByReservationId(99L).isEmpty());
    }

    @Test
    @Tag("H4Y-134-AC-5")
    void onCheckIn_returnCodeThatLogsIn_whenCardIsIssued() {
        addReservation(1L, ReservationStatus.RESERVED);

        IssuedCard issued = facade.checkIn(1L, RECEPTIONIST);

        assertTrue(issued.accessCode().matches("[A-HJ-NP-Z2-9]{6}"));
        assertEquals(issued.card().getNumber(),
                cardAccessService.login(issued.card().getNumber(), issued.accessCode()).getNumber());
    }
}
