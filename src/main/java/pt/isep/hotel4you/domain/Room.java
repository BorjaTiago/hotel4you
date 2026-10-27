package pt.isep.hotel4you.domain;

/**
 * A hotel room with its visible number, floor and room type.
 *
 * @param id the database identifier, or null before the room is saved
 * @param number the visible room number
 * @param floorNumber the floor number
 * @param roomTypeName the room type name from the room type catalogue
 */
public record Room(Long id, int number, int floorNumber, String roomTypeName) {

    /**
     * Creates a room value.
     *
     * @param id the database identifier, or null before the room is saved
     * @param number the visible room number
     * @param floorNumber the floor number
     * @param roomTypeName the room type name from the room type catalogue
     * @throws IllegalArgumentException if the room number or type name is invalid
     */
    public Room {
        if (number < 1 || number > Short.MAX_VALUE) {
            throw new IllegalArgumentException("O número do quarto é inválido.");
        }
        if (roomTypeName == null || roomTypeName.isBlank()) {
            throw new IllegalArgumentException("Indique o tipo de quarto.");
        }
        roomTypeName = roomTypeName.trim();
    }
}
