package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import pt.isep.hotel4you.data.InMemoryFeedbackRepository;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.DocumentType;
import pt.isep.hotel4you.domain.Feedback;
import pt.isep.hotel4you.domain.FeedbackType;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.User;

/** Tests the rules to register and list the suggestions and complaints (H4Y-111, H4Y-131, RF-27). */
class FeedbackServiceTest {
    private static final User MANAGER = new Manager(1L, "Rui Costa", "rui", "hash");
    private static final User RECEPTIONIST = new Receptionist(2L, "Ana Silva", "ana", "hash");
    private static final User RESTAURANT_STAFF = new RestaurantStaff(3L, "Joao Lima", "joao", "hash");
    private static final Client CLIENT = new Client(7L, "Maria Sousa", DocumentType.CC, "12345678",
            LocalDate.of(1990, 5, 1), "912345678", "PT", null, null);

    private InMemoryFeedbackRepository repository;
    private FeedbackService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryFeedbackRepository();
        service = new FeedbackService(repository);
    }

    @Test
    @Tag("H4Y-111-AC-1")
    void onSave_saveSuggestionWithClientAndTime_whenReceptionistGivesTextAndClient() {
        Feedback feedback = service.save(CLIENT, FeedbackType.SUGGESTION, "Pequeno-almoço mais tarde.",
                RECEPTIONIST);

        assertEquals(FeedbackType.SUGGESTION, feedback.getType());
        assertSame(CLIENT, feedback.getClient());
        assertEquals("Pequeno-almoço mais tarde.", feedback.getDescription());
        assertEquals(InMemoryFeedbackRepository.NOW, feedback.getCreatedAt());
        assertEquals(1, repository.saved().size());
    }

    @Test
    @Tag("H4Y-111-AC-2")
    void onSave_saveComplaintWithTime_whenNoClientIsChosen() {
        Feedback feedback = service.save(null, FeedbackType.COMPLAINT, "O ar condicionado faz barulho.",
                RECEPTIONIST);

        assertEquals(FeedbackType.COMPLAINT, feedback.getType());
        assertNull(feedback.getClient());
        assertEquals(InMemoryFeedbackRepository.NOW, feedback.getCreatedAt());
        assertEquals(1, repository.saved().size());
    }

    @Test
    @Tag("H4Y-111-AC-1")
    void onSave_removeOuterSpaces_whenTextHasSpaces() {
        Feedback feedback = service.save(null, FeedbackType.SUGGESTION, "  Mais toalhas.  ", RECEPTIONIST);

        assertEquals("Mais toalhas.", feedback.getDescription());
    }

    @Test
    @Tag("H4Y-111-AC-1")
    void onSave_saveFeedback_whenActorIsManager() {
        User manager = new Manager(1L, "Rui Costa", "rui", "hash");

        assertEquals(1L, service.save(null, FeedbackType.SUGGESTION, "Mais toalhas.", manager).getId());
    }

    @Test
    @Tag("H4Y-111-AC-1")
    void onSave_saveFeedback_whenTextHasMaximumLength() {
        String text = "a".repeat(Feedback.MAX_DESCRIPTION_LENGTH);

        assertEquals(text, service.save(null, FeedbackType.SUGGESTION, text, RECEPTIONIST).getDescription());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   ", "\n\t"})
    @Tag("H4Y-111-AC-3")
    void onSave_throwAndSaveNothing_whenTextIsEmpty(String text) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.save(CLIENT, FeedbackType.SUGGESTION, text, RECEPTIONIST));

        assertEquals("Escreva o texto da sugestão ou reclamação.", error.getMessage());
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-111-AC-3")
    void onSave_throwAndSaveNothing_whenTextIsTooLong() {
        String text = "a".repeat(Feedback.MAX_DESCRIPTION_LENGTH + 1);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.save(null, FeedbackType.COMPLAINT, text, RECEPTIONIST));

        assertEquals("O texto tem no máximo 500 caracteres.", error.getMessage());
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-111-AC-3")
    void onSave_throwAndSaveNothing_whenTypeIsMissing() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.save(null, null, "Mais toalhas.", RECEPTIONIST));

        assertEquals("Escolha o tipo: sugestão ou reclamação.", error.getMessage());
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-111-AC-4")
    void onSave_throwAccessDenied_whenActorIsRestaurantStaff() {
        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.save(null, FeedbackType.COMPLAINT, "Mais toalhas.", RESTAURANT_STAFF));

        assertEquals("Não tem permissão para registar sugestões ou reclamações.", error.getMessage());
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-111-AC-4")
    void onSave_throwAccessDenied_whenNoUserIsLoggedIn() {
        assertThrows(AccessDeniedException.class,
                () -> service.save(null, FeedbackType.COMPLAINT, "Mais toalhas.", null));
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-111-AC-4")
    void onSave_checkPermissionFirst_whenDataIsAlsoInvalid() {
        assertThrows(AccessDeniedException.class, () -> service.save(null, null, "", RESTAURANT_STAFF));
    }

    @Test
    @Tag("H4Y-131-AC-1")
    void onList_returnSuggestionAndComplaint_whenManagerOpensList() {
        Feedback suggestion = service.save(CLIENT, FeedbackType.SUGGESTION, "Mais toalhas.", RECEPTIONIST);
        Feedback complaint = service.save(null, FeedbackType.COMPLAINT, "Barulho.", RECEPTIONIST);

        List<Feedback> list = service.list(MANAGER);

        assertEquals(List.of(complaint, suggestion), list);
        assertEquals(FeedbackType.COMPLAINT, list.get(0).getType());
        assertEquals("Barulho.", list.get(0).getDescription());
        assertEquals(InMemoryFeedbackRepository.NOW, list.get(0).getCreatedAt());
    }

    @Test
    @Tag("H4Y-131-AC-2")
    void onList_returnEmptyList_whenNoFeedbackExists() {
        assertTrue(service.list(MANAGER).isEmpty());
    }

    @Test
    @Tag("H4Y-131-AC-3")
    void onList_throwAccessDenied_whenActorIsReceptionist() {
        service.save(null, FeedbackType.COMPLAINT, "Barulho.", RECEPTIONIST);

        AccessDeniedException error = assertThrows(AccessDeniedException.class,
                () -> service.list(RECEPTIONIST));

        assertEquals("Não tem permissão para consultar sugestões ou reclamações.", error.getMessage());
    }

    @Test
    @Tag("H4Y-131-AC-3")
    void onList_throwAccessDenied_whenActorIsRestaurantStaff() {
        assertThrows(AccessDeniedException.class, () -> service.list(RESTAURANT_STAFF));
    }

    @Test
    @Tag("H4Y-131-AC-3")
    void onList_throwAccessDenied_whenNoUserIsLoggedIn() {
        assertThrows(AccessDeniedException.class, () -> service.list(null));
    }
}
