package pt.isep.hotel4you.service;

/** Signals that a staff account has associated records and cannot be removed. */
public class StaffInUseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public StaffInUseException() {
        super("A conta tem registos associados e não pode ser removida.");
    }
}
