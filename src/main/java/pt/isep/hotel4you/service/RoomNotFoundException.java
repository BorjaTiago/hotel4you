package pt.isep.hotel4you.service;

/** Signals that no room has the given number. */
public class RoomNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public RoomNotFoundException() {
        super("O quarto não existe.");
    }
}
