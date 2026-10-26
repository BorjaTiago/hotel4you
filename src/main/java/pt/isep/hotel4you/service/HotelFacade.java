package pt.isep.hotel4you.service;

import java.util.List;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

/** Single entry point for the GUI. */
public class HotelFacade {

    private final AuthService authService;
    private final StaffService staffService;
    private final LocationService locationService;

    /**
     * Creates the facade.
     *
     * @param authService the login service
     * @param staffService the staff service
     * @param locationService the floor and zone service
     */
    public HotelFacade(AuthService authService, StaffService staffService,
            LocationService locationService) {
        this.authService = authService;
        this.staffService = staffService;
        this.locationService = locationService;
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
     * Tells if the user may create staff accounts.
     *
     * @param actor the logged-in user, can be null
     * @return true if the actor is a manager
     */
    public boolean canCreateStaff(User actor) {
        return staffService.canCreateStaff(actor);
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
     * Tells if the user may view the floors.
     *
     * @param actor the logged-in user, can be null
     * @return true if the actor is a manager
     */
    public boolean canViewFloors(User actor) {
        return locationService.canViewFloors(actor);
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
}
