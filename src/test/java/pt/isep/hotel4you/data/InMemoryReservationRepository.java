package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import pt.isep.hotel4you.domain.ActiveReservation;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;

/** Own reservation fixtures for availability and reservation tests. */
public class InMemoryReservationRepository implements ReservationRepository {
    private final List<Reservation> reservations = new ArrayList<>();
    private final List<Reservation> saved = new ArrayList<>();
    private final Map<Long, Integer> roomNumbers = new HashMap<>();
    private final Map<Long, String[]> clients = new HashMap<>();
    private long nextId = 100;
    public int findByRoomCalls;
    public int findByRoomsCalls;
    /** When true, the next save acts as if another user saved the same nights first. */
    public boolean loseNextSave;
    /** When true, the next check-in or check-out acts as if another user changed the state first. */
    public boolean loseNextUpdate;

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

    /**
     * Sets the name and the document of a client, as the customers table does in SQL.
     * @param clientId the client identifier
     * @param name the client name
     * @param document the client document number
     */
    public void addClient(long clientId, String name, String document) {
        clients.put(clientId, new String[] {name, document});
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
        if (loseNextSave) {
            loseNextSave = false;
            throw new RoomNotAvailableException();
        }
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

    @Override
    public Optional<Reservation> findById(long id) {
        return reservations.stream().filter(value -> Objects.equals(value.getId(), id)).findFirst();
    }

    @Override
    public List<ActiveReservation> findActive() {
        return reservations.stream()
                .filter(value -> value.getStatus() == ReservationStatus.RESERVED
                        || value.getStatus() == ReservationStatus.CHECKED_IN)
                .map(value -> new ActiveReservation(value.getId(),
                        clients.getOrDefault(value.getClientId(), new String[] {"", ""})[0],
                        clients.getOrDefault(value.getClientId(), new String[] {"", ""})[1],
                        roomNumbers.getOrDefault(value.getRoomId(), 0), value.getStartDate(),
                        value.getEndDate(), value.getStatus()))
                .toList();
    }

    @Override
    public boolean checkIn(long id) {
        return changeStatus(id, ReservationStatus.RESERVED, ReservationStatus.CHECKED_IN);
    }

    @Override
    public boolean checkOut(long id) {
        return changeStatus(id, ReservationStatus.CHECKED_IN, ReservationStatus.CHECKED_OUT);
    }

    private boolean changeStatus(long id, ReservationStatus from, ReservationStatus to) {
        if (loseNextUpdate) {
            loseNextUpdate = false;
            return false;
        }
        for (int i = 0; i < reservations.size(); i++) {
            Reservation value = reservations.get(i);
            if (Objects.equals(value.getId(), id) && value.getStatus() == from) {
                reservations.set(i, new Reservation(value.getId(), value.getClientId(),
                        value.getRoomId(), value.getStartDate(), value.getEndDate(), to,
                        value.getCreatedByUserId()));
                return true;
            }
        }
        return false;
    }
}
