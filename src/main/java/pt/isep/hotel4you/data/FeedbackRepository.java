package pt.isep.hotel4you.data;

import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.Feedback;
import pt.isep.hotel4you.domain.FeedbackType;

/** Saves the client suggestions and complaints. */
public interface FeedbackRepository {
    /**
     * Saves a feedback with the current date and time.
     *
     * @param client the saved client, or null if no client is known
     * @param type the feedback type
     * @param description the checked text
     * @return the saved feedback
     * @throws IllegalArgumentException if the client no longer exists
     * @throws IllegalStateException if the database is not available
     */
    Feedback save(Client client, FeedbackType type, String description);
}
