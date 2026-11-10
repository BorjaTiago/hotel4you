package pt.isep.hotel4you.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import pt.isep.hotel4you.data.ClientExistsException;
import pt.isep.hotel4you.data.ClientInUseException;
import pt.isep.hotel4you.data.DuplicateRoomException;
import pt.isep.hotel4you.data.RoomLimitExceededException;
import pt.isep.hotel4you.data.RoomNotAvailableException;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationQuote;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomSummary;
import pt.isep.hotel4you.domain.RoomSearchResult;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.User;

/** Single entry point for the GUI. */
public class HotelFacade {

    private final AuthService authService;
    private final StaffService staffService;
    private final LocationService locationService;
    private final RoomService roomService;
    private final StockService stockService;
    private final ClientService clientService;
    private final ReservationService reservationService;

    /**
     * Creates the facade.
     *
     * @param authService the login service
     * @param staffService the staff service
     * @param locationService the floor and zone service
     * @param roomService the room registration service
     * @param stockService the stock item service
     * @param clientService the client registration service
     * @param reservationService the reservation service
     */
    public HotelFacade(AuthService authService, StaffService staffService,
            LocationService locationService, RoomService roomService, StockService stockService,
            ClientService clientService, ReservationService reservationService) {
        this.authService = authService;
        this.staffService = staffService;
        this.locationService = locationService;
        this.roomService = roomService;
        this.stockService = stockService;
        this.clientService = clientService;
        this.reservationService = reservationService;
    }

    /**
     * Creates a reservation of a free room for a client.
     *
     * @param client the saved client
     * @param room the room to reserve
     * @param start the first night
     * @param end the departure date
     * @param actor the logged-in user who creates the reservation
     * @return the saved reservation with the state RESERVED
     * @throws AccessDeniedException if the actor may not manage reservations
     * @throws IllegalArgumentException if data is missing (the message names all missing fields),
     *         the departure is not after the arrival, or the arrival is before today
     * @throws RoomNotAvailableException if the room has a reservation in the nights
     * @throws IllegalStateException if the database is not available
     */
    public Reservation createReservation(Client client, Room room, LocalDate start, LocalDate end,
            User actor) {
        return reservationService.create(client, room, start, end, actor);
    }

    /**
     * Gives the base price per night of the room type and the number of nights.
     *
     * @param room the chosen room
     * @param start the first night
     * @param end the departure date
     * @param actor the logged-in user who asks
     * @return the price summary; the price is null if the type has no base price yet
     * @throws AccessDeniedException if the actor may not manage reservations
     * @throws IllegalArgumentException if data is missing or the departure is not after the arrival
     * @throws IllegalStateException if the database is not available
     */
    public ReservationQuote quoteReservation(Room room, LocalDate start, LocalDate end, User actor) {
        return reservationService.quote(room, start, end, actor);
    }

    /**
     * Lists the reservations of a client, with the cancelled ones.
     *
     * @param client the saved client
     * @param actor the logged-in user who asks
     * @return the reservations, newest arrival first
     * @throws AccessDeniedException if the actor may not manage reservations
     * @throws IllegalArgumentException if the client is missing
     * @throws IllegalStateException if the database is not available
     */
    public List<ClientReservation> listClientReservations(Client client, User actor) {
        return reservationService.listClientReservations(client, actor);
    }

    /**
     * Does the check-in of a reservation.
     *
     * @param reservationId the reservation identifier
     * @param actor the logged-in user who does the check-in
     * @throws AccessDeniedException if the actor may not manage reservations
     * @throws ReservationNotFoundException if the reservation does not exist
     * @throws IllegalStateException if the state does not allow the check-in or the database fails
     */
    public void checkIn(long reservationId, User actor) {
        reservationService.checkIn(reservationId, actor);
    }

    /**
     * Does the check-out of a reservation.
     *
     * @param reservationId the reservation identifier
     * @param actor the logged-in user who does the check-out
     * @throws AccessDeniedException if the actor may not manage reservations
     * @throws ReservationNotFoundException if the reservation does not exist
     * @throws IllegalStateException if the state does not allow the check-out or the database fails
     */
    public void checkOut(long reservationId, User actor) {
        reservationService.checkOut(reservationId, actor);
    }

