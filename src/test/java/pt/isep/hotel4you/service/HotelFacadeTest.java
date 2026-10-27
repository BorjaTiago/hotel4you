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
import pt.isep.hotel4you.domain.Permission;
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
        assertTrue(facade.canUse(user, Permission.MANAGE_STAFF));
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
    void onCanUse_returnTrue_whenManagerManagesStaff() {
        assertTrue(facade.canUse(new Manager(1L, "Rui Costa", "rui", "hash"),
                Permission.MANAGE_STAFF));
    }

    @Test
    @Tag("H4Y-32-AC-3")
    @Tag("H4Y-24-AC-3")
    @Tag("H4Y-23-AC-2")
    void onCanUse_returnFalse_whenReceptionistManagesStaff() {
        assertFalse(facade.canUse(new Receptionist(2L, "Ana Silva", "ana", "hash"),
                Permission.MANAGE_STAFF));
    }

    @Test
    @Tag("H4Y-24-AC-3")
    void onCanUse_returnFalse_whenRestaurantStaffManagesStaff() {
        assertFalse(facade.canUse(new RestaurantStaff(3L, "Joao Lima", "joao", "hash"),
                Permission.MANAGE_STAFF));
    }

    @Test
    @Tag("H4Y-24-AC-3")
    @Tag("H4Y-23-AC-3")
    void onCanUse_returnFalse_whenActorIsNull() {
        assertFalse(facade.canUse(null, Permission.MANAGE_STAFF));
    }

    @Test
    @Tag("H4Y-23-AC-1")
    void onCanUse_returnTrue_whenReceptionistListsRooms() {
        assertTrue(facade.canUse(new Receptionist(2L, "Ana Silva", "ana", "hash"),
                Permission.LIST_ROOMS));
    }

    @Test
    @Tag("H4Y-27-AC-1")
    void onCreateStaff_saveAccount_whenActorIsManager() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        facade.createStaff("Ana Silva", "ana", PASSWORD, Role.RECEPTIONIST, manager);

        assertEquals(Role.RECEPTIONIST, repository.findByUsername("ana").orElseThrow().getRole());
    }

    @Test
    @Tag("H4Y-27-AC-1")
    void onListUsers_returnCreatedAccount_whenAccountCreated() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        facade.createStaff("Ana Silva", "ana", PASSWORD, Role.RECEPTIONIST, manager);

        assertEquals("ana", facade.listUsers().get(0).getUsername());
    }

    @Test
    @Tag("H4Y-27-AC-4")
    @Tag("H4Y-23-AC-2")
    void onCreateStaff_throwAccessDenied_whenActorIsReceptionist() {
        User receptionist = new Receptionist(2L, "Ana Silva", "ana", "hash");

        assertThrows(AccessDeniedException.class, () -> facade.createStaff(
                "Bia Neves", "bia", PASSWORD, Role.RECEPTIONIST, receptionist));
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-23-AC-3")
    void onCreateStaff_throwAccessDenied_whenActorIsNull() {
        assertThrows(AccessDeniedException.class, () -> facade.createStaff(
                "Bia Neves", "bia", PASSWORD, Role.RECEPTIONIST, null));
        assertEquals(0, repository.count());
    }
}
