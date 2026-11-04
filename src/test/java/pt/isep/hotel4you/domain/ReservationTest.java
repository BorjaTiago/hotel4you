package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

/** Tests the one overlap rule shared by availability and future reservation creation. */
class ReservationTest {
    private static final LocalDate START = LocalDate.of(2026, 11, 10);
    private static final LocalDate END = LocalDate.of(2026, 11, 12);

    @ParameterizedTest
    @CsvSource({"9,10,false", "8,9,false", "12,13,false", "13,14,false",
            "9,11,true", "10,12,true", "11,13,true", "9,13,true", "10,11,true"})
    @Tag("H4Y-54-AC-2")
    void onOverlaps_compareHalfOpenPeriods_whenReservationIsActive(int arrival, int departure,
            boolean expected) {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.RESERVED);

        assertEquals(expected, reservation.overlaps(START.withDayOfMonth(arrival),
                START.withDayOfMonth(departure)));
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"RESERVED", "CHECKED_IN", "CHECKED_OUT"})
    @Tag("H4Y-54-AC-2")
    void onOverlaps_returnTrue_whenNonCancelledReservationUsesNights(ReservationStatus status) {
        assertTrue(new Reservation(1L, 2L, START, END, status).overlaps(START, END));
    }

    @Test
    @Tag("H4Y-54-AC-3")
    void onOverlaps_returnFalse_whenReservationIsCancelled() {
        assertFalse(new Reservation(1L, 2L, START, END, ReservationStatus.CANCELLED)
                .overlaps(START, END));
    }

    @Test
    @Tag("H4Y-54-AC-4")
    void onOverlaps_throw_whenPeriodIsInvalid() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.RESERVED);
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> reservation.overlaps(START, START));
        assertEquals("A data de saída tem de ser depois da data de entrada.", error.getMessage());
    }

    @Test
    @Tag("H4Y-54-AC-4")
    void onValidatePeriod_throw_whenStartIsNull() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Reservation.validatePeriod(null, END));
        assertEquals("Escolha a data de entrada e a data de saída.", error.getMessage());
    }

    @Test
    @Tag("H4Y-54-AC-4")
    void onValidatePeriod_throw_whenEndIsNull() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Reservation.validatePeriod(START, null));
        assertEquals("Escolha a data de entrada e a data de saída.", error.getMessage());
    }

    @Test
    @Tag("H4Y-54-AC-4")
    void onValidatePeriod_throw_whenEndIsBeforeStart() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Reservation.validatePeriod(END, START));
        assertEquals("A data de saída tem de ser depois da data de entrada.", error.getMessage());
    }
}
