package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
import pt.isep.hotel4you.domain.CardType;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.MagneticCard;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.User;

/** Tests the master card screen functions of the facade: list and block the master cards (H4Y-153). */
class MasterCardFacadeTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 24, 10, 0);
    private static final Room ROOM_202 = new Room(10L, 202, 2, "Duplo");
    private static final Location BAR = new Location(5L, "Bar", false, false);
    private static final User MANAGER = new Manager(1L, "Rui Costa", "gestor", "hash");
    private static final User RECEPTIONIST = new Receptionist(2L, "Ana Silva", "ana", "hash");

    private final InMemoryCardRepository cards = new InMemoryCardRepository();
    private final InMemoryReservationRepository reservations = new InMemoryReservationRepository();
    private final InMemoryUserRepository users = new InMemoryUserRepository();
    private final AuthService authService = new AuthService(users);
    private CardAccessService service;
    private HotelFacade facade;

    @BeforeEach
    void setUp() {
        users.save(MANAGER);
        users.save(RECEPTIONIST);
        reservations.add(new Reservation(1L, 50L, ROOM_202.getId(), NOW.toLocalDate(),
                NOW.toLocalDate().plusDays(2), ReservationStatus.CHECKED_IN, 2L, List.of()));
        Clock clock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        InMemoryFloorRepository floors = new InMemoryFloorRepository();
        service = new CardAccessService(cards, new InMemoryCardAccessRecordRepository(), reservations, users,
                new InMemoryRoomRepository(), floors,
                new LocationService(floors, new InMemoryLocationRepository()),
                new CardNumberGenerator(cards), new AccessCodeGenerator(), authService, clock);
        facade = new HotelFacade(null, null, null, null, null, null, null, null, service);
    }

    private MagneticCard masterCard() {
        return service.issueMasterCard(MANAGER, MANAGER).card();
    }

    private MagneticCard reload(MagneticCard card) {
        return cards.findByNumber(card.getNumber()).orElseThrow();
    }

    @Test
    @Tag("H4Y-153-AC-6")
    void onListMasterCards_returnOnlyMasterCards_whenClientCardsExist() {
        MagneticCard master = masterCard();
        service.issueCard(reservations.findById(1L).orElseThrow(), RECEPTIONIST);

        List<MagneticCard> list = facade.listMasterCards(MANAGER);

        assertEquals(List.of(master.getNumber()), list.stream().map(MagneticCard::getNumber).toList());
        assertEquals(CardType.MASTER, list.getFirst().getType());
    }

    @Test
    @Tag("H4Y-153-AC-5")
    void onListMasterCards_throw_whenActorIsReceptionist() {
        masterCard();

        assertThrows(AccessDeniedException.class, () -> facade.listMasterCards(RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-153-AC-1")
    void onBlockMasterCard_blockAtOnceAndRefuseDoors_whenCardIsActive() {
        MagneticCard master = masterCard();

        MagneticCard blocked = facade.blockMasterCard(master.getNumber(), MANAGER);

        assertTrue(blocked.isBlocked());
        assertTrue(reload(master).isBlocked());
        assertFalse(reload(master).isActive());
        assertFalse(service.canOpenRoom(reload(master), ROOM_202));
        assertFalse(service.canOpen(reload(master), BAR));
    }

    @Test
    @Tag("H4Y-153-AC-2")
    void onBlockMasterCard_saveBlockTimeAndAllowNewMasterCard_whenCardIsBlocked() {
        MagneticCard master = masterCard();
        assertThrows(MasterCardActiveException.class, () -> service.replaceMasterCard(master.getNumber(), MANAGER));

        facade.blockMasterCard(master.getNumber(), MANAGER);

        assertEquals(NOW, facade.listMasterCards(MANAGER).getFirst().getBlockedAt());
        assertEquals(CardType.MASTER, service.replaceMasterCard(master.getNumber(), MANAGER).card().getType());
    }

    @Test
    @Tag("H4Y-153-AC-3")
    void onBlockMasterCard_throwAlreadyBlockedMessage_whenCardIsBlocked() {
        MagneticCard master = masterCard();
        facade.blockMasterCard(master.getNumber(), MANAGER);

        CardBlockedException error = assertThrows(CardBlockedException.class,
                () -> facade.blockMasterCard(master.getNumber(), MANAGER));

        assertEquals("O cartão já está bloqueado.", error.getMessage());
        assertEquals(NOW, reload(master).getBlockedAt());
    }

    @Test
    @Tag("H4Y-153-AC-4")
    void onBlockMasterCard_throw_whenCardDoesNotExist() {
        masterCard();

        assertThrows(CardNotFoundException.class, () -> facade.blockMasterCard("99999", MANAGER));
    }

    @Test
    @Tag("H4Y-153-AC-4")
    void onBlockMasterCard_throwAndKeepCard_whenCardIsClientCard() {
        MagneticCard client = service.issueCard(reservations.findById(1L).orElseThrow(), RECEPTIONIST).card();

        assertThrows(CardNotFoundException.class, () -> facade.blockMasterCard(client.getNumber(), MANAGER));
        assertFalse(reload(client).isBlocked());
    }

    @Test
    @Tag("H4Y-153-AC-5")
    void onBlockMasterCard_throwAndKeepCard_whenActorIsReceptionist() {
        MagneticCard master = masterCard();

        assertThrows(AccessDeniedException.class, () -> facade.blockMasterCard(master.getNumber(), RECEPTIONIST));
        assertFalse(reload(master).isBlocked());
    }

    @Test
    @Tag("H4Y-153-AC-6")
    void onLogin_throw_whenMasterCardIsBlocked() {
        var issued = service.issueMasterCard(MANAGER, MANAGER);
        facade.blockMasterCard(issued.card().getNumber(), MANAGER);

        assertThrows(CardBlockedException.class,
                () -> service.login(issued.card().getNumber(), issued.accessCode()));
    }
}
