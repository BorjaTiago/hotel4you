package pt.isep.hotel4you.service;

/** Signals that a stock item name is already used. */
public class StockItemNameTakenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public StockItemNameTakenException() {
        super("Já existe um item com este nome.");
    }
}
