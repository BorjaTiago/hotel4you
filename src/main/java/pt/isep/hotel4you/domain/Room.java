package pt.isep.hotel4you.domain;

/** A hotel room with its visible number, floor and room type. */
public class Room {

    /** The greatest positive room number supported by SQL Server SMALLINT. */
    public static final int MAX_NUMBER = Short.MAX_VALUE;

    private final Long id;
    private final int number;
    private final int floorNumber;
    private final String roomTypeName;

    /**
     * Creates a room entity.
     *
     * @param id the database identifier, or null before the room is saved
     * @param number the visible room number, from 1 to MAX_NUMBER
     * @param floorNumber the floor number
     * @param roomTypeName the room type name from the catalogue
     * @throws IllegalArgumentException if the number or type name is invalid
     */
    public Room(Long id, int number, int floorNumber, String roomTypeName) {
        if (number < 1 || number > MAX_NUMBER) {
            throw new IllegalArgumentException("O número do quarto é inválido.");
        }
        if (roomTypeName == null || roomTypeName.isBlank()) {
            throw new IllegalArgumentException("Indique o tipo de quarto.");
        }
        this.id = id;
        this.number = number;
        this.floorNumber = floorNumber;
        this.roomTypeName = roomTypeName.trim();
    }

    /** @return the database identifier, or null before saving */
    public Long getId() {
        return id;
    }

    /** @return the visible room number */
    public int getNumber() {
        return number;
    }

    /** @return the floor number */
    public int getFloorNumber() {
        return floorNumber;
    }

    /** @return the stored room type name */
    public String getRoomTypeName() {
        return roomTypeName;
    }
}
