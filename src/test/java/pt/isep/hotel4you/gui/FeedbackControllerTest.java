package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;
import pt.isep.hotel4you.domain.Feedback;
import pt.isep.hotel4you.domain.FeedbackType;

/** Tests the texts of the suggestion and complaint screen (H4Y-111). */
class FeedbackControllerTest {
    private static final LocalDateTime TIME = LocalDateTime.of(2026, 11, 27, 10, 5);

    @Test
    @Tag("H4Y-111-AC-1")
    void onConfirmationText_showTypeClientAndTime_whenSuggestionHasClient() {
        Client client = new Client(7L, "Maria Sousa", DocumentType.CC, "12345678",
                LocalDate.of(1990, 5, 1), "912345678", "PT", null, null);
        Feedback feedback = new Feedback(4L, client, FeedbackType.SUGGESTION, "Mais toalhas.", TIME);

        assertEquals("Sugestão registada para o gestor, em 27/11/2026 às 10:05. Cliente: Maria Sousa.",
                FeedbackController.confirmationText(feedback));
    }

    @Test
    @Tag("H4Y-111-AC-2")
    void onConfirmationText_showTypeAndTime_whenComplaintHasNoClient() {
        Feedback feedback = new Feedback(5L, null, FeedbackType.COMPLAINT, "Barulho.", TIME);

        assertEquals("Reclamação registada para o gestor, em 27/11/2026 às 10:05. Sem cliente.",
                FeedbackController.confirmationText(feedback));
    }

    @Test
    @Tag("H4Y-111-AC-1")
    void onClientText_showNameAndDocument_whenClientIsGiven() {
        Client client = new Client(7L, "Maria Sousa", DocumentType.CC, "12345678",
                LocalDate.of(1990, 5, 1), "912345678", "PT", null, null);

        assertEquals("Maria Sousa (12345678)", FeedbackController.clientText(client));
    }

    @Test
    @Tag("H4Y-111-AC-2")
    void onClientText_showNoClient_whenClientIsNull() {
        assertEquals("Sem cliente", FeedbackController.clientText(null));
    }
}
