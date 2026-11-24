package pt.isep.hotel4you.gui;

import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.isep.hotel4you.domain.MagneticCard;
import pt.isep.hotel4you.service.HotelFacade;

/** Shows the master cards and lets the manager block a lost master card (H4Y-153). */
public class MasterCardController {

    private static final DateTimeFormatter BLOCKED_AT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm");

    private final HotelFacade facade;
    @FXML private Label cardsLabel;
    @FXML private TableView<MagneticCard> cardTable;
    @FXML private TableColumn<MagneticCard, String> numberColumn;
    @FXML private TableColumn<MagneticCard, String> stateColumn;
    @FXML private TableColumn<MagneticCard, String> blockedAtColumn;
    @FXML private Button blockButton;
    @FXML private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that reads and blocks the master cards
     */
    public MasterCardController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        cardsLabel.setLabelFor(cardTable);
        numberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumber()));
        stateColumn.setCellValueFactory(data -> new SimpleStringProperty(stateText(data.getValue())));
        blockedAtColumn.setCellValueFactory(data -> new SimpleStringProperty(blockedAtText(data.getValue())));
        blockButton.disableProperty().bind(cardTable.getSelectionModel().selectedItemProperty().isNull());
        loadCards();
    }

    private boolean loadCards() {
        try {
            cardTable.getItems().setAll(facade.listMasterCards(HotelApp.currentUser));
            return true;
        } catch (RuntimeException error) {
            showMessage(error.getMessage(), false);
            return false;
        }
    }

    @FXML
    private void onBlockClicked() {
        showMessage("", false);
        MagneticCard chosen = cardTable.getSelectionModel().getSelectedItem();
        if (chosen == null) {
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Bloquear o cartão mestre " + chosen.getNumber() + "? O cartão deixa de abrir as portas.",
                ButtonType.YES, ButtonType.NO);
        ((Button) confirmation.getDialogPane().lookupButton(ButtonType.YES)).setDefaultButton(false);
        ((Button) confirmation.getDialogPane().lookupButton(ButtonType.NO)).setDefaultButton(true);
        if (confirmation.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            MagneticCard blocked = facade.blockMasterCard(chosen.getNumber(), HotelApp.currentUser);
            loadCards();
            showMessage(blockedMessage(blocked), true);
        } catch (RuntimeException error) {
            loadCards();
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

    /** Gives the state of a card for the table. */
    static String stateText(MagneticCard card) {
        if (card.isBlocked()) {
            return "Bloqueado";
        }
        return card.isActive() ? "Ativo" : "Inativo";
    }

    /** Gives the block time of a card for the table, or an empty text. */
    static String blockedAtText(MagneticCard card) {
        return card.isBlocked() ? card.getBlockedAt().format(BLOCKED_AT_FORMAT) : "";
    }

    /** Gives the message after the block of a card. */
    static String blockedMessage(MagneticCard card) {
        return "Cartão mestre " + card.getNumber() + " bloqueado.";
    }
}
