package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import pt.isep.hotel4you.domain.CardAccessRecord;
import pt.isep.hotel4you.domain.CardEntry;
import pt.isep.hotel4you.domain.CardType;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.MagneticCard;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.User;

/** Tests the card queries with the place names (H4Y-155) and the card numbers (H4Y-156). */
class CardQueryServiceTest {

    private static final LocalDateTime FIRST = LocalDateTime.of(2026, 11, 24, 10, 0);
    private static final LocalDateTime SECOND = LocalDateTime.of(2026, 11, 24, 13, 30);

    private static final User MANAGER = new Manager(1L, "Rui Costa", "gestor", "hash");
    private static final User RECEPTIONIST = new Receptionist(2L, "Ana Silva", "ana", "hash");

    private final InMemoryCardRepository cards = new InMemoryCardRepository();
    private final InMemoryCardAccessRecordRepository records = new InMemoryCardAccessRecordRepository();
    private final InMemoryRoomRepository rooms = new InMemoryRoomRepository();
    private final InMemoryLocationRepository locations = new InMemoryLocationRepository();
    private CardQueryService service;
    private Room room202;
    private Room room301;
    private Room room302;
    private Location restaurant;

    @BeforeEach
    void setUp() {
        rooms.addFloor(2);
        rooms.addRoomType("Duplo");
        room202 = rooms.save(new Room(null, 202, 2, "Duplo"), 10);
        rooms.addFloor(3);
        room301 = rooms.save(new Room(null, 301, 3, "Duplo"), 10);
        room302 = rooms.save(new Room(null, 302, 3, "Duplo"), 10);
        restaurant = locations.save(1, new Location(null, "Restaurante", true, true));
        InMemoryUserRepository users = new InMemoryUserRepository();
        InMemoryFloorRepository floors = new InMemoryFloorRepository();
        CardAccessService cardAccessService = new CardAccessService(cards, records,
                new InMemoryReservationRepository(), users, rooms, floors,
                new LocationService(floors, locations),
                new CardNumberGenerator(cards), new AccessCodeGenerator(), new AuthService(users),
                Clock.fixed(FIRST.toInstant(ZoneOffset.UTC), ZoneOffset.UTC));
        service = new CardQueryService(cardAccessService, cards, rooms, locations);
    }

    private MagneticCard card(String number, boolean blocked) {
        return cards.save(new MagneticCard(null, number, CardType.CLIENT, 1L, null, !blocked,
                blocked ? SECOND : null));
    }

    @Test
    @Tag("H4Y-155-AC-1")
    void onGetPlacesByCard_showRoomAndZoneWithTime_whenCardEnteredBoth() {
        MagneticCard card = card("12345", false);
        records.save(new CardAccessRecord(null, card.getId(), room202.getId(), null, FIRST));
        records.save(new CardAccessRecord(null, card.getId(), null, restaurant.getId(), SECOND));

        List<CardEntry> entries = service.getPlacesByCard("12345", MANAGER);

        assertEquals(List.of(
                new CardEntry("12345", CardType.CLIENT, "Quarto 202", FIRST),
                new CardEntry("12345", CardType.CLIENT, "Restaurante", SECOND)), entries);
    }

    @Test
    @Tag("H4Y-155-AC-1")
    void onGetPlacesByCard_showEntries_whenCardIsBlocked() {
        MagneticCard card = card("12345", true);
        records.save(new CardAccessRecord(null, card.getId(), room202.getId(), null, FIRST));

        List<CardEntry> entries = service.getPlacesByCard(" 12345 ", MANAGER);

        assertEquals(List.of(new CardEntry("12345", CardType.CLIENT, "Quarto 202", FIRST)), entries);
    }

    @Test
    @Tag("H4Y-155-AC-2")
    void onGetPlacesByCard_returnEmptyList_whenCardWasNeverUsed() {
        card("12346", false);

        assertTrue(service.getPlacesByCard("12346", MANAGER).isEmpty());
    }

    @Test
    @Tag("H4Y-155-AC-3")
    void onGetPlacesByCard_throw_whenCardDoesNotExist() {
        CardNotFoundException error = assertThrows(CardNotFoundException.class,
                () -> service.getPlacesByCard("99999", MANAGER));

        assertEquals("O cartão não existe.", error.getMessage());
    }

