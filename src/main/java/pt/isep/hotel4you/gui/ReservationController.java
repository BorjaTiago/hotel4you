package pt.isep.hotel4you.gui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.ReservationQuote;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.service.HotelFacade;

/**
 * Controller for the reservation screen. It has three tabs: choose the client, choose the room
 * and the dates, and confirm the reservation.
 */
public class ReservationController {

    static final NumberFormat PRICE_FORMAT =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-PT"));
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final HotelFacade facade;
    private Client chosenClient;
    private Room chosenRoom;
    private LocalDate chosenStart;
    private LocalDate chosenEnd;

    @FXML
    private ClientController clientStepController;

    @FXML
    private FreeRoomController roomStepController;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab roomTab;

    @FXML
    private Tab confirmTab;

    @FXML
    private Button confirmButton;

    @FXML
    private Label clientLabel;

    @FXML
    private Label roomLabel;

    @FXML
    private Label datesLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private TableView<ClientReservation> reservationTable;

    @FXML
    private TableColumn<ClientReservation, Integer> roomColumn;

    @FXML
    private TableColumn<ClientReservation, String> startColumn;

    @FXML
    private TableColumn<ClientReservation, String> endColumn;

    @FXML
    private TableColumn<ClientReservation, String> statusColumn;

    /**
     * Creates the controller.
     *
     * @param facade the entry point for reservation operations
     */
    public ReservationController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        roomColumn.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().roomNumber()));
        startColumn.setCellValueFactory(
                data -> new SimpleStringProperty(DATE_FORMAT.format(data.getValue().startDate())));
        endColumn.setCellValueFactory(
                data -> new SimpleStringProperty(DATE_FORMAT.format(data.getValue().endDate())));
        statusColumn.setCellValueFactory(
                data -> new SimpleStringProperty(statusText(data.getValue().status())));
        clientStepController.setOnClientChosen(this::onClientChosen);
        roomStepController.setOnRoomChosen(this::onRoomChosen);
    }

    private void onClientChosen(Client client) {
        chosenClient = client;
        clearRoomChoice();
        confirmTab.setDisable(true);
        messageLabel.setText("");
        clientLabel.setText("Cliente: " + client.getName() + " (" + client.getDocument() + ")");
        showReservations();
        roomTab.setDisable(false);
        tabPane.getSelectionModel().select(roomTab);
    }

    private void onRoomChosen(FreeRoomController.Choice choice) {
        messageLabel.setText("");
        ReservationQuote quote = facade.quoteReservation(choice.room(), choice.start(), choice.end(),
                HotelApp.currentUser);
        chosenRoom = choice.room();
        chosenStart = choice.start();
        chosenEnd = choice.end();
        roomLabel.setText("Quarto " + chosenRoom.getNumber() + " ("
                + typeText(chosenRoom.getRoomTypeName()) + ")");
        datesLabel.setText("Entrada: " + DATE_FORMAT.format(chosenStart) + ". Saída: "
                + DATE_FORMAT.format(chosenEnd) + ".");
        summaryLabel.setText(summaryText(chosenRoom.getNumber(), quote));
        totalLabel.setText("Total: " + priceText(totalOf(quote)));
        confirmTab.setDisable(false);
        confirmButton.setDisable(false);
        tabPane.getSelectionModel().select(confirmTab);
    }

    @FXML
    private void onCreateReservationClicked() {
        messageLabel.setText("");
        try {
            facade.createReservation(chosenClient, chosenRoom, chosenStart, chosenEnd,
                    HotelApp.currentUser);
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
            return;
        }
        int number = chosenRoom.getNumber();
        clearRoomChoice();
        showReservations();
        showMessage("Reserva criada para o quarto " + number + ".", true);
    }

    /** Forgets the room and the dates, and disables the confirm button. */
    private void clearRoomChoice() {
        chosenRoom = null;
        chosenStart = null;
        chosenEnd = null;
        roomLabel.setText("");
        datesLabel.setText("");
        summaryLabel.setText("");
        totalLabel.setText("");
        confirmButton.setDisable(true);
    }

    private void showReservations() {
        try {
            reservationTable.getItems().setAll(
                    facade.listClientReservations(chosenClient, HotelApp.currentUser));
        } catch (RuntimeException e) {
            reservationTable.getItems().clear();
            showMessage(e.getMessage(), false);
        }
    }

    private static BigDecimal totalOf(ReservationQuote quote) {
        if (quote.pricePerNight() == null) {
            return null;
        }
        return quote.pricePerNight().multiply(BigDecimal.valueOf(quote.nights()));
    }

    /**
     * Builds the price summary that the screen shows before the confirmation (AC-4).
     *
     * @param roomNumber the visible room number
     * @param quote the price per night and the number of nights
     * @return the summary text
     */
    static String summaryText(int roomNumber, ReservationQuote quote) {
        String nights = quote.nights() == 1 ? " noite" : " noites";
        return "Quarto " + roomNumber + ": " + quote.nights() + nights + ". Preço base por noite: "
                + priceText(quote.pricePerNight()) + ".";
    }

    private static String statusText(ReservationStatus status) {
        return switch (status) {
            case RESERVED -> "Reservada";
            case CANCELLED -> "Cancelada";
            case CHECKED_IN -> "Check-in feito";
            case CHECKED_OUT -> "Check-out feito";
        };
    }

    private static String typeText(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private static String priceText(BigDecimal price) {
        return price == null ? "—" : PRICE_FORMAT.format(price);
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message == null ? "O pedido não foi concluído." : message);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }
}