    /**
     * Lists the active reservations of the hotel: the states RESERVED and CHECKED_IN.
     *
     * @param actor the logged-in user who asks
     * @return the reservations with the room number, first arrival first
     * @throws AccessDeniedException if the actor may not manage reservations
     * @throws IllegalStateException if the database is not available
     */
    public List<ClientReservation> listActiveReservations(User actor) {
        return reservationService.listActive(actor);
    }

    /**
     * Registers a new client.
     *
     * @param client the new client data
     * @param actor the logged-in user who asks
     * @return the saved client with its id
     * @throws AccessDeniedException if the actor is not a receptionist or a manager
     * @throws IllegalArgumentException if a field is missing or not valid; the message names the field
     * @throws ClientExistsException if a client with this document type and number already exists
     * @throws IllegalStateException if the database is not available
     */
    public Client registerClient(Client client, User actor) {
        return clientService.register(client, actor);
    }

    /**
     * Lists all clients.
     *
     * @param actor the logged-in user who asks
     * @return the clients ordered by name
     * @throws AccessDeniedException if the actor is not a receptionist or a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<Client> listClients(User actor) {
        return clientService.listClients(actor);
    }

    /**
     * Finds the clients whose name or document number contains the text.
     *
     * @param text the text to find; a blank text lists all clients
     * @param actor the logged-in user who asks
     * @return the matching clients ordered by name
     * @throws AccessDeniedException if the actor is not a receptionist or a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<Client> searchClients(String text, User actor) {
        return clientService.searchClients(text, actor);
    }

    /**
     * Changes the data of a saved client.
     *
     * @param client the client with its id and the new data
     * @param actor the logged-in user who asks
     * @throws AccessDeniedException if the actor is not a receptionist or a manager
     * @throws ClientNotFoundException if the client does not exist
     * @throws IllegalArgumentException if a field is missing or not valid; the message names the field
     * @throws ClientExistsException if another client has this document type and number
     * @throws IllegalStateException if the database is not available
     */
    public void editClient(Client client, User actor) {
        clientService.editClient(client, actor);
    }

    /**
     * Removes a client.
     *
     * @param id the client id
     * @param actor the logged-in user who asks
     * @throws AccessDeniedException if the actor is not a receptionist or a manager
     * @throws ClientNotFoundException if the client does not exist
     * @throws ClientInUseException if the client has reservations
     * @throws IllegalStateException if the database is not available
     */
    public void removeClient(long id, User actor) {
        clientService.removeClient(id, actor);
    }

    /**
     * Logs a user in.
     *
     * @param username the login name
     * @param password the plain password
     * @return the user who logged in
     * @throws IllegalArgumentException if the username or password is blank
     * @throws LoginFailedException if the user is unknown or the password is wrong
     */
    public User login(String username, String password) {
        return authService.login(username, password);
    }

    /**
     * Tells if the user may use a function. The GUI uses it to show or hide a menu button.
     *
     * @param actor the logged-in user, can be null
     * @param permission the function to check
     * @return true if the role of the actor may use the function
     */
    public boolean canUse(User actor, Permission permission) {
        return permission.allows(actor);
    }

    /**
     * Finds free rooms of one catalogue type for the requested nights.
     * @param start the arrival
     * @param end the departure
     * @param type the selected catalogue type
     * @param actor the signed-in user
     * @return the free rooms, or an empty list
     * @throws AccessDeniedException if the actor cannot search
     * @throws IllegalArgumentException if the dates or type are invalid
     * @throws IllegalStateException if the database cannot be read
     */
    public List<Room> searchRooms(LocalDate start, LocalDate end, RoomType type, User actor) {
        return roomService.searchRooms(start, end, type, actor);
    }

    /**
     * Supplies the free rooms and the service's no-room message to a screen.
     * @param start the arrival
     * @param end the departure
     * @param type the selected catalogue type
     * @param actor the signed-in user
     * @return the search result and optional message
     * @throws AccessDeniedException if the actor cannot search
     * @throws IllegalArgumentException if the dates or type are invalid
     * @throws IllegalStateException if the database cannot be read
     */
    public RoomSearchResult searchRoomsWithMessage(LocalDate start, LocalDate end,
            RoomType type, User actor) {
        return roomService.searchRoomsWithMessage(start, end, type, actor);
    }

