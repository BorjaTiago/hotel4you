
package pt.isep.hotel4you.service;

import pt.isep.hotel4you.data.UserRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

import java.util.List;

/**
 * Creates staff accounts.
 */
public class StaffService {

    private final UserRepository userRepository;
    private final AuthService authService;

    /**
     * Creates the service.
     *
     * @param userRepository the user store
     * @param authService    the service that hashes passwords
     */
    public StaffService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Creates the first manager if no manager exists. The manager name is the username.
     *
     * @param username the login name, not blank
     * @param password the plain password, not blank
     * @return true if the manager was created, false if a manager already exists
     * @throws IllegalArgumentException if the username or password is blank
     * @throws UsernameTakenException   if the username is already in use
     */
    public boolean createFirstManager(String username, String password) {
        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Fill in username and password.");
        }

        String trimmed = username.trim();

        if (userRepository.existsByRole(Role.MANAGER)) {
            return false;
        }

        if (userRepository.existsByUsername(trimmed)) {
            throw new UsernameTakenException();
        }

        userRepository.save(
                new Manager(null, trimmed, trimmed, authService.hashPassword(password)));

        return true;
    }

    /**
     * Tells whether the user may create staff accounts.
     *
     * @param actor the logged-in user, can be null
     * @return true if the actor is a manager, false otherwise
     */
    public boolean canCreateStaff(User actor) {
        return actor != null && actor.getRole() == Role.MANAGER;
    }

    /**
     * Creates a staff account.
     *
     * @param name     the full name of the new user
     * @param username the login name of the new user
     * @param password the plain password of the new user
     * @param role     the role of the new user
     * @param actor    the logged-in user requesting the account
     * @throws RuntimeException         if the actor is not a manager
     * @throws IllegalArgumentException if a field is blank or a name exceeds its maximum length
     * @throws UsernameTakenException   if the username is already in use
     */
    public void createStaff(
            String name, String username, String password, Role role, User actor) {

        if (!canCreateStaff(actor)) {
            throw new RuntimeException("Criar funcionário não permitodo");
        }

        if (name == null || name.isBlank()
                || username == null || username.isBlank()
                || password == null || password.isBlank()
                || role == null) {
            throw new IllegalArgumentException("Fill in all fields.");
        }

        name = name.trim();
        username = username.trim();

        if (name.length() > 100 || username.length() > 50) {
            throw new IllegalArgumentException("Fill in all fields.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new UsernameTakenException();
        }

        String passwordHash = authService.hashPassword(password);

        User staff = switch (role) {
            case RECEPTIONIST -> new Receptionist(null, name, username, passwordHash);
            case RESTAURANT -> new RestaurantStaff(null, name, username, passwordHash);
            case MANAGER -> new Manager(null, name, username, passwordHash);
            default -> throw new IllegalArgumentException("Permissão inválida.");
        };

        userRepository.save(staff);
    }
}