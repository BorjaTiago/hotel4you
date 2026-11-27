package pt.isep.hotel4you.service;

/** Signals that a room has no reservation with check-in. */
public class RoomNotOccupiedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with the room number in the message.
     *
     * @param roomNumber the room number
     */
    public RoomNotOccupiedException(int roomNumber) {
        super("O quarto " + roomNumber
                + " não está ocupado. Só um quarto com check-in pode pedir serviço de quarto.");
    }
}
