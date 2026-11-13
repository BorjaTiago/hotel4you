package pt.isep.hotel4you.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.ReservationSummary;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.service.HotelFacade;

/**
 * Controller of the Reservas screen. It lists the active reservations, searches them by name or
 * document, filters them, and turns on only the check-in, check-out or delete button that the
 * chosen row allows. Only the manager sees the delete button. It also opens the screen to create a
 * reservation.
 */
public class ReservationsController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** The quick filters of the list. */
    enum QuickFilter {
        /** Every active reservation. */
        ALL_ACTIVE,
        /** The reservations that start today and are not checked in. */
        ARRIVING_TODAY,
        /** The guests that are in the hotel. */
        CHECKED_IN
    }

    private final HotelFacade facade;
    private List<ReservationSummary> loaded = List.of();

    @FXML
    private TableView<ReservationSummary> reservationTable;

    @FXML
    private TableColumn<ReservationSummary, String> clientColumn;

    @FXML
    private TableColumn<ReservationSummary, String> documentColumn;

    @FXML
    private TableColumn<ReservationSummary, Integer> roomColumn;

    @FXML
    private TableColumn<ReservationSummary, LocalDate> startColumn;

    @FXML
    private TableColumn<ReservationSummary, LocalDate> endColumn;

    @FXML
    private TableColumn<ReservationSummary, String> statusColumn;

    @FXML
    private VBox screenRoot;

    @FXML
    private TextField searchField;

    @FXML
    private ToggleGroup filterGroup;

    @FXML
    private RadioButton arrivingTodayToggle;

    @FXML
    private RadioButton checkedInToggle;

    @FXML
    private Button checkInButton;

    @FXML
    private Button checkOutButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that does the check-in and the check-out
     */
    public ReservationsController(HotelFacade facade) {
        this.facade = facade;
    }

    /**
     * Tells if the check-in button is on for the chosen row.
     *
     * @param row the chosen row, or null if no row is chosen
     * @param today the current date
     * @return true only for a RESERVED row whose start date is today or before and whose end date is after today
     */
    static boolean canCheckIn(ReservationSummary row, LocalDate today) {
        if (row == null || row.status() != ReservationStatus.RESERVED) {
            return false;
        }
        return !today.isBefore(row.startDate()) && today.isBefore(row.endDate());
    }

    /**
     * Tells if the check-out button is on for the chosen row.
     *
     * @param row the chosen row, or null if no row is chosen
     * @return true only for a CHECKED_IN row
     */
    static boolean canCheckOut(ReservationSummary row) {
        return row != null && row.status() == ReservationStatus.CHECKED_IN;
    }

    /**
     * Tells if the cancel button is on for the chosen row.
     *
     * @param row the chosen row, or null if no row is chosen
     * @return true only for a RESERVED row
     */
    static boolean canCancel(ReservationSummary row) {
        return row != null && row.status() == ReservationStatus.RESERVED;
    }

    /**
     * Tells if the delete button is on for the chosen row.
     *
     * @param row the chosen row, or null if no row is chosen
     * @return true only for a RESERVED row
     */
    static boolean canDelete(ReservationSummary row) {
        return row != null && row.status() == ReservationStatus.RESERVED;
    }

    /**
     * Gives the question that the screen asks before it cancels a reservation.
     *
     * @param row the chosen row
     * @return the question with the room number and the dates
     */
    static String cancelQuestion(ReservationSummary row) {
        return "Cancelar a reserva do quarto " + row.roomNumber() + ", de "
                + DATE_FORMAT.format(row.startDate()) + " a " + DATE_FORMAT.format(row.endDate()) + "?";
    }

    /**
     * Gives the question that the screen asks before it deletes a reservation.
     *
     * @param row the chosen row
     * @return the question with the room number
     */
    static String deleteQuestion(ReservationSummary row) {
        return "Apagar de vez a reserva do quarto " + row.roomNumber()
                + "? Esta ação não pode ser desfeita.";
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

    /**
     * Searches and filters the reservations. The text matches the client name or the document,
     * without case. A blank text matches every row.
     *
     * @param rows the active reservations
     * @param filter the quick filter
     * @param text the search text, or null
     * @param today the date of today
     * @return the rows that match, in the same order
     */
    static List<ReservationSummary> filter(List<ReservationSummary> rows, QuickFilter filter,
            String text, LocalDate today) {
        String search = text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
        return rows.stream()
                .filter(row -> matchesFilter(row, filter, today))
                .filter(row -> row.clientName().toLowerCase(Locale.ROOT).contains(search)
                        || row.document().toLowerCase(Locale.ROOT).contains(search))
                .toList();
    }

    private static boolean matchesFilter(ReservationSummary row, QuickFilter filter, LocalDate today) {
        return switch (filter) {
            case ALL_ACTIVE -> true;
            case ARRIVING_TODAY -> row.status() == ReservationStatus.RESERVED
                    && row.startDate().equals(today);
            case CHECKED_IN -> row.status() == ReservationStatus.CHECKED_IN;
        };
    }

    @FXML
    private void initialize() {
        clientColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().clientName()));
        documentColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().document()));
        roomColumn.setCellValueFactory(
                data -> new SimpleObjectProperty<>(data.getValue().roomNumber()));
        startColumn.setCellValueFactory(
                data -> new SimpleObjectProperty<>(data.getValue().startDate()));
        endColumn.setCellValueFactory(
                data -> new SimpleObjectProperty<>(data.getValue().endDate()));
        startColumn.setCellFactory(column -> new DateCell());
        endColumn.setCellFactory(column -> new DateCell());
        statusColumn.setCellValueFactory(
                data -> new SimpleStringProperty(statusText(data.getValue().status())));
        reservationTable.getSelectionModel().selectedItemProperty()
                .addListener((value, before, chosen) -> updateButtons(chosen));
        searchField.textProperty().addListener((value, before, text) -> applyFilter());
        filterGroup.selectedToggleProperty().addListener((value, before, chosen) -> applyFilter());
        deleteButton.managedProperty().bind(deleteButton.visibleProperty());
        deleteButton.setVisible(facade.canUse(HotelApp.currentUser, Permission.DELETE_RESERVATIONS));
        loadReservations();
    }

    @FXML
    private void onNewReservationClicked() {
        if (!(screenRoot.getParent() instanceof Pane parent)) {
            return;
        }
        parent.getChildren().setAll(new ScreenLoader(facade).load("reservations.fxml"));
    }

    @FXML
    private void onCheckInClicked() {
        ReservationSummary chosen = reservationTable.getSelectionModel().getSelectedItem();
        if (!canCheckIn(chosen, LocalDate.now())) {
            return;
        }
        try {
            facade.checkIn(chosen.id(), HotelApp.currentUser);
            if (loadReservations()) {
                showMessage("Check-in feito: quarto " + chosen.roomNumber() + ".", true);
                reservationTable.requestFocus();
            }
        } catch (RuntimeException e) {
            loadReservations();
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onCheckOutClicked() {
        ReservationSummary chosen = reservationTable.getSelectionModel().getSelectedItem();
        if (!canCheckOut(chosen)) {
            return;
        }
        try {
            facade.checkOut(chosen.id(), HotelApp.currentUser);
            if (loadReservations()) {
                showMessage("Check-out feito: quarto " + chosen.roomNumber() + ".", true);
                reservationTable.requestFocus();
            }
        } catch (RuntimeException e) {
            loadReservations();
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onCancelReservationClicked() {
        ReservationSummary chosen = reservationTable.getSelectionModel().getSelectedItem();
        if (!canCancel(chosen)) {
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, cancelQuestion(chosen),
                ButtonType.YES, ButtonType.NO);
        if (confirmation.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            facade.cancelReservation(chosen.id(), HotelApp.currentUser);
            if (loadReservations()) {
                showMessage("Reserva cancelada: quarto " + chosen.roomNumber() + ".", true);
                reservationTable.requestFocus();
            }
        } catch (RuntimeException e) {
            loadReservations();
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onDeleteReservationClicked() {
        ReservationSummary chosen = reservationTable.getSelectionModel().getSelectedItem();
        if (!canDelete(chosen)) {
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, deleteQuestion(chosen),
                ButtonType.YES, ButtonType.NO);
        if (confirmation.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            facade.deleteReservation(chosen.id(), HotelApp.currentUser);
            if (loadReservations()) {
                showMessage("Reserva apagada: quarto " + chosen.roomNumber() + ".", true);
                reservationTable.requestFocus();
            }
        } catch (RuntimeException e) {
            loadReservations();
            showMessage(e.getMessage(), false);
        }
    }

    /** Loads the list again. On an error it shows the error and returns false. */
    private boolean loadReservations() {
        boolean loadedOk;
        try {
            this.loaded = facade.listActiveReservations(HotelApp.currentUser);
            applyFilter();
            showMessage("", false);
            loadedOk = true;
        } catch (RuntimeException e) {
            this.loaded = List.of();
            applyFilter();
            showMessage(e.getMessage(), false);
            loadedOk = false;
        }
        updateButtons(reservationTable.getSelectionModel().getSelectedItem());
        return loadedOk;
    }

    private void applyFilter() {
        reservationTable.setItems(FXCollections.observableArrayList(
                filter(loaded, currentFilter(), searchField.getText(), LocalDate.now())));
    }

    private QuickFilter currentFilter() {
        Toggle chosen = filterGroup.getSelectedToggle();
        if (chosen == arrivingTodayToggle) {
            return QuickFilter.ARRIVING_TODAY;
        }
        if (chosen == checkedInToggle) {
            return QuickFilter.CHECKED_IN;
        }
        return QuickFilter.ALL_ACTIVE;
    }

    private void updateButtons(ReservationSummary chosen) {
        checkInButton.setDisable(!canCheckIn(chosen, LocalDate.now()));
        checkOutButton.setDisable(!canCheckOut(chosen));
        cancelButton.setDisable(!canCancel(chosen));
        deleteButton.setDisable(!canDelete(chosen));
    }

    /** A table cell that shows a date as dd/MM/yyyy. */
    private static class DateCell extends TableCell<ReservationSummary, LocalDate> {
        @Override
        protected void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            setText(empty || date == null ? null : DATE_FORMAT.format(date));
        }
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message == null ? "O pedido não foi concluído." : message);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }
}
