package pt.isep.hotel4you.service;

import java.util.List;
import pt.isep.hotel4you.data.FeedbackRepository;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.Feedback;
import pt.isep.hotel4you.domain.FeedbackType;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.User;

/** Applies the rules of the client suggestions and complaints (RF-27, Q-18). */
public class FeedbackService {
    private final FeedbackRepository repository;

    /**
     * Creates the service.
     *
     * @param repository the feedback store
     */
    public FeedbackService(FeedbackRepository repository) {
        this.repository = repository;
    }

    /**
     * Registers a suggestion or a complaint of a client. Invalid data saves nothing.
     *
     * @param client the saved client, or null if no client is known
     * @param type the feedback type
     * @param description the text of the client; outer spaces are removed
     * @param actor the logged-in user
     * @return the saved feedback with the date and time
     * @throws AccessDeniedException if the actor may not register feedback
     * @throws IllegalArgumentException if the type is missing, the text is empty or longer than
     *         {@link Feedback#MAX_DESCRIPTION_LENGTH}, or the client no longer exists
     * @throws IllegalStateException if the database is not available
     */
    public Feedback save(Client client, FeedbackType type, String description, User actor) {
        if (!Permission.REGISTER_FEEDBACK.allows(actor)) {
            throw new AccessDeniedException("Não tem permissão para registar sugestões ou reclamações.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Escolha o tipo: sugestão ou reclamação.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Escreva o texto da sugestão ou reclamação.");
        }
        String text = description.strip();
        if (text.length() > Feedback.MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException(
                    "O texto tem no máximo " + Feedback.MAX_DESCRIPTION_LENGTH + " caracteres.");
        }
        return repository.save(client, type, text);
    }

    /**
     * Lists all suggestions and complaints for the manager, the newest first.
     *
     * @param actor the logged-in user
     * @return the saved feedback, or an empty list
     * @throws AccessDeniedException if the actor may not view feedback
     * @throws IllegalStateException if the database is not available
     */
    public List<Feedback> list(User actor) {
        if (!Permission.VIEW_FEEDBACK.allows(actor)) {
            throw new AccessDeniedException("Não tem permissão para consultar sugestões ou reclamações.");
        }
        return repository.findAll();
    }
}
