package pt.isep.hotel4you.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A saved room service request of a guest with check-in. It is one row of the table
 * {@code reservation_services} (Q-33).
 */
public class RoomServiceRequest {
    private final long id;
    private final long reservationId;
    private final int roomNumber;
    private final RoomServiceType type;
    private final BigDecimal unitPrice;
    private final LocalDateTime requestedAt;

    /**
     * Creates a saved request.
     *
     * @param id the saved identifier
     * @param reservationId the reservation with check-in of the room
     * @param roomNumber the room number
     * @param type the service type
     * @param unitPrice the catalogue price copied at the request, or null if not set
     * @param requestedAt the date and time of the request
     */
    public RoomServiceRequest(long id, long reservationId, int roomNumber, RoomServiceType type,
            BigDecimal unitPrice, LocalDateTime requestedAt) {
        this.id = id;
        this.reservationId = reservationId;
        this.roomNumber = roomNumber;
        this.type = Objects.requireNonNull(type);
        this.unitPrice = unitPrice;
        this.requestedAt = Objects.requireNonNull(requestedAt);
    }

    /** @return the saved identifier */
    public long getId() { return id; }
    /** @return the reservation with check-in of the room */
    public long getReservationId() { return reservationId; }
    /** @return the room number */
    public int getRoomNumber() { return roomNumber; }
    /** @return the service type */
    public RoomServiceType getType() { return type; }
    /** @return the price copied at the request, or null if the catalogue has no price */
    public BigDecimal getUnitPrice() { return unitPrice; }
    /** @return the date and time of the request */
    public LocalDateTime getRequestedAt() { return requestedAt; }
}
