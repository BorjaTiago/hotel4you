package pt.isep.hotel4you.service;

/** Signals that a staff account does not exist. */
public class StaffNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public StaffNotFoundException() {
        super("A conta não existe.");
    }
}
