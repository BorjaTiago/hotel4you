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
     * Creates a staff account. The actor must be a manager. The rules come with H4Y-27.
     *
     * @param user the staff user to create
     * @param password the plain password
     * @param actor the user who asks for the account
     * @throws UnsupportedOperationException always, until H4Y-27
     */
    public void createStaff(User user, String password, User actor) {
        throw new UnsupportedOperationException("Not implemented yet. Comes with H4Y-27.");
    }
}
