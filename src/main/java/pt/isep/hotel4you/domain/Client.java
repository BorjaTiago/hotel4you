package pt.isep.hotel4you.domain;

/** A hotel client. The receptionist registers the client before the reservation. */
public class Client {

    private final Long id;
    private final String name;
    private final String document;

    /**
     * Creates a client.
     *
     * @param id the identifier, or null if the client is not saved yet
     * @param name the full name
     * @param document the ID document number (ID card or passport)
     */
    public Client(Long id, String name, String document) {
        this.id = id;
        this.name = name;
        this.document = document;
    }

    /**
     * Returns the identifier.
     *
     * @return the identifier, or null if the client is not saved yet
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the full name.
     *
     * @return the full name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the ID document number.
     *
     * @return the document number
     */
    public String getDocument() {
        return document;
    }
}
