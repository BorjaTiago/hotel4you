package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryCardAccessRecordRepository;
import pt.isep.hotel4you.data.InMemoryCardRepository;
import pt.isep.hotel4you.data.InMemoryFloorRepository;
import pt.isep.hotel4you.data.InMemoryLocationRepository;
import pt.isep.hotel4you.data.InMemoryReservationRepository;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.InMemoryUserRepository;
import pt.isep.hotel4you.domain.CardPlace;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.IssuedCard;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.MagneticCard;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.User;

/** Tests the card flow at the facade boundary: login, places and entries (H4Y-109 AC-4, AC-6, AC-7). */
class CardFacadeTest {

    private static final Location BAR = new Location(5L, "Bar", false, false);
    private static final Location GARDEN = new Location(9L, "Jardim", true, false);
    private static final Room ROOM_202 = new Room(1L, 202, 2, "Duplo");
    private static final User MANAGER = new Manager(1L, "Rui Costa", "gestor", "hash");
    private static final User RECEPTIONIST = new Receptionist(2L, "Ana Silva", "ana", "hash");

    private final InMemoryCardAccessRecordRepository records = new InMemoryCardAccessRecordRepository();
    private CardAccessService cardService;
    private HotelFacade facade;
    private IssuedCard clientCard;

    @BeforeEach
    void setUp() {
        InMemoryUserRepository users = new InMemoryUserRepository();
        users.save(MANAGER);
        users.save(RECEPTIONIST);
        InMemoryFloorRepository floors = new InMemoryFloorRepository();
        floors.add(new Floor(0, "Receção e bar", List.of(BAR, GARDEN)));
        InMemoryRoomRepository rooms = new InMemoryRoomRepository();
        rooms.addFloor(2);
        rooms.addRoomType("Duplo");
        rooms.save(ROOM_202, 10);
        InMemoryReservationRepository reservations = new InMemoryReservationRepository();
        reservations.add(new Reservation(1L, 50L, ROOM_202.getId(), LocalDate.now(),
                LocalDate.now().plusDays(2), ReservationStatus.CHECKED_IN, 2L, List.of()));
        InMemoryCardRepository cards = new InMemoryCardRepository();
        AuthService authService = new AuthService(users);
        cardService = new CardAccessService(cards, records, reservations, users, rooms, floors,
                new LocationService(floors, new InMemoryLocationRepository()),
                new CardNumberGenerator(cards), new AccessCodeGenerator(), authService,
                Clock.systemDefaultZone());
        facade = new HotelFacade(authService, null, null, null, null, null, null, null, cardService, null, null);
        clientCard = cardService.issueCard(reservations.findById(1L).orElseThrow(), RECEPTIONIST);
    }

    @Test
    @Tag("H4Y-109-AC-4")
    void onLoginWithCard_returnCard_whenCodeMatches() {
        MagneticCard card = facade.loginWithCard(clientCard.card().getNumber(), clientCard.accessCode());

        assertEquals(clientCard.card().getId(), card.getId());
    }

    @Test
    @Tag("H4Y-109-AC-4")
    void onLoginWithCard_throw_whenCodeIsWrong() {
        assertThrows(LoginFailedException.class,
                () -> facade.loginWithCard(clientCard.card().getNumber(), "XXXXXX"));
    }

    @Test
    @Tag("H4Y-109-AC-6")
    void onListCardPlaces_markAllowedPlaces_whenClientLoggedIn() {
        MagneticCard card = facade.loginWithCard(clientCard.card().getNumber(), clientCard.accessCode());

        List<CardPlace> places = facade.listCardPlaces(card);

        assertEquals(List.of("Quarto 202", "Bar", "Jardim"), places.stream().map(CardPlace::name).toList());
        assertEquals(List.of(true, false, true), places.stream().map(CardPlace::allowed).toList());
    }

    @Test
    @Tag("H4Y-109-AC-7")
    void onEnterPlace_recordOnlyAllowedEntries_whenClientClicksPlaces() {
        MagneticCard card = facade.loginWithCard(clientCard.card().getNumber(), clientCard.accessCode());
        List<CardPlace> places = facade.listCardPlaces(card);

        facade.enterPlace(card, places.get(0));
        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> facade.enterPlace(card, places.get(1)));
        facade.enterPlace(card, places.get(2));

        assertEquals("Entrada recusada.", error.getMessage());
        assertEquals(2, records.findByCardId(card.getId()).size());
    }

    @Test
    @Tag("H4Y-109-AC-7")
    void onListCardPlaces_allowEveryPlace_whenManagerUsesMasterCard() {
        IssuedCard master = cardService.issueMasterCard(MANAGER, MANAGER);
        MagneticCard card = facade.loginWithCard(master.card().getNumber(), master.accessCode());

        assertTrue(facade.listCardPlaces(card).stream().allMatch(CardPlace::allowed));
    }
}
