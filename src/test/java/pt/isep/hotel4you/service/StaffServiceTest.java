package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.InMemoryUserRepository;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;

/** Tests the creation of the first manager and of staff accounts. */
class StaffServiceTest {

    private static final String HASH =
            "$2a$12$abcdefghijklmnopqrstuuAbCdEfGhIjKlMnOpQrStUvWxYz01234";
    private static final String PASSWORD = "secret123";

    private InMemoryUserRepository repository;
    private AuthService authService;
    private StaffService staffService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        authService = new AuthService(repository);
        staffService = new StaffService(repository, authService);
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

    @Test
    @Tag("H4Y-32-AC-7")
    void onCreateFirstManager_createManagerThatCanLogIn_whenNoManagerExists() {
        staffService.createFirstManager("rui", PASSWORD);

        assertEquals(Role.MANAGER, authService.login("rui", PASSWORD).getRole());
    }

    @Test
    @Tag("H4Y-32-AC-7")
    void onCreateFirstManager_trimUsername_whenUsernameHasSpaces() {
        staffService.createFirstManager("  rui  ", PASSWORD);

        User manager = repository.findByUsername("rui").orElseThrow();
        assertEquals("rui", manager.getUsername());
        assertEquals("rui", manager.getName());
    }

    @Test
    @Tag("H4Y-32-AC-7")
    void onCreateFirstManager_throw_whenManagerExistsAndUsernameBlank() {
        repository.save(new Manager(null, "Rui Costa", "rui", HASH));

        assertThrows(IllegalArgumentException.class,
                () -> staffService.createFirstManager("   ", PASSWORD));
    }

