package pt.isep.hotel4you.service;

/** Signals that no card has the given number. */
public class CardNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public CardNotFoundException() {
        super("O cartão não existe.");
    }
}
