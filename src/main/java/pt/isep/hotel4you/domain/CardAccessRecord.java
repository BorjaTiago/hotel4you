package pt.isep.hotel4you.domain;

import java.time.LocalDateTime;

/** One entry of a card. It points to a room or to a zone, never to both. */
public class CardAccessRecord {

    private final Long id;
    private final long cardId;
    private final Long roomId;
    private final Long locationId;
    private final LocalDateTime entryDateTime;

    /**
     * Creates an entry.
     *
     * @param id the identifier, or null if the entry is not saved yet
     * @param cardId the id of the card that opened the door
     * @param roomId the room that was entered, or null if it was a zone
     * @param locationId the zone that was entered, or null if it was a room
     * @param entryDateTime the date and time of the entry
     * @throws IllegalArgumentException if the entry has both a room and a zone, or none, or no time
     */
    public CardAccessRecord(Long id, long cardId, Long roomId, Long locationId, LocalDateTime entryDateTime) {
        if ((roomId == null) == (locationId == null)) {
            throw new IllegalArgumentException("A entrada é num quarto ou num local, nunca em ambos.");
        }
        if (entryDateTime == null) {
            throw new IllegalArgumentException("A data e hora da entrada são obrigatórias.");
        }
        this.id = id;
        this.cardId = cardId;
        this.roomId = roomId;
        this.locationId = locationId;
        this.entryDateTime = entryDateTime;
    }

    /**
     * Returns the identifier.
     *
     * @return the identifier, or null if the entry is not saved yet
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the card of the entry.
     *
     * @return the card id
     */
    public long getCardId() {
        return cardId;
    }

    /**
     * Returns the room that was entered.
     *
     * @return the room id, or null if it was a zone
     */
    public Long getRoomId() {
        return roomId;
    }

    /**
     * Returns the zone that was entered.
     *
     * @return the zone id, or null if it was a room
     */
    public Long getLocationId() {
        return locationId;
    }

    /**
     * Returns the time of the entry.
     *
     * @return the date and time of the entry
     */
    public LocalDateTime getEntryDateTime() {
        return entryDateTime;
    }
}
