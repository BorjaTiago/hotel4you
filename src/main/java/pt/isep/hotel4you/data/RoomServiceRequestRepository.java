package pt.isep.hotel4you.data;

import java.util.Optional;
import pt.isep.hotel4you.domain.RoomServiceRequest;
import pt.isep.hotel4you.domain.RoomServiceType;

/** Saves the room service requests. */
public interface RoomServiceRequestRepository {
    /**
     * Saves a request for the reservation with check-in of the room, with the current time and
     * the current catalogue price. The check of the check-in and the insert are one step, so a
     * check-out at the same moment cannot get a request.
     *
     * @param roomNumber the room number
     * @param type the service type
     * @return the saved request, or empty if the room has no reservation with check-in
     * @throws IllegalStateException if the catalogue has no entry for the type or the database
     *         is not available
     */
    Optional<RoomServiceRequest> save(int roomNumber, RoomServiceType type);
}
