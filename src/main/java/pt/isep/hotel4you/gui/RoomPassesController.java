package pt.isep.hotel4you.gui;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.isep.hotel4you.domain.CardEntry;
import pt.isep.hotel4you.domain.CardType;
import pt.isep.hotel4you.service.AccessDeniedException;
import pt.isep.hotel4you.service.CardNotFoundException;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.RoomNotFoundException;

/** Shows the cards that entered a room, with the time of each entry (RF-21, H4Y-156). */
public class RoomPassesController {

    private final HotelFacade facade;
    @FXML private Label roomNumberLabel;
    @FXML private ComboBox<String> roomNumberBox;
    @FXML private TableView<CardEntry> entryTable;
    @FXML private TableColumn<CardEntry, String> cardColumn;
    @FXML private TableColumn<CardEntry, String> typeColumn;
    @FXML private TableColumn<CardEntry, String> timeColumn;
    @FXML private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that reads the rooms and their entries
     */
    public RoomPassesController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        roomNumberLabel.setLabelFor(roomNumberBox);
        cardColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().cardNumber()));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(cardTypeText(data.getValue().cardType())));
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(
                CardPlacesController.timeText(data.getValue().entryDateTime())));
        try {
            roomNumberBox.getItems().setAll(facade.listRooms(HotelApp.currentUser).stream()
                    .map(room -> String.valueOf(room.getNumber()))
                    .toList());
        } catch (AccessDeniedException | IllegalStateException error) {
            showMessage(error.getMessage(), false);
        }
    }

    @FXML
    private void onSearchClicked() {
        entryTable.getItems().clear();
        try {
            List<CardEntry> entries = facade.getPassesByRoom(roomNumberBox.getEditor().getText(),
                    HotelApp.currentUser);
            entryTable.getItems().setAll(entries);
            showMessage(resultMessage(entries), !entries.isEmpty());
        } catch (AccessDeniedException | RoomNotFoundException | CardNotFoundException
                | IllegalStateException error) {
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

    /** Gives the name of a card type for the table. */
    static String cardTypeText(CardType type) {
        return type == CardType.MASTER ? "Mestre" : "Cliente";
    }

    /** Tells the result of a query: no entries, or how many. */
    static String resultMessage(List<CardEntry> entries) {
        if (entries.isEmpty()) {
            return "O quarto não tem passagens.";
        }
        return entries.size() == 1 ? "1 passagem." : entries.size() + " passagens.";
    }
}
