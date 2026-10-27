package pt.isep.hotel4you.service;

/** Signals that a zone has associations and cannot be removed. */
public class LocationInUseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public LocationInUseException() {
        super("A zona tem associações e não pode ser removida.");
    }
}
