package pt.isep.hotel4you.data;

/** Signals that the room already has a reservation in the chosen nights. */
public class RoomNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Creates the exception with a fixed message. */
    public RoomNotAvailableException() {
        super("O quarto já está reservado nessas datas.");
    }
}
