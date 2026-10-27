package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.User;

/** Keeps users in memory. Unit tests use it instead of the database. */
public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public List<User> findAll() {
        return List.copyOf(users);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    @Override
    public User save(User user) {
        User saved = user.getId() == null ? withId(user, users.size() + 1L) : user;
        users.add(saved);
        return saved;
    }

    /**
     * Returns how many users are stored.
     *
     * @return the number of users
     */
    public int count() {
        return users.size();
    }

    private static User withId(User user, long id) {
        return switch (user) {
            case Manager m -> new Manager(id, m.getName(), m.getUsername(), m.getPasswordHash());
            case Receptionist r ->
                    new Receptionist(id, r.getName(), r.getUsername(), r.getPasswordHash());
            case RestaurantStaff s ->
                    new RestaurantStaff(id, s.getName(), s.getUsername(), s.getPasswordHash());
            default -> throw new IllegalArgumentException("Unknown user type");
        };
    }
}
