package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.ClientExistsException;
import pt.isep.hotel4you.data.ClientInUseException;
import pt.isep.hotel4you.data.InMemoryClientRepository;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.User;

/** Tests that the receptionist and the manager can register, find, edit and remove clients. */
class ClientServiceTest {

    private static final String NAME = "Maria Santos";
    private static final String DOCUMENT = "12345678";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1990, 5, 17);
    private static final String PHONE = "+351 912 345 678";
    private static final String NATIONALITY = "PT";
    private static final String EMAIL = "maria@example.com";
    private static final String TAX_NUMBER = "123456789";

    private static final String DENIED_MESSAGE = "Só o rececionista ou o gestor podem gerir clientes.";
    private static final String EXISTS_MESSAGE = "O cliente já existe.";
    private static final String NOT_FOUND_MESSAGE = "O cliente não existe.";

    private InMemoryClientRepository repository;
    private ClientService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryClientRepository();
        service = new ClientService(repository);
    }

    private static User receptionist() {
        return new Receptionist(2L, "Ana Silva", "ana", "hash");
    }

    private static User manager() {
        return new Manager(1L, "Rui Costa", "rui", "hash");
    }

    private static User cook() {
        return new RestaurantStaff(3L, "Joao Lima", "joao", "hash");
    }

    private static Client client(String name, DocumentType type, String document) {
        return new Client(null, name, type, document, BIRTH_DATE, PHONE, NATIONALITY, EMAIL, TAX_NUMBER);
    }

    private static Client valid() {
        return client(NAME, DocumentType.CC, DOCUMENT);
    }

    private static Client withBirthDate(LocalDate birthDate) {
        return new Client(null, NAME, DocumentType.CC, DOCUMENT, birthDate, PHONE, NATIONALITY, EMAIL,
                TAX_NUMBER);
    }

    private static Client withPhone(String phone) {
        return new Client(null, NAME, DocumentType.CC, DOCUMENT, BIRTH_DATE, phone, NATIONALITY, EMAIL,
                TAX_NUMBER);
    }

    private static Client withNationality(String nationality) {
        return new Client(null, NAME, DocumentType.CC, DOCUMENT, BIRTH_DATE, PHONE, nationality, EMAIL,
                TAX_NUMBER);
    }

    private static Client withEmail(String email) {
        return new Client(null, NAME, DocumentType.CC, DOCUMENT, BIRTH_DATE, PHONE, NATIONALITY, email,
                TAX_NUMBER);
    }

    private static Client withTaxNumber(String taxNumber) {
        return new Client(null, NAME, DocumentType.CC, DOCUMENT, BIRTH_DATE, PHONE, NATIONALITY, EMAIL,
                taxNumber);
    }

    private static Client changed(long id, String name, String document) {
        return new Client(id, name, DocumentType.PASSPORT, document, LocalDate.of(1985, 1, 2),
                "912 000 111", "ES", null, null);
    }

    private void assertRefused(Client client, String message) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.register(client, receptionist()));
        assertEquals(message, error.getMessage());
        assertEquals(0, repository.count());
    }

    // ---------- Register ----------

    @Test
    @Tag("H4Y-52-AC-1")
    void onRegister_saveClient_whenDataValid() {
        Client saved = service.register(valid(), receptionist());

        assertNotNull(saved.getId());
        assertEquals(NAME, saved.getName());
        assertEquals(DocumentType.CC, saved.getDocumentType());
        assertEquals(DOCUMENT, saved.getDocument());
        assertEquals(BIRTH_DATE, saved.getBirthDate());
        assertEquals(PHONE, saved.getPhone());
        assertEquals(NATIONALITY, saved.getNationality());
        assertEquals(EMAIL, saved.getEmail());
        assertEquals(TAX_NUMBER, saved.getTaxNumber());
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onRegister_trimAndNormalizeFields_whenSpacesAround() {
        Client input = new Client(null, "  " + NAME + " ", DocumentType.CC, " " + DOCUMENT + " ",
                BIRTH_DATE, " " + PHONE + " ", " pt ", " " + EMAIL + " ", " " + TAX_NUMBER + " ");

        Client saved = service.register(input, receptionist());

        assertEquals(NAME, saved.getName());
        assertEquals(DOCUMENT, saved.getDocument());
        assertEquals(PHONE, saved.getPhone());
        assertEquals("PT", saved.getNationality());
        assertEquals(EMAIL, saved.getEmail());
        assertEquals(TAX_NUMBER, saved.getTaxNumber());
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onRegister_saveWithoutOptionalFields_whenEmailAndTaxNumberBlank() {
        Client input = new Client(null, NAME, DocumentType.CC, DOCUMENT, BIRTH_DATE, PHONE, NATIONALITY,
                "  ", "");

        Client saved = service.register(input, receptionist());

        assertNull(saved.getEmail());
        assertNull(saved.getTaxNumber());
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onRegister_saveClient_whenBirthDateIsToday() {
        Client saved = service.register(withBirthDate(LocalDate.now()), receptionist());

        assertEquals(LocalDate.now(), saved.getBirthDate());
    }

    @Test
    @Tag("H4Y-52-AC-2")
    void onRegister_saveClient_whenFieldsAtMaxLength() {
        Client input = new Client(null, "a".repeat(100), DocumentType.OTHER, "1".repeat(20), BIRTH_DATE,
                "9".repeat(20), NATIONALITY, "a".repeat(88) + "@example.com", TAX_NUMBER);

        assertNotNull(service.register(input, receptionist()).getId());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenNameMissing(String name) {
        assertRefused(client(name, DocumentType.CC, DOCUMENT), "Escreva o nome do cliente.");
    }

    @Test
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenNameTooLong() {
        assertRefused(client("a".repeat(101), DocumentType.CC, DOCUMENT),
                "O nome do cliente tem no máximo 100 caracteres.");
    }

    @Test
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenDocumentTypeMissing() {
        assertRefused(client(NAME, null, DOCUMENT), "Escolha o tipo de documento.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenDocumentMissing(String document) {
        assertRefused(client(NAME, DocumentType.CC, document), "Escreva o documento do cliente.");
    }

    @Test
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenDocumentTooLong() {
        assertRefused(client(NAME, DocumentType.CC, "1".repeat(21)),
                "O documento tem no máximo 20 caracteres.");
    }

    @Test
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenBirthDateMissing() {
        assertRefused(withBirthDate(null), "Escolha a data de nascimento.");
    }

    @Test
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenBirthDateInFuture() {
        assertRefused(withBirthDate(LocalDate.now().plusDays(1)),
                "A data de nascimento não pode ser no futuro.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenPhoneMissing(String phone) {
        assertRefused(withPhone(phone), "Escreva o telefone do cliente.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"912-345-678", "abc", "+", "912+345"})
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenPhoneHasWrongCharacters(String phone) {
        assertRefused(withPhone(phone), "O telefone só pode ter dígitos, espaços e um '+' no início.");
    }

    @Test
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenPhoneTooLong() {
        assertRefused(withPhone("9".repeat(21)), "O telefone tem no máximo 20 caracteres.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenNationalityMissing(String nationality) {
        assertRefused(withNationality(nationality), "Escolha a nacionalidade.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"XX", "POR", "P"})
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenNationalityNotIsoCountry(String nationality) {
        assertRefused(withNationality(nationality), "A nacionalidade não é um país válido.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"maria", "maria@", "maria@example", "@example.com", "ma ria@example.com"})
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenEmailNotValid(String email) {
        assertRefused(withEmail(email), "O email não é válido.");
    }

    @Test
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenEmailTooLong() {
        assertRefused(withEmail("a".repeat(89) + "@example.com"), "O email tem no máximo 100 caracteres.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "1234567890", "12345678A"})
    @Tag("H4Y-52-AC-2")
    void onRegister_throw_whenTaxNumberNotNineDigits(String taxNumber) {
        assertRefused(withTaxNumber(taxNumber), "O NIF tem de ter 9 dígitos.");
    }

    @Test
    @Tag("H4Y-52-AC-3")
    void onRegister_throw_whenTypeAndDocumentExist() {
        service.register(valid(), receptionist());

        ClientExistsException error = assertThrows(ClientExistsException.class,
                () -> service.register(client("Outra Pessoa", DocumentType.CC, DOCUMENT), receptionist()));

        assertEquals(EXISTS_MESSAGE, error.getMessage());
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-3")
    void onRegister_throw_whenDocumentExistsWithOtherCaseAndSpaces() {
        service.register(client(NAME, DocumentType.PASSPORT, "AB123"), receptionist());

        assertThrows(ClientExistsException.class,
                () -> service.register(client(NAME, DocumentType.PASSPORT, " ab123 "), receptionist()));
        assertEquals(1, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-3")
    void onRegister_saveClient_whenSameDocumentHasOtherType() {
        service.register(client(NAME, DocumentType.CC, DOCUMENT), receptionist());

        service.register(client("Outra Pessoa", DocumentType.PASSPORT, DOCUMENT), receptionist());

        assertEquals(2, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-3")
    void onRegister_throw_whenRepositoryReportsDuplicate() {
        ClientService duplicateAtSave = new ClientService(new InMemoryClientRepository() {
            @Override
            public Client save(Client client) {
                throw new ClientExistsException();
            }
        });

        assertThrows(ClientExistsException.class, () -> duplicateAtSave.register(valid(), receptionist()));
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onRegister_saveClient_whenActorIsManager() {
        assertNotNull(service.register(valid(), manager()).getId());
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onRegister_throw_whenActorIsRestaurantStaff() {
        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.register(valid(), cook()));

        assertEquals(DENIED_MESSAGE, error.getMessage());
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onRegister_throw_whenNoActor() {
        assertThrows(AccessDeniedException.class, () -> service.register(valid(), null));
        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onRegister_checkPermissionFirst_whenDataAlsoInvalid() {
        assertThrows(AccessDeniedException.class,
                () -> service.register(client(null, null, null), cook()));
    }

    // ---------- List and search ----------

    @Test
    @Tag("H4Y-52-AC-4")
    void onListClients_returnAllClients_whenClientsExist() {
        service.register(valid(), receptionist());
        service.register(client("João Pereira", DocumentType.PASSPORT, "X998877"), receptionist());

        List<Client> clients = service.listClients(receptionist());

        assertEquals(2, clients.size());
    }

    @Test
    @Tag("H4Y-52-AC-4")
    void onSearchClients_returnMatches_whenTextIsPartOfName() {
        service.register(valid(), receptionist());
        service.register(client("João Pereira", DocumentType.PASSPORT, "X998877"), receptionist());

        List<Client> clients = service.searchClients("  santos ", receptionist());

        assertEquals(1, clients.size());
        assertEquals(NAME, clients.get(0).getName());
    }

    @Test
    @Tag("H4Y-52-AC-4")
    void onSearchClients_returnMatches_whenTextIsPartOfDocument() {
        service.register(valid(), receptionist());
        service.register(client("João Pereira", DocumentType.PASSPORT, "X998877"), receptionist());

        List<Client> clients = service.searchClients("9988", manager());

        assertEquals(1, clients.size());
        assertEquals("João Pereira", clients.get(0).getName());
    }

    @Test
    @Tag("H4Y-52-AC-4")
    void onSearchClients_returnEmpty_whenNoClientMatches() {
        service.register(valid(), receptionist());

        assertTrue(service.searchClients("Inexistente", receptionist()).isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @Tag("H4Y-52-AC-4")
    void onSearchClients_returnAllClients_whenTextBlank(String text) {
        service.register(valid(), receptionist());
        service.register(client("João Pereira", DocumentType.PASSPORT, "X998877"), receptionist());

        assertEquals(2, service.searchClients(text, receptionist()).size());
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onListClients_throw_whenActorIsRestaurantStaff() {
        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.listClients(cook()));

        assertEquals(DENIED_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onSearchClients_throw_whenNoActor() {
        assertThrows(AccessDeniedException.class, () -> service.searchClients("Maria", null));
    }

    // ---------- Edit ----------

    @Test
    @Tag("H4Y-52-AC-5")
    void onEditClient_saveNewData_whenDataValid() {
        Client saved = service.register(valid(), receptionist());

        service.editClient(changed(saved.getId(), " Maria S. Santos ", " P123 "), receptionist());

        Client stored = repository.findById(saved.getId()).orElseThrow();
        assertEquals("Maria S. Santos", stored.getName());
        assertEquals(DocumentType.PASSPORT, stored.getDocumentType());
        assertEquals("P123", stored.getDocument());
        assertEquals(LocalDate.of(1985, 1, 2), stored.getBirthDate());
        assertEquals("912 000 111", stored.getPhone());
        assertEquals("ES", stored.getNationality());
        assertNull(stored.getEmail());
        assertNull(stored.getTaxNumber());
    }

    @Test
    @Tag("H4Y-52-AC-5")
    void onEditClient_keepSameDocument_whenOnlyOtherDataChanges() {
        Client saved = service.register(valid(), receptionist());
        Client sameDocument = new Client(saved.getId(), "Maria Santos Silva", DocumentType.CC, DOCUMENT,
                BIRTH_DATE, PHONE, NATIONALITY, EMAIL, TAX_NUMBER);

        service.editClient(sameDocument, manager());

        assertEquals("Maria Santos Silva", repository.findById(saved.getId()).orElseThrow().getName());
    }

    @Test
    @Tag("H4Y-52-AC-5")
    void onEditClient_throw_whenDataInvalid() {
        Client saved = service.register(valid(), receptionist());

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.editClient(changed(saved.getId(), " ", "P123"), receptionist()));

        assertEquals("Escreva o nome do cliente.", error.getMessage());
        assertEquals(NAME, repository.findById(saved.getId()).orElseThrow().getName());
    }

    @Test
    @Tag("H4Y-52-AC-5")
    void onEditClient_throw_whenOtherClientHasTypeAndDocument() {
        service.register(valid(), receptionist());
        Client other = service.register(client("João Pereira", DocumentType.PASSPORT, "X998877"),
                receptionist());
        Client copy = new Client(other.getId(), "João Pereira", DocumentType.CC, DOCUMENT, BIRTH_DATE,
                PHONE, NATIONALITY, EMAIL, TAX_NUMBER);

        assertThrows(ClientExistsException.class, () -> service.editClient(copy, receptionist()));
        assertEquals("X998877", repository.findById(other.getId()).orElseThrow().getDocument());
    }

    @Test
    @Tag("H4Y-52-AC-5")
    void onEditClient_throw_whenClientDoesNotExist() {
        ClientNotFoundException error = assertThrows(ClientNotFoundException.class,
                () -> service.editClient(changed(99L, NAME, DOCUMENT), receptionist()));

        assertEquals(NOT_FOUND_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-52-AC-5")
    void onEditClient_throw_whenIdNull() {
        assertThrows(ClientNotFoundException.class, () -> service.editClient(valid(), receptionist()));
    }

    @Test
    @Tag("H4Y-52-AC-5")
    void onEditClient_throw_whenUpdateChangesNoRow() {
        ClientService goneAtUpdate = new ClientService(new InMemoryClientRepository() {
            @Override
            public Optional<Client> findById(long id) {
                return Optional.of(changed(id, NAME, DOCUMENT));
            }

            @Override
            public boolean update(Client client) {
                return false;
            }
        });

        assertThrows(ClientNotFoundException.class,
                () -> goneAtUpdate.editClient(changed(1L, NAME, DOCUMENT), receptionist()));
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onEditClient_throw_whenActorIsRestaurantStaff() {
        Client saved = service.register(valid(), receptionist());

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.editClient(changed(saved.getId(), "Outro", "P123"), cook()));

        assertEquals(DENIED_MESSAGE, error.getMessage());
        assertEquals(NAME, repository.findById(saved.getId()).orElseThrow().getName());
    }

    // ---------- Remove ----------

    @Test
    @Tag("H4Y-52-AC-6")
    void onRemoveClient_deleteClient_whenClientExists() {
        Client saved = service.register(valid(), receptionist());

        service.removeClient(saved.getId(), manager());

        assertEquals(0, repository.count());
    }

    @Test
    @Tag("H4Y-52-AC-6")
    void onRemoveClient_throw_whenClientDoesNotExist() {
        ClientNotFoundException error = assertThrows(ClientNotFoundException.class,
                () -> service.removeClient(99L, receptionist()));

        assertEquals(NOT_FOUND_MESSAGE, error.getMessage());
    }

    @Test
    @Tag("H4Y-52-AC-7")
    void onRemoveClient_throw_whenClientHasReservations() {
        ClientService withReservations = new ClientService(new InMemoryClientRepository() {
            @Override
            public boolean delete(long id) {
                throw new ClientInUseException();
            }
        });

        ClientInUseException error = assertThrows(ClientInUseException.class,
                () -> withReservations.removeClient(1L, receptionist()));

        assertEquals("O cliente tem reservas e não pode ser removido.", error.getMessage());
    }

    @Test
    @Tag("H4Y-52-AC-8")
    void onRemoveClient_throw_whenActorIsRestaurantStaff() {
        Client saved = service.register(valid(), receptionist());

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.removeClient(saved.getId(), cook()));

        assertEquals(DENIED_MESSAGE, error.getMessage());
        assertEquals(1, repository.count());
    }
}
