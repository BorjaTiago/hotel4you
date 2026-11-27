package pt.isep.hotel4you.service;

/** Signals that the card is blocked. A blocked card never works again. */
public class CardBlockedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public CardBlockedException() {
        super("O cartão está bloqueado.");
    }
}
