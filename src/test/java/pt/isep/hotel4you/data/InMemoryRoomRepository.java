package pt.isep.hotel4you.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomSummary;

/** Keeps room and reference data in memory for unit tests. */
public class InMemoryRoomRepository implements RoomRepository {

    private final Set<Integer> floors = new HashSet<>();
    private final Set<String> roomTypes = new HashSet<>();
    private final List<Room> rooms = new ArrayList<>();
    private final Map<String, BigDecimal> basePrices = new HashMap<>();

    /** Adds a floor to the test catalogue. */
    public synchronized void addFloor(int floorNumber) {
        floors.add(floorNumber);
    }

    /** Adds a room type to the test catalogue. */
    public synchronized void addRoomType(String roomTypeName) {
        roomTypes.add(roomTypeName.toLowerCase(Locale.ROOT));
    }

    /** Sets the base price per night of a room type in the test catalogue. */
    public synchronized void setBasePrice(String roomTypeName, BigDecimal basePrice) {
        basePrices.put(roomTypeName.toLowerCase(Locale.ROOT), basePrice);
    }

    private boolean floorExists(int floorNumber) {
        return floors.contains(floorNumber);
    }

    private boolean roomTypeExists(String roomTypeName) {
        return roomTypeName != null && roomTypes.contains(roomTypeName.toLowerCase(Locale.ROOT));
    }

    private boolean existsByNumber(int roomNumber) {
        return rooms.stream().anyMatch(room -> room.getNumber() == roomNumber);
    }

    private int countByFloor(int floorNumber) {
        return (int) rooms.stream().filter(room -> room.getFloorNumber() == floorNumber).count();
    }

    @Override
    public synchronized List<Room> findAll() {
        return List.copyOf(rooms);
    }

    @Override
    public synchronized List<RoomSummary> findSummaries() {
        return rooms.stream()
                .sorted(Comparator.comparingInt(Room::getNumber))
                .map(room -> new RoomSummary(room.getNumber(), room.getFloorNumber(), room.getRoomTypeName(),
                        basePrices.get(room.getRoomTypeName().toLowerCase(Locale.ROOT))))
                .toList();
    }

    @Override
    public synchronized Room save(Room room, int floorLimit) {
        if (!floorExists(room.getFloorNumber())) {
            throw new IllegalArgumentException("O piso indicado não existe.");
        }
        if (!roomTypeExists(room.getRoomTypeName())) {
            throw new IllegalArgumentException("O tipo de quarto indicado não existe.");
        }
        if (countByFloor(room.getFloorNumber()) >= floorLimit) {
            throw new RoomLimitExceededException(floorLimit);
        }
        if (existsByNumber(room.getNumber())) {
            throw new DuplicateRoomException();
        }
        Room saved = new Room((long) rooms.size() + 1, room.getNumber(),
                room.getFloorNumber(), room.getRoomTypeName());
        rooms.add(saved);
        return saved;
    }
}
