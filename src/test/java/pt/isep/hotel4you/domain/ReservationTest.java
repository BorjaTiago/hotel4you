package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    void onCheckCanCheckIn_notThrow_whenReservationIsReserved() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.RESERVED);

        assertDoesNotThrow(() -> reservation.checkCanCheckIn(START));
        assertEquals(ReservationStatus.RESERVED, reservation.getStatus());
    }

    @Test
    @Tag("H4Y-82-AC-2")
    void onCheckCanCheckIn_throw_whenReservationIsCheckedIn() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.CHECKED_IN);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> reservation.checkCanCheckIn(START));
        assertEquals("A reserva já tem check-in.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-2")
    void onCheckCanCheckIn_throw_whenReservationIsCheckedOut() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.CHECKED_OUT);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> reservation.checkCanCheckIn(START));
        assertEquals("A reserva já tem check-out.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-3")
    void onCheckCanCheckIn_throw_whenReservationIsCancelled() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.CANCELLED);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> reservation.checkCanCheckIn(START));
        assertEquals("A reserva está cancelada.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-14")
    void onCheckCanCheckIn_throw_whenTodayIsBeforeStartDate() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.RESERVED);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> reservation.checkCanCheckIn(START.minusDays(1)));
        assertEquals("O check-in só é possível a partir do dia de entrada.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-14")
    void onCheckCanCheckIn_notThrow_whenTodayIsStartDate() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.RESERVED);

        assertDoesNotThrow(() -> reservation.checkCanCheckIn(START));
    }

    @Test
    @Tag("H4Y-82-AC-14")
    void onCheckCanCheckIn_notThrow_whenTodayIsBetweenStartAndEndDate() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.RESERVED);

        assertDoesNotThrow(() -> reservation.checkCanCheckIn(START.plusDays(1)));
    }

    @Test
    @Tag("H4Y-82-AC-15")
    void onCheckCanCheckIn_throw_whenTodayIsEndDate() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.RESERVED);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> reservation.checkCanCheckIn(END));
        assertEquals("A data de saída da reserva já passou.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-15")
    void onCheckCanCheckIn_throw_whenTodayIsAfterEndDate() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.RESERVED);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> reservation.checkCanCheckIn(END.plusDays(5)));
        assertEquals("A data de saída da reserva já passou.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-2")
    void onCheckCanCheckIn_throwStateMessage_whenStateIsWrongAndDateIsOutOfPeriod() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.CANCELLED);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> reservation.checkCanCheckIn(END.plusDays(5)));
        assertEquals("A reserva está cancelada.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-5")
    void onCheckCanCheckOut_notThrow_whenReservationIsCheckedIn() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.CHECKED_IN);

        assertDoesNotThrow(reservation::checkCanCheckOut);
        assertEquals(ReservationStatus.CHECKED_IN, reservation.getStatus());
    }

    @Test
    @Tag("H4Y-82-AC-6")
    void onCheckCanCheckOut_throw_whenReservationHasNoCheckIn() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.RESERVED);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                reservation::checkCanCheckOut);
        assertEquals("A reserva não tem check-in.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-6")
    void onCheckCanCheckOut_throw_whenReservationIsCancelled() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.CANCELLED);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                reservation::checkCanCheckOut);
        assertEquals("A reserva está cancelada.", error.getMessage());
    }

    @Test
    @Tag("H4Y-82-AC-7")
    void onCheckCanCheckOut_throw_whenReservationHasCheckOut() {
        Reservation reservation = new Reservation(1L, 2L, START, END, ReservationStatus.CHECKED_OUT);

        IllegalStateException error = assertThrows(IllegalStateException.class,
                reservation::checkCanCheckOut);
        assertEquals("A reserva já tem check-out.", error.getMessage());
    }
}
