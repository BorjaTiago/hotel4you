package pt.isep.hotel4you.domain;

/**
 * A place on the screen "O meu cartão": a room or a zone, and if the card may enter it. The
 * place has a room or a zone, never both.
 *
 * @param name the text of the button, for example "Quarto 202" or "Piscina"
 * @param room the room, or null for a zone
 * @param location the zone, or null for a room
 * @param allowed true if the card may enter the place
 */
public record CardPlace(String name, Room room, Location location, boolean allowed) {
}
