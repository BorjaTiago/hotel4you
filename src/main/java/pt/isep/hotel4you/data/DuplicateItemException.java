package pt.isep.hotel4you.data;

/** Signals that a stock item name is already in use. */
public class DuplicateItemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception. */
    public DuplicateItemException() {
        super("Já existe um item com este nome.");
    }
}
