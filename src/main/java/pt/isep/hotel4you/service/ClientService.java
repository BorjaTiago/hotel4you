package pt.isep.hotel4you.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import pt.isep.hotel4you.data.ClientExistsException;
import pt.isep.hotel4you.data.ClientInUseException;
import pt.isep.hotel4you.data.ClientRepository;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.User;

/**
 * Registers, lists, searches, edits and removes the hotel clients. Only the receptionist and the
 * manager may use it (Q-13, Q-54).
 */
public class ClientService {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DOCUMENT_LENGTH = 20;
    private static final int MAX_PHONE_LENGTH = 20;
    private static final int MAX_EMAIL_LENGTH = 100;

    private static final Pattern PHONE = Pattern.compile("\\+?[0-9 ]*[0-9][0-9 ]*");
    private static final Pattern EMAIL = Pattern.compile("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
    private static final Pattern TAX_NUMBER = Pattern.compile("[0-9]{9}");
    private static final Set<String> COUNTRIES = Set.of(Locale.getISOCountries());

    private final ClientRepository clientRepository;

    /**
     * Creates the service.
     *
     * @param clientRepository the client store
     */
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Registers a new client.
     *
     * @param client the new client, its id is ignored; the service trims the texts
     * @param actor the logged-in user who asks
     * @return the saved client with its id
     * @throws AccessDeniedException if the actor is not a receptionist or a manager
     * @throws IllegalArgumentException if a field is missing or not valid; the message names the field
     * @throws ClientExistsException if a client with this document type and number already exists,
     *         also when the database finds the same document at save time
     * @throws IllegalStateException if the database is not available
     */
    public Client register(Client client, User actor) {
        checkManage(actor);
        Client clean = validate(null, client);
        checkDocumentFree(clean, null);
        return clientRepository.save(clean);
    }

    /**
     * Lists all clients.
     *
     * @param actor the logged-in user who asks
     * @return the clients ordered by name
     * @throws AccessDeniedException if the actor is not a receptionist or a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<Client> listClients(User actor) {
        checkManage(actor);
        return clientRepository.findAll();
    }

    /**
     * Finds the clients whose name or document number contains the text.
     *
     * @param text the text to find; a blank text lists all clients
     * @param actor the logged-in user who asks
     * @return the matching clients ordered by name
     * @throws AccessDeniedException if the actor is not a receptionist or a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<Client> searchClients(String text, User actor) {
        checkManage(actor);
        if (text == null || text.isBlank()) {
            return clientRepository.findAll();
        }
        return clientRepository.search(text.trim());
    }

    /**
     * Changes the data of a saved client.
     *
     * @param client the client with its id and the new data
     * @param actor the logged-in user who asks
     * @throws AccessDeniedException if the actor is not a receptionist or a manager
     * @throws ClientNotFoundException if the client does not exist
     * @throws IllegalArgumentException if a field is missing or not valid; the message names the field
     * @throws ClientExistsException if another client has this document type and number
     * @throws IllegalStateException if the database is not available
     */
    public void editClient(Client client, User actor) {
        checkManage(actor);
        if (client.getId() == null) {
            throw new ClientNotFoundException();
        }
        Client clean = validate(client.getId(), client);
        clientRepository.findById(client.getId()).orElseThrow(ClientNotFoundException::new);
        checkDocumentFree(clean, clean.getId());
        if (!clientRepository.update(clean)) {
            throw new ClientNotFoundException();
        }
    }

    /**
     * Removes a client.
     *
     * @param id the client id
     * @param actor the logged-in user who asks
     * @throws AccessDeniedException if the actor is not a receptionist or a manager
     * @throws ClientNotFoundException if the client does not exist
     * @throws ClientInUseException if the client has reservations (Q-36)
     * @throws IllegalStateException if the database is not available
     */
    public void removeClient(long id, User actor) {
        checkManage(actor);
        if (!clientRepository.delete(id)) {
            throw new ClientNotFoundException();
        }
    }

    private static void checkManage(User actor) {
        if (!Permission.MANAGE_CLIENTS.allows(actor)) {
            throw new AccessDeniedException("Só o rececionista ou o gestor podem gerir clientes.");
        }
    }

    /** Throws if another client (not the one with {@code ownId}) has the same document. */
    private void checkDocumentFree(Client client, Long ownId) {
        Optional<Client> same = clientRepository.findByDocument(client.getDocumentType(),
                client.getDocument());
        if (same.isPresent() && !same.get().getId().equals(ownId)) {
            throw new ClientExistsException();
        }
    }

    private static Client validate(Long id, Client client) {
        String name = required(client.getName(), "Escreva o nome do cliente.");
        maxLength(name, MAX_NAME_LENGTH, "O nome do cliente tem no máximo 100 caracteres.");
        DocumentType documentType = client.getDocumentType();
        if (documentType == null) {
            throw new IllegalArgumentException("Escolha o tipo de documento.");
        }
        String document = required(client.getDocument(), "Escreva o documento do cliente.");
        maxLength(document, MAX_DOCUMENT_LENGTH, "O documento tem no máximo 20 caracteres.");
        LocalDate birthDate = checkBirthDate(client.getBirthDate());
        String phone = checkPhone(client.getPhone());
        String nationality = checkNationality(client.getNationality());
        String email = checkEmail(client.getEmail());
        String taxNumber = checkTaxNumber(client.getTaxNumber());
        return new Client(id, name, documentType, document, birthDate, phone, nationality, email,
                taxNumber);
    }

    private static LocalDate checkBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Escolha a data de nascimento.");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento não pode ser no futuro.");
        }
        return birthDate;
    }

    private static String checkPhone(String text) {
        String phone = required(text, "Escreva o telefone do cliente.");
        maxLength(phone, MAX_PHONE_LENGTH, "O telefone tem no máximo 20 caracteres.");
        if (!PHONE.matcher(phone).matches()) {
            throw new IllegalArgumentException(
                    "O telefone só pode ter dígitos, espaços e um '+' no início.");
        }
        return phone;
    }

    private static String checkNationality(String text) {
        String nationality = required(text, "Escolha a nacionalidade.").toUpperCase(Locale.ROOT);
        if (!COUNTRIES.contains(nationality)) {
            throw new IllegalArgumentException("A nacionalidade não é um país válido.");
        }
        return nationality;
    }

    private static String checkEmail(String text) {
        String email = optional(text);
        if (email == null) {
            return null;
        }
        maxLength(email, MAX_EMAIL_LENGTH, "O email tem no máximo 100 caracteres.");
        if (!EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("O email não é válido.");
        }
        return email;
    }

    private static String checkTaxNumber(String text) {
        String taxNumber = optional(text);
        if (taxNumber != null && !TAX_NUMBER.matcher(taxNumber).matches()) {
            throw new IllegalArgumentException("O NIF tem de ter 9 dígitos.");
        }
        return taxNumber;
    }

    private static String required(String text, String message) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return text.trim();
    }

    private static String optional(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return text.trim();
    }

    private static void maxLength(String text, int maxLength, String message) {
        if (text.length() > maxLength) {
            throw new IllegalArgumentException(message);
        }
    }
}
