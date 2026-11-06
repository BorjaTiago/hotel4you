package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.ReservationQuote;

/** Tests the price summary text of the reservation screen. */
class ReservationControllerTest {

    @Test
    @Tag("H4Y-56-AC-4")
    void onSummaryText_showNightsAndPrice_whenPriceIsSet() {
        String price = ReservationController.PRICE_FORMAT.format(new BigDecimal("80.00"));

        assertEquals("Quarto 201: 3 noites. Preço base por noite: " + price + ".",
                ReservationController.summaryText(201, new ReservationQuote(new BigDecimal("80.00"), 3)));
    }

    @Test
    @Tag("H4Y-56-AC-4")
    void onSummaryText_useSingular_whenOneNight() {
        String price = ReservationController.PRICE_FORMAT.format(new BigDecimal("80.00"));

        assertEquals("Quarto 201: 1 noite. Preço base por noite: " + price + ".",
                ReservationController.summaryText(201, new ReservationQuote(new BigDecimal("80.00"), 1)));
    }

    @Test
    @Tag("H4Y-56-AC-4")
    void onSummaryText_showDash_whenPriceIsNotSet() {
        assertEquals("Quarto 202: 2 noites. Preço base por noite: —.",
                ReservationController.summaryText(202, new ReservationQuote(null, 2)));
    }
}
