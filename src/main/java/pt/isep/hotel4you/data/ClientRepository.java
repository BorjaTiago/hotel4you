package pt.isep.hotel4you.data;

import java.util.Optional;
import pt.isep.hotel4you.domain.Client;

/** Stores and finds the hotel clients. */
public interface ClientRepository {

    /**
     * Finds a client by ID document. The match is not case sensitive.
     *
     * @param document the ID document number
     * @return the client, or empty if no client has this document
     * @throws IllegalStateException if the database is not available
     */
    Optional<Client> findByDocument(String document);

    /**
     * Saves a new client.
     *
     * @param client the new client, its id is ignored
     * @return the saved client with its new id
     * @throws ClientExistsException if another client has this document
     * @throws IllegalStateException if the database is not available
     */
    Client save(Client client);
}
