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

/** Tests the texts of the manager list of suggestions and complaints (H4Y-131). */
class FeedbackListControllerTest {
    private static final LocalDateTime TIME = LocalDateTime.of(2026, 11, 27, 9, 5);

    @Test
    @Tag("H4Y-131-AC-1")
    void onDateTimeText_showDateAndTime_whenFeedbackIsSaved() {
        Feedback feedback = new Feedback(4L, null, FeedbackType.SUGGESTION, "Mais toalhas.", TIME);

        assertEquals("27/11/2026 09:05", FeedbackListController.dateTimeText(feedback));
    }

    @Test
    @Tag("H4Y-131-AC-1")
    void onClientText_showClientName_whenFeedbackHasClient() {
        Client client = new Client(7L, "Maria Sousa", DocumentType.CC, "12345678",
                LocalDate.of(1990, 5, 1), "912345678", "PT", null, null);
        Feedback feedback = new Feedback(4L, client, FeedbackType.SUGGESTION, "Mais toalhas.", TIME);

        assertEquals("Maria Sousa", FeedbackListController.clientText(feedback));
    }

    @Test
    @Tag("H4Y-131-AC-1")
    void onClientText_showNoClient_whenFeedbackHasNoClient() {
        Feedback feedback = new Feedback(5L, null, FeedbackType.COMPLAINT, "Barulho.", TIME);

        assertEquals("Sem cliente", FeedbackListController.clientText(feedback));
    }

    @Test
    @Tag("H4Y-131-AC-2")
    void onEmptyText_tellThatNoRecordExists_whenListIsEmpty() {
        assertEquals("Ainda não existem sugestões nem reclamações registadas.",
                FeedbackListController.EMPTY_TEXT);
    }
}
