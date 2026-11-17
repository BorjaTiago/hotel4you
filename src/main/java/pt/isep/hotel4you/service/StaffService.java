package pt.isep.hotel4you.service;

import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.data.UserRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

/** Creates, lists, finds and edits staff accounts. */
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
     * Lists all staff accounts: receptionists, restaurant staff and managers. Only a manager may
     * do it.
     *
     * @param actor the logged-in user who asks for the list
     * @return the accounts, or an empty list if none exists
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<User> listUsers(User actor) {
        checkCanConsult(actor);
        return userRepository.findAll();
    }

    /**
     * Finds one staff account by its login name. Only a manager may do it. The username is
     * trimmed.
     *
     * @param username the login name of the account
     * @param actor the logged-in user who asks for the account
     * @return the account
     * @throws AccessDeniedException if the actor is not a manager
     * @throws StaffNotFoundException if the username is blank or no account uses it
     * @throws IllegalStateException if the database is not available
     */
    public User findUser(String username, User actor) {
        checkCanConsult(actor);
        if (username == null || username.isBlank()) {
            throw new StaffNotFoundException();
        }
        return userRepository.findByUsername(username.trim())
                .orElseThrow(StaffNotFoundException::new);
    }

    /** Checks that the actor is a manager before a list or a lookup. */
    private static void checkCanConsult(User actor) {
        if (!Permission.MANAGE_STAFF.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode consultar contas.");
        }
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
        if (!Permission.MANAGE_STAFF.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode criar contas.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Preencha todos os campos.");
        }
        checkFields(name, username, role);
        String trimmedName = name.trim();
        String trimmedUsername = username.trim();
        if (userRepository.existsByUsername(trimmedUsername)) {
            throw new UsernameTakenException();
        }
        String passwordHash = authService.hashPassword(password);
        userRepository.save(newUser(null, trimmedName, trimmedUsername, passwordHash, role));
    }

    /**
     * Changes the name, login name and role of a staff account. Only a manager may do it. The
     * password stays the same. The new role applies at the next login. The name and the
     * username are trimmed.
     *
     * @param id the identifier of the account
     * @param name the new full name, at most 100 characters
     * @param username the new login name, at most 50 characters
     * @param role the new role: receptionist, restaurant staff or manager
     * @param actor the logged-in user who asks for the change
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if a field is blank, too long, or the role has no account
     * @throws StaffNotFoundException if the account does not exist
     * @throws UsernameTakenException if another account uses the username
     * @throws IllegalStateException if the database is not available
     */
    public void editStaff(long id, String name, String username, Role role, User actor) {
        if (!Permission.MANAGE_STAFF.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode editar contas.");
        }
        checkFields(name, username, role);
        String trimmedName = name.trim();
        String trimmedUsername = username.trim();
        User current = userRepository.findById(id).orElseThrow(StaffNotFoundException::new);
        Optional<User> owner = userRepository.findByUsername(trimmedUsername);
        boolean takenByOther = owner.isPresent() && owner.get().getId() != id;
        if (takenByOther) {
            throw new UsernameTakenException();
        }
        // The account can be removed between findById and update.
        if (!userRepository.update(newUser(id, trimmedName, trimmedUsername,
                current.getPasswordHash(), role))) {
            throw new StaffNotFoundException();
        }
    }

    /** Checks the name, the username and the role of an account. */
    private static void checkFields(String name, String username, Role role) {
        if (name == null || name.isBlank() || username == null || username.isBlank()
                || role == null) {
            throw new IllegalArgumentException("Preencha todos os campos.");
        }
        if (name.trim().length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("O nome tem no máximo 100 caracteres.");
        }
        if (username.trim().length() > MAX_USERNAME_LENGTH) {
            throw new IllegalArgumentException("O nome de utilizador tem no máximo 50 caracteres.");
        }
        if (role != Role.RECEPTIONIST && role != Role.RESTAURANT && role != Role.MANAGER) {
            throw new IllegalArgumentException("Este tipo de conta não é permitido.");
        }
    }

    private static User newUser(Long id, String name, String username, String passwordHash,
            Role role) {
        return switch (role) {
            case RECEPTIONIST -> new Receptionist(id, name, username, passwordHash);
            case RESTAURANT -> new RestaurantStaff(id, name, username, passwordHash);
            default -> new Manager(id, name, username, passwordHash);
        };
    }
}
