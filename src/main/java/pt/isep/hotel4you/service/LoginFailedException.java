package pt.isep.hotel4you.service;

/** Signals a failed login. The message does not tell which part was wrong. */
public class LoginFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public LoginFailedException() {
        super("Credenciais inválidas");
    }

    /**
     * Creates the exception with a given message.
     *
     * @param message the message for the user; it does not tell which part was wrong
     */
    public LoginFailedException(String message) {
        super(message);
    }
}
