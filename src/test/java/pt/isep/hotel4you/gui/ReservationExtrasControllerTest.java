package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.ReservationSummary;

class ReservationExtrasControllerTest {
    @Test
    @Tag("H4Y-107-AC-1")
    void onCanEdit_returnTrue_whenReservedRowIsSelected() {
        assertTrue(ReservationExtrasController.canEdit(row(ReservationStatus.RESERVED)));
    }

    @Test
    @Tag("H4Y-107-AC-7")
    void onCanEdit_returnFalse_whenReservationHasCheckedInOrNoRowIsSelected() {
        assertFalse(ReservationExtrasController.canEdit(row(ReservationStatus.CHECKED_IN)));
        assertFalse(ReservationExtrasController.canEdit(null));
    }

    private static ReservationSummary row(ReservationStatus status) {
        LocalDate arrival = LocalDate.of(2026, 12, 1);
        return new ReservationSummary(1L, "Ana", "12345678", 201,
                arrival, arrival.plusDays(3), status, "");
    }
}
