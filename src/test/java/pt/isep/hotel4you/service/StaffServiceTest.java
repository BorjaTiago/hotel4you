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

/** Tests the creation, list and edit of staff accounts. */
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
    @Tag("H4Y-23-AC-2")
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
    @Tag("H4Y-23-AC-3")
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
    @Tag("H4Y-85-AC-1")
    void onEditStaff_saveNewRole_whenActorIsManager() {
        long id = saveAna();

        staffService.editStaff(id, "Ana Silva", "ana", Role.RESTAURANT, manager());

        User ana = repository.findById(id).orElseThrow();
        assertTrue(ana instanceof RestaurantStaff);
        assertEquals(Role.RESTAURANT, ana.getRole());
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-85-AC-1")
    void onEditStaff_loginWithNewRoleAndSamePassword_whenRoleChanged() {
        long id = saveAna();

        staffService.editStaff(id, "Ana Silva", "ana", Role.RESTAURANT, manager());

        assertEquals(Role.RESTAURANT, authService.login("ana", PASSWORD).getRole());
    }

    @Test
    @Tag("H4Y-85-AC-1")
    void onEditStaff_saveTrimmedNameAndUsername_whenTheyChange() {
        long id = saveAna();

        staffService.editStaff(id, "  Ana Costa  ", "  anac  ", Role.RECEPTIONIST, manager());

        User ana = repository.findById(id).orElseThrow();
        assertEquals("Ana Costa", ana.getName());
        assertEquals("anac", ana.getUsername());
        assertTrue(repository.findByUsername("ana").isEmpty());
    }

    @Test
    @Tag("H4Y-85-AC-1")
    void onEditStaff_saveAccount_whenUsernameOnlyChangesCase() {
        long id = saveAna();

        staffService.editStaff(id, "Ana Silva", "Ana", Role.RECEPTIONIST, manager());

        assertEquals("Ana", repository.findById(id).orElseThrow().getUsername());
    }

    @Test
    @Tag("H4Y-85-AC-1")
    void onEditStaff_throw_whenNameBlank() {
        long id = saveAna();

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> staffService.editStaff(id, "  ", "ana", Role.RESTAURANT, manager()));

        assertEquals("Preencha todos os campos.", error.getMessage());
        assertAnaUnchanged(id);
    }

    @Test
    @Tag("H4Y-85-AC-1")
    void onEditStaff_throw_whenUsernameLongerThan50() {
        long id = saveAna();

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> staffService.editStaff(
                        id, "Ana Silva", "a".repeat(51), Role.RESTAURANT, manager()));

        assertEquals("O nome de utilizador tem no máximo 50 caracteres.", error.getMessage());
        assertAnaUnchanged(id);
    }

    @Test
    @Tag("H4Y-85-AC-1")
    void onEditStaff_throw_whenRoleIsClient() {
        long id = saveAna();

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> staffService.editStaff(id, "Ana Silva", "ana", Role.CLIENT, manager()));

        assertEquals("Este tipo de conta não é permitido.", error.getMessage());
        assertAnaUnchanged(id);
    }

    @Test
    @Tag("H4Y-85-AC-2")
    void onEditStaff_throwUsernameTaken_whenUsernameOfOtherAccount() {
        long id = saveAna();
        staffService.createStaff("Rui Lima", "rui", PASSWORD, Role.MANAGER, manager());

        assertThrows(UsernameTakenException.class,
                () -> staffService.editStaff(id, "Ana Silva", "rui", Role.RESTAURANT, manager()));

        assertAnaUnchanged(id);
    }

    @Test
    @Tag("H4Y-85-AC-2")
    void onEditStaff_throwUsernameTaken_whenUsernameOfOtherAccountDiffersOnlyInCase() {
        long id = saveAna();
        staffService.createStaff("Rui Lima", "rui", PASSWORD, Role.MANAGER, manager());

        assertThrows(UsernameTakenException.class,
                () -> staffService.editStaff(id, "Ana Silva", "RUI", Role.RESTAURANT, manager()));

        assertAnaUnchanged(id);
    }

    @Test
    @Tag("H4Y-85-AC-3")
    void onEditStaff_throwStaffNotFound_whenAccountMissing() {
        StaffNotFoundException error = assertThrows(StaffNotFoundException.class,
                () -> staffService.editStaff(99L, "Zé Lopes", "ze", Role.RECEPTIONIST, manager()));

        assertEquals("A conta não existe.", error.getMessage());
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-85-AC-4")
    void onEditStaff_throwAccessDenied_whenActorIsReceptionist() {
        assertEditDenied(new Receptionist(2L, "Bia Neves", "bia", HASH));
    }

    @Test
    @Tag("H4Y-85-AC-4")
    void onEditStaff_throwAccessDenied_whenActorIsRestaurantStaff() {
        assertEditDenied(new RestaurantStaff(3L, "Joao Lima", "joao", HASH));
    }

    @Test
    @Tag("H4Y-85-AC-4")
    void onEditStaff_throwAccessDenied_whenActorIsNull() {
        assertEditDenied(null);
    }

    private void assertEditDenied(User actor) {
        long id = saveAna();

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> staffService.editStaff(id, "Ana Silva", "ana", Role.RESTAURANT, actor));

        assertEquals("Só o gestor pode editar contas.", error.getMessage());
        assertAnaUnchanged(id);
    }

    private void assertAnaUnchanged(long id) {
        User ana = repository.findById(id).orElseThrow();
        assertEquals("Ana Silva", ana.getName());
        assertEquals("ana", ana.getUsername());
        assertEquals(Role.RECEPTIONIST, ana.getRole());
    }

    private long saveAna() {
        staffService.createStaff("Ana Silva", "ana", PASSWORD, Role.RECEPTIONIST, manager());
        return repository.findByUsername("ana").orElseThrow().getId();
    }
}
