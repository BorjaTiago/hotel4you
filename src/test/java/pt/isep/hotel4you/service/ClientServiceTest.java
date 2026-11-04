package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.ClientExistsException;
import pt.isep.hotel4you.data.InMemoryClientRepository;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.User;

/** Tests that the receptionist can register a new client. */
class ClientServiceTest {

    private static final String NAME = "Maria Santos";
    private static final String DOCUMENT = "12345678";

    private static final String DENIED_MESSAGE = "Só o rececionista pode registar clientes.";
    private static final String NAME_MISSING_MESSAGE = "Escreva o nome do cliente.";
    private static final String DOCUMENT_MISSING_MESSAGE = "Escreva o documento do cliente.";
    private static final String NAME_LONG_MESSAGE = "O nome do cliente tem no máximo 100 caracteres.";
    private static final String DOCUMENT_LONG_MESSAGE = "O documento tem no máximo 20 caracteres.";
    private static final String EXISTS_MESSAGE = "O cliente já existe.";

    private InMemoryClientRepository repository;
    private ClientService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryClientRepository();
        service = new ClientService(repository);
    }

    private User receptionist() {
        return new Receptionist(2L, "Ana Silva", "ana", "hash");
    }

    private User manager() {
        return new Manager(1L, "Rui Costa", "rui", "hash");
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onRegister_saveClient_whenDataValid() {
        Client client = service.register(NAME, DOCUMENT, receptionist());

        assertNotNull(client.getId());
        assertEquals(NAME, client.getName());
        assertEquals(DOCUMENT, client.getDocument());
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onRegister_trimNameAndDocument_whenSpacesAround() {
        Client client = service.register("  " + NAME + " ", " " + DOCUMENT + "  ", receptionist());

        assertEquals(NAME, client.getName());
        assertEquals(DOCUMENT, client.getDocument());
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onRegister_clientAvailable_whenSaved() {
        service.register(NAME, DOCUMENT, receptionist());

        assertTrue(repository.findByDocument(DOCUMENT).isPresent());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenNameMissing(String name) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.register(name, DOCUMENT, receptionist()));

        assertEquals(NAME_MISSING_MESSAGE, error.getMessage());
        assertEquals(0, repository.count());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenDocumentMissing(String document) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.register(NAME, document, receptionist()));

        assertEquals(DOCUMENT_MISSING_MESSAGE, error.getMessage());
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenNameTooLong() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.register("a".repeat(101), DOCUMENT, receptionist()));

        assertEquals(NAME_LONG_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenDocumentTooLong() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.register(NAME, "1".repeat(21), receptionist()));

        assertEquals(DOCUMENT_LONG_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-52-AC-3")
    void onRegister_throw_whenDocumentExists() {
        service.register(NAME, DOCUMENT, receptionist());

        ClientExistsException error = assertThrows(ClientExistsException.class,
                () -> service.register("Outra Pessoa", DOCUMENT, receptionist()));

        assertEquals(EXISTS_MESSAGE, error.getMessage());
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-3")
    void onRegister_throw_whenDocumentExistsWithOtherCaseAndSpaces() {
        service.register(NAME, "AB123", receptionist());

        assertThrows(ClientExistsException.class,
                () -> service.register(NAME, " ab123 ", receptionist()));
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onRegister_throw_whenActorIsManager() {
        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.register(NAME, DOCUMENT, manager()));

        assertEquals(DENIED_MESSAGE, error.getMessage());
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onRegister_throw_whenActorIsRestaurantStaff() {
        User cook = new RestaurantStaff(3L, "Joao Lima", "joao", "hash");

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.register(NAME, DOCUMENT, cook));

        assertEquals(DENIED_MESSAGE, error.getMessage());
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onRegister_throw_whenNoActor() {
        assertThrows(AccessDeniedException.class,
                () -> service.register(NAME, DOCUMENT, null));
        assertEquals(0, repository.count());
    }
}
