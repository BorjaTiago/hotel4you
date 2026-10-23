package pt.isep.hotel4you.service;

import java.util.List;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

/** Single entry point for the GUI. */
public class HotelFacade {

    private final AuthService authService;
    private final StaffService staffService;

    /**
     * Creates the facade.
     *
     * @param authService the login service
     * @param staffService the staff service
     */
    public HotelFacade(AuthService authService, StaffService staffService) {
        this.authService = authService;
        this.staffService = staffService;
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
}
