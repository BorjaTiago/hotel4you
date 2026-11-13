package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.ReservationSummary;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.gui.ReservationsController.QuickFilter;

/** Tests the buttons, the status labels and the search and filters of the Reservas screen. */
class ReservationsControllerTest {
    private static final LocalDate START = LocalDate.of(2026, 11, 10);
    private static final LocalDate TODAY = LocalDate.of(2026, 11, 9);

    private static ReservationSummary row(ReservationStatus status) {
        return new ReservationSummary(1L, "Maria Sousa", "12345678", 201, START,
                START.plusDays(2), status);
    }

    private static ReservationSummary row(long id, String name, String document, LocalDate start,
            ReservationStatus status) {
        return new ReservationSummary(id, name, document, 200 + (int) id, start, start.plusDays(2),
                status);
    }

    private static List<ReservationSummary> sample() {
        return List.of(
                row(1L, "Maria Sousa", "12345678", TODAY, ReservationStatus.RESERVED),
                row(2L, "João Costa", "AB99887", TODAY, ReservationStatus.CHECKED_IN),
                row(3L, "Ana Silva", "55512345", TODAY.plusDays(1), ReservationStatus.RESERVED));
    }

    private static List<Long> ids(List<ReservationSummary> rows) {
        return rows.stream().map(ReservationSummary::id).toList();
    }

    @Test
    @Tag("H4Y-55-AC-5")
    void onCanCancel_returnTrue_whenRowIsReserved() {
        assertTrue(ReservationsController.canCancel(row(ReservationStatus.RESERVED)));
    }

    @Test
    @Tag("H4Y-55-AC-5")
    void onCanCancel_returnFalse_whenRowIsCheckedInOrNotChosen() {
        assertFalse(ReservationsController.canCancel(row(ReservationStatus.CHECKED_IN)));
        assertFalse(ReservationsController.canCancel(null));
    }

    @Test
    @Tag("H4Y-55-AC-5")
    void onCancelQuestion_nameRoomAndDates_whenRowIsChosen() {
        assertEquals("Cancelar a reserva do quarto 201, de 10/11/2026 a 12/11/2026?",
                ReservationsController.cancelQuestion(row(ReservationStatus.RESERVED)));
    }

    @Test
    @Tag("H4Y-82-AC-9")
    void onCanCheckInOrOut_returnFalse_whenNoRowIsChosen() {
        assertFalse(ReservationsController.canCheckIn(null, START));
        assertFalse(ReservationsController.canCheckOut(null));
    }

    @Test
    @Tag("H4Y-82-AC-10")
    void onCanCheckIn_returnTrueAndCheckOutFalse_whenRowIsReserved() {
        assertTrue(ReservationsController.canCheckIn(row(ReservationStatus.RESERVED), START));
        assertFalse(ReservationsController.canCheckOut(row(ReservationStatus.RESERVED)));
    }

    @Test
    @Tag("H4Y-82-AC-11")
    void onCanCheckOut_returnTrueAndCheckInFalse_whenRowIsCheckedIn() {
        assertTrue(ReservationsController.canCheckOut(row(ReservationStatus.CHECKED_IN)));
        assertFalse(ReservationsController.canCheckIn(row(ReservationStatus.CHECKED_IN), START));
    }

    @Test
    @Tag("H4Y-82-AC-14")
    void onCanCheckIn_returnFalse_whenTodayIsBeforeStartDate() {
        assertFalse(ReservationsController.canCheckIn(row(ReservationStatus.RESERVED), TODAY));
    }

    @Test
    @Tag("H4Y-82-AC-14")
    void onCanCheckIn_returnTrue_whenTodayIsStartDate() {
        assertTrue(ReservationsController.canCheckIn(row(ReservationStatus.RESERVED), START));
    }

    @Test
    @Tag("H4Y-82-AC-15")
    void onCanCheckIn_returnFalse_whenTodayIsEndDate() {
        assertFalse(ReservationsController.canCheckIn(row(ReservationStatus.RESERVED),
                START.plusDays(2)));
    }

    @Test
    @Tag("H4Y-82-AC-10")
    void onCanCheckIn_returnFalse_whenRowIsNotReserved() {
        assertFalse(ReservationsController.canCheckIn(row(ReservationStatus.CANCELLED), START));
        assertFalse(ReservationsController.canCheckIn(row(ReservationStatus.CHECKED_OUT), START));
    }

    @Test
    @Tag("H4Y-82-AC-12")
    void onStatusText_showPortugueseLabel_whenStatusIsActive() {
        assertEquals("Reservada", ReservationsController.statusText(ReservationStatus.RESERVED));
        assertEquals("Check-in feito", ReservationsController.statusText(ReservationStatus.CHECKED_IN));
    }

    @Test
    @Tag("H4Y-81-AC-3")
    void onFilter_returnMatchingClient_whenTextIsPartOfName() {
        assertEquals(List.of(3L), ids(ReservationsController.filter(sample(),
                QuickFilter.ALL_ACTIVE, "silva", TODAY)));
    }

    @Test
    @Tag("H4Y-81-AC-3")
    void onFilter_returnMatchingClient_whenTextIsPartOfDocument() {
        assertEquals(List.of(2L), ids(ReservationsController.filter(sample(),
                QuickFilter.ALL_ACTIVE, "99887", TODAY)));
    }

    @Test
    @Tag("H4Y-81-AC-3")
    void onFilter_ignoreCase_whenTextHasOtherCase() {
        assertEquals(List.of(2L), ids(ReservationsController.filter(sample(),
                QuickFilter.ALL_ACTIVE, "joão COSTA", TODAY)));
        assertEquals(List.of(2L), ids(ReservationsController.filter(sample(),
                QuickFilter.ALL_ACTIVE, "ab99887", TODAY)));
    }

    @Test
    @Tag("H4Y-81-AC-4")
    void onFilter_returnOnlyReservedArrivingToday_whenFilterIsArrivingToday() {
        assertEquals(List.of(1L), ids(ReservationsController.filter(sample(),
                QuickFilter.ARRIVING_TODAY, "", TODAY)));
    }

    @Test
    @Tag("H4Y-81-AC-5")
    void onFilter_returnOnlyCheckedIn_whenFilterIsCheckedIn() {
        assertEquals(List.of(2L), ids(ReservationsController.filter(sample(),
                QuickFilter.CHECKED_IN, null, TODAY)));
    }

    @Test
    @Tag("H4Y-81-AC-6")
    void onFilter_returnAllRows_whenFilterIsAllActiveAndTextIsBlank() {
        assertEquals(List.of(1L, 2L, 3L), ids(ReservationsController.filter(sample(),
                QuickFilter.ALL_ACTIVE, "  ", TODAY)));
        assertEquals(List.of(1L, 2L, 3L), ids(ReservationsController.filter(sample(),
                QuickFilter.ALL_ACTIVE, null, TODAY)));
    }

    @Test
    @Tag("H4Y-81-AC-7")
    void onFilter_returnEmptyList_whenNoRowMatches() {
        assertTrue(ReservationsController.filter(sample(), QuickFilter.ALL_ACTIVE, "zzz", TODAY)
                .isEmpty());
        assertTrue(ReservationsController.filter(List.of(), QuickFilter.CHECKED_IN, "", TODAY)
                .isEmpty());
    }
}
