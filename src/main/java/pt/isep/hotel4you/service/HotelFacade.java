package pt.isep.hotel4you.service;

import java.util.List;
import java.util.Map;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomSummary;
import pt.isep.hotel4you.domain.User;

/** Single entry point for the GUI. */
public class HotelFacade {

    private final AuthService authService;
    private final StaffService staffService;
    private final LocationService locationService;
    private final RoomService roomService;

    /**
     * Creates the facade.
     *
     * @param authService the login service
     * @param staffService the staff service
     * @param locationService the floor and zone service
     * @param roomService the room registration service
     */
    public HotelFacade(AuthService authService, StaffService staffService,
            LocationService locationService, RoomService roomService) {
        this.authService = authService;
        this.staffService = staffService;
        this.locationService = locationService;
        this.roomService = roomService;
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
     */
    public Room saveRoom(Room room, User actor) {
        return roomService.saveRoom(room, actor);
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
     * Lists saved rooms.
     *
     * @return all saved rooms
     */
    public List<Room> listRooms() {
        return roomService.listRooms();
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
     * @return all room type names
     */
    public List<String> listRoomTypes() {
        return roomService.listRoomTypes();
    }
}
