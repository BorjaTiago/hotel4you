package pt.isep.hotel4you.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pt.isep.hotel4you.domain.ActiveReservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.service.HotelFacade;

/**
 * Controller of the Reservas screen. It lists the active reservations, searches them by name or
 * document, filters them, and turns on only the check-in or check-out button that the chosen row
 * allows. It also opens the screen to create a reservation.
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
    private List<ActiveReservation> loaded = List.of();

    @FXML
    private TableView<ActiveReservation> reservationTable;

    @FXML
    private TableColumn<ActiveReservation, String> clientColumn;

    @FXML
    private TableColumn<ActiveReservation, String> documentColumn;

    @FXML
    private TableColumn<ActiveReservation, String> roomColumn;

    @FXML
    private TableColumn<ActiveReservation, String> startColumn;

    @FXML
    private TableColumn<ActiveReservation, String> endColumn;

    @FXML
    private TableColumn<ActiveReservation, String> statusColumn;

    @FXML
    private VBox screenRoot;

    @FXML
    private TextField searchField;

    @FXML
    private ToggleGroup filterGroup;

    @FXML
    private Toggle allActiveToggle;

    @FXML
    private Toggle arrivingTodayToggle;

    @FXML
    private Toggle checkedInToggle;

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
    public ReservationsController(HotelFacade facade) {
        this.facade = facade;
    }

    /**
     * Tells if the check-in button is on for the chosen row.
     *
     * @param row the chosen row, or null if no row is chosen
     * @return true only for a RESERVED row
     */
    static boolean canCheckIn(ActiveReservation row) {
        return row != null && row.status() == ReservationStatus.RESERVED;
    }

    /**
     * Tells if the check-out button is on for the chosen row.
     *
     * @param row the chosen row, or null if no row is chosen
     * @return true only for a CHECKED_IN row
     */
    static boolean canCheckOut(ActiveReservation row) {
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
    static List<ActiveReservation> filter(List<ActiveReservation> rows, QuickFilter filter,
            String text, LocalDate today) {
        String search = text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
        return rows.stream()
                .filter(row -> matchesFilter(row, filter, today))
                .filter(row -> row.clientName().toLowerCase(Locale.ROOT).contains(search)
                        || row.document().toLowerCase(Locale.ROOT).contains(search))
                .toList();
    }

    private static boolean matchesFilter(ActiveReservation row, QuickFilter filter, LocalDate today) {
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
                data -> new SimpleStringProperty(String.valueOf(data.getValue().roomNumber())));
        startColumn.setCellValueFactory(
                data -> new SimpleStringProperty(DATE_FORMAT.format(data.getValue().startDate())));
        endColumn.setCellValueFactory(
                data -> new SimpleStringProperty(DATE_FORMAT.format(data.getValue().endDate())));
        statusColumn.setCellValueFactory(
                data -> new SimpleStringProperty(statusText(data.getValue().status())));
        reservationTable.getSelectionModel().selectedItemProperty()
                .addListener((value, before, chosen) -> updateButtons(chosen));
        searchField.textProperty().addListener((value, before, text) -> applyFilter());
        filterGroup.selectedToggleProperty().addListener((value, before, chosen) -> keepOneToggle(before));
        loadReservations();
    }

    @FXML
    private void onNewReservationClicked() {
        Pane parent = (Pane) screenRoot.getParent();
        ScreenLoader.LoadedScreen screen = new ScreenLoader(facade).open("reservations.fxml");
        ReservationController controller = (ReservationController) screen.controller();
        controller.setOnReservationCreated(() -> parent.getChildren()
                .setAll(new ScreenLoader(facade).load("reservations-list.fxml")));
        parent.getChildren().setAll(screen.root());
    }

    @FXML
    private void onCheckInClicked() {
        ActiveReservation chosen = reservationTable.getSelectionModel().getSelectedItem();
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
        ActiveReservation chosen = reservationTable.getSelectionModel().getSelectedItem();
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

    /** Puts the old toggle back when the user clicks the chosen one, so one filter is always on. */
    private void keepOneToggle(Toggle before) {
        if (filterGroup.getSelectedToggle() == null) {
            filterGroup.selectToggle(before == null ? allActiveToggle : before);
            return;
        }
        applyFilter();
    }

    private void updateButtons(ActiveReservation chosen) {
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
