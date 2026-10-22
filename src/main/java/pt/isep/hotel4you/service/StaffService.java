package pt.isep.hotel4you.service;

import pt.isep.hotel4you.data.UserRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

/** Creates staff accounts. */
public class StaffService {

    private final UserRepository userRepository;
    private final AuthService authService;

    /**
     * Creates the service.
     *
     * @param userRepository the user store
     * @param authService the service that hashes passwords
     */
    public StaffService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    /**
     * Creates the first manager if no manager exists. The manager name is the username.
     *
     * @param username the login name, not blank
     * @param password the plain password, not blank
     * @return true if the manager was created, false if a manager already exists
     * @throws IllegalArgumentException if the username or password is blank
     * @throws UsernameTakenException if the username is already in use
     */
    public boolean createFirstManager(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Fill in username and password.");
        }
        String trimmed = username.trim();
        if (userRepository.existsByRole(Role.MANAGER)) {
            return false;
        }
        if (userRepository.existsByUsername(trimmed)) {
            throw new UsernameTakenException();
        }
        userRepository.save(new Manager(null, trimmed, trimmed, authService.hashPassword(password)));
        return true;
    }

    /**
     * Tells if the user may create staff accounts. Only a manager may. The GUI also uses this
     * method to show or hide the create-staff button.
     *
     * @param actor the logged-in user, can be null
     * @return true if the actor is a manager
     */
    public boolean canCreateStaff(User actor) {
        return actor != null && actor.getRole() == Role.MANAGER;
    }

    /**
     * Creates a staff account. The rules come with H4Y-27.
     *
     * @param name the full name of the new user
     * @param username the login name of the new user
     * @param password the plain password of the new user
     * @param role the role of the new user
     * @param actor the logged-in user who asks for the account
     * @throws UnsupportedOperationException always, until H4Y-27
     */
    public void createStaff(String name, String username, String password, Role role, User actor) {
        throw new UnsupportedOperationException("Not implemented yet. Comes with H4Y-27.");
    }
}
