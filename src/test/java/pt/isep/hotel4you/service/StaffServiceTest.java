package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.InMemoryUserRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

/** Tests the creation of the first manager. */
class StaffServiceTest {

    private static final String HASH =
            "$2a$12$abcdefghijklmnopqrstuuAbCdEfGhIjKlMnOpQrStUvWxYz01234";
    private static final String PASSWORD = "secret123";

    private InMemoryUserRepository repository;
    private StaffService staffService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        staffService = new StaffService(repository, new AuthService(repository));
    }

    @Test
    @Tag("H4Y-32-AC-7")
    void onCreateFirstManager_createManager_whenNoManagerExists() {
        assertTrue(staffService.createFirstManager("rui", PASSWORD));

        User manager = repository.findByUsername("rui").orElseThrow();
        assertEquals(Role.MANAGER, manager.getRole());
        assertTrue(manager.getPasswordHash().startsWith("$2a$12$"));
        assertNotEquals(PASSWORD, manager.getPasswordHash());
    }

    @Test
    @Tag("H4Y-32-AC-7")
    void onCreateFirstManager_returnFalse_whenManagerExists() {
        repository.save(new Manager(null, "Rui Costa", "rui", HASH));

        assertFalse(staffService.createFirstManager("paula", PASSWORD));
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-32-AC-7")
    void onCreateFirstManager_throwUsernameTaken_whenUsernameUsedByOtherRole() {
        repository.save(new Receptionist(null, "Rita Lopes", "rita", HASH));

        assertThrows(UsernameTakenException.class,
                () -> staffService.createFirstManager("rita", PASSWORD));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-32-AC-7")
    void onCreateFirstManager_throw_whenUsernameBlank(String username) {
        assertThrows(IllegalArgumentException.class,
                () -> staffService.createFirstManager(username, PASSWORD));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-32-AC-7")
    void onCreateFirstManager_throw_whenPasswordBlank(String password) {
        assertThrows(IllegalArgumentException.class,
                () -> staffService.createFirstManager("rui", password));
    }
}
