package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.ReservationPrice;

class ReservationPriceControllerTest {
    @Test
    @Tag("H4Y-130-AC-6")
    void onTotalText_showTotalInEuros_whenAQuoteIsAvailable() {
        var quote = new ReservationPrice(new BigDecimal("80.00"), new BigDecimal("15.00"),
                new BigDecimal("40.00"), 3);

        assertTrue(ReservationPriceController.totalText(quote).contains("405,00"));
        assertTrue(ReservationPriceController.totalText(quote).contains("€"));
    }

    @Test
    @Tag("H4Y-130-AC-5")
    void onTotalText_showMissingBasePrice_whenTheQuoteHasNoBasePrice() {
        var quote = new ReservationPrice(null, BigDecimal.ZERO, BigDecimal.ZERO, 3);

        assertEquals("Não é possível calcular o total. Falta o preço base do tipo de quarto.",
                ReservationPriceController.totalText(quote));
    }
}
