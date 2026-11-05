package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests the birth date format of the client screen. */
class ClientControllerTest {

    @Test
    @Tag("H4Y-52-AC-2")
    void onParseBirthDate_throw_whenDayDoesNotExist() {
        assertThrows(
                DateTimeParseException.class,
                () -> LocalDate.parse("31/02/1990", ClientController.DATE_FORMAT));
    }

    @Test
    @Tag("H4Y-52-AC-2")
    void onParseBirthDate_readDate_whenDateValid() {
        assertEquals(LocalDate.of(2000, 2, 29), LocalDate.parse("29/02/2000", ClientController.DATE_FORMAT));
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onFindCountry_returnCode_whenNameStartsWithText() {
        assertEquals("PT", ClientController.findCountry(ClientController.countryCodes(), "portu"));
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onFindCountry_returnCode_whenTextHasOtherCaseAndAccents() {
        assertEquals("ZA", ClientController.findCountry(ClientController.countryCodes(), "AFRICA DO"));
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onFindCountry_returnNull_whenNoNameMatches() {
        assertNull(ClientController.findCountry(ClientController.countryCodes(), "zzz"));
    }
}
