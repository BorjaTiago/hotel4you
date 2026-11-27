package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.RoomServiceRequest;
import pt.isep.hotel4you.domain.RoomServiceType;

/** Tests the confirmation text of the room service screen (H4Y-110). */
class RoomServiceControllerTest {

    @Test
    @Tag("H4Y-110-AC-1")
    void onConfirmationText_showRoomTypeAndTime_whenRequestIsSaved() {
        RoomServiceRequest request = new RoomServiceRequest(11L, 9L, 202, RoomServiceType.LAUNDRY,
                new BigDecimal("4.50"), LocalDateTime.of(2026, 11, 24, 21, 5));

        assertEquals("Pedido registado: Lavandaria para o quarto 202, em 24/11/2026 às 21:05.",
                RoomServiceController.confirmationText(request));
    }
}
