package pt.isep.hotel4you.gui;

import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.service.HotelFacade;

/**
 * Controller of the check-in and check-out screen. It lists the active reservations and turns on
 * only the button that the chosen row allows.
 */
public class CheckInOutController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final HotelFacade facade;

    @FXML
    private TableView<ClientReservation> reservationTable;

    @FXML
    private TableColumn<ClientReservation, String> roomColumn;

    @FXML
    private TableColumn<ClientReservation, String> startColumn;

    @FXML
    private TableColumn<ClientReservation, String> endColumn;

    @FXML
    private TableColumn<ClientReservation, String> statusColumn;

    @FXML
    private Button checkInButton;

    @FXML
    private Button checkOutButton;

    @FXML
    private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that does the check-in and the check-out
     */
    public CheckInOutController(HotelFacade facade) {
        this.facade = facade;
    }

    /**
     * Tells if the check-in button is on for the chosen row.
     *
     * @param row the chosen row, or null if no row is chosen
     * @return true only for a RESERVED row
     */
    static boolean canCheckIn(ClientReservation row) {
        return row != null && row.status() == ReservationStatus.RESERVED;
    }

    /**
     * Tells if the check-out button is on for the chosen row.
     *
     * @param row the chosen row, or null if no row is chosen
     * @return true only for a CHECKED_IN row
     */
    static boolean canCheckOut(ClientReservation row) {
        return row != null && row.status() == ReservationStatus.CHECKED_IN;
    }

    /**
     * Gives the Portuguese label of a reservation state.
     *
     * @param status the reservation state
     * @return the label that the table shows
     */
    static String statusText(ReservationStatus status) {
        return switch (status) {
            case RESERVED -> "Reservada";
            case CANCELLED -> "Cancelada";
            case CHECKED_IN -> "Check-in feito";
            case CHECKED_OUT -> "Check-out feito";
        };
    }

    @FXML
    private void initialize() {
        roomColumn.setCellValueFactory(
                data -> new SimpleStringProperty(String.valueOf(data.getValue().roomNumber())));
        startColumn.setCellValueFactory(
                data -> new SimpleStringProperty(DATE_FORMAT.format(data.getValue().startDate())));
        endColumn.setCellValueFactory(
                data -> new SimpleStringProperty(DATE_FORMAT.format(data.getValue().endDate())));
        statusColumn.setCellValueFactory(
                data -> new SimpleStringProperty(statusText(data.getValue().status())));
        reservationTable.getSelectionModel().selectedItemProperty()
                .addListener((value, before, chosen) -> updateButtons(chosen));
        loadReservations();
    }

    @FXML
    private void onCheckInClicked() {
        ClientReservation chosen = reservationTable.getSelectionModel().getSelectedItem();
        if (!canCheckIn(chosen)) {
            return;
        }
        try {
            facade.checkIn(chosen.id(), HotelApp.currentUser);
            if (loadReservations()) {
                showMessage("Check-in feito: quarto " + chosen.roomNumber() + ".", true);
            }
        } catch (RuntimeException e) {
            loadReservations();
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onCheckOutClicked() {
        ClientReservation chosen = reservationTable.getSelectionModel().getSelectedItem();
        if (!canCheckOut(chosen)) {
            return;
        }
        try {
            facade.checkOut(chosen.id(), HotelApp.currentUser);
            if (loadReservations()) {
                showMessage("Check-out feito: quarto " + chosen.roomNumber() + ".", true);
            }
        } catch (RuntimeException e) {
            loadReservations();
            showMessage(e.getMessage(), false);
        }
    }

    /** Loads the list again. On an error it shows the error and returns false. */
    private boolean loadReservations() {
        boolean loaded;
        try {
            reservationTable.getItems().setAll(facade.listActiveReservations(HotelApp.currentUser));
            showMessage("", false);
            loaded = true;
        } catch (RuntimeException e) {
            reservationTable.getItems().clear();
            showMessage(e.getMessage(), false);
            loaded = false;
        }
        updateButtons(reservationTable.getSelectionModel().getSelectedItem());
        return loaded;
    }

    private void updateButtons(ClientReservation chosen) {
        checkInButton.setDisable(!canCheckIn(chosen));
        checkOutButton.setDisable(!canCheckOut(chosen));
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message == null ? "O pedido não foi concluído." : message);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }
}
