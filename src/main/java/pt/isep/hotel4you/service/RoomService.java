package pt.isep.hotel4you.service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import pt.isep.hotel4you.data.DuplicateRoomException;
import pt.isep.hotel4you.data.RoomLimitExceededException;
import pt.isep.hotel4you.data.RoomRepository;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomSummary;
import pt.isep.hotel4you.domain.User;

/** Applies room registration rules. */
public class RoomService {

    private static final int ROOMS_PER_FLOOR = 10;
    private static final Map<Integer, Integer> ROOM_FLOOR_LIMITS =
            Map.of(2, ROOMS_PER_FLOOR, 3, ROOMS_PER_FLOOR);

    private final RoomRepository roomRepository;

    /**
     * Creates the service.
     *
     * @param roomRepository the room store
     */
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Returns the room floors and limits from the statement for the screen.
     *
     * @return an immutable map from floor number to maximum room count
     */
    public Map<Integer, Integer> roomFloorLimits() {
        return ROOM_FLOOR_LIMITS;
    }

    /**
     * Saves a room. Only a manager may register a room. Calls are serialized in this service, and
     * the repository checks the floor limit again in its save operation.
     *
     * @param room the room to save
     * @param actor the logged-in user
     * @return the saved room
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the floor or room type is invalid
     * @throws DuplicateRoomException if the room number is already in use
     * @throws RoomLimitExceededException if the floor already has 10 rooms
     */
    public synchronized Room saveRoom(Room room, User actor) {
        if (!Permission.MANAGE_ROOMS.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode registar quartos.");
        }
        if (room == null) {
            throw new IllegalArgumentException("Indique os dados do quarto.");
        }
        if (!roomRepository.floorExists(room.floorNumber())) {
            throw new IllegalArgumentException("O piso indicado não existe.");
        }
        Integer floorLimit = ROOM_FLOOR_LIMITS.get(room.floorNumber());
        if (floorLimit == null) {
            throw new IllegalArgumentException("O piso indicado não tem quartos.");
        }
        if (!roomRepository.roomTypeExists(room.roomTypeName())) {
            throw new IllegalArgumentException("O tipo de quarto indicado não existe.");
        }
        if (roomRepository.existsByNumber(room.number())) {
            throw new DuplicateRoomException();
        }
        if (roomRepository.countByFloor(room.floorNumber()) >= floorLimit) {
            throw new RoomLimitExceededException(floorLimit);
        }
        return roomRepository.save(room, floorLimit);
    }

    /**
     * Lists saved rooms.
     *
     * @return all saved rooms
     */
    public List<Room> listRooms() {
        return roomRepository.findAll();
    }

    /**
     * Lists the rooms with type, floor and base price. Receptionists and managers may see the list.
     *
     * @param roomTypeName the room type to show, or null or blank to show all rooms
     * @param actor the logged-in user
     * @return the rooms ordered by number, or an empty list if no room matches
     * @throws AccessDeniedException if the actor is not logged in or may not see the list
     */
    public List<RoomSummary> listRoomSummaries(String roomTypeName, User actor) {
        if (!Permission.LIST_ROOMS.allows(actor)) {
            throw new AccessDeniedException("Não tem permissão para ver a lista de quartos.");
        }
        List<RoomSummary> rooms = roomRepository.findSummaries();
        if (roomTypeName == null || roomTypeName.isBlank()) {
            return rooms;
        }
        String wantedType = roomTypeName.trim().toLowerCase(Locale.ROOT);
        return rooms.stream()
                .filter(room -> room.roomTypeName().toLowerCase(Locale.ROOT).equals(wantedType))
                .toList();
    }

    /**
     * Lists the room types from the catalogue.
     *
     * @return all room type names
     */
    public List<String> listRoomTypes() {
        return roomRepository.findRoomTypeNames();
    }
}
