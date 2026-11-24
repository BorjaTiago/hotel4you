package pt.isep.hotel4you.data;

import java.util.List;
import java.util.Optional;
import pt.isep.hotel4you.domain.MagneticCard;

/** Stores and finds magnetic cards. */
public interface CardRepository {

    /**
     * Finds a card by its number.
     *
     * @param number the card number
     * @return the card, or empty if no card has this number
     * @throws IllegalStateException if the database is not available
     */
    Optional<MagneticCard> findByNumber(String number);

    /**
     * Checks if a card with this number exists. A number is never used twice.
     *
     * @param number the card number
     * @return true if a card has this number
     * @throws IllegalStateException if the database is not available
     */
    boolean existsByNumber(String number);

    /**
     * Finds the card of a reservation.
     *
     * @param reservationId the reservation id
     * @return the card, or empty if the reservation has no card
     * @throws IllegalStateException if the database is not available
     */
    Optional<MagneticCard> findByReservationId(long reservationId);

    /**
     * Finds the cards of a staff user or manager, the oldest card first.
     *
     * @param userId the user id
     * @return the cards of the user, or an empty list
     * @throws IllegalStateException if the database is not available
     */
    List<MagneticCard> findByUserId(long userId);

    /**
     * Finds all master cards, blocked or not, the oldest card first.
     *
     * @return the master cards, or an empty list
     * @throws IllegalStateException if the database is not available
     */
    List<MagneticCard> findMasters();

    /**
     * Saves a new card with the hash of its access code.
     *
     * @param card the new card, its id is ignored
     * @return the saved card with its new id
     * @throws IllegalStateException if the database is not available
     */
    MagneticCard save(MagneticCard card);

    /**
     * Changes the active flag and the block time of a saved card. The number and the owner stay the same.
     *
     * @param card the card with its number and the new values
     * @return true if the card was changed, false if no card has this number
     * @throws IllegalStateException if the database is not available
     */
    boolean update(MagneticCard card);
}
