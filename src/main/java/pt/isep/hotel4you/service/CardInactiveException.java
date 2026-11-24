package pt.isep.hotel4you.service;

/** Signals that the card is not active, for example after the check-out. */
public class CardInactiveException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public CardInactiveException() {
        super("O cartão não está ativo.");
    }
}
