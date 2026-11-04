package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.List;
import pt.isep.hotel4you.domain.Reservation;

/** Own reservation fixtures for availability tests. */
public class InMemoryReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations = new ArrayList<>();

    /** @param reservation the test reservation to add */
    public void add(Reservation reservation) {
        reservations.add(reservation);
    }

    @Override
    public List<Reservation> findByRoom(long roomId) {
        return reservations.stream().filter(value -> value.getRoomId() == roomId).toList();
    }
}
