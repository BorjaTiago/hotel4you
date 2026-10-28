package pt.isep.hotel4you.service;

/** Signals that the asked floor does not exist. */
public class FloorNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public FloorNotFoundException() {
        super("O piso não existe.");
    }
}
