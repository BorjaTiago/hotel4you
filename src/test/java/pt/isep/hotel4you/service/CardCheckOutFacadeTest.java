package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Tests the card that the check-out deactivates (H4Y-151). */
class CardCheckOutFacadeTest {

    private static final LocalDate START = LocalDate.now();
    private static final LocalDate END = START.plusDays(2);
    private static final User RECEPTIONIST = new Receptionist(4L, "Ana", "ana", "hash");
    private static final User MANAGER = new Manager(1L, "Rui", "rui", "hash");
    private static final RoomType DOUBLE = new RoomType(1L, "duplo", 2, new BigDecimal("80.00"));

    private final InMemoryCardRepository cards = new InMemoryCardRepository();
    private final InMemoryCardAccessRecordRepository records = new InMemoryCardAccessRecordRepository();
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
        cardAccessService = new CardAccessService(cards, records, reservations, users, rooms, floors,
                new LocationService(floors, new InMemoryLocationRepository()),
                new CardNumberGenerator(cards), new AccessCodeGenerator(), authService,
                Clock.systemDefaultZone());
        facade = new HotelFacade(authService, null, null, roomService, null, null,
                reservationService, null, cardAccessService, null);
    }

    private IssuedCard checkedInWithEntry() {
        reservations.add(new Reservation(1L, 7L, room.getId(), START, END, ReservationStatus.RESERVED, 4L));
        IssuedCard issued = facade.checkIn(1L, RECEPTIONIST);
        cardAccessService.register(savedCard(), room, null);
        return issued;
    }

    private MagneticCard savedCard() {
        return cards.findByReservationId(1L).orElseThrow();
    }

    @Test
    @Tag("H4Y-151-AC-1")
    void onCheckOut_deactivateCardAndKeepEntries_whenReservationHasCard() {
        checkedInWithEntry();

        facade.checkOut(1L, RECEPTIONIST);

        assertEquals(ReservationStatus.CHECKED_OUT, reservations.findById(1L).orElseThrow().getStatus());
        assertFalse(savedCard().isActive());
        assertEquals(1, records.findByCardId(savedCard().getId()).size());
    }

    @Test
    @Tag("H4Y-151-AC-2")
    void onRegister_throwAndKeepOldEntries_whenCheckOutIsDone() {
        String number = checkedInWithEntry().card().getNumber();
        facade.checkOut(1L, RECEPTIONIST);

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> cardAccessService.register(savedCard(), room, null));

        assertEquals("Entrada recusada.", error.getMessage());
        assertFalse(cardAccessService.canOpenRoom(savedCard(), room));
        assertEquals(1, cardAccessService.getPlacesByCard(number, MANAGER).size());
    }

    @Test
    @Tag("H4Y-151-AC-3")
    void onCheckOut_throwAndKeepCardActive_whenReservationHasNoCheckIn() {
        reservations.add(new Reservation(1L, 7L, room.getId(), START, END, ReservationStatus.RESERVED, 4L));
        cardAccessService.issueCard(reservations.findById(1L).orElseThrow(), RECEPTIONIST);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> facade.checkOut(1L, RECEPTIONIST));

        assertEquals("A reserva não tem check-in.", error.getMessage());
        assertTrue(savedCard().isActive());
        assertEquals(ReservationStatus.RESERVED, reservations.findById(1L).orElseThrow().getStatus());
    }

    @Test
    @Tag("H4Y-151-AC-4")
    void onCardLogin_throwAndKeepEntries_whenCheckOutIsDone() {
        IssuedCard issued = checkedInWithEntry();

        facade.checkOut(1L, RECEPTIONIST);

        CardInactiveException error = assertThrows(CardInactiveException.class,
                () -> cardAccessService.login(issued.card().getNumber(), issued.accessCode()));
        assertEquals("O cartão não está ativo.", error.getMessage());
        assertEquals(1, records.findByCardId(savedCard().getId()).size());
    }

    @Test
    @Tag("H4Y-151-AC-1")
    void onCheckOut_finishStay_whenReservationHasNoCard() {
        reservations.add(new Reservation(1L, 7L, room.getId(), START, END, ReservationStatus.CHECKED_IN, 4L));

        facade.checkOut(1L, RECEPTIONIST);

        assertEquals(ReservationStatus.CHECKED_OUT, reservations.findById(1L).orElseThrow().getStatus());
        assertTrue(cards.findByReservationId(1L).isEmpty());
    }
}
