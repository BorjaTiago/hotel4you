package pt.isep.hotel4you.data;

/** Signals that a client with the same ID document is already registered. */
public class ClientExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public ClientExistsException() {
        super("O cliente já existe.");
    }
}
