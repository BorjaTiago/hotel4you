package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Tests the three room service types (H4Y-110, Q-33). */
class RoomServiceTypeTest {

    @Test
    @Tag("H4Y-110-AC-3")
    void onValues_returnOnlyFoodDrinkAndLaundry_whenListed() {
        assertArrayEquals(new RoomServiceType[] {
            RoomServiceType.FOOD, RoomServiceType.DRINK, RoomServiceType.LAUNDRY
        }, RoomServiceType.values());
    }

    @ParameterizedTest
    @CsvSource({"FOOD,Comida,Comida", "DRINK,Bebida,Bebidas", "LAUNDRY,Lavandaria,Lavandaria"})
    @Tag("H4Y-110-AC-1")
    void onNames_matchScreenAndCatalogue_whenTypeIsShown(RoomServiceType type, String displayName,
            String catalogueName) {
        assertEquals(displayName, type.displayName());
        assertEquals(catalogueName, type.catalogueName());
    }
}
