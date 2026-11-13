package pt.isep.hotel4you.domain;

import java.time.LocalDate;
import java.util.Objects;

/** A room booking of a client for a period of nights. */
public class Reservation {
    private final Long id;
    private final Long clientId;
    private final long roomId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final ReservationStatus status;
    private final Long createdByUserId;

    /**
     * Reads a booking period. This constructor does not create a database booking.
     * @param id the persisted booking identifier
     * @param roomId the reserved room identifier
     * @param startDate the first occupied night
     * @param endDate the departure date, which is not occupied
     * @param status the stored state
     * @throws IllegalArgumentException if the period is invalid
     */
    public Reservation(Long id, long roomId, LocalDate startDate, LocalDate endDate,
            ReservationStatus status) {
        this(id, null, roomId, startDate, endDate, status, null);
    }

    /**
     * Creates a booking with its client and the user who made it.
     * @param id the booking identifier, or null before it is saved
     * @param clientId the client identifier, or null when the read does not need it
     * @param roomId the reserved room identifier
     * @param startDate the first occupied night
     * @param endDate the departure date, which is not occupied
     * @param status the state
     * @param createdByUserId the user who created the booking, or null when the read does not need it
     * @throws IllegalArgumentException if the period is invalid
     */
    public Reservation(Long id, Long clientId, long roomId, LocalDate startDate, LocalDate endDate,
            ReservationStatus status, Long createdByUserId) {
        validatePeriod(startDate, endDate);
        this.id = id;
        this.clientId = clientId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = Objects.requireNonNull(status);
        this.createdByUserId = createdByUserId;
    }

    /** @return the booking identifier */
    public Long getId() { return id; }
    /** @return the client identifier, or null when the read does not need it */
    public Long getClientId() { return clientId; }
    /** @return the room identifier */
    public long getRoomId() { return roomId; }
    /** @return the first occupied night */
    public LocalDate getStartDate() { return startDate; }
    /** @return the departure date */
    public LocalDate getEndDate() { return endDate; }
    /** @return the stored state */
    public ReservationStatus getStatus() { return status; }
    /** @return the user who created the booking, or null when the read does not need it */
    public Long getCreatedByUserId() { return createdByUserId; }

    /**
     * Checks that the booking allows the check-in. This method does not change the booking.
     * @param today the current date; the check-in is allowed from the start date until the day before the end date
     * @throws IllegalStateException if the booking is cancelled, already has a check-in or
     *         already has a check-out, or if today is before the start date or on or after the end date
     */
    public void checkCanCheckIn(LocalDate today) {
        if (status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("A reserva está cancelada.");
        }
        if (status == ReservationStatus.CHECKED_IN) {
            throw new IllegalStateException("A reserva já tem check-in.");
        }
        if (status == ReservationStatus.CHECKED_OUT) {
            throw new IllegalStateException("A reserva já tem check-out.");
        }
        if (today.isBefore(startDate)) {
            throw new IllegalStateException("O check-in só é possível a partir do dia de entrada.");
        }
        if (!today.isBefore(endDate)) {
            throw new IllegalStateException("A data de saída da reserva já passou.");
        }
    }

    /**
     * Checks that the booking allows the check-out. This method does not change the booking.
     * @throws IllegalStateException if the booking is cancelled, has no check-in or already has a check-out
     */
    public void checkCanCheckOut() {
        if (status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("A reserva está cancelada.");
        }
        if (status == ReservationStatus.RESERVED) {
            throw new IllegalStateException("A reserva não tem check-in.");
        }
        if (status == ReservationStatus.CHECKED_OUT) {
            throw new IllegalStateException("A reserva já tem check-out.");
        }
    }

    /**
     * Checks that the booking allows the cancel (RF-12, Q-05). This method does not change the booking.
     * @throws IllegalStateException if the booking is already cancelled, has a check-in or has a check-out
     */
    public void checkCanCancel() {
        if (status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("A reserva já está cancelada.");
        }
        if (status == ReservationStatus.CHECKED_IN) {
            throw new IllegalStateException("A reserva já tem check-in.");
        }
        if (status == ReservationStatus.CHECKED_OUT) {
            throw new IllegalStateException("A reserva já tem check-out.");
        }
    }

    /**
     * Tests half-open periods. Cancelled bookings never occupy a night.
     * @param start the requested arrival
     * @param end the requested departure
     * @return true if this non-cancelled booking occupies a requested night
     * @throws IllegalArgumentException if the requested period is invalid
     */
    public boolean overlaps(LocalDate start, LocalDate end) {
        validatePeriod(start, end);
        return status != ReservationStatus.CANCELLED
                && start.isBefore(endDate) && end.isAfter(startDate);
    }

    /**
     * Checks the shared period rule before any availability query.
     * @param start the arrival
     * @param end the departure
     * @throws IllegalArgumentException if a date is missing or departure is not after arrival
     */
    public static void validatePeriod(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Escolha a data de entrada e a data de saída.");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("A data de saída tem de ser depois da data de entrada.");
        }
    }
}
