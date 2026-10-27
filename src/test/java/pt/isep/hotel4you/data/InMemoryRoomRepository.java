package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import pt.isep.hotel4you.domain.Room;

/** Keeps room and reference data in memory for unit tests. */
public class InMemoryRoomRepository implements RoomRepository {

    private final Set<Integer> floors = new HashSet<>();
    private final Set<String> roomTypes = new HashSet<>();
    private final List<Room> rooms = new ArrayList<>();

    /** Adds a floor to the test catalogue. */
    public synchronized void addFloor(int floorNumber) {
        floors.add(floorNumber);
    }

    /** Adds a room type to the test catalogue. */
    public synchronized void addRoomType(String roomTypeName) {
        roomTypes.add(roomTypeName.toLowerCase(Locale.ROOT));
    }

    @Override
    public synchronized boolean floorExists(int floorNumber) {
        return floors.contains(floorNumber);
    }

    @Override
    public synchronized boolean roomTypeExists(String roomTypeName) {
        return roomTypeName != null && roomTypes.contains(roomTypeName.toLowerCase(Locale.ROOT));
    }

    @Override
    public synchronized List<String> findRoomTypeNames() {
        return roomTypes.stream().sorted().toList();
    }

    @Override
    public synchronized boolean existsByNumber(int roomNumber) {
        return rooms.stream().anyMatch(room -> room.number() == roomNumber);
    }

    @Override
    public synchronized int countByFloor(int floorNumber) {
        return (int) rooms.stream().filter(room -> room.floorNumber() == floorNumber).count();
    }

    @Override
    public synchronized List<Room> findAll() {
        return List.copyOf(rooms);
    }

    @Override
    public synchronized Room saveWithinFloorLimit(Room room, int floorLimit) {
        if (!floorExists(room.floorNumber())) {
            throw new IllegalArgumentException("O piso indicado não existe.");
        }
        if (!roomTypeExists(room.roomTypeName())) {
            throw new IllegalArgumentException("O tipo de quarto indicado não existe.");
        }
        if (existsByNumber(room.number())) {
            throw new DuplicateRoomException();
        }
        if (countByFloor(room.floorNumber()) >= floorLimit) {
            throw new RoomLimitExceededException(floorLimit);
        }
        Room saved = new Room((long) rooms.size() + 1, room.number(),
                room.floorNumber(), room.roomTypeName());
        rooms.add(saved);
        return saved;
    }
}
