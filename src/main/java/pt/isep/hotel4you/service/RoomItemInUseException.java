package pt.isep.hotel4you.service;

/** Signals that a room item has associations and cannot be removed. */
public class RoomItemInUseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public RoomItemInUseException() {
        super("O item tem associações e não pode ser removido.");
    }
}
