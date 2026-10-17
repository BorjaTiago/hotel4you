package pt.isep.hotel4you.service;

/** Signals that a login name is already in use. */
public class UsernameTakenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public UsernameTakenException() {
        super("Username is already taken.");
    }
}
