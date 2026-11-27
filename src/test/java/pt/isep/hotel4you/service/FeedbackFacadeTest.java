package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryFeedbackRepository;
import pt.isep.hotel4you.domain.FeedbackType;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.RestaurantStaff;
import pt.isep.hotel4you.domain.User;

/** Tests the suggestion and complaint registration at the facade boundary (H4Y-111). */
class FeedbackFacadeTest {
    private final User receptionist = new Receptionist(2L, "Ana Silva", "ana", "hash");
    private final User restaurantStaff = new RestaurantStaff(3L, "Joao Lima", "joao", "hash");
    private InMemoryFeedbackRepository repository;
    private HotelFacade facade;

    @BeforeEach
    void setUp() {
        repository = new InMemoryFeedbackRepository();
        facade = new HotelFacade(null, null, null, null, null, null, null, null, null, null, null,
                new FeedbackService(repository));
    }

    @Test
    @Tag("H4Y-111-AC-1")
    void onSaveFeedback_saveSuggestion_whenReceptionistRegisters() {
        assertEquals(FeedbackType.SUGGESTION,
                facade.saveFeedback(null, FeedbackType.SUGGESTION, "Mais toalhas.", receptionist).getType());
        assertEquals(1, repository.saved().size());
    }

    @Test
    @Tag("H4Y-111-AC-3")
    void onSaveFeedback_throwAndSaveNothing_whenTextIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> facade.saveFeedback(null, FeedbackType.COMPLAINT, " ", receptionist));
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-111-AC-4")
    void onSaveFeedback_throwAccessDenied_whenActorIsRestaurantStaff() {
        assertThrows(AccessDeniedException.class,
                () -> facade.saveFeedback(null, FeedbackType.COMPLAINT, "Mais toalhas.", restaurantStaff));
        assertTrue(repository.saved().isEmpty());
    }

    @Test
    @Tag("H4Y-111-AC-4")
    void onCanUse_showMenuOnlyToReception_whenFeedbackPermission() {
        assertTrue(facade.canUse(receptionist, Permission.REGISTER_FEEDBACK));
        assertFalse(facade.canUse(restaurantStaff, Permission.REGISTER_FEEDBACK));
    }
}