    @Test
    @Tag("H4Y-155-AC-4")
    void onGetPlacesByCard_throw_whenUserIsReceptionist() {
        card("12345", false);

        assertThrows(AccessDeniedException.class, () -> service.getPlacesByCard("12345", RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-155-AC-5")
    void onGetPlacesByCard_readEntriesThroughFacade_whenManagerAsks() {
        MagneticCard card = card("12345", false);
        records.save(new CardAccessRecord(null, card.getId(), room202.getId(), null, FIRST));
        HotelFacade facade = new HotelFacade(null, null, null, null, null, null, null, null, null, service);

        assertEquals(List.of(new CardEntry("12345", CardType.CLIENT, "Quarto 202", FIRST)),
                facade.getPlacesByCard("12345", MANAGER));
        assertThrows(CardNotFoundException.class, () -> facade.getPlacesByCard("99999", MANAGER));
    }

    private MagneticCard masterCard(String number) {
        return cards.save(new MagneticCard(null, number, CardType.MASTER, null, 1L, true, null));
    }

    @Test
    @Tag("H4Y-156-AC-1")
    void onGetPassesByRoom_showCardWithTime_whenClientCardEnteredRoom() {
        MagneticCard card = card("12345", false);
        records.save(new CardAccessRecord(null, card.getId(), room301.getId(), null, FIRST));

        List<CardEntry> entries = service.getPassesByRoom("301", MANAGER);

        assertEquals(List.of(new CardEntry("12345", CardType.CLIENT, "Quarto 301", FIRST)), entries);
    }

    @Test
    @Tag("H4Y-156-AC-2")
    void onGetPassesByRoom_includeMasterCard_whenMasterCardEnteredRoom() {
        MagneticCard client = card("12345", false);
        MagneticCard master = masterCard("90001");
        records.save(new CardAccessRecord(null, client.getId(), room301.getId(), null, FIRST));
        records.save(new CardAccessRecord(null, master.getId(), room301.getId(), null, SECOND));
        records.save(new CardAccessRecord(null, master.getId(), room202.getId(), null, SECOND));

        List<CardEntry> entries = service.getPassesByRoom(" 301 ", MANAGER);

        assertEquals(List.of(
                new CardEntry("12345", CardType.CLIENT, "Quarto 301", FIRST),
                new CardEntry("90001", CardType.MASTER, "Quarto 301", SECOND)), entries);
    }

    @Test
    @Tag("H4Y-156-AC-2")
    void onGetPassesByRoom_showEntry_whenCardIsBlocked() {
        MagneticCard card = card("12345", true);
        records.save(new CardAccessRecord(null, card.getId(), room301.getId(), null, FIRST));

        assertEquals(List.of(new CardEntry("12345", CardType.CLIENT, "Quarto 301", FIRST)),
                service.getPassesByRoom("301", MANAGER));
    }

    @Test
    @Tag("H4Y-156-AC-3")
    void onGetPassesByRoom_returnEmptyList_whenNobodyEnteredRoom() {
        MagneticCard card = card("12345", false);
        records.save(new CardAccessRecord(null, card.getId(), room301.getId(), null, FIRST));

        assertTrue(service.getPassesByRoom(String.valueOf(room302.getNumber()), MANAGER).isEmpty());
    }

    @Test
    @Tag("H4Y-156-AC-4")
    void onGetPassesByRoom_throw_whenRoomDoesNotExist() {
        RoomNotFoundException error = assertThrows(RoomNotFoundException.class,
                () -> service.getPassesByRoom("999", MANAGER));

        assertEquals("O quarto não existe.", error.getMessage());
    }

    @Test
    @Tag("H4Y-156-AC-4")
    void onGetPassesByRoom_throw_whenRoomNumberIsNotValid() {
        assertThrows(RoomNotFoundException.class, () -> service.getPassesByRoom("abc", MANAGER));
        assertThrows(RoomNotFoundException.class, () -> service.getPassesByRoom(" ", MANAGER));
        assertThrows(RoomNotFoundException.class, () -> service.getPassesByRoom(null, MANAGER));
    }

    @Test
    @Tag("H4Y-156-AC-5")
    void onGetPassesByRoom_throw_whenUserIsReceptionist() {
        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.getPassesByRoom("301", RECEPTIONIST));

        assertEquals("Só o gestor pode consultar as entradas.", error.getMessage());
        assertThrows(AccessDeniedException.class, () -> service.getPassesByRoom("999", RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-156-AC-6")
    void onGetPassesByRoom_readEntriesThroughFacade_whenManagerAsks() {
        MagneticCard card = card("12345", false);
        records.save(new CardAccessRecord(null, card.getId(), room301.getId(), null, FIRST));
        HotelFacade facade = new HotelFacade(null, null, null, null, null, null, null, null, null, service);

        assertEquals(List.of(new CardEntry("12345", CardType.CLIENT, "Quarto 301", FIRST)),
                facade.getPassesByRoom("301", MANAGER));
        assertThrows(RoomNotFoundException.class, () -> facade.getPassesByRoom("999", MANAGER));
    }
}
