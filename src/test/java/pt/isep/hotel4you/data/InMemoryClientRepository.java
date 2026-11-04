package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.Client;

/** Keeps clients in memory. Unit tests use it instead of the database. */
public class InMemoryClientRepository implements ClientRepository {

    private final List<Client> clients = new ArrayList<>();
    private long nextId = 1;

    @Override
    public Optional<Client> findByDocument(String document) {
        return clients.stream()
                .filter(client -> client.getDocument().equalsIgnoreCase(document))
                .findFirst();
    }

    @Override
    public Client save(Client client) {
        Client saved = new Client(nextId++, client.getName(), client.getDocument());
        clients.add(saved);
        return saved;
    }

    /**
     * Returns how many clients are stored.
     *
     * @return the number of clients
     */
    public int count() {
        return clients.size();
    }
}
