package pt.isep.hotel4you.service;

/** Signals that the reservation already has a card. */
public class CardAlreadyIssuedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public CardAlreadyIssuedException() {
        super("A reserva já tem cartão.");
    }
}
