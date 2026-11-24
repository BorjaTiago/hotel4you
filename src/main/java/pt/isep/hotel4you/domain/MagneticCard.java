package pt.isep.hotel4you.domain;

import java.time.LocalDateTime;

/** A magnetic card. It belongs to one reservation or to one staff user, never to both. */
public class MagneticCard {

    /** The longest card number the database accepts. */
    public static final int MAX_NUMBER_LENGTH = 20;

    private final Long id;
    private final String number;
    private final CardType type;
    private final Long reservationId;
    private final Long userId;
    private final boolean active;
    private final LocalDateTime blockedAt;

    /**
     * Creates a card.
     *
     * @param id the identifier, or null if the card is not saved yet
     * @param number the card number, not blank and at most 20 characters
     * @param type the card type
     * @param reservationId the reservation of the guest, or null for a staff card
     * @param userId the staff user or manager, or null for a guest card
     * @param active true if the card works
     * @param blockedAt when the card was blocked, or null if it was never blocked
     * @throws IllegalArgumentException if the number or type is not valid, if the card has both
     *         owners or none, or if a master card has no user
     */
    public MagneticCard(Long id, String number, CardType type, Long reservationId, Long userId,
            boolean active, LocalDateTime blockedAt) {
        if (number == null || number.isBlank() || number.length() > MAX_NUMBER_LENGTH) {
            throw new IllegalArgumentException("O número do cartão é inválido.");
        }
        if (type == null) {
            throw new IllegalArgumentException("O tipo do cartão é obrigatório.");
        }
        if ((reservationId == null) == (userId == null)) {
            throw new IllegalArgumentException("O cartão pertence a uma reserva ou a um utilizador, nunca a ambos.");
        }
        if (type == CardType.MASTER && userId == null) {
            throw new IllegalArgumentException("O cartão mestre pertence a um utilizador.");
        }
        this.id = id;
        this.number = number;
        this.type = type;
        this.reservationId = reservationId;
        this.userId = userId;
        this.active = active;
        this.blockedAt = blockedAt;
    }

    /**
     * Returns the identifier.
     *
     * @return the identifier, or null if the card is not saved yet
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the card number.
     *
     * @return the card number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Returns the card type.
     *
     * @return the card type
     */
    public CardType getType() {
        return type;
    }

    /**
     * Returns the reservation of the card.
     *
     * @return the reservation id, or null for a staff card
     */
    public Long getReservationId() {
        return reservationId;
    }

    /**
     * Returns the staff user of the card.
     *
     * @return the user id, or null for a guest card
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Tells if the card works.
     *
     * @return true if the card is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns when the card was blocked.
     *
     * @return the block time, or null if the card was never blocked
     */
    public LocalDateTime getBlockedAt() {
        return blockedAt;
    }

    /**
     * Tells if the card was blocked.
     *
     * @return true if the card has a block time
     */
    public boolean isBlocked() {
        return blockedAt != null;
    }
}
