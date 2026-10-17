package pt.isep.hotel4you.domain;

/** A restaurant employee. */
public class RestaurantStaff extends User {

    /**
     * Creates a restaurant staff member.
     *
     * @param id the identifier, or null if not saved yet
     * @param name the full name, not blank
     * @param username the login name, not blank
     * @param passwordHash the hash of the password, not blank
     */
    public RestaurantStaff(Long id, String name, String username, String passwordHash) {
        super(id, name, username, passwordHash, Role.RESTAURANT);
    }
}
