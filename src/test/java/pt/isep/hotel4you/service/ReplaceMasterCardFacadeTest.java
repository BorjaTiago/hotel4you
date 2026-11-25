package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
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
import pt.isep.hotel4you.domain.CardType;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.IssuedCard;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.MagneticCard;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.User;

/** Tests the issue of a new master card after the block of the lost one, at the facade (H4Y-154). */
class ReplaceMasterCardFacadeTest {

    private static final Location BAR = new Location(5L, "Bar", false, false);
    private static final Location GARDEN = new Location(9L, "Jardim", true, false);
    private static final Room ROOM_202 = new Room(1L, 202, 2, "Duplo");
    private static final User MANAGER = new Manager(1L, "Rui Costa", "gestor", "hash");
    private static final User RECEPTIONIST = new Receptionist(2L, "Ana Silva", "ana", "hash");

    private final InMemoryCardRepository cards = new InMemoryCardRepository();
    private final InMemoryCardAccessRecordRepository records = new InMemoryCardAccessRecordRepository();
    private HotelFacade facade;
    private IssuedCard oldMaster;

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
        AuthService authService = new AuthService(users);
        CardAccessService service = new CardAccessService(cards, records, new InMemoryReservationRepository(),
                users, rooms, floors, new LocationService(floors, new InMemoryLocationRepository()),
                new CardNumberGenerator(cards), new AccessCodeGenerator(), authService, Clock.systemDefaultZone());
        facade = new HotelFacade(authService, null, null, null, null, null, null, null, service);
        oldMaster = service.issueMasterCard(MANAGER, MANAGER);
    }

    private String oldNumber() {
        return oldMaster.card().getNumber();
    }

    @Test
    @Tag("H4Y-154-AC-1")
    void onReplaceMasterCard_issueCardWithNewNumber_whenOldCardBlocked() {
        facade.blockMasterCard(oldNumber(), MANAGER);

        IssuedCard issued = facade.replaceMasterCard(oldNumber(), MANAGER);

        assertNotEquals(oldNumber(), issued.card().getNumber());
        assertEquals(CardType.MASTER, issued.card().getType());
        assertEquals(MANAGER.getId(), issued.card().getUserId());
        assertTrue(issued.card().isActive());
        assertEquals(6, issued.accessCode().length());
    }

    @Test
    @Tag("H4Y-154-AC-1")
    void onListCardPlaces_allowEveryRoomAndZone_whenNewMasterCardIssued() {
        facade.blockMasterCard(oldNumber(), MANAGER);
        MagneticCard card = facade.replaceMasterCard(oldNumber(), MANAGER).card();

        List<CardPlace> places = facade.listCardPlaces(card);

        assertEquals(List.of("Quarto 202", "Bar", "Jardim"), places.stream().map(CardPlace::name).toList());
        assertTrue(places.stream().allMatch(CardPlace::allowed));
    }

    @Test
    @Tag("H4Y-154-AC-2")
    void onReplaceMasterCard_keepOldEntriesAndBlock_whenNewCardIssued() {
        CardPlace bar = facade.listCardPlaces(oldMaster.card()).get(1);
        facade.enterPlace(oldMaster.card(), bar);
        facade.blockMasterCard(oldNumber(), MANAGER);

        facade.replaceMasterCard(oldNumber(), MANAGER);

        MagneticCard old = cards.findByNumber(oldNumber()).orElseThrow();
        assertTrue(old.isBlocked());
        assertEquals(1, records.findByCardId(old.getId()).size());
        assertEquals(2, facade.listMasterCards(MANAGER).size());
    }

    @Test
    @Tag("H4Y-154-AC-3")
    void onEnterPlace_throw_whenOldMasterCardReplaced() {
        facade.blockMasterCard(oldNumber(), MANAGER);
        facade.replaceMasterCard(oldNumber(), MANAGER);
        MagneticCard old = cards.findByNumber(oldNumber()).orElseThrow();
        CardPlace bar = facade.listCardPlaces(old).get(1);

        assertThrows(AccessDeniedException.class, () -> facade.enterPlace(old, bar));
        assertThrows(CardBlockedException.class, () -> facade.loginWithCard(oldNumber(), oldMaster.accessCode()));
    }

    @Test
    @Tag("H4Y-154-AC-4")
    void onReplaceMasterCard_throw_whenOldCardStillActive() {
        MasterCardActiveException error = assertThrows(MasterCardActiveException.class,
                () -> facade.replaceMasterCard(oldNumber(), MANAGER));

        assertEquals("Bloqueie primeiro o cartão mestre atual.", error.getMessage());
        assertEquals(1, facade.listMasterCards(MANAGER).size());
    }

    @Test
    @Tag("H4Y-154-AC-5")
    void onReplaceMasterCard_throw_whenActorIsReceptionist() {
        facade.blockMasterCard(oldNumber(), MANAGER);

        assertThrows(AccessDeniedException.class, () -> facade.replaceMasterCard(oldNumber(), RECEPTIONIST));
        assertEquals(1, facade.listMasterCards(MANAGER).size());
    }

    @Test
    @Tag("H4Y-154-AC-6")
    void onLoginWithCard_seeEveryPlaceAllowed_whenManagerUsesNewMasterCard() {
        facade.blockMasterCard(oldNumber(), MANAGER);
        IssuedCard issued = facade.replaceMasterCard(oldNumber(), MANAGER);

        MagneticCard card = facade.loginWithCard(issued.card().getNumber(), issued.accessCode().toLowerCase());

        assertEquals(issued.card().getId(), card.getId());
        assertTrue(facade.listCardPlaces(card).stream().allMatch(CardPlace::allowed));
        assertFalse(facade.listCardPlaces(card).isEmpty());
    }
}
