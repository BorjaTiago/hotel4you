package pt.isep.hotel4you.gui;

import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.Feedback;
import pt.isep.hotel4you.domain.FeedbackType;
import pt.isep.hotel4you.service.HotelFacade;

/** Screen where the reception registers a suggestion or a complaint of a client (H4Y-111). */
public class FeedbackController {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

    private final HotelFacade facade;
    @FXML private Label typeLabel;
    @FXML private ComboBox<FeedbackType> typeBox;
    @FXML private Label clientLabel;
    @FXML private ComboBox<Client> clientBox;
    @FXML private Label descriptionLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that registers the feedback
     */
    public FeedbackController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        typeLabel.setLabelFor(typeBox);
        clientLabel.setLabelFor(clientBox);
        descriptionLabel.setLabelFor(descriptionArea);
        typeBox.getItems().setAll(FeedbackType.values());
        typeBox.setConverter(converter(FeedbackType::displayName));
        clientBox.setConverter(converter(FeedbackController::clientText));
        try {
            clientBox.getItems().addAll(facade.listClients(HotelApp.currentUser));
        } catch (RuntimeException error) {
            showMessage(error.getMessage(), false);
        }
    }

    @FXML
    private void onNoClientClicked() {
        clientBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void onSaveFeedbackClicked() {
        showMessage("", false);
        Feedback feedback;
        try {
            feedback = facade.saveFeedback(clientBox.getValue(), typeBox.getValue(),
                    descriptionArea.getText(), HotelApp.currentUser);
        } catch (RuntimeException error) {
            showMessage(error.getMessage(), false);
            return;
        }
        typeBox.getSelectionModel().clearSelection();
        clientBox.getSelectionModel().clearSelection();
        descriptionArea.clear();
        showMessage(confirmationText(feedback), true);
    }

    private void showMessage(String text, boolean success) {
        messageLabel.setText(text);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }

    private static <T> StringConverter<T> converter(Function<T, String> text) {
        return new StringConverter<>() {
            @Override
            public String toString(T value) {
                return value == null ? "" : text.apply(value);
            }

            @Override
            public T fromString(String value) {
                return null;
            }
        };
    }

    /**
     * Builds the text of one client in the list.
     *
     * @param client the client, or null for no client
     * @return the name and the document, or "Sem cliente"
     */
    static String clientText(Client client) {
        if (client == null) {
            return "Sem cliente";
        }
        return client.getName() + " (" + client.getDocument() + ")";
    }

    /**
     * Builds the confirmation with the type, the date and time and the client.
     *
     * @param feedback the saved feedback
     * @return the text to show
     */
    static String confirmationText(Feedback feedback) {
        String client = feedback.getClient() == null
                ? "Sem cliente."
                : "Cliente: " + feedback.getClient().getName() + ".";
        return feedback.getType().displayName() + " registada para o gestor, em "
                + DATE.format(feedback.getCreatedAt()) + " às " + TIME.format(feedback.getCreatedAt())
                + ". " + client;
    }
}
