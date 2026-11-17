package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests how the extra price screen shows unset and configured amounts. */
class ExtraPriceControllerTest {
    @Test
    @Tag("H4Y-93-AC-3")
    void onPriceText_showUnset_whenPriceHasNotBeenDefined() {
        assertEquals("Por definir", ExtraPriceController.priceText(null));
    }

    @Test
    @Tag("H4Y-93-AC-1")
    void onPriceText_showCommaAndTwoDecimals_whenPriceIsDefined() {
        assertEquals("15,00 €", ExtraPriceController.priceText(new BigDecimal("15.00")));
    }

    @Test
    @Tag("H4Y-93-AC-2")
    void onPriceText_showZero_whenExtraIsFree() {
        assertEquals("0,00 €", ExtraPriceController.priceText(new BigDecimal("0.00")));
    }
}
