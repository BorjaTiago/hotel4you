package pt.isep.hotel4you.data;

import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;

/** Stores and finds the hotel clients. */
public interface ClientRepository {

    /**
     * Finds a client by id.
     *
     * @param id the client id
     * @return the client, or empty if it does not exist
     * @throws IllegalStateException if the database is not available
     */
    Optional<Client> findById(long id);

    /**
     * Finds a client by ID document type and number. The number match is not case sensitive.
     *
     * @param documentType the document type
     * @param document the ID document number
     * @return the client, or empty if no client has this document
     * @throws IllegalStateException if the database is not available
     */
    Optional<Client> findByDocument(DocumentType documentType, String document);

    /**
     * Lists all clients.
     *
     * @return the clients ordered by name
     * @throws IllegalStateException if the database is not available
     */
    List<Client> findAll();

    /**
     * Finds the clients whose name or document number contains the text. The match is not case
     * sensitive.
     *
     * @param text the text to find, not blank
     * @return the matching clients ordered by name
     * @throws IllegalStateException if the database is not available
     */
    List<Client> search(String text);

    /**
     * Saves a new client.
     *
     * @param client the new client, its id is ignored
     * @return the saved client with its new id
     * @throws ClientExistsException if another client has this document type and number
     * @throws IllegalStateException if the database is not available
     */
    Client save(Client client);

    /**
     * Changes all the data of a saved client.
     *
     * @param client the client with its id and new values
     * @return true if the client was changed, false if no client has this id
     * @throws ClientExistsException if another client has this document type and number
     * @throws IllegalStateException if the database is not available
     */
    boolean update(Client client);

    /**
     * Removes a client.
     *
     * @param id the client id
     * @return true if the client was removed, false if no client has this id
     * @throws ClientInUseException if a reservation uses the client
     * @throws IllegalStateException if the database is not available
     */
    boolean delete(long id);
}
