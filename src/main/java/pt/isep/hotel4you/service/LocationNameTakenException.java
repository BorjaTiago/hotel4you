package pt.isep.hotel4you.service;

/** Signals that a zone name is already used on the floor. */
public class LocationNameTakenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public LocationNameTakenException() {
        super("Já existe uma zona com este nome neste piso.");
    }
}
