package pt.isep.hotel4you.service;

/** Signals that the logged-in user may not do the action. */
public class AccessDeniedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param message the text to show to the user
     */
    public AccessDeniedException(String message) {
        super(message);
    }
}
