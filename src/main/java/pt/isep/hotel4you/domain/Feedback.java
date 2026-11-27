package pt.isep.hotel4you.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A saved suggestion or complaint of a client. The reception records it and the manager
 * receives it (RF-27, Q-18). It is one row of the table {@code feedback}.
 */
public class Feedback {
    /** The maximum length of the text, the size of the column {@code feedback.description}. */
    public static final int MAX_DESCRIPTION_LENGTH = 500;

    private final long id;
    private final Client client;
    private final FeedbackType type;
    private final String description;
    private final LocalDateTime createdAt;

    /**
     * Creates a saved feedback.
     *
     * @param id the saved identifier
     * @param client the client who gave the feedback, or null if no client is known
     * @param type the feedback type
     * @param description the text of the client
     * @param createdAt the date and time of the record
     */
    public Feedback(long id, Client client, FeedbackType type, String description,
            LocalDateTime createdAt) {
        this.id = id;
        this.client = client;
        this.type = Objects.requireNonNull(type);
        this.description = Objects.requireNonNull(description);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    /** @return the saved identifier */
    public long getId() { return id; }
    /** @return the client who gave the feedback, or null if no client is known */
    public Client getClient() { return client; }
    /** @return the feedback type */
    public FeedbackType getType() { return type; }
    /** @return the text of the client */
    public String getDescription() { return description; }
    /** @return the date and time of the record */
    public LocalDateTime getCreatedAt() { return createdAt; }
}