    /**
     * Lists types and prices for availability search.
     * @param actor the signed-in user
     * @return the catalogue types
     * @throws AccessDeniedException if the actor cannot search
     * @throws IllegalStateException if the database cannot be read
     */
    public List<RoomType> listSearchRoomTypes(User actor) {
        return roomService.listSearchRoomTypes(actor);
    }

    /**
     * Creates a staff account.
     *
     * @param name the full name of the new user
     * @param username the login name of the new user
     * @param password the plain password of the new user
     * @param role the role of the new user
     * @param actor the logged-in user who asks for the account
     */
    public void createStaff(String name, String username, String password, Role role, User actor) {
        staffService.createStaff(name, username, password, role, actor);
    }

    /**
     * Changes the name, login name and role of a staff account. The password stays the same.
     *
     * @param id the identifier of the account
     * @param name the new full name
     * @param username the new login name
     * @param role the new role
     * @param actor the logged-in user who asks for the change
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if a field is blank, too long, or the role has no account
     * @throws StaffNotFoundException if the account does not exist
     * @throws UsernameTakenException if another account uses the username
     * @throws IllegalStateException if the database is not available
     */
    public void editStaff(long id, String name, String username, Role role, User actor) {
        staffService.editStaff(id, name, username, role, actor);
    }

    /**
     * Lists all staff accounts.
     *
     * @return the accounts, or an empty list if none exists
     * @throws IllegalStateException if the database is not available
     */
    public List<User> listUsers() {
        return staffService.listUsers();
    }

    /**
     * Lists all floors with their zones.
     *
     * @param actor the logged-in user who asks
     * @return the floors ordered by number
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<Floor> listFloors(User actor) {
        return locationService.listFloors(actor);
    }

    /**
     * Finds one floor with its zones.
     *
     * @param number the floor number
     * @param actor the logged-in user who asks
     * @return the floor
     * @throws AccessDeniedException if the actor is not a manager
     * @throws FloorNotFoundException if the floor does not exist
     * @throws IllegalStateException if the database is not available
     */
    public Floor findFloor(int number, User actor) {
        return locationService.findFloor(number, actor);
    }

    /**
     * Creates a zone on a floor.
     *
     * @param floorNumber the floor number
     * @param name the zone name
     * @param guestCardAccess true if the guest card opens the zone
     * @param requiresReservation true if the zone needs a reservation
     * @param actor the logged-in user who asks
     * @return the new zone with its id
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the name is blank or too long
     * @throws FloorNotFoundException if the floor does not exist
     * @throws LocationNameTakenException if the floor already has a zone with this name
     * @throws IllegalStateException if the database is not available
     */
    public Location createLocation(int floorNumber, String name, boolean guestCardAccess,
            boolean requiresReservation, User actor) {
        return locationService.createLocation(floorNumber, name, guestCardAccess, requiresReservation, actor);
    }

    /**
     * Changes a zone.
     *
     * @param id the zone id
     * @param floorNumber the new floor number
     * @param name the new zone name
     * @param guestCardAccess true if the guest card opens the zone
     * @param requiresReservation true if the zone needs a reservation
     * @param actor the logged-in user who asks
     * @return the changed zone
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the name is blank or too long
     * @throws FloorNotFoundException if the floor does not exist
     * @throws LocationNotFoundException if the zone does not exist
     * @throws LocationNameTakenException if the floor has another zone with this name
     * @throws IllegalStateException if the database is not available
     */
    public Location editLocation(long id, int floorNumber, String name, boolean guestCardAccess,
            boolean requiresReservation, User actor) {
        return locationService.editLocation(id, floorNumber, name, guestCardAccess, requiresReservation, actor);
    }

    /**
     * Removes a zone.
     *
     * @param id the zone id
     * @param actor the logged-in user who asks
     * @throws AccessDeniedException if the actor is not a manager
     * @throws LocationNotFoundException if the zone does not exist
     * @throws LocationInUseException if other data still uses the zone
     * @throws IllegalStateException if the database is not available
     */
    public void removeLocation(long id, User actor) {
        locationService.removeLocation(id, actor);
    }

    /**
     * Saves a room.
     *
     * @param room the room to save
     * @param actor the logged-in user
     * @return the saved room
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the room data, floor or room type is invalid
     * @throws DuplicateRoomException if the room number is already in use
     * @throws RoomLimitExceededException if the floor already has 10 rooms
     * @throws IllegalStateException if the database is not available
     */
    public Room saveRoom(Room room, User actor) {
        return roomService.saveRoom(room, actor);
    }

