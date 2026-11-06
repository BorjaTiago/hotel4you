package pt.isep.hotel4you.data;

/** Signals that a client has reservations and cannot be removed (Q-36). */
public class ClientInUseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public ClientInUseException() {
        super("O cliente tem reservas e não pode ser removido.");
    }
}
