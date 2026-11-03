package pt.isep.hotel4you.domain;

import java.util.Locale;

/** The four reservation states stored in SQL Server. */
public enum ReservationStatus {
    /** A booking that has not checked in. */
    RESERVED,
    /** A cancelled booking does not occupy nights. */
    CANCELLED,
    /** The guest has checked in. */
    CHECKED_IN,
    /** The guest has checked out. */
    CHECKED_OUT;

    /**
     * Reads the stored state without changing its meaning.
     * @param value the database value
     * @return the matching state
     * @throws IllegalArgumentException if the value is not a known state
     */
    public static ReservationStatus fromDatabase(String value) {
        return valueOf(value.toUpperCase(Locale.ROOT));
    }
}
