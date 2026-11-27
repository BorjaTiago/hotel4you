package pt.isep.hotel4you.service;

/**
 * Signals that a card may not enter a room or a zone (H4Y-135). The message is always "Entrada
 * recusada." and the reason tells why, to show on the screen "O meu cartão".
 */
public class EntryRefusedException extends AccessDeniedException {

    private static final long serialVersionUID = 1L;

    private final String reason;

    /**
     * Creates the exception.
     *
     * @param reason why the card may not enter the place
     */
    public EntryRefusedException(String reason) {
        super(CardAccessService.ENTRY_REFUSED);
        this.reason = reason;
    }

    /**
     * Gives the reason of the refusal.
     *
     * @return why the card may not enter the place
     */
    public String getReason() {
        return reason;
    }
}
