package pt.isep.hotel4you.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pt.isep.hotel4you.domain.RoomServiceRequest;
import pt.isep.hotel4you.domain.RoomServiceType;

/** Keeps the room service requests in memory for unit tests. */
public class InMemoryRoomServiceRequestRepository implements RoomServiceRequestRepository {
    /** The fixed request time that the tests expect. */
    public static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 24, 21, 0);

    private final Map<Integer, Long> checkedInReservations = new HashMap<>();
    private final List<RoomServiceRequest> saved = new ArrayList<>();

    @Override
    public Optional<RoomServiceRequest> save(int roomNumber, RoomServiceType type) {
        Long reservationId = checkedInReservations.get(roomNumber);
        if (reservationId == null) {
            return Optional.empty();
        }
        RoomServiceRequest request = new RoomServiceRequest((long) saved.size() + 1, reservationId,
                roomNumber, type, new BigDecimal("4.50"), NOW);
        saved.add(request);
        return Optional.of(request);
    }

    /**
     * Marks a room as occupied by a reservation with check-in.
     * @param roomNumber the room number
     * @param reservationId the reservation identifier
     */
    public void checkIn(int roomNumber, long reservationId) {
        checkedInReservations.put(roomNumber, reservationId);
    }

    /**
     * Returns the saved requests.
     * @return the requests in save order
     */
    public List<RoomServiceRequest> saved() {
        return List.copyOf(saved);
    }
}
