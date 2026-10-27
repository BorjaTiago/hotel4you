package pt.isep.hotel4you.data;

/** Signals that the floor has reached its room limit. */
public class RoomLimitExceededException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param floorLimit the maximum number of rooms on the floor
     */
    public RoomLimitExceededException(int floorLimit) {
        super("O piso já tem " + floorLimit + " quartos.");
    }
}
