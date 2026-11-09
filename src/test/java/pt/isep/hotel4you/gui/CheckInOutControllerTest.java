package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.ReservationStatus;

/** Tests which check-in and check-out buttons the screen turns on for the chosen row. */
class CheckInOutControllerTest {
    private static final LocalDate START = LocalDate.of(2026, 11, 10);

    private static ClientReservation row(ReservationStatus status) {
        return new ClientReservation(1L, 201, START, START.plusDays(2), status);
    }

    @Test
    @Tag("H4Y-82-AC-9")
    void onCanCheckInOrOut_returnFalse_whenNoRowIsChosen() {
        assertFalse(CheckInOutController.canCheckIn(null));
        assertFalse(CheckInOutController.canCheckOut(null));
    }

    @Test
    @Tag("H4Y-82-AC-10")
    void onCanCheckIn_returnTrueAndCheckOutFalse_whenRowIsReserved() {
        assertTrue(CheckInOutController.canCheckIn(row(ReservationStatus.RESERVED)));
        assertFalse(CheckInOutController.canCheckOut(row(ReservationStatus.RESERVED)));
    }

    @Test
    @Tag("H4Y-82-AC-11")
    void onCanCheckOut_returnTrueAndCheckInFalse_whenRowIsCheckedIn() {
        assertTrue(CheckInOutController.canCheckOut(row(ReservationStatus.CHECKED_IN)));
        assertFalse(CheckInOutController.canCheckIn(row(ReservationStatus.CHECKED_IN)));
    }

    @Test
    @Tag("H4Y-82-AC-12")
    void onStatusText_showPortugueseLabel_whenStatusIsActive() {
        assertEquals("Reservada", CheckInOutController.statusText(ReservationStatus.RESERVED));
        assertEquals("Check-in feito", CheckInOutController.statusText(ReservationStatus.CHECKED_IN));
    }
}
