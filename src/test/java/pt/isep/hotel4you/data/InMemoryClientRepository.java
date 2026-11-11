package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.ClientSearchField;
import pt.isep.hotel4you.domain.DocumentType;

/** Keeps clients in memory. Unit tests use it instead of the database. */
public class InMemoryClientRepository implements ClientRepository {

    private final Map<Long, Client> clients = new LinkedHashMap<>();
    private long nextId = 1;

    @Override
    public Optional<Client> findById(long id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public Optional<Client> findByDocument(DocumentType documentType, String document) {
        return clients.values().stream()
                .filter(client -> client.getDocumentType() == documentType
                        && client.getDocument().equalsIgnoreCase(document))
                .findFirst();
    }

    @Override
    public List<Client> findAll() {
        List<Client> all = new ArrayList<>(clients.values());
        all.sort(Comparator.comparing(Client::getName));
        return all;
    }

    @Override
    public List<Client> search(ClientSearchField field, String text) {
        String part = text.toLowerCase(Locale.ROOT);
        return findAll().stream()
                .filter(client -> valueOf(field, client).toLowerCase(Locale.ROOT).contains(part))
                .toList();
    }

    private static String valueOf(ClientSearchField field, Client client) {
        if (field == ClientSearchField.NAME) {
            return client.getName();
        }
        if (field == ClientSearchField.PHONE) {
            return client.getPhone();
        }
        return client.getDocument();
    }

    @Override
    public Client save(Client client) {
        Client saved = withId(nextId++, client);
        clients.put(saved.getId(), saved);
        return saved;
    }

    @Override
    public boolean update(Client client) {
        if (!clients.containsKey(client.getId())) {
            return false;
        }
        clients.put(client.getId(), client);
        return true;
    }

    @Override
    public boolean delete(long id) {
        return clients.remove(id) != null;
    }

    /**
     * Returns how many clients are stored.
     *
     * @return the number of clients
     */
    public int count() {
        return clients.size();
    }

    private static Client withId(long id, Client client) {
        return new Client(id, client.getName(), client.getDocumentType(), client.getDocument(),
                client.getBirthDate(), client.getPhone(), client.getNationality(), client.getEmail(),
                client.getTaxNumber());
    }
}
