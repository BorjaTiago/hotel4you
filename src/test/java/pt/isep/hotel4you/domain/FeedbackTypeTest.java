package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests the names of the feedback types (H4Y-111). */
class FeedbackTypeTest {

    @Test
    @Tag("H4Y-111-AC-1")
    void onDisplayName_returnSugestao_whenTypeIsSuggestion() {
        assertEquals("Sugestão", FeedbackType.SUGGESTION.displayName());
    }

    @Test
    @Tag("H4Y-111-AC-2")
    void onDisplayName_returnReclamacao_whenTypeIsComplaint() {
        assertEquals("Reclamação", FeedbackType.COMPLAINT.displayName());
    }

    @Test
    @Tag("H4Y-111-AC-1")
    void onDatabaseValue_returnSuggestion_whenTypeIsSuggestion() {
        assertEquals("suggestion", FeedbackType.SUGGESTION.databaseValue());
    }

    @Test
    @Tag("H4Y-111-AC-2")
    void onDatabaseValue_returnComplaint_whenTypeIsComplaint() {
        assertEquals("complaint", FeedbackType.COMPLAINT.databaseValue());
    }

    @Test
    @Tag("H4Y-131-AC-1")
    void onFromDatabase_returnSuggestion_whenValueIsSuggestion() {
        assertEquals(FeedbackType.SUGGESTION, FeedbackType.fromDatabase("suggestion"));
    }

    @Test
    @Tag("H4Y-131-AC-1")
    void onFromDatabase_returnComplaint_whenValueIsComplaint() {
        assertEquals(FeedbackType.COMPLAINT, FeedbackType.fromDatabase("complaint"));
    }
}
