package pt.isep.hotel4you.service;

/** Signals that a client does not exist. */
public class ClientNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public ClientNotFoundException() {
        super("O cliente não existe.");
    }
}
