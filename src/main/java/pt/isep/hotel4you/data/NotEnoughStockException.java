package pt.isep.hotel4you.data;

/** Signals that a stock movement would make the stock of an item lower than zero. */
public class NotEnoughStockException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception. */
    public NotEnoughStockException() {
        super("A quantidade é maior que o stock do item.");
    }
}