    @Test
    @Tag("H4Y-32-AC-7")
    void onCreateFirstManager_throwUsernameTaken_whenUsernameDiffersOnlyInCase() {
        repository.save(new Receptionist(null, "Rita Lopes", "rita", HASH));

        assertThrows(UsernameTakenException.class,
                () -> staffService.createFirstManager("RITA", PASSWORD));
        assertEquals(1, repository.count());
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

    @Test
    @Tag("H4Y-32-AC-3")
    @Tag("H4Y-24-AC-3")
    void onCanCreateStaff_returnTrue_whenActorIsManager() {
        assertTrue(staffService.canCreateStaff(new Manager(1L, "Rui Costa", "rui", HASH)));
    }

    @Test
    @Tag("H4Y-32-AC-3")
    @Tag("H4Y-24-AC-3")
    @Tag("H4Y-27-AC-4")
    void onCanCreateStaff_returnFalse_whenActorIsReceptionist() {
        assertFalse(staffService.canCreateStaff(new Receptionist(2L, "Ana Silva", "ana", HASH)));
    }

    @Test
    @Tag("H4Y-32-AC-3")
    @Tag("H4Y-24-AC-3")
    void onCanCreateStaff_returnFalse_whenActorIsNull() {
        assertFalse(staffService.canCreateStaff(null));
    }

    private User manager() {
        return new Manager(1L, "Rui Costa", "rui", HASH);
    }

    @Test
    @Tag("H4Y-27-AC-1")
    void onCreateStaff_saveReceptionistWithHash_whenActorIsManager() {
        staffService.createStaff("Ana Silva", "ana", PASSWORD, Role.RECEPTIONIST, manager());

        User ana = repository.findByUsername("ana").orElseThrow();
        assertEquals("Ana Silva", ana.getName());
        assertEquals(Role.RECEPTIONIST, ana.getRole());
        assertTrue(ana.getPasswordHash().startsWith("$2a$12$"));
        assertNotEquals(PASSWORD, ana.getPasswordHash());
    }

    @Test
    @Tag("H4Y-27-AC-1")
    void onCreateStaff_saveAccountThatCanLogIn_whenDataValid() {
        staffService.createStaff("Ana Silva", "ana", PASSWORD, Role.RECEPTIONIST, manager());

        assertEquals("ana", authService.login("ana", PASSWORD).getUsername());
    }

    @Test
    @Tag("H4Y-27-AC-1")
    void onCreateStaff_trimNameAndUsername_whenTheyHaveSpaces() {
        staffService.createStaff("  Ana Silva  ", "  ana  ", PASSWORD, Role.RECEPTIONIST,
                manager());

        User ana = repository.findByUsername("ana").orElseThrow();
        assertEquals("Ana Silva", ana.getName());
        assertEquals("ana", ana.getUsername());
    }

    @Test
    @Tag("H4Y-27-AC-2")
    void onCreateStaff_throwUsernameTaken_whenUsernameExists() {
        repository.save(new Receptionist(null, "Ana Silva", "ana", HASH));

        assertThrows(UsernameTakenException.class, () -> staffService.createStaff(
                "Ana Costa", "ana", PASSWORD, Role.RESTAURANT, manager()));
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-27-AC-2")
    void onCreateStaff_throwUsernameTaken_whenUsernameDiffersOnlyInCase() {
        repository.save(new Receptionist(null, "Ana Silva", "ana", HASH));

        assertThrows(UsernameTakenException.class, () -> staffService.createStaff(
                "Ana Costa", "ANA", PASSWORD, Role.RESTAURANT, manager()));
        assertEquals(1, repository.count());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-27-AC-3")
    void onCreateStaff_throw_whenNameBlank(String name) {
        assertFieldsMissing(() -> staffService.createStaff(
                name, "ana", PASSWORD, Role.RECEPTIONIST, manager()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-27-AC-3")
    void onCreateStaff_throw_whenUsernameBlank(String username) {
        assertFieldsMissing(() -> staffService.createStaff(
                "Ana Silva", username, PASSWORD, Role.RECEPTIONIST, manager()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-27-AC-3")
    void onCreateStaff_throw_whenPasswordBlank(String password) {
        assertFieldsMissing(() -> staffService.createStaff(
                "Ana Silva", "ana", password, Role.RECEPTIONIST, manager()));
    }

    @Test
    @Tag("H4Y-27-AC-3")
    void onCreateStaff_throw_whenRoleNull() {
        assertFieldsMissing(() -> staffService.createStaff(
                "Ana Silva", "ana", PASSWORD, null, manager()));
    }

    private void assertFieldsMissing(Executable call) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, call);

        assertEquals("Preencha todos os campos.", error.getMessage());
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-27-AC-3")
    void onCreateStaff_throw_whenUsernameLongerThan50() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> staffService.createStaff(
                        "Ana Silva", "a".repeat(51), PASSWORD, Role.RECEPTIONIST, manager()));

        assertEquals("O nome de utilizador tem no máximo 50 caracteres.", error.getMessage());
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-27-AC-3")
    void onCreateStaff_throw_whenNameLongerThan100() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> staffService.createStaff(
                        "a".repeat(101), "ana", PASSWORD, Role.RECEPTIONIST, manager()));

        assertEquals("O nome tem no máximo 100 caracteres.", error.getMessage());
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-27-AC-3")
    void onCreateStaff_saveAccount_whenLengthsAtLimit() {
        staffService.createStaff(
                "a".repeat(100), "b".repeat(50), PASSWORD, Role.RECEPTIONIST, manager());

        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-27-AC-4")
    void onCreateStaff_throwAccessDenied_whenActorIsReceptionist() {
        assertAccessDenied(new Receptionist(2L, "Ana Silva", "ana", HASH));
    }

    @Test
    @Tag("H4Y-27-AC-4")
    void onCreateStaff_throwAccessDenied_whenActorIsRestaurantStaff() {
        assertAccessDenied(new RestaurantStaff(3L, "Joao Lima", "joao", HASH));
    }

    @Test
    @Tag("H4Y-27-AC-4")
    void onCreateStaff_throwAccessDenied_whenActorIsNull() {
        assertAccessDenied(null);
    }

    private void assertAccessDenied(User actor) {
        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> staffService.createStaff(
                        "Bia Neves", "bia", PASSWORD, Role.RECEPTIONIST, actor));

        assertEquals("Só o gestor pode criar contas.", error.getMessage());
        assertEquals(0, repository.count());
    }

    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"RECEPTIONIST", "RESTAURANT", "MANAGER"})
    @Tag("H4Y-27-AC-5")
    void onCreateStaff_saveAccount_whenRoleIsStaffRole(Role role) {
        staffService.createStaff("Ana Silva", "ana", PASSWORD, role, manager());

        assertEquals(role, repository.findByUsername("ana").orElseThrow().getRole());
    }

    @Test
    @Tag("H4Y-27-AC-1")
    @Tag("H4Y-27-AC-5")
    void onCreateStaff_saveRestaurantStaff_whenRoleRestaurant() {
        staffService.createStaff("Joao Lima", "joao", PASSWORD, Role.RESTAURANT, manager());

        User joao = repository.findByUsername("joao").orElseThrow();
        assertTrue(joao instanceof RestaurantStaff);
        assertEquals(Role.RESTAURANT, joao.getRole());
    }

    @Test
    @Tag("H4Y-27-AC-5")
    void onCreateStaff_throw_whenRoleIsClient() {
        assertRoleRefused(Role.CLIENT);
    }

    @Test
    @Tag("H4Y-27-AC-5")
    void onCreateStaff_throw_whenRoleIsSupplier() {
        assertRoleRefused(Role.SUPPLIER);
    }

    private void assertRoleRefused(Role role) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> staffService.createStaff("Ana Silva", "ana", PASSWORD, role, manager()));

        assertEquals("Este tipo de conta não é permitido.", error.getMessage());
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-27-AC-1")
    void onListUsers_returnCreatedAccounts_whenAccountsExist() {
        staffService.createStaff("Ana Silva", "ana", PASSWORD, Role.RECEPTIONIST, manager());
        staffService.createStaff("Joao Lima", "joao", PASSWORD, Role.RESTAURANT, manager());

        List<User> users = staffService.listUsers();

        assertEquals(List.of("ana", "joao"), users.stream().map(User::getUsername).toList());
        assertEquals(List.of(Role.RECEPTIONIST, Role.RESTAURANT),
                users.stream().map(User::getRole).toList());
    }

    @Test
    @Tag("H4Y-27-AC-1")
    void onListUsers_returnEmptyList_whenNoAccountExists() {
        assertTrue(staffService.listUsers().isEmpty());
    }

    @Test
    @Tag("H4Y-32-AC-7")
    void onCreateFirstManager_returnFalse_whenManagerExistsAndUsernameTaken() {
        repository.save(new Manager(null, "Rui Costa", "rui", HASH));

        assertFalse(staffService.createFirstManager("rui", PASSWORD));
    }
}
