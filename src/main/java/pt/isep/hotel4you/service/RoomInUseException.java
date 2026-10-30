package pt.isep.hotel4you.service;

/** Signals that a room has associations and cannot be removed. */
public class RoomInUseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public RoomInUseException() {
        super("O quarto tem associações e não pode ser removido.");
    }
}
