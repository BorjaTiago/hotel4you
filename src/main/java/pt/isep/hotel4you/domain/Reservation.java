package pt.isep.hotel4you.domain;

import java.time.LocalDate;
import java.util.Objects;

/** The persisted booking fields needed to decide room availability. */
public class Reservation {
    private final Long id;
    private final long roomId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final ReservationStatus status;

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
        validatePeriod(startDate, endDate);
        this.id = id;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = Objects.requireNonNull(status);
    }

    /** @return the booking identifier */
    public Long getId() { return id; }
    /** @return the room identifier */
    public long getRoomId() { return roomId; }
    /** @return the first occupied night */
    public LocalDate getStartDate() { return startDate; }
    /** @return the departure date */
    public LocalDate getEndDate() { return endDate; }
    /** @return the stored state */
    public ReservationStatus getStatus() { return status; }

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
