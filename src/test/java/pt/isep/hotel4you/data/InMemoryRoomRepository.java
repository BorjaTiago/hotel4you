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
import java.util.TreeMap;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomItem;
import pt.isep.hotel4you.domain.RoomSummary;
import pt.isep.hotel4you.domain.StockItem;

/** Keeps room and reference data in memory for unit tests. */
public class InMemoryRoomRepository implements RoomRepository {

    private final Set<Integer> floors = new HashSet<>();
    private final Set<String> roomTypes = new HashSet<>();
    private final List<Room> rooms = new ArrayList<>();
    private final Map<String, BigDecimal> basePrices = new HashMap<>();
    private final Set<Long> inUse = new HashSet<>();
    private final Map<Long, Map<Long, Integer>> roomItems = new HashMap<>();
    private final Set<String> itemsInUse = new HashSet<>();

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
                .map(room -> new RoomSummary(room.getId(), room.getNumber(), room.getFloorNumber(), "Quartos",
                        room.getRoomTypeName(), basePrices.get(room.getRoomTypeName().toLowerCase(Locale.ROOT))))
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

    @Override
    public synchronized Room update(Room room, int floorLimit) {
        if (!floorExists(room.getFloorNumber())) {
            throw new IllegalArgumentException("O piso indicado não existe.");
        }
        if (!roomTypeExists(room.getRoomTypeName())) {
            throw new IllegalArgumentException("O tipo de quarto indicado não existe.");
        }
        long others = rooms.stream().filter(other -> other.getFloorNumber() == room.getFloorNumber()
                && !other.getId().equals(room.getId())).count();
        if (others >= floorLimit) {
            throw new RoomLimitExceededException(floorLimit);
        }
        int index = -1;
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId().equals(room.getId())) {
                index = i;
            }
        }
        if (index < 0) {
            throw new IllegalArgumentException("O quarto indicado não existe.");
        }
        boolean duplicate = rooms.stream().anyMatch(other -> other.getNumber() == room.getNumber()
                && !other.getId().equals(room.getId()));
        if (duplicate) {
            throw new DuplicateRoomException();
        }
        Room saved = new Room(room.getId(), room.getNumber(), room.getFloorNumber(), room.getRoomTypeName());
        rooms.set(index, saved);
        return saved;
    }

    @Override
    public synchronized boolean delete(long id) {
        if (inUse.contains(id)) {
            return false;
        }
        if (!rooms.removeIf(room -> room.getId() == id)) {
            throw new IllegalArgumentException("O quarto indicado não existe.");
        }
        return true;
    }

    /** Marks a room as used by other data, so that delete refuses it. */
    public synchronized void markInUse(long id) {
        inUse.add(id);
    }

    /**
     * Returns the items of a room. The stock item is a placeholder with the saved id, because
     * this double does not hold the catalogue.
     */
    @Override
    public synchronized List<RoomItem> findItems(long roomId) {
        Map<Long, Integer> quantities = new TreeMap<>(roomItems.getOrDefault(roomId, Map.of()));
        return quantities.entrySet().stream()
                .map(entry -> new RoomItem(new StockItem(entry.getKey(), "item " + entry.getKey(), "un", 0),
                        entry.getValue()))
                .toList();
    }

    @Override
    public synchronized void saveItem(long roomId, long itemId, int quantity) {
        roomItems.computeIfAbsent(roomId, id -> new HashMap<>()).put(itemId, quantity);
    }

    @Override
    public synchronized boolean deleteItem(long roomId, long itemId) {
        if (itemsInUse.contains(roomId + ":" + itemId)) {
            return false;
        }
        roomItems.getOrDefault(roomId, new HashMap<>()).remove(itemId);
        return true;
    }

    /** Marks an item of a room as used by other data, so that deleteItem refuses it. */
    public synchronized void markItemInUse(long roomId, long itemId) {
        itemsInUse.add(roomId + ":" + itemId);
    }
}
