package pt.isep.hotel4you.service;

import java.util.List;
import pt.isep.hotel4you.data.UserRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

/** Creates staff accounts. */
public class StaffService {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_USERNAME_LENGTH = 50;

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
     * Lists all staff accounts: receptionists, restaurant staff and managers.
     *
     * @return the accounts, or an empty list if none exists
     */
    public List<User> listUsers() {
        return userRepository.findAll();
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
     * Creates a staff account. Only a manager may do it. The name and the username are trimmed.
     * The password is saved as a bcrypt hash.
     *
     * @param name the full name of the new user, at most 100 characters
     * @param username the login name of the new user, at most 50 characters
     * @param password the plain password of the new user
     * @param role the role of the new user: receptionist, restaurant staff or manager
     * @param actor the logged-in user who asks for the account
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if a field is blank, too long, or the role has no account
     * @throws UsernameTakenException if the username is already in use
     */
    public void createStaff(String name, String username, String password, Role role, User actor) {
        if (!canCreateStaff(actor)) {
            throw new AccessDeniedException("Só o gestor pode criar contas.");
        }
        if (name == null || name.isBlank() || username == null || username.isBlank()
                || password == null || password.isBlank() || role == null) {
            throw new IllegalArgumentException("Preencha todos os campos.");
        }
        String trimmedName = name.trim();
        String trimmedUsername = username.trim();
        if (trimmedName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("O nome tem no máximo 100 caracteres.");
        }
        if (trimmedUsername.length() > MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException("O nome de utilizador tem no máximo 50 caracteres.");
        }
        if (role != Role.RECEPTIONIST && role != Role.RESTAURANT && role != Role.MANAGER) {
            throw new IllegalArgumentException("Este tipo de conta não é permitido.");
        }
        if (userRepository.existsByUsername(trimmedUsername)) {
            throw new UsernameTakenException();
        }
        String passwordHash = authService.hashPassword(password);
        userRepository.save(switch (role) {
            case RECEPTIONIST -> new Receptionist(null, trimmedName, trimmedUsername, passwordHash);
            case RESTAURANT -> new RestaurantStaff(null, trimmedName, trimmedUsername, passwordHash);
            default -> new Manager(null, trimmedName, trimmedUsername, passwordHash);
        });
    }
}
