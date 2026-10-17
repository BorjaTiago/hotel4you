package pt.isep.hotel4you.domain;

import java.util.Objects;

/** Base class for all system users that can log in. */
public abstract class User {

    private final Long id;
    private final String name;
    private final String username;
    private final String passwordHash;
    private final Role role;

    /**
     * Creates a user.
     *
     * @param id the identifier, or null if the user is not saved yet
     * @param name the full name, not blank
     * @param username the login name, not blank; spaces at the start and end are removed
     * @param passwordHash the hash of the password, not blank
     * @param role the role, not null
     * @throws IllegalArgumentException if name, username or passwordHash is blank
     * @throws NullPointerException if role is null
     */
    protected User(Long id, String name, String username, String passwordHash, Role role) {
        this.id = id;
        this.name = requireNotBlank(name, "name");
        this.username = requireNotBlank(username, "username").trim();
        this.passwordHash = requireNotBlank(passwordHash, "passwordHash");
        this.role = Objects.requireNonNull(role, "role must not be null");
    }

    private static String requireNotBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }

    /**
     * Returns the identifier.
     *
     * @return the identifier, or null if the user is not saved yet
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the full name.
     *
     * @return the full name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the login name.
     *
     * @return the login name
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password hash.
     *
     * @return the password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Returns the role.
     *
     * @return the role
     */
    public Role getRole() {
        return role;
    }
}
