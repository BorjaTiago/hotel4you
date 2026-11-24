package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
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
import pt.isep.hotel4you.domain.CardPlace;
import pt.isep.hotel4you.domain.CardType;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.IssuedCard;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.MagneticCard;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.User;

/**
 * Tests the card service: issue, activate, deactivate, block, replace the master card, the door
 * rule, the entry log, the queries by card and by room (H4Y-109 AC-3, RF-16 to RF-21, RF-23) and the
 * login with the card (H4Y-109 AC-4) and the places of the screen "O meu cartão" (H4Y-109 AC-6).
 */
class CardAccessServiceTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 24, 10, 0);
    private static final LocalDate START = LocalDate.of(2026, 11, 23);
    private static final LocalDate END = LocalDate.of(2026, 11, 26);

    private static final Location GARAGE = new Location(1L, "Garagem", false, false);
    private static final Location BAR = new Location(5L, "Bar", false, false);
    private static final Location RESTAURANT = new Location(7L, "Restaurante", true, true);
    private static final Location POOL = new Location(8L, "Piscina", true, true);
    private static final Location GARDEN = new Location(9L, "Jardim", true, false);

    private static final Room ROOM_202 = new Room(1L, 202, 2, "Duplo");
    private static final Room ROOM_203 = new Room(2L, 203, 2, "Duplo");

    private static final User MANAGER = new Manager(1L, "Rui Costa", "gestor", "hash");
    private static final User RECEPTIONIST = new Receptionist(2L, "Ana Silva", "ana", "hash");
    private static final User RESTAURANT_STAFF = new RestaurantStaff(3L, "Joana Lima", "joana", "hash");

    private final InMemoryCardRepository cards = new InMemoryCardRepository();
    private final InMemoryCardAccessRecordRepository records = new InMemoryCardAccessRecordRepository();
    private final InMemoryReservationRepository reservations = new InMemoryReservationRepository();
    private final InMemoryUserRepository users = new InMemoryUserRepository();
    private final AuthService authService = new AuthService(users);
    private CardAccessService service;

    @BeforeEach
    void setUp() {
        InMemoryFloorRepository floors = new InMemoryFloorRepository();
        InMemoryRoomRepository rooms = new InMemoryRoomRepository();
        rooms.addFloor(2);
        rooms.addRoomType("Duplo");
        rooms.save(ROOM_202, 10);
        rooms.save(ROOM_203, 10);
        floors.add(new Floor(-1, "Garagem e arrecadação", List.of(GARAGE)));
        floors.add(new Floor(0, "Receção e bar", List.of(BAR, GARDEN)));
        floors.add(new Floor(1, "Restaurante e piscina", List.of(RESTAURANT, POOL)));
        users.save(MANAGER);
        users.save(RECEPTIONIST);
        users.save(RESTAURANT_STAFF);
        reservations.add(reservation(1L, ROOM_202, ReservationStatus.CHECKED_IN, List.of(RESTAURANT.getId())));
        reservations.add(reservation(2L, ROOM_203, ReservationStatus.CHECKED_IN, List.of()));
        reservations.add(reservation(3L, ROOM_203, ReservationStatus.CANCELLED, List.of()));
        reservations.add(reservation(4L, ROOM_202, ReservationStatus.RESERVED, List.of()));
        Clock clock = Clock.fixed(NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        service = new CardAccessService(cards, records, reservations, users, rooms, floors,
                new LocationService(floors, new InMemoryLocationRepository()),
                new CardNumberGenerator(cards), new AccessCodeGenerator(), authService, clock);
    }

    private static Reservation reservation(long id, Room room, ReservationStatus status, List<Long> zones) {
        return new Reservation(id, 50L, room.getId(), START, END, status, 2L, zones);
    }

    private Reservation reservationById(long id) {
        return reservations.findById(id).orElseThrow();
    }

    private MagneticCard clientCard() {
        return service.issueCard(reservationById(1L), RECEPTIONIST).card();
    }

    private MagneticCard masterCard() {
        return service.issueMasterCard(MANAGER, MANAGER).card();
    }

    private MagneticCard reload(MagneticCard card) {
        return cards.findByNumber(card.getNumber()).orElseThrow();
    }

    // --- issue a client card ---

    @Test
    @Tag("H4Y-109-AC-3")
    void onIssueCard_saveActiveCardWithHashedCode_whenReservationIsCheckedIn() {
        IssuedCard issued = service.issueCard(reservationById(1L), RECEPTIONIST);

        MagneticCard card = issued.card();
        assertEquals(CardType.CLIENT, card.getType());
        assertEquals(1L, card.getReservationId());
        assertNull(card.getUserId());
        assertTrue(card.isActive());
        assertFalse(card.isBlocked());
        assertTrue(issued.accessCode().matches("[A-HJ-NP-Z2-9]{6}"));
        assertNotEquals(issued.accessCode(), reload(card).getAccessCodeHash());
        assertTrue(authService.matchesPassword(issued.accessCode(), reload(card).getAccessCodeHash()));
    }

    @Test
    @Tag("H4Y-109-AC-2")
    void onIssueCard_giveDifferentNumbers_whenTwoReservationsGetCards() {
        String first = service.issueCard(reservationById(1L), RECEPTIONIST).card().getNumber();
        String second = service.issueCard(reservationById(2L), MANAGER).card().getNumber();

        assertNotEquals(first, second);
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onIssueCard_throw_whenReservationAlreadyHasCard() {
        clientCard();

        assertThrows(CardAlreadyIssuedException.class,
                () -> service.issueCard(reservationById(1L), RECEPTIONIST));
        assertEquals(1, cards.count());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onIssueCard_throw_whenReservationIsCancelled() {
        assertThrows(IllegalStateException.class, () -> service.issueCard(reservationById(3L), RECEPTIONIST));
        assertEquals(0, cards.count());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onIssueCard_throw_whenReservationDoesNotExist() {
        Reservation unknown = reservation(99L, ROOM_202, ReservationStatus.CHECKED_IN, List.of());

        assertThrows(ReservationNotFoundException.class, () -> service.issueCard(unknown, RECEPTIONIST));
        assertEquals(0, cards.count());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onIssueCard_throw_whenActorIsRestaurantStaff() {
        assertThrows(AccessDeniedException.class, () -> service.issueCard(reservationById(1L), RESTAURANT_STAFF));
        assertEquals(0, cards.count());
    }

    // --- issue and replace the master card ---

    @Test
    @Tag("H4Y-109-AC-3")
    void onIssueMasterCard_saveMasterCardWithCode_whenManagerHasNone() {
        IssuedCard issued = service.issueMasterCard(MANAGER, MANAGER);

        assertEquals(CardType.MASTER, issued.card().getType());
        assertEquals(MANAGER.getId(), issued.card().getUserId());
        assertTrue(issued.card().isActive());
        assertTrue(authService.matchesPassword(issued.accessCode(), reload(issued.card()).getAccessCodeHash()));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onIssueMasterCard_throw_whenManagerHasActiveMasterCard() {
        masterCard();

        assertThrows(MasterCardActiveException.class, () -> service.issueMasterCard(MANAGER, MANAGER));
        assertEquals(1, cards.count());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onIssueMasterCard_throw_whenActorIsReceptionist() {
        assertThrows(AccessDeniedException.class, () -> service.issueMasterCard(MANAGER, RECEPTIONIST));
        assertEquals(0, cards.count());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onIssueMasterCard_throw_whenOwnerIsNotManager() {
        assertThrows(IllegalArgumentException.class, () -> service.issueMasterCard(RECEPTIONIST, MANAGER));
        assertEquals(0, cards.count());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onReplaceMasterCard_issueNewNumberAndKeepOldEntries_whenOldCardIsBlocked() {
        MagneticCard old = masterCard();
        service.register(old, ROOM_202, null);
        service.blockCard(old.getNumber(), MANAGER);

        IssuedCard replacement = service.replaceMasterCard(old.getNumber(), MANAGER);

        assertNotEquals(old.getNumber(), replacement.card().getNumber());
        assertEquals(CardType.MASTER, replacement.card().getType());
        assertEquals(MANAGER.getId(), replacement.card().getUserId());
        assertTrue(reload(old).isBlocked());
        assertFalse(service.canOpenRoom(reload(old), ROOM_202));
        assertTrue(service.canOpenRoom(replacement.card(), ROOM_202));
        assertEquals(1, service.getPlacesByCard(old.getNumber(), MANAGER).size());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onReplaceMasterCard_throw_whenOldCardIsStillActive() {
        MagneticCard old = masterCard();

        assertThrows(MasterCardActiveException.class, () -> service.replaceMasterCard(old.getNumber(), MANAGER));
        assertEquals(1, cards.count());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onReplaceMasterCard_throw_whenCardDoesNotExist() {
        assertThrows(CardNotFoundException.class, () -> service.replaceMasterCard("99999", MANAGER));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onReplaceMasterCard_throw_whenCardIsNotMaster() {
        MagneticCard card = clientCard();
        service.blockCard(card.getNumber(), MANAGER);

        assertThrows(IllegalArgumentException.class, () -> service.replaceMasterCard(card.getNumber(), MANAGER));
        assertEquals(1, cards.count());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onReplaceMasterCard_throw_whenActorIsReceptionist() {
        MagneticCard old = masterCard();
        service.blockCard(old.getNumber(), MANAGER);

        assertThrows(AccessDeniedException.class, () -> service.replaceMasterCard(old.getNumber(), RECEPTIONIST));
    }

    // --- activate and deactivate ---

    @Test
    @Tag("H4Y-109-AC-3")
    void onDeactivateCard_refuseEveryDoor_whenCardWasActive() {
        MagneticCard card = clientCard();

        MagneticCard inactive = service.deactivateCard(card.getNumber(), RECEPTIONIST);

        assertFalse(inactive.isActive());
        assertFalse(reload(card).isActive());
        assertFalse(service.canOpenRoom(reload(card), ROOM_202));
        assertFalse(service.canOpen(reload(card), RESTAURANT));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onDeactivateCard_keepAccessCode_whenCardWasActive() {
        IssuedCard issued = service.issueCard(reservationById(1L), RECEPTIONIST);

        service.deactivateCard(issued.card().getNumber(), RECEPTIONIST);

        assertTrue(authService.matchesPassword(issued.accessCode(), reload(issued.card()).getAccessCodeHash()));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onActivateCard_openRoomAgain_whenCardWasInactive() {
        MagneticCard card = clientCard();
        service.deactivateCard(card.getNumber(), RECEPTIONIST);

        MagneticCard active = service.activateCard(card.getNumber(), RECEPTIONIST);

        assertTrue(active.isActive());
        assertTrue(service.canOpenRoom(reload(card), ROOM_202));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onActivateCard_throw_whenCardIsBlocked() {
        MagneticCard card = clientCard();
        service.blockCard(card.getNumber(), MANAGER);

        assertThrows(CardBlockedException.class, () -> service.activateCard(card.getNumber(), RECEPTIONIST));
        assertFalse(reload(card).isActive());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onDeactivateCard_throw_whenCardDoesNotExist() {
        assertThrows(CardNotFoundException.class, () -> service.deactivateCard("99999", RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onActivateCard_throw_whenActorIsRestaurantStaff() {
        MagneticCard card = clientCard();
        service.deactivateCard(card.getNumber(), RECEPTIONIST);

        assertThrows(AccessDeniedException.class, () -> service.activateCard(card.getNumber(), RESTAURANT_STAFF));
        assertFalse(reload(card).isActive());
    }

    // --- block ---

    @Test
    @Tag("H4Y-109-AC-3")
    void onBlockCard_saveBlockTimeAndRefuseDoors_whenCardIsActive() {
        MagneticCard card = masterCard();

        MagneticCard blocked = service.blockCard(card.getNumber(), MANAGER);

        assertEquals(NOW, blocked.getBlockedAt());
        assertEquals(NOW, reload(card).getBlockedAt());
        assertFalse(reload(card).isActive());
        assertFalse(service.canOpen(reload(card), BAR));
        assertFalse(service.canOpenRoom(reload(card), ROOM_202));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onBlockCard_throw_whenCardIsAlreadyBlocked() {
        MagneticCard card = masterCard();
        service.blockCard(card.getNumber(), MANAGER);

        assertThrows(CardBlockedException.class, () -> service.blockCard(card.getNumber(), MANAGER));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onBlockCard_throw_whenCardDoesNotExist() {
        assertThrows(CardNotFoundException.class, () -> service.blockCard("99999", MANAGER));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onBlockCard_throw_whenActorIsReceptionist() {
        MagneticCard card = masterCard();

        assertThrows(AccessDeniedException.class, () -> service.blockCard(card.getNumber(), RECEPTIONIST));
        assertFalse(reload(card).isBlocked());
    }

    // --- door rule for rooms ---

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpenRoom_allow_whenRoomIsTheReservationRoom() {
        assertTrue(service.canOpenRoom(clientCard(), ROOM_202));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpenRoom_refuse_whenRoomIsAnotherRoom() {
        assertFalse(service.canOpenRoom(clientCard(), ROOM_203));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpenRoom_refuse_whenReservationHasNoCheckIn() {
        MagneticCard card = service.issueCard(reservationById(4L), RECEPTIONIST).card();

        assertFalse(service.canOpenRoom(card, ROOM_202));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpenRoom_allow_whenCardIsActiveMasterCard() {
        MagneticCard card = masterCard();

        assertTrue(service.canOpenRoom(card, ROOM_202));
        assertTrue(service.canOpenRoom(card, ROOM_203));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpenRoom_refuse_whenCardOrRoomIsNull() {
        assertFalse(service.canOpenRoom(null, ROOM_202));
        assertFalse(service.canOpenRoom(clientCard(), null));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpenRoom_refuse_whenCardIsStaffCard() {
        MagneticCard card = cards.save(new MagneticCard(null, "555", CardType.CLIENT, null, 2L, true, null));

        assertFalse(service.canOpenRoom(card, ROOM_202));
    }

    // --- door rule for zones ---

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpen_allow_whenReservationIncludesZone() {
        assertTrue(service.canOpen(clientCard(), RESTAURANT));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpen_refuse_whenReservationDoesNotIncludeZone() {
        assertFalse(service.canOpen(clientCard(), POOL));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpen_refuse_whenClientCardNeverOpensZone() {
        MagneticCard card = clientCard();

        assertFalse(service.canOpen(card, BAR));
        assertFalse(service.canOpen(card, GARAGE));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpen_allow_whenZoneOpensForEveryGuest() {
        assertTrue(service.canOpen(clientCard(), GARDEN));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpen_allow_whenCardIsActiveMasterCard() {
        MagneticCard card = masterCard();

        for (Location zone : List.of(GARAGE, BAR, RESTAURANT, POOL, GARDEN)) {
            assertTrue(service.canOpen(card, zone), zone.getName());
        }
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpen_useRoleRule_whenCardIsStaffCard() {
        MagneticCard card = cards.save(new MagneticCard(null, "555", CardType.CLIENT, null, 2L, true, null));

        assertTrue(service.canOpen(card, BAR));
        assertFalse(service.canOpen(card, GARAGE));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onCanOpen_refuse_whenCardOrZoneIsNull() {
        assertFalse(service.canOpen(null, BAR));
        assertFalse(service.canOpen(masterCard(), null));
    }

    // --- entry log ---

    @Test
    @Tag("H4Y-109-AC-3")
    void onRegister_saveRoomAndTime_whenRoomIsAllowed() {
        MagneticCard card = clientCard();

        CardAccessRecord record = service.register(card, ROOM_202, null);

        assertEquals(card.getId(), record.getCardId());
        assertEquals(ROOM_202.getId(), record.getRoomId());
        assertNull(record.getLocationId());
        assertEquals(NOW, record.getEntryDateTime());
        assertEquals(1, records.count());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onRegister_saveZoneAndTime_whenZoneIsAllowed() {
        CardAccessRecord record = service.register(clientCard(), null, RESTAURANT);

        assertEquals(RESTAURANT.getId(), record.getLocationId());
        assertNull(record.getRoomId());
        assertEquals(NOW, record.getEntryDateTime());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onRegister_throwAndSaveNothing_whenEntryIsRefused() {
        MagneticCard card = clientCard();

        assertThrows(AccessDeniedException.class, () -> service.register(card, null, BAR));
        assertThrows(AccessDeniedException.class, () -> service.register(card, ROOM_203, null));
        assertEquals(0, records.count());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onRegister_throw_whenPlaceIsBothOrNone() {
        MagneticCard card = masterCard();

        assertThrows(IllegalArgumentException.class, () -> service.register(card, ROOM_202, BAR));
        assertThrows(IllegalArgumentException.class, () -> service.register(card, null, null));
        assertEquals(0, records.count());
    }

    // --- queries ---

    @Test
    @Tag("H4Y-109-AC-3")
    void onGetPlacesByCard_returnEntries_whenCardHasEntries() {
        MagneticCard card = clientCard();
        service.register(card, ROOM_202, null);
        service.register(card, null, RESTAURANT);

        List<CardAccessRecord> places = service.getPlacesByCard(card.getNumber(), MANAGER);

        assertEquals(2, places.size());
        assertEquals(ROOM_202.getId(), places.get(0).getRoomId());
        assertEquals(RESTAURANT.getId(), places.get(1).getLocationId());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onGetPlacesByCard_returnEmptyList_whenCardHasNoEntries() {
        assertTrue(service.getPlacesByCard(clientCard().getNumber(), MANAGER).isEmpty());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onGetPlacesByCard_throw_whenCardDoesNotExist() {
        assertThrows(CardNotFoundException.class, () -> service.getPlacesByCard("99999", MANAGER));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onGetPlacesByCard_throw_whenActorIsReceptionist() {
        MagneticCard card = clientCard();

        assertThrows(AccessDeniedException.class, () -> service.getPlacesByCard(card.getNumber(), RECEPTIONIST));
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onGetPassesByRoom_includeMasterCard_whenClientAndManagerEntered() {
        MagneticCard client = clientCard();
        MagneticCard master = masterCard();
        service.register(client, ROOM_202, null);
        service.register(master, ROOM_202, null);
        service.register(master, ROOM_203, null);

        List<CardAccessRecord> passes = service.getPassesByRoom(ROOM_202, MANAGER);

        assertEquals(List.of(client.getId(), master.getId()),
                passes.stream().map(CardAccessRecord::getCardId).toList());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onGetPassesByRoom_returnEmptyList_whenNobodyEntered() {
        assertTrue(service.getPassesByRoom(ROOM_203, MANAGER).isEmpty());
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onGetPassesByRoom_throw_whenActorIsReceptionist() {
        assertThrows(AccessDeniedException.class, () -> service.getPassesByRoom(ROOM_202, RECEPTIONIST));
    }

    // --- login with the card (H4Y-133) ---

    @Test
    @Tag("H4Y-109-AC-4")
    void onLogin_returnCard_whenNumberAndCodeMatch() {
        IssuedCard issued = service.issueCard(reservationById(1L), RECEPTIONIST);

        MagneticCard card = service.login(issued.card().getNumber(), issued.accessCode());

        assertEquals(issued.card().getNumber(), card.getNumber());
        assertTrue(service.canOpenRoom(card, ROOM_202));
    }

    @Test
    @Tag("H4Y-109-AC-4")
    void onLogin_returnCard_whenInputHasSpacesAndLowerCaseCode() {
        IssuedCard issued = service.issueCard(reservationById(1L), RECEPTIONIST);

        MagneticCard card = service.login(" " + issued.card().getNumber() + " ",
                " " + issued.accessCode().toLowerCase() + " ");

        assertEquals(issued.card().getNumber(), card.getNumber());
    }

    @Test
    @Tag("H4Y-109-AC-4")
    void onLogin_returnMasterCard_whenManagerUsesMasterCode() {
        IssuedCard issued = service.issueMasterCard(MANAGER, MANAGER);

        MagneticCard card = service.login(issued.card().getNumber(), issued.accessCode());

        assertEquals(CardType.MASTER, card.getType());
        assertTrue(service.canOpen(card, GARAGE));
    }

    @Test
    @Tag("H4Y-109-AC-4")
    void onLogin_throw_whenCardDoesNotExist() {
        LoginFailedException error = assertThrows(LoginFailedException.class,
                () -> service.login("99999999", "ABCDEF"));

        assertEquals("Número do cartão ou código errado.", error.getMessage());
    }

    @Test
    @Tag("H4Y-109-AC-4")
    void onLogin_throw_whenCodeIsWrong() {
        IssuedCard issued = service.issueCard(reservationById(1L), RECEPTIONIST);
        String wrong = issued.accessCode().equals("AAAAAA") ? "BBBBBB" : "AAAAAA";

        LoginFailedException error = assertThrows(LoginFailedException.class,
                () -> service.login(issued.card().getNumber(), wrong));

        assertEquals("Número do cartão ou código errado.", error.getMessage());
    }

    @Test
    @Tag("H4Y-109-AC-4")
    void onLogin_throw_whenCardHasNoCode() {
        MagneticCard card = cards.save(new MagneticCard(null, "90001", CardType.MASTER, null,
                MANAGER.getId(), true, null));

        assertThrows(LoginFailedException.class, () -> service.login(card.getNumber(), "ABCDEF"));
    }

    @Test
    @Tag("H4Y-109-AC-4")
    void onLogin_throw_whenCardIsInactive() {
        IssuedCard issued = service.issueCard(reservationById(1L), RECEPTIONIST);
        service.deactivateCard(issued.card().getNumber(), RECEPTIONIST);

        CardInactiveException error = assertThrows(CardInactiveException.class,
                () -> service.login(issued.card().getNumber(), issued.accessCode()));

        assertEquals("O cartão não está ativo.", error.getMessage());
    }

    @Test
    @Tag("H4Y-109-AC-4")
    void onLogin_throw_whenCardIsBlocked() {
        IssuedCard issued = service.issueCard(reservationById(1L), RECEPTIONIST);
        service.blockCard(issued.card().getNumber(), MANAGER);

        CardBlockedException error = assertThrows(CardBlockedException.class,
                () -> service.login(issued.card().getNumber(), issued.accessCode()));

        assertEquals("O cartão está bloqueado.", error.getMessage());
    }

    @Test
    @Tag("H4Y-109-AC-4")
    void onLogin_throw_whenNumberOrCodeIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> service.login(" ", "ABCDEF"));
        assertThrows(IllegalArgumentException.class, () -> service.login("12345678", null));
    }

    // --- places of the screen "O meu cartão" ---

    private static List<String> names(List<CardPlace> places) {
        return places.stream().map(CardPlace::name).toList();
    }

    private static List<Boolean> allowed(List<CardPlace> places) {
        return places.stream().map(CardPlace::allowed).toList();
    }

    @Test
    @Tag("H4Y-109-AC-6")
    void onListPlaces_showReservationRoomAndAllZones_whenClientCardIsCheckedIn() {
        List<CardPlace> places = service.listPlaces(clientCard());

        assertEquals(List.of("Quarto 202", "Garagem", "Bar", "Jardim", "Restaurante", "Piscina"),
                names(places));
        assertEquals(List.of(true, false, false, true, true, false), allowed(places));
        assertEquals(ROOM_202.getId(), places.get(0).room().getId());
        assertNull(places.get(0).location());
        assertEquals(RESTAURANT.getId(), places.get(4).location().getId());
        assertNull(places.get(4).room());
    }

    @Test
    @Tag("H4Y-109-AC-6")
    void onListPlaces_lockEveryPlace_whenReservationHasNoCheckIn() {
        MagneticCard card = service.issueCard(reservationById(4L), RECEPTIONIST).card();

        List<CardPlace> places = service.listPlaces(card);

        assertEquals("Quarto 202", places.get(0).name());
        assertTrue(places.stream().noneMatch(CardPlace::allowed));
    }

    @Test
    @Tag("H4Y-109-AC-6")
    void onListPlaces_allowEveryRoomAndZone_whenCardIsMaster() {
        List<CardPlace> places = service.listPlaces(masterCard());

        assertEquals(List.of("Quarto 202", "Quarto 203", "Garagem", "Bar", "Jardim", "Restaurante",
                "Piscina"), names(places));
        assertTrue(places.stream().allMatch(CardPlace::allowed));
    }

    @Test
    @Tag("H4Y-109-AC-6")
    void onListPlaces_throw_whenCardIsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.listPlaces(null));
    }

    @Test
    @Tag("H4Y-109-AC-7")
    void onEnter_recordEntry_whenPlaceIsAllowed() {
        MagneticCard card = clientCard();
        CardPlace restaurant = service.listPlaces(card).get(4);

        CardAccessRecord record = service.enter(card, restaurant);

        assertEquals(RESTAURANT.getId(), record.getLocationId());
        assertEquals(NOW, record.getEntryDateTime());
        assertEquals(1, records.findByCardId(card.getId()).size());
    }

    @Test
    @Tag("H4Y-109-AC-7")
    void onEnter_refuseAndRecordNothing_whenPlaceIsLocked() {
        MagneticCard card = clientCard();
        CardPlace pool = service.listPlaces(card).get(5);

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.enter(card, pool));

        assertEquals("Entrada recusada.", error.getMessage());
        assertTrue(records.findByCardId(card.getId()).isEmpty());
    }

    @Test
    @Tag("H4Y-109-AC-7")
    void onEnter_recordRoomEntry_whenMasterCardOpensAnyRoom() {
        MagneticCard card = masterCard();
        CardPlace room203 = service.listPlaces(card).get(1);

        CardAccessRecord record = service.enter(card, room203);

        assertEquals(ROOM_203.getId(), record.getRoomId());
    }
}
