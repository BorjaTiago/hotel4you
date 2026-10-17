package pt.isep.hotel4you.service;

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
     * Creates a staff account. The actor must be a manager.
     *
     * @param user the staff user to create
     * @param password the plain password
     * @param actor the user who asks for the account
     * @throws UnsupportedOperationException always, until H4Y-27
     */
    public void createStaff(User user, String password, User actor) {
        staffService.createStaff(user, password, actor);
    }
}
