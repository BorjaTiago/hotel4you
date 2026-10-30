package pt.isep.hotel4you.data;

/** Signals that a visible room number is already in use. */
public class DuplicateRoomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception. */
    public DuplicateRoomException() {
        super("O número do quarto já existe.");
    }
}
