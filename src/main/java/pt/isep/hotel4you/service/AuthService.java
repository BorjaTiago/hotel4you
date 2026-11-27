package pt.isep.hotel4you.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;
import pt.isep.hotel4you.data.UserRepository;
import pt.isep.hotel4you.domain.User;

/** Hashes passwords and logs users in. */
public class AuthService {

    private static final int COST = 12;
    private static final BCrypt.Version VERSION = BCrypt.Version.VERSION_2A;

    private final UserRepository userRepository;

    /**
     * Creates the service.
     *
     * @param userRepository the user store
     */
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Hashes a password with bcrypt.
     *
     * @param password the plain password, not blank
     * @return the bcrypt hash
     * @throws IllegalArgumentException if the password is blank
     */
    public String hashPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("A palavra-passe não pode estar vazia.");
        }
        return BCrypt.with(VERSION, LongPasswordStrategies.hashSha512(VERSION))
                .hashToString(COST, password.toCharArray());
    }

    /**
     * Checks the credentials and returns the user.
     *
     * @param username the login name
     * @param password the plain password
     * @return the user who logged in
     * @throws IllegalArgumentException if the username or password is blank
     * @throws LoginFailedException if the user is unknown or the password is wrong
     */
    public User login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Preencha o nome de utilizador e a palavra-passe.");
        }
        User user = userRepository.findByUsername(username.trim())
                .orElseThrow(LoginFailedException::new);
        if (!matchesPassword(password, user.getPasswordHash())) {
            throw new LoginFailedException();
        }
        return user;
    }

    /**
     * Checks a plain secret against a hash made by {@link #hashPassword(String)}. The card code
     * uses the same hash as the passwords.
     *
     * @param password the plain password or card code
     * @param passwordHash the stored hash
     * @return true if the secret matches the hash; false if not, or if a value is null
     */
    public boolean matchesPassword(String password, String passwordHash) {
        if (password == null || passwordHash == null) {
            return false;
        }
        return BCrypt.verifyer(VERSION, LongPasswordStrategies.hashSha512(VERSION))
                .verify(password.toCharArray(), passwordHash)
                .verified;
    }
}
