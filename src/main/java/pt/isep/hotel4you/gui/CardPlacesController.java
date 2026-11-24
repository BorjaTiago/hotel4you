package pt.isep.hotel4you.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pt.isep.hotel4you.domain.CardEntry;
import pt.isep.hotel4you.service.AccessDeniedException;
import pt.isep.hotel4you.service.CardNotFoundException;
import pt.isep.hotel4you.service.HotelFacade;

/** Shows the places a card entered, with the time of each entry (RF-20, H4Y-155). */
public class CardPlacesController {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final HotelFacade facade;
    @FXML private Label cardNumberLabel;
    @FXML private TextField cardNumberField;
    @FXML private TableView<CardEntry> entryTable;
    @FXML private TableColumn<CardEntry, String> placeColumn;
    @FXML private TableColumn<CardEntry, String> timeColumn;
    @FXML private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that reads the entries of the card
     */
    public CardPlacesController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        cardNumberLabel.setLabelFor(cardNumberField);
        placeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().place()));
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(timeText(data.getValue().entryDateTime())));
    }

    @FXML
    private void onSearchClicked() {
        entryTable.getItems().clear();
        try {
            List<CardEntry> entries = facade.getPlacesByCard(cardNumberField.getText(), HotelApp.currentUser);
            entryTable.getItems().setAll(entries);
            showMessage(resultMessage(entries), !entries.isEmpty());
        } catch (AccessDeniedException | CardNotFoundException | IllegalStateException error) {
            showMessage(error.getMessage(), false);
        }
    }

    private void showMessage(String text, boolean success) {
        messageLabel.setText(text);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }

    /** Formats the time of an entry for the table. */
    static String timeText(LocalDateTime time) {
        return TIME_FORMAT.format(time);
    }

    /** Tells the result of a query: no entries, or how many. */
    static String resultMessage(List<CardEntry> entries) {
        if (entries.isEmpty()) {
            return "O cartão não tem passagens.";
        }
        return entries.size() == 1 ? "1 passagem." : entries.size() + " passagens.";
    }
}
