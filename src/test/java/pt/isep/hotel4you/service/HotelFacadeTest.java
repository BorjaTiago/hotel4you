package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryUserRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

/** Tests that the facade passes calls to the services. */
class HotelFacadeTest {

    private static final String PASSWORD = "secret123";
    private static final String LOGIN_FAILED_MESSAGE = "Credenciais inválidas";

    private InMemoryUserRepository repository;
    private AuthService authService;
    private HotelFacade facade;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        authService = new AuthService(repository);
        StaffService staffService = new StaffService(repository, authService);
        facade = new HotelFacade(authService, staffService);
    }

    private User saveAna() {
        return repository.save(
                new Receptionist(null, "Ana Silva", "ana", authService.hashPassword(PASSWORD)));
    }

    @Test
    @Tag("H4Y-32-AC-3")
    void onLogin_returnUser_whenCredentialsValid() {
        User ana = saveAna();

        assertEquals(ana, facade.login("ana", PASSWORD));
    }

    private User saveRui() {
        return repository.save(
                new Manager(null, "Rui Costa", "rui", authService.hashPassword(PASSWORD)));
    }

    @Test
    @Tag("H4Y-20-AC-1")
    void onLogin_returnManager_whenManagerCredentialsValid() {
        User rui = saveRui();

        User user = facade.login("rui", PASSWORD);

        assertEquals(rui, user);
        assertEquals(Role.MANAGER, user.getRole());
        assertTrue(facade.canCreateStaff(user));
    }

    @Test
    @Tag("H4Y-20-AC-2")
    void onLogin_throwLoginFailed_whenManagerPasswordWrong() {
        saveRui();

        LoginFailedException error = assertThrows(LoginFailedException.class,
                () -> facade.login("rui", "wrong-pass"));

        assertEquals(LOGIN_FAILED_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-20-AC-3")
    void onLogin_throwLoginFailed_whenUsernameUnknown() {
        saveRui();

        LoginFailedException error = assertThrows(LoginFailedException.class,
                () -> facade.login("nobody", PASSWORD));

        assertEquals(LOGIN_FAILED_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-32-AC-3")
    void onLogin_throwLoginFailed_whenPasswordWrong() {
        saveAna();

        assertThrows(LoginFailedException.class, () -> facade.login("ana", "wrong-pass"));
    }

    @Test
    @Tag("H4Y-32-AC-3")
    @Tag("H4Y-24-AC-3")
    void onCanCreateStaff_returnTrue_whenActorIsManager() {
        assertTrue(facade.canCreateStaff(new Manager(1L, "Rui Costa", "rui", "hash")));
    }

    @Test
    @Tag("H4Y-32-AC-3")
    @Tag("H4Y-24-AC-3")
    void onCanCreateStaff_returnFalse_whenActorIsReceptionist() {
        assertFalse(facade.canCreateStaff(new Receptionist(2L, "Ana Silva", "ana", "hash")));
    }

    @Test
    @Tag("H4Y-24-AC-3")
    void onCanCreateStaff_returnFalse_whenActorIsRestaurantStaff() {
        assertFalse(facade.canCreateStaff(new RestaurantStaff(3L, "Joao Lima", "joao", "hash")));
    }

    @Test
    @Tag("H4Y-24-AC-3")
    void onCanCreateStaff_returnFalse_whenActorIsNull() {
        assertFalse(facade.canCreateStaff(null));
    }

    @Test
    @Tag("H4Y-32-AC-3")
    void onCreateStaff_throwUnsupported_always() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        assertThrows(UnsupportedOperationException.class,
                () -> facade.createStaff("Ana Silva", "ana", PASSWORD, Role.RECEPTIONIST, manager));
    }
}
