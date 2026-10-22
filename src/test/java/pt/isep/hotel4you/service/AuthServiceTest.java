package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.User;

/** Tests password hashing and login. */
class AuthServiceTest {

    private static final String PASSWORD = "secret123";

    private InMemoryUserRepository repository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        authService = new AuthService(repository);
    }

    private User saveAna() {
        return repository.save(
                new Receptionist(null, "Ana Silva", "ana", authService.hashPassword(PASSWORD)));
    }

    @Test
    @Tag("H4Y-32-AC-4")
    void onHashPassword_returnBcryptHash_whenPasswordValid() {
        String hash = authService.hashPassword(PASSWORD);

        assertTrue(hash.startsWith("$2a$12$"));
        assertEquals(60, hash.length());
        assertNotEquals(PASSWORD, hash);
    }

    @Test
    @Tag("H4Y-32-AC-4")
    void onHashPassword_returnDifferentHash_whenCalledTwice() {
        assertNotEquals(authService.hashPassword(PASSWORD), authService.hashPassword(PASSWORD));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-32-AC-4")
    void onHashPassword_throw_whenPasswordBlank(String password) {
        assertThrows(IllegalArgumentException.class, () -> authService.hashPassword(password));
    }

    @Test
    @Tag("H4Y-32-AC-4")
    void onLogin_returnUser_whenPasswordIsLong() {
        String longPassword = "a".repeat(100);
        User ana = repository.save(
                new Receptionist(null, "Ana Silva", "ana", authService.hashPassword(longPassword)));

        assertEquals(ana, authService.login("ana", longPassword));
    }

    @Test
    @Tag("H4Y-32-AC-4")
    void onLogin_throwLoginFailed_whenLongPasswordDiffersOnlyAtEnd() {
        String start = "a".repeat(90);
        repository.save(
                new Receptionist(null, "Ana Silva", "ana", authService.hashPassword(start + "x")));

        assertThrows(LoginFailedException.class, () -> authService.login("ana", start + "y"));
    }

    @Test
    @Tag("H4Y-32-AC-8")
    void onLogin_returnUser_whenCredentialsValid() {
        User ana = saveAna();

        assertEquals(ana, authService.login("ana", PASSWORD));
    }

    @Test
    @Tag("H4Y-32-AC-8")
    void onLogin_returnUser_whenUsernameCaseDiffers() {
        User ana = saveAna();

        assertEquals(ana, authService.login("ANA", PASSWORD));
    }

    @Test
    @Tag("H4Y-32-AC-8")
    void onLogin_returnUser_whenUsernameHasSpaces() {
        User ana = saveAna();

        assertEquals(ana, authService.login("  ana  ", PASSWORD));
    }

    @Test
    @Tag("H4Y-32-AC-8")
    void onLogin_throwLoginFailed_whenPasswordWrong() {
        saveAna();

        assertThrows(LoginFailedException.class, () -> authService.login("ana", "wrong-pass"));
    }

    @Test
    @Tag("H4Y-32-AC-8")
    void onLogin_throwLoginFailed_whenUserUnknown() {
        assertThrows(LoginFailedException.class, () -> authService.login("nobody", PASSWORD));
    }

    @Test
    @Tag("H4Y-32-AC-8")
    void onLogin_giveSameMessage_whenUserUnknownOrPasswordWrong() {
        saveAna();

        LoginFailedException unknown = assertThrows(LoginFailedException.class,
                () -> authService.login("nobody", PASSWORD));
        LoginFailedException wrong = assertThrows(LoginFailedException.class,
                () -> authService.login("ana", "wrong-pass"));

        assertEquals(unknown.getMessage(), wrong.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-32-AC-8")
    void onLogin_throw_whenUsernameBlank(String username) {
        assertThrows(IllegalArgumentException.class, () -> authService.login(username, PASSWORD));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-32-AC-8")
    void onLogin_throw_whenPasswordBlank(String password) {
        assertThrows(IllegalArgumentException.class, () -> authService.login("ana", password));
    }

    @Test
    @Tag("H4Y-32-AC-8")
    void onLogin_returnRightUser_whenSeveralUsersExist() {
        String biaPassword = "other-pass-456";
        saveAna();
        repository.save(new Receptionist(
                null, "Bia Neves", "bia", authService.hashPassword(biaPassword)));

        assertEquals("bia", authService.login("bia", biaPassword).getUsername());
        assertThrows(LoginFailedException.class, () -> authService.login("ana", biaPassword));
    }
}