    /**
     * Edits a room.
     *
     * @param room the room to edit, with its identifier
     * @param actor the logged-in user
     * @return the saved room
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the room, floor or room type is invalid
     * @throws DuplicateRoomException if the room number is already in use
     * @throws RoomLimitExceededException if the floor already has 10 rooms
     * @throws IllegalStateException if the database is not available
     */
    public Room editRoom(Room room, User actor) {
        return roomService.editRoom(room, actor);
    }

    /**
     * Removes a room.
     *
     * @param id the room id
     * @param actor the logged-in user
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the room does not exist
     * @throws RoomInUseException if other data still uses the room
     * @throws IllegalStateException if the database is not available
     */
    public void removeRoom(long id, User actor) {
        roomService.removeRoom(id, actor);
    }

    /**
     * Returns the room floors and limits from the statement.
     *
     * @return an immutable map from floor number to maximum room count
     */
    public Map<Integer, Integer> roomFloorLimits() {
        return roomService.roomFloorLimits();
    }

    /**
     * Returns the labels and provisional room type totals for the summary.
     *
     * @return immutable display targets, not registration limits
     */
    public List<RoomService.RoomTypeTarget> roomTypeTargets() {
        return roomService.roomTypeTargets();
    }

    /**
     * Lists saved rooms.
     *
     * @param actor the logged-in user
     * @return all saved rooms
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<Room> listRooms(User actor) {
        return roomService.listRooms(actor);
    }

    /**
     * Lists the rooms with type, floor and base price.
     *
     * @param roomTypeName the room type to show, or null or blank to show all rooms
     * @param actor the logged-in user
     * @return the rooms ordered by number, or an empty list if no room matches
     * @throws AccessDeniedException if the actor is not logged in or may not see the list
     * @throws IllegalStateException if the database is not available
     */
    public List<RoomSummary> listRoomSummaries(String roomTypeName, User actor) {
        return roomService.listRoomSummaries(roomTypeName, actor);
    }

    /**
     * Lists the room types from the catalogue.
     *
     * @param actor the logged-in user
     * @return all room type names
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<String> listRoomTypes(User actor) {
        return roomService.listRoomTypes(actor);
    }
    /**
     * Lists the room types with their base price.
     *
     * @param actor the logged-in user who asks
     * @return the room types ordered by id
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<RoomType> listBasePrices(User actor) {
        return roomService.listBasePrices(actor);
    }

    /**
     * Sets the base price per night of a room type.
     *
     * @param type the room type
     * @param price the new price
     * @param actor the logged-in user who asks
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the type or the price is not valid, or the type does not exist
     * @throws IllegalStateException if the database is not available
     */
    public void setBasePrice(RoomType type, BigDecimal price, User actor) {
        roomService.setBasePrice(type, price, actor);
    }

    /**
     * Converts the text of the screen to a price.
     *
     * @param text the text typed by the user
     * @return the price
     * @throws IllegalArgumentException if the text is null, blank or not a number
     */
    public BigDecimal parsePrice(String text) {
        return roomService.parsePrice(text);
    }

    /**
     * Creates a stock item with zero stock.
     *
     * @param name the item name
     * @param unit the unit of measure
     * @param actor the logged-in user who asks
     * @return the new item with its id
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the name or the unit is blank or too long
     * @throws StockItemNameTakenException if another item has this name
     * @throws IllegalStateException if the database is not available
     */
    public StockItem createItem(String name, String unit, User actor) {
        return stockService.createItem(name, unit, actor);
    }

    /**
     * Changes the name and the unit of a stock item.
     *
     * @param item the item with its id and the new name and unit
     * @param actor the logged-in user who asks
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the name or the unit is blank or too long
     * @throws StockItemNotFoundException if the item does not exist
     * @throws StockItemNameTakenException if another item has this name
     * @throws IllegalStateException if the database is not available
     */
    public void editItem(StockItem item, User actor) {
        stockService.editItem(item, actor);
    }

    /**
     * Lists all stock items.
     *
     * @param actor the logged-in user who asks
     * @return the items ordered by name
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<StockItem> listItems(User actor) {
        return stockService.listItems(actor);
    }
}
