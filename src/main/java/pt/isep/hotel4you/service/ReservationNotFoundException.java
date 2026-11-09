package pt.isep.hotel4you.service;

/** Signals that a reservation does not exist. */
public class ReservationNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public ReservationNotFoundException() {
        super("A reserva não existe.");
    }
}
