package pt.isep.hotel4you.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.Feedback;
import pt.isep.hotel4you.domain.FeedbackType;

/** Keeps the suggestions and complaints in memory for unit tests. */
public class InMemoryFeedbackRepository implements FeedbackRepository {
    /** The fixed time of every saved row. */
    public static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 27, 10, 5);

    private final List<Feedback> saved = new ArrayList<>();

    @Override
    public Feedback save(Client client, FeedbackType type, String description) {
        Feedback feedback = new Feedback(saved.size() + 1L, client, type, description, NOW);
        saved.add(feedback);
        return feedback;
    }

    /** @return the saved rows, in save order */
    public List<Feedback> saved() {
        return List.copyOf(saved);
    }
}
