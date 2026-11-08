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

    @Test
    @Tag("H4Y-82-AC-1")
    void onCheckIn_returnCheckedIn_whenReservationIsReserved() {
        Reservation reservation = new Reservation(1L, 7L, 2L, START, END, ReservationStatus.RESERVED, 4L);

        Reservation checkedIn = reservation.checkIn();

        assertEquals(ReservationStatus.CHECKED_IN, checkedIn.getStatus());
        assertEquals(1L, checkedIn.getId());
        assertEquals(7L, checkedIn.getClientId());
        assertEquals(2L, checkedIn.getRoomId());
        assertEquals(START, checkedIn.getStartDate());
        assertEquals(END, checkedIn.getEndDate());
        assertEquals(4L, checkedIn.getCreatedByUserId());
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"CHECKED_IN", "CHECKED_OUT"})
    @Tag("H4Y-82-AC-2")
    void onCheckIn_throw_whenReservationHasCheckIn(ReservationStatus status) {
        Reservation reservation = new Reservation(1L, 2L, START, END, status);

        IllegalStateException error = assertThrows(IllegalStateException.class, reservation::checkIn);
        assertEquals("A reserva já tem check-in.", error.getMessage());
        assertEquals(status, reservation.getStatus());
    }

    @Test
    @Tag("H4Y-82-AC-3")
    void onCheckIn_throw_whenReservationIsCancelled() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.CANCELLED);

        IllegalStateException error = assertThrows(IllegalStateException.class, reservation::checkIn);
        assertEquals("A reserva está cancelada.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-5")
    void onCheckOut_returnCheckedOut_whenReservationIsCheckedIn() {
        Reservation reservation = new Reservation(1L, 7L, 2L, START, END, ReservationStatus.CHECKED_IN, 4L);

        Reservation checkedOut = reservation.checkOut();

        assertEquals(ReservationStatus.CHECKED_OUT, checkedOut.getStatus());
        assertEquals(1L, checkedOut.getId());
        assertEquals(7L, checkedOut.getClientId());
        assertEquals(2L, checkedOut.getRoomId());
        assertEquals(4L, checkedOut.getCreatedByUserId());
    }

    @Test
    @Tag("H4Y-82-AC-6")
    void onCheckOut_throw_whenReservationHasNoCheckIn() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.RESERVED);

        IllegalStateException error = assertThrows(IllegalStateException.class, reservation::checkOut);
        assertEquals("A reserva não tem check-in.", error.getMessage());
        assertEquals(ReservationStatus.RESERVED, reservation.getStatus());
    }

    @Test
    @Tag("H4Y-82-AC-6")
    void onCheckOut_throw_whenReservationIsCancelled() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.CANCELLED);

        IllegalStateException error = assertThrows(IllegalStateException.class, reservation::checkOut);
        assertEquals("A reserva está cancelada.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-7")
    void onCheckOut_throw_whenReservationHasCheckOut() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.CHECKED_OUT);

        IllegalStateException error = assertThrows(IllegalStateException.class, reservation::checkOut);
        assertEquals("A reserva já tem check-out.", error.getMessage());
    }
}
