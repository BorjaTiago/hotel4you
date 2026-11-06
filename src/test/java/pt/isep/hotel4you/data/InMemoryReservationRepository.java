package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.Reservation;

/** Own reservation fixtures for availability and reservation tests. */
public class InMemoryReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations = new ArrayList<>();
    private final List<Reservation> saved = new ArrayList<>();
    private final Map<Long, Integer> roomNumbers = new HashMap<>();
    private long nextId = 100;
    public int findByRoomCalls;
    public int findByRoomsCalls;

    /** @param reservation the test reservation to add */
    public void add(Reservation reservation) {
        reservations.add(reservation);
    }

    /**
     * Sets the visible number of a room, as the rooms table does in SQL.
     * @param roomId the room identifier
     * @param number the visible room number
     */
    public void addRoomNumber(long roomId, int number) {
        roomNumbers.put(roomId, number);
    }

    /** @return the reservations saved through save, in save order */
    public List<Reservation> saved() {
        return List.copyOf(saved);
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

    @Override
    public Reservation save(Reservation reservation) {
        Reservation stored = new Reservation(nextId++, reservation.getClientId(),
                reservation.getRoomId(), reservation.getStartDate(), reservation.getEndDate(),
                reservation.getStatus(), reservation.getCreatedByUserId());
        reservations.add(stored);
        saved.add(stored);
        return stored;
    }

    @Override
    public List<ClientReservation> findByClient(long clientId) {
        return reservations.stream()
                .filter(value -> Objects.equals(value.getClientId(), clientId))
                .map(value -> new ClientReservation(value.getId(),
                        roomNumbers.getOrDefault(value.getRoomId(), 0), value.getStartDate(),
                        value.getEndDate(), value.getStatus()))
                .toList();
    }
}
