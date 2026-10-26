package pt.isep.hotel4you.service;

/** Signals that a zone does not exist. */
public class LocationNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public LocationNotFoundException() {
        super("A zona não existe.");
    }
}
