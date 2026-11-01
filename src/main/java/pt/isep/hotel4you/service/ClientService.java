package pt.isep.hotel4you.service;

import pt.isep.hotel4you.data.ClientRepository;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.User;

/** Registers the hotel clients. Only the receptionist or the manager may use it. */
public class ClientService {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DOCUMENT_LENGTH = 20;

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
     * @param name the full name, the service trims it
     * @param document the ID document number, the service trims it
     * @param actor the logged-in user who asks
     * @return the saved client with its id
     * @throws AccessDeniedException if the actor is not a receptionist or a manager
     * @throws IllegalArgumentException if a field is missing or too long; the message names the field
     * @throws ClientExistsException if a client with this document already exists
     * @throws IllegalStateException if the database is not available
     */
    public Client register(String name, String document, User actor) {
        if (!Permission.MANAGE_CLIENTS.allows(actor)) {
            throw new AccessDeniedException("Só o rececionista ou o gestor pode registar clientes.");
        }
        String cleanName = checkName(name);
        String cleanDocument = checkDocument(document);
        if (clientRepository.findByDocument(cleanDocument).isPresent()) {
            throw new ClientExistsException();
        }
        return clientRepository.save(new Client(null, cleanName, cleanDocument));
    }

    private static String checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Escreva o nome do cliente.");
        }
        String cleanName = name.trim();
        if (cleanName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("O nome do cliente tem no máximo 100 caracteres.");
        }
        return cleanName;
    }

    private static String checkDocument(String document) {
        if (document == null || document.isBlank()) {
            throw new IllegalArgumentException("Escreva o documento do cliente.");
        }
        String cleanDocument = document.trim();
        if (cleanDocument.length() > MAX_DOCUMENT_LENGTH) {
            throw new IllegalArgumentException("O documento tem no máximo 20 caracteres.");
        }
        return cleanDocument;
    }
}
