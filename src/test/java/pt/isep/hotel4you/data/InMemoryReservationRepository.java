package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.List;
import pt.isep.hotel4you.domain.Reservation;

/** Own reservation fixtures for availability tests. */
public class InMemoryReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations = new ArrayList<>();
    public int findByRoomCalls;
    public int findByRoomsCalls;

    /** @param reservation the test reservation to add */
    public void add(Reservation reservation) {
        reservations.add(reservation);
    }

    @Override
    public List<Reservation> findByRoom(long roomId) {
        findByRoomCalls++;
        return reservations.stream().filter(value -> value.getRoomId() == roomId).toList();
    }

    @Override
    public List<Reservation> findByRooms(List<Long> roomIds) {
        findByRoomsCalls++;
        return reservations.stream().filter(value -> roomIds.contains(value.getRoomId())).toList();
    }
}
