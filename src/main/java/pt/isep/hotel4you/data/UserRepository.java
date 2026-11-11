package pt.isep.hotel4you.data;

import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.User;

/** Stores and finds users. */
public interface UserRepository {

    /**
     * Finds all saved users.
     *
     * @return all users, or an empty list if none exists
     */
    List<User> findAll();

    /**
     * Finds a user by login name. The search ignores case.
     *
     * @param username the login name
     * @return the user, or empty if none matches
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by identifier.
     *
     * @param id the identifier
     * @return the user, or empty if none matches
     */
    Optional<User> findById(long id);

    /**
     * Checks if a user with this login name exists. The check ignores case.
     *
     * @param username the login name
     * @return true if a user with this login name exists
     */
    boolean existsByUsername(String username);

    /**
     * Saves a user.
     *
     * @param user the user to save
     * @return the saved user, with its identifier
     */
    User save(User user);

    /**
     * Changes the name, login name and role of a saved user. The password stays the same.
     *
     * @param user the user with its identifier and the new data
     * @return true if the user was changed, false if no user has this identifier
     */
    boolean update(User user);
}
