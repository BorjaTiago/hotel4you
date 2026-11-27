package pt.isep.hotel4you.service;

/** Signals that the manager still has a master card that is not blocked. */
public class MasterCardActiveException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public MasterCardActiveException() {
        super("Bloqueie primeiro o cartão mestre atual.");
    }
}
