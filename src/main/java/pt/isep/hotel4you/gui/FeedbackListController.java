package pt.isep.hotel4you.gui;

import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.isep.hotel4you.domain.Feedback;
import pt.isep.hotel4you.service.HotelFacade;

/** Screen where the manager reads the suggestions and complaints of the clients (H4Y-131). */
public class FeedbackListController {
    /** The text of the empty list. */
    static final String EMPTY_TEXT = "Ainda não existem sugestões nem reclamações registadas.";

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final HotelFacade facade;
    @FXML private Label listLabel;
    @FXML private TableView<Feedback> feedbackTable;
    @FXML private TableColumn<Feedback, String> typeColumn;
    @FXML private TableColumn<Feedback, String> descriptionColumn;
    @FXML private TableColumn<Feedback, String> createdAtColumn;
    @FXML private TableColumn<Feedback, String> clientColumn;
    @FXML private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that lists the feedback
     */
    public FeedbackListController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        listLabel.setLabelFor(feedbackTable);
        feedbackTable.setPlaceholder(new Label(EMPTY_TEXT));
        typeColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getType().displayName()));
        descriptionColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getDescription()));
        createdAtColumn.setCellValueFactory(
                data -> new SimpleStringProperty(dateTimeText(data.getValue())));
        clientColumn.setCellValueFactory(data -> new SimpleStringProperty(clientText(data.getValue())));
        try {
            feedbackTable.getItems().setAll(facade.listFeedback(HotelApp.currentUser));
        } catch (RuntimeException error) {
            messageLabel.setText(error.getMessage());
        }
    }

    /**
     * Builds the date and time of one feedback.
     *
     * @param feedback the saved feedback
     * @return the date and time, for example "27/11/2026 09:05"
     */
    static String dateTimeText(Feedback feedback) {
        return DATE_TIME.format(feedback.getCreatedAt());
    }

    /**
     * Builds the client of one feedback.
     *
     * @param feedback the saved feedback
     * @return the client name, or "Sem cliente"
     */
    static String clientText(Feedback feedback) {
        return feedback.getClient() == null ? "Sem cliente" : feedback.getClient().getName();
    }
}
