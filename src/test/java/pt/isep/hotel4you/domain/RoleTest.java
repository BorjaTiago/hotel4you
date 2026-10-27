package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Tests the name of each role on the screen. */
class RoleTest {

    @ParameterizedTest
    @CsvSource({
        "CLIENT, Cliente",
        "RECEPTIONIST, Rececionista",
        "MANAGER, Gestor",
        "RESTAURANT, Funcionário do restaurante",
        "SUPPLIER, Fornecedor"
    })
    @Tag("H4Y-23-AC-1")
    void onDisplayName_returnPortugueseName_always(Role role, String expected) {
        assertEquals(expected, role.displayName());
    }
}
