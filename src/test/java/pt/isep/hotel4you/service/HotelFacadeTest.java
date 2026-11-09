package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.ClientExistsException;
import pt.isep.hotel4you.data.InMemoryClientRepository;
import pt.isep.hotel4you.data.InMemoryFloorRepository;
import pt.isep.hotel4you.data.InMemoryItemRepository;
import pt.isep.hotel4you.data.InMemoryLocationRepository;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.InMemoryRoomTypeRepository;
import pt.isep.hotel4you.data.InMemoryUserRepository;
import pt.isep.hotel4you.data.RoomNotAvailableException;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomSummary;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.User;

/** Tests that the facade passes calls to the services. */
class HotelFacadeTest {

    private static final String PASSWORD = "secret123";
    private static final String LOGIN_FAILED_MESSAGE = "Credenciais inválidas";

    private InMemoryUserRepository repository;
    private AuthService authService;
    private InMemoryFloorRepository floorRepository;
    private InMemoryLocationRepository locationRepository;
    private InMemoryRoomRepository roomRepository;
    private InMemoryRoomTypeRepository roomTypeRepository;
    private InMemoryItemRepository itemRepository;
    private HotelFacade facade;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        authService = new AuthService(repository);
        StaffService staffService = new StaffService(repository, authService);
        floorRepository = new InMemoryFloorRepository();
        locationRepository = new InMemoryLocationRepository();
        LocationService locationService = new LocationService(floorRepository, locationRepository);
        roomRepository = new InMemoryRoomRepository();
        roomTypeRepository = new InMemoryRoomTypeRepository();
        pt.isep.hotel4you.data.InMemoryReservationRepository reservationRepository =
                new pt.isep.hotel4you.data.InMemoryReservationRepository();
        RoomService roomService = new RoomService(roomRepository, roomTypeRepository,
                reservationRepository);
        itemRepository = new InMemoryItemRepository();
        StockService stockService = new StockService(itemRepository);
        ClientService clientService = new ClientService(new InMemoryClientRepository());
        ReservationService reservationService = new ReservationService(reservationRepository,
                roomService, roomTypeRepository);
        facade = new HotelFacade(authService, staffService, locationService, roomService,
                stockService, clientService, reservationService);
    }

    private User saveAna() {
        return repository.save(
                new Receptionist(null, "Ana Silva", "ana", authService.hashPassword(PASSWORD)));
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onListRooms_passActorToService_whenReadingRooms() {
        assertEquals(List.of(), facade.listRooms(new Manager(1L, "Rui", "rui", "hash")));
        assertThrows(AccessDeniedException.class,
                () -> facade.listRooms(new Receptionist(2L, "Ana", "ana", "hash")));
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onListRoomTypes_passActorToService_whenReadingCatalogue() {
        assertEquals(List.of(), facade.listRoomTypes(new Manager(1L, "Rui", "rui", "hash")));
        assertThrows(AccessDeniedException.class, () -> facade.listRoomTypes(null));
    }

    @Test
    @Tag("H4Y-25-AC-1")
    void onCanUse_allowRooms_whenActorIsManager() {
        assertTrue(facade.canUse(new Manager(1L, "Rui", "rui", "hash"), Permission.MANAGE_ROOMS));
        assertFalse(facade.canUse(new Receptionist(2L, "Ana", "ana", "hash"), Permission.MANAGE_ROOMS));
        assertEquals(Map.of(2, 10, 3, 10), facade.roomFloorLimits());
    }

    @Test
    @Tag("H4Y-50-AC-2")
    void onListRoomSummaries_returnRoomsFromRoomService_whenReceptionistFiltersByType() {
        roomRepository.addFloor(3);
        roomRepository.addRoomType("familiar");
        roomRepository.addRoomType("duplo");
        roomRepository.setBasePrice("familiar", new BigDecimal("180.00"));
        roomRepository.save(new Room(null, 301, 3, "familiar"), 10);
        roomRepository.save(new Room(null, 302, 3, "duplo"), 10);
        User ana = new Receptionist(2L, "Ana", "ana", "hash");

        assertTrue(facade.canUse(ana, Permission.LIST_ROOMS));
        assertEquals(List.of(new RoomSummary(1L, 301, 3, "Quartos", "familiar", new BigDecimal("180.00"))),
                facade.listRoomSummaries("familiar", ana));
    }

    @Test
    @Tag("H4Y-25-AC-5")
    void onEditRoom_changeRoomThroughRoomService_whenManagerEdits() {
        roomRepository.addFloor(2);
        roomRepository.addFloor(3);
        roomRepository.addRoomType("duplo");
        roomRepository.addRoomType("familiar");
        Room saved = roomRepository.save(new Room(null, 202, 2, "duplo"), 10);
        User rui = new Manager(1L, "Rui", "rui", "hash");

        Room edited = facade.editRoom(new Room(saved.getId(), 305, 3, "familiar"), rui);

        assertEquals(saved.getId(), edited.getId());
        assertEquals(305, edited.getNumber());
        assertEquals(3, edited.getFloorNumber());
        assertEquals("familiar", edited.getRoomTypeName());
        assertEquals(305, roomRepository.findAll().getFirst().getNumber());
    }

    @Test
    @Tag("H4Y-25-AC-10")
    void onRemoveRoom_deleteRoomThroughRoomService_whenManagerRemoves() {
        roomRepository.addFloor(2);
        roomRepository.addRoomType("duplo");
        Room saved = roomRepository.save(new Room(null, 202, 2, "duplo"), 10);
        User rui = new Manager(1L, "Rui", "rui", "hash");

        facade.removeRoom(saved.getId(), rui);

        assertTrue(roomRepository.findAll().isEmpty());
    }

    @Test
    @Tag("H4Y-50-AC-4")
    void onListRoomSummaries_throwAccessDenied_whenActorIsNotLoggedIn() {
        assertThrows(AccessDeniedException.class, () -> facade.listRoomSummaries(null, null));
    }

    @Test
    @Tag("H4Y-32-AC-3")
    void onLogin_returnUser_whenCredentialsValid() {
        User ana = saveAna();

        assertEquals(ana, facade.login("ana", PASSWORD));
    }

    private User saveRui() {
        return repository.save(
                new Manager(null, "Rui Costa", "rui", authService.hashPassword(PASSWORD)));
    }

    @Test
    @Tag("H4Y-20-AC-1")
    void onLogin_returnManager_whenManagerCredentialsValid() {
        User rui = saveRui();

        User user = facade.login("rui", PASSWORD);

        assertEquals(rui, user);
        assertEquals(Role.MANAGER, user.getRole());
        assertTrue(facade.canUse(user, Permission.MANAGE_STAFF));
    }

    @Test
    @Tag("H4Y-20-AC-2")
    void onLogin_throwLoginFailed_whenManagerPasswordWrong() {
        saveRui();

        LoginFailedException error = assertThrows(LoginFailedException.class,
                () -> facade.login("rui", "wrong-pass"));

        assertEquals(LOGIN_FAILED_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-20-AC-3")
    void onLogin_throwLoginFailed_whenUsernameUnknown() {
        saveRui();

        LoginFailedException error = assertThrows(LoginFailedException.class,
                () -> facade.login("nobody", PASSWORD));

        assertEquals(LOGIN_FAILED_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-32-AC-3")
    void onLogin_throwLoginFailed_whenPasswordWrong() {
        saveAna();

        assertThrows(LoginFailedException.class, () -> facade.login("ana", "wrong-pass"));
    }

    @Test
    @Tag("H4Y-32-AC-3")
    @Tag("H4Y-24-AC-3")
    void onCanUse_returnTrue_whenManagerManagesStaff() {
        assertTrue(facade.canUse(new Manager(1L, "Rui Costa", "rui", "hash"),
                Permission.MANAGE_STAFF));
    }

    @Test
    @Tag("H4Y-32-AC-3")
    @Tag("H4Y-24-AC-3")
    @Tag("H4Y-23-AC-2")
    void onCanUse_returnFalse_whenReceptionistManagesStaff() {
        assertFalse(facade.canUse(new Receptionist(2L, "Ana Silva", "ana", "hash"),
                Permission.MANAGE_STAFF));
    }

    @Test
    @Tag("H4Y-24-AC-3")
    void onCanUse_returnFalse_whenRestaurantStaffManagesStaff() {
        assertFalse(facade.canUse(new RestaurantStaff(3L, "Joao Lima", "joao", "hash"),
                Permission.MANAGE_STAFF));
    }

    @Test
    @Tag("H4Y-24-AC-3")
    @Tag("H4Y-23-AC-3")
    void onCanUse_returnFalse_whenActorIsNull() {
        assertFalse(facade.canUse(null, Permission.MANAGE_STAFF));
    }

    @Test
    @Tag("H4Y-27-AC-1")
    void onCreateStaff_saveAccount_whenActorIsManager() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        facade.createStaff("Ana Silva", "ana", PASSWORD, Role.RECEPTIONIST, manager);

        assertEquals(Role.RECEPTIONIST, repository.findByUsername("ana").orElseThrow().getRole());
    }

    @Test
    @Tag("H4Y-27-AC-1")
    void onListUsers_returnCreatedAccount_whenAccountCreated() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        facade.createStaff("Ana Silva", "ana", PASSWORD, Role.RECEPTIONIST, manager);

        assertEquals("ana", facade.listUsers().get(0).getUsername());
    }

    @Test
    @Tag("H4Y-27-AC-4")
    @Tag("H4Y-23-AC-2")
    void onCreateStaff_throwAccessDenied_whenActorIsReceptionist() {
        User receptionist = new Receptionist(2L, "Ana Silva", "ana", "hash");

        assertThrows(AccessDeniedException.class, () -> facade.createStaff(
                "Bia Neves", "bia", PASSWORD, Role.RECEPTIONIST, receptionist));
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-23-AC-3")
    void onCreateStaff_throwAccessDenied_whenActorIsNull() {
        assertThrows(AccessDeniedException.class, () -> facade.createStaff(
                "Bia Neves", "bia", PASSWORD, Role.RECEPTIONIST, null));
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-28-AC-1")
    void onListFloors_returnFloors_whenManager() {
        floorRepository.add(new Floor(1, "Piso 1", List.of(new Location(null, "Piscina", false, false))));
        floorRepository.add(new Floor(0, "Piso 0", List.of()));
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        List<Floor> floors = facade.listFloors(manager);

        assertEquals(List.of(0, 1), floors.stream().map(Floor::number).toList());
        assertEquals("Piscina", floors.get(1).locations().get(0).getName());
    }

    @Test
    @Tag("H4Y-28-AC-2")
    void onFindFloor_throwFloorNotFound_whenFloorMissing() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        FloorNotFoundException error = assertThrows(FloorNotFoundException.class,
                () -> facade.findFloor(4, manager));

        assertEquals("O piso não existe.", error.getMessage());
    }

    @Test
    @Tag("H4Y-28-AC-1")
    void onCanUse_returnTrue_whenManagerManagesFloors() {
        assertTrue(facade.canUse(new Manager(1L, "Rui Costa", "rui", "hash"), Permission.MANAGE_FLOORS));
    }

    @Test
    @Tag("H4Y-28-AC-1")
    void onCanUse_returnFalse_whenReceptionistManagesFloors() {
        assertFalse(facade.canUse(new Receptionist(2L, "Ana Silva", "ana", "hash"), Permission.MANAGE_FLOORS));
    }

    @Test
    @Tag("H4Y-28-AC-3")
    void onCreateLocation_returnZone_whenManager() {
        floorRepository.add(new Floor(1, "Piso 1", List.of()));
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        Location zone = facade.createLocation(1, "Spa", true, true, manager);

        assertEquals("Spa", zone.getName());
        assertTrue(zone.isRequiresReservation());
        assertEquals(zone.getId(), locationRepository.findById(zone.getId()).get().getId());
    }

    @Test
    @Tag("H4Y-28-AC-11")
    void onEditLocation_throwLocationNotFound_whenZoneMissing() {
        floorRepository.add(new Floor(1, "Piso 1", List.of()));
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        LocationNotFoundException error = assertThrows(LocationNotFoundException.class,
                () -> facade.editLocation(99L, 1, "Spa", false, false, manager));

        assertEquals("A zona não existe.", error.getMessage());
    }

    @Test
    @Tag("H4Y-28-AC-14")
    void onRemoveLocation_throwInUse_whenZoneHasAssociations() {
        floorRepository.add(new Floor(1, "Piso 1", List.of()));
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");
        Location zone = facade.createLocation(1, "Spa", false, false, manager);
        locationRepository.markInUse(zone.getId());

        LocationInUseException error = assertThrows(LocationInUseException.class,
                () -> facade.removeLocation(zone.getId(), manager));

        assertEquals("A zona tem associações e não pode ser removida.", error.getMessage());
    }

    @Test
    @Tag("H4Y-26-AC-1")
    void onSetBasePrice_savePrice_whenManager() {
        RoomType duplo = new RoomType(2L, "duplo", 2, null);
        roomTypeRepository.add(duplo);
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        facade.setBasePrice(duplo, new BigDecimal("80.00"), manager);

        assertEquals(new BigDecimal("80.00"), facade.listBasePrices(manager).get(0).getBasePrice());
    }

    @Test
    @Tag("H4Y-26-AC-4")
    void onListBasePrices_throwAccessDenied_whenReceptionist() {
        User receptionist = new Receptionist(2L, "Ana Silva", "ana", "hash");

        assertThrows(AccessDeniedException.class, () -> facade.listBasePrices(receptionist));
    }

    @Test
    @Tag("H4Y-26-AC-3")
    void onParsePrice_returnPrice_whenTextHasComma() {
        assertEquals(new BigDecimal("80.50"), facade.parsePrice("80,50"));
    }

    @Test
    @Tag("H4Y-51-AC-1")
    void onCreateItem_returnItemWithZeroStock_whenManager() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        StockItem item = facade.createItem("toalhas", "unidade", manager);

        assertEquals("toalhas", item.getName());
        assertEquals(0, item.getStockQuantity());
        assertEquals(1, itemRepository.count());
    }

    @Test
    @Tag("H4Y-51-AC-5")
    void onEditItem_changeItemThroughStockService_whenManagerEdits() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");
        StockItem item = facade.createItem("toalhas", "unidade", manager);

        facade.editItem(new StockItem(item.getId(), "lençóis", "caixa", 0), manager);

        assertEquals("lençóis", itemRepository.findById(item.getId()).get().getName());
    }

    @Test
    @Tag("H4Y-51-AC-1")
    void onListItems_returnItems_whenManager() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");
        facade.createItem("toalhas", "unidade", manager);

        assertEquals(1, facade.listItems(manager).size());
    }

    @Test
    @Tag("H4Y-51-AC-4")
    void onListItems_throwAccessDenied_whenReceptionist() {
        User receptionist = new Receptionist(2L, "Ana Silva", "ana", "hash");

        assertThrows(AccessDeniedException.class, () -> facade.listItems(receptionist));
    }

    @Test
    @Tag("H4Y-51-AC-4")
    void onCanUse_returnFalse_whenReceptionistManagesStock() {
        assertFalse(facade.canUse(new Receptionist(2L, "Ana Silva", "ana", "hash"), Permission.MANAGE_STOCK));
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onCanUse_returnTrue_whenReceptionistManagesClients() {
        assertTrue(facade.canUse(new Receptionist(2L, "Ana Silva", "ana", "hash"), Permission.MANAGE_CLIENTS));
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onCanUse_returnTrue_whenManagerManagesClients() {
        assertTrue(facade.canUse(new Manager(1L, "Rui", "rui", "hash"), Permission.MANAGE_CLIENTS));
    }

    private static Client newClient(String name, String document) {
        return new Client(null, name, DocumentType.CC, document, LocalDate.of(1990, 5, 17),
                "912 345 678", "PT", null, null);
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onRegisterClient_saveClient_whenReceptionist() {
        User receptionist = new Receptionist(2L, "Ana Silva", "ana", "hash");

        Client client = facade.registerClient(newClient("Maria Santos", "12345678"), receptionist);

        assertEquals("Maria Santos", client.getName());
    }

    @Test
    @Tag("H4Y-52-AC-3")
    void onRegisterClient_throw_whenDocumentExists() {
        User receptionist = new Receptionist(2L, "Ana Silva", "ana", "hash");
        facade.registerClient(newClient("Maria Santos", "12345678"), receptionist);

        assertThrows(ClientExistsException.class,
                () -> facade.registerClient(newClient("Outra Pessoa", "12345678"), receptionist));
    }

    @Test
    @Tag("H4Y-52-AC-4")
    void onListClients_returnClients_whenManager() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");
        facade.registerClient(newClient("Maria Santos", "12345678"), manager);

        assertEquals(1, facade.listClients(manager).size());
    }

    @Test
    @Tag("H4Y-52-AC-4")
    void onSearchClients_returnMatches_whenReceptionistSearches() {
        User receptionist = new Receptionist(2L, "Ana Silva", "ana", "hash");
        facade.registerClient(newClient("Maria Santos", "12345678"), receptionist);
        facade.registerClient(newClient("João Pereira", "87654321"), receptionist);

        List<Client> clients = facade.searchClients("pereira", receptionist);

        assertEquals(1, clients.size());
        assertEquals("João Pereira", clients.get(0).getName());
    }

    @Test
    @Tag("H4Y-52-AC-5")
    void onEditClient_changeClientThroughClientService_whenReceptionistEdits() {
        User receptionist = new Receptionist(2L, "Ana Silva", "ana", "hash");
        Client saved = facade.registerClient(newClient("Maria Santos", "12345678"), receptionist);
        Client edited = new Client(saved.getId(), "Maria Silva", DocumentType.CC, "12345678",
                LocalDate.of(1990, 5, 17), "912 345 678", "PT", null, null);

        facade.editClient(edited, receptionist);

        assertEquals("Maria Silva", facade.searchClients("12345678", receptionist).get(0).getName());
    }

    @Test
    @Tag("H4Y-52-AC-6")
    void onRemoveClient_deleteClientThroughClientService_whenManagerRemoves() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");
        Client saved = facade.registerClient(newClient("Maria Santos", "12345678"), manager);

        facade.removeClient(saved.getId(), manager);

        assertTrue(facade.listClients(manager).isEmpty());
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onListClients_throwAccessDenied_whenRestaurantStaff() {
        User cook = new RestaurantStaff(3L, "Joao Lima", "joao", "hash");

        assertThrows(AccessDeniedException.class, () -> facade.listClients(cook));
    }

    @Test
    @Tag("H4Y-56-AC-1")
    @Tag("H4Y-56-AC-4")
    void onCreateReservation_saveAndQuote_whenFacadeReceivesFreeRoom() {
        roomRepository.addFloor(2);
        roomRepository.addRoomType("duplo");
        Room room = roomRepository.save(new Room(null, 201, 2, "duplo"), 10);
        roomTypeRepository.add(new RoomType(1L, "duplo", 2, new BigDecimal("80.00")));
        User ana = new Receptionist(4L, "Ana Silva", "ana", "hash");
        Client client = new Client(7L, "Maria Sousa", DocumentType.CC, "12345678",
                LocalDate.of(1990, 5, 1), "912345678", "PT", null, null);
        LocalDate start = LocalDate.now().plusDays(10);
        LocalDate end = start.plusDays(2);

        var quote = facade.quoteReservation(room, start, end, ana);
        var saved = facade.createReservation(client, room, start, end, ana);

        assertEquals(new BigDecimal("80.00"), quote.pricePerNight());
        assertEquals(2, quote.nights());
        assertEquals(1, facade.listClientReservations(client, ana).size());
        assertEquals(saved.getId(), facade.listClientReservations(client, ana).get(0).id());
        assertThrows(RoomNotAvailableException.class,
                () -> facade.createReservation(client, room, start, end, ana));
    }

    @Test
    @Tag("H4Y-82-AC-12")
    @Tag("H4Y-82-AC-13")
    void onCheckInAndCheckOut_updateActiveList_whenFacadeReceivesReservation() {
        roomRepository.addFloor(2);
        roomRepository.addRoomType("duplo");
        Room room = roomRepository.save(new Room(null, 201, 2, "duplo"), 10);
        User ana = new Receptionist(4L, "Ana Silva", "ana", "hash");
        Client client = new Client(7L, "Maria Sousa", DocumentType.CC, "12345678",
                LocalDate.of(1990, 5, 1), "912345678", "PT", null, null);
        LocalDate start = LocalDate.now();
        var saved = facade.createReservation(client, room, start, start.plusDays(2), ana);

        facade.checkIn(saved.getId(), ana);
        var afterCheckIn = facade.listActiveReservations(ana);
        facade.checkOut(saved.getId(), ana);

        assertEquals(1, afterCheckIn.size());
        assertEquals(ReservationStatus.CHECKED_IN, afterCheckIn.get(0).status());
        assertTrue(facade.listActiveReservations(ana).isEmpty());
    }
}
