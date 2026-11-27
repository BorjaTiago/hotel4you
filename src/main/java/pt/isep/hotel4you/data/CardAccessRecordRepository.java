package pt.isep.hotel4you.data;

import java.util.List;
import pt.isep.hotel4you.domain.CardAccessRecord;

/** Stores and finds the entries of the cards. A saved entry is never changed. */
public interface CardAccessRecordRepository {

    /**
     * Saves a new entry.
     *
     * @param record the new entry, its id is ignored
     * @return the saved entry with its new id
     * @throws IllegalStateException if the database is not available
     */
    CardAccessRecord save(CardAccessRecord record);

    /**
     * Finds the entries of a card, the oldest first.
     *
     * @param cardId the card id
     * @return the entries, or an empty list if the card has none
     * @throws IllegalStateException if the database is not available
     */
    List<CardAccessRecord> findByCardId(long cardId);

    /**
     * Finds the entries in a room, the oldest first.
     *
     * @param roomId the room id
     * @return the entries, or an empty list if nobody entered the room
     * @throws IllegalStateException if the database is not available
     */
    List<CardAccessRecord> findByRoomId(long roomId);
}
