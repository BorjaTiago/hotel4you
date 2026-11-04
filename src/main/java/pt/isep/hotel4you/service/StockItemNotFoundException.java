package pt.isep.hotel4you.service;

/** Signals that a stock item does not exist. */
public class StockItemNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public StockItemNotFoundException() {
        super("O item não existe.");
    }
}
