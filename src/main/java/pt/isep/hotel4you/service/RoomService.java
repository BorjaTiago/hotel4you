package pt.isep.hotel4you.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import pt.isep.hotel4you.data.DuplicateRoomException;
import pt.isep.hotel4you.data.RoomLimitExceededException;
import pt.isep.hotel4you.data.RoomRepository;
import pt.isep.hotel4you.data.RoomTypeRepository;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Applies room registration and base price rules. */
public class RoomService {

    private static final int ROOMS_PER_FLOOR = 10;
    private static final Map<Integer, Integer> ROOM_FLOOR_LIMITS =
            Map.of(2, ROOMS_PER_FLOOR, 3, ROOMS_PER_FLOOR);

    /** The biggest price that fits in DECIMAL(8,2). */
    private static final BigDecimal MAX_PRICE = new BigDecimal("999999.99");
    private static final int PRICE_SCALE = 2;
    /** Plain ASCII digits only. BigDecimal alone also accepts exponents and non-ASCII digits. */
    private static final String PRICE_TEXT = "-?\\d+(\\.\\d+)?";

    /** Fixed labels and provisional Q-01 targets for the room summary. */
    private static final List<RoomTypeTarget> ROOM_TYPE_TARGETS = List.of(
            new RoomTypeTarget("individual", "Individuais", 7),
            new RoomTypeTarget("duplo", "Duplos", 10),
            new RoomTypeTarget("familiar", "Familiares", 3));

    /**
     * Fixed summary data. A target is not a registration limit.
     *
     * @param name the catalogue name
     * @param label the Portuguese summary label
     * @param total the provisional total for the hotel
     */
    public record RoomTypeTarget(String name, String label, int total) { }

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    /**
     * Creates the service.
     *
     * @param roomRepository the room store
     * @param roomTypeRepository the room type store
     */
    public RoomService(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
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
     * Returns the labels and provisional type totals for the room summary.
     *
     * @return immutable summary data, not enforced type limits
     */
    public List<RoomTypeTarget> roomTypeTargets() {
        return ROOM_TYPE_TARGETS;
    }

    /**
     * Saves a room. Only a manager may register a room.
     * The repository checks the floor, room type, floor limit and room number inside its save
     * transaction.
     *
     * @param room the room to save
     * @param actor the logged-in user
     * @return the saved room
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the floor or room type is invalid
     * @throws DuplicateRoomException if the room number is already in use
     * @throws RoomLimitExceededException if the floor already has 10 rooms
     * @throws IllegalStateException if the database is not available
     */
    public Room saveRoom(Room room, User actor) {
        if (!Permission.MANAGE_ROOMS.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode registar quartos.");
        }
        if (room == null) {
            throw new IllegalArgumentException("Indique os dados do quarto.");
        }
        Integer floorLimit = ROOM_FLOOR_LIMITS.get(room.getFloorNumber());
        if (floorLimit == null) {
            throw new IllegalArgumentException("Só os pisos 2 e 3 têm quartos.");
        }
        return roomRepository.save(room, floorLimit);
    }

    /**
     * Lists saved rooms.
     *
     * @param actor the logged-in user
     * @return all saved rooms
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<Room> listRooms(User actor) {
        if (!Permission.MANAGE_ROOMS.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode consultar os quartos.");
        }
        return roomRepository.findAll();
    }

    /**
     * Lists the room types from the catalogue.
     *
     * @param actor the logged-in user
     * @return all room type names
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<String> listRoomTypes(User actor) {
        if (!Permission.MANAGE_ROOMS.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode consultar os tipos de quarto.");
        }
        return roomTypeRepository.findAll().stream().map(RoomType::getName).toList();
    }

    /**
     * Lists the room types with their base price.
     *
     * @param actor the logged-in user who asks
     * @return the room types ordered by id
     * @throws AccessDeniedException if the actor may not see the prices
     * @throws IllegalStateException if the database is not available
     */
    public List<RoomType> listBasePrices(User actor) {
        if (!Permission.MANAGE_PRICES.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode consultar os preços.");
        }
        return roomTypeRepository.findAll();
    }

    /**
     * Converts the text of the screen to a price. It accepts a comma or a point as decimal mark.
     *
     * @param text the text typed by the user
     * @return the price
     * @throws IllegalArgumentException if the text is null, blank or not a number
     */
    public BigDecimal parsePrice(String text) {
        if (text == null) {
            throw new IllegalArgumentException("O preço tem de ser um número.");
        }
        String number = text.trim().replace(',', '.');
        if (!number.matches(PRICE_TEXT)) {
            throw new IllegalArgumentException("O preço tem de ser um número.");
        }
        return new BigDecimal(number);
    }

    /**
     * Sets the base price per night of a room type.
     *
     * @param type the room type
     * @param price the new price, from 0.00 to 999999.99 with at most two decimals
     * @param actor the logged-in user who asks
     * @throws AccessDeniedException if the actor may not set the prices
     * @throws IllegalArgumentException if the type or the price is not valid, or the type does not exist
     * @throws IllegalStateException if the database is not available
     */
    public void setBasePrice(RoomType type, BigDecimal price, User actor) {
        if (!Permission.MANAGE_PRICES.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode definir os preços.");
        }
        if (type == null || type.getId() == null) {
            throw new IllegalArgumentException("Indique o tipo de quarto.");
        }
        if (!isValidPrice(price)) {
            throw new IllegalArgumentException(
                    "O preço tem de estar entre 0,00 e 999999,99, com duas casas decimais.");
        }
        if (!roomTypeRepository.updateBasePrice(type.getId(), price.setScale(PRICE_SCALE))) {
            throw new IllegalArgumentException("O tipo de quarto indicado não existe.");
        }
    }

    private static boolean isValidPrice(BigDecimal price) {
        return price != null
                && price.signum() >= 0
                && price.scale() <= PRICE_SCALE
                && price.compareTo(MAX_PRICE) <= 0;
    }
}
