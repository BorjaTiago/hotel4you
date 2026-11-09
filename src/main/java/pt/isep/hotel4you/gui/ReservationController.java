package pt.isep.hotel4you.gui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.Client;
import pt.isep.hotel4you.domain.ClientReservation;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationQuote;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomSearchResult;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.service.HotelFacade;

/** Controller for the reservation screen: find a client, see the reservations, create one. */
public class ReservationController {

    static final NumberFormat PRICE_FORMAT =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-PT"));
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final HotelFacade facade;
    private Runnable onReservationCreated;

    @FXML
    private TextField documentField;

    @FXML
    private ComboBox<Client> clientBox;

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

    @FXML
    private VBox newReservationBox;

    @FXML
    private ComboBox<RoomType> typeBox;

    @FXML
    private DatePicker startPicker;

    @FXML
    private DatePicker endPicker;

    @FXML
    private ComboBox<Room> roomBox;

    @FXML
    private Label summaryLabel;

    @FXML
    private Label messageLabel;

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
        clientBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Client client) {
                return client == null ? "" : client.getName() + " (" + client.getDocument() + ")";
            }

            @Override
            public Client fromString(String text) {
                return null;
            }
        });
        typeBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(RoomType type) {
                return type == null ? "" : typeText(type.getName());
            }

            @Override
            public RoomType fromString(String text) {
                return null;
            }
        });
        roomBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Room room) {
                return room == null ? "" : "Quarto " + room.getNumber()
                        + " (piso " + room.getFloorNumber() + ", " + typeText(room.getRoomTypeName()) + ")";
            }

            @Override
            public Room fromString(String text) {
                return null;
            }
        });
        clientBox.valueProperty().addListener((value, before, after) -> showReservations());
        roomBox.valueProperty().addListener((value, before, after) -> showSummary());
        startPicker.valueProperty().addListener((value, before, after) -> showSummary());
        endPicker.valueProperty().addListener((value, before, after) -> showSummary());
        newReservationBox.managedProperty().bind(newReservationBox.visibleProperty());
        newReservationBox.setVisible(false);
    }

    @FXML
    private void onFindClientClicked() {
        messageLabel.setText("");
        List<Client> clients;
        try {
            clients = facade.searchClients(documentField.getText(), HotelApp.currentUser);
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
            return;
        }
        clientBox.getItems().setAll(clients);
        if (clients.isEmpty()) {
            showMessage("Não há clientes com esse documento.", false);
            return;
        }
        if (clients.size() > 1) {
            showMessage("Escolha o cliente na lista.", false);
            return;
        }
        clientBox.setValue(clients.get(0));
    }

    @FXML
    private void onNewReservationClicked() {
        messageLabel.setText("");
        newReservationBox.setVisible(true);
        try {
            typeBox.getItems().setAll(facade.listSearchRoomTypes(HotelApp.currentUser));
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
        Room chosen = HotelApp.chosenRoom;
        if (chosen == null) {
            return;
        }
        for (RoomType type : typeBox.getItems()) {
            if (type.getName().equalsIgnoreCase(chosen.getRoomTypeName())) {
                typeBox.setValue(type);
            }
        }
        startPicker.setValue(HotelApp.chosenStart);
        endPicker.setValue(HotelApp.chosenEnd);
        roomBox.getItems().setAll(chosen);
        roomBox.setValue(chosen);
    }

    @FXML
    private void onFindRoomsClicked() {
        messageLabel.setText("");
        roomBox.getItems().clear();
        try {
            RoomSearchResult result = facade.searchRoomsWithMessage(startPicker.getValue(),
                    endPicker.getValue(), typeBox.getValue(), HotelApp.currentUser);
            roomBox.getItems().setAll(result.rooms());
            showMessage(result.message(), false);
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onCreateReservationClicked() {
        messageLabel.setText("");
        if (!saveReservation()) {
            return;
        }
        runOnReservationCreated();
    }

    /** Saves the reservation. On an error it shows the error and returns false. */
    private boolean saveReservation() {
        try {
            Room room = roomBox.getValue();
            Reservation saved = facade.createReservation(clientBox.getValue(), room,
                    startPicker.getValue(), endPicker.getValue(), HotelApp.currentUser);
            HotelApp.chosenRoom = null;
            HotelApp.chosenStart = null;
            HotelApp.chosenEnd = null;
            roomBox.getItems().clear();
            showReservations();
            showMessage("Reserva criada: quarto " + room.getNumber() + ", de "
                    + DATE_FORMAT.format(saved.getStartDate()) + " a "
                    + DATE_FORMAT.format(saved.getEndDate()) + ".", true);
            return true;
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
            return false;
        }
    }

    /**
     * Sets the action to run after a reservation is saved. A failed save does not run it.
     *
     * @param action the action, or null for none
     */
    public void setOnReservationCreated(Runnable action) {
        this.onReservationCreated = action;
    }

    private void runOnReservationCreated() {
        if (onReservationCreated != null) {
            onReservationCreated.run();
        }
    }

    private void showReservations() {
        Client client = clientBox.getValue();
        if (client == null) {
            reservationTable.getItems().clear();
            return;
        }
        try {
            reservationTable.getItems().setAll(
                    facade.listClientReservations(client, HotelApp.currentUser));
        } catch (RuntimeException e) {
            reservationTable.getItems().clear();
            showMessage(e.getMessage(), false);
        }
    }

    private void showSummary() {
        Room room = roomBox.getValue();
        LocalDate start = startPicker.getValue();
        LocalDate end = endPicker.getValue();
        if (room == null || start == null || end == null) {
            summaryLabel.setText("");
            return;
        }
        try {
            ReservationQuote quote = facade.quoteReservation(room, start, end, HotelApp.currentUser);
            summaryLabel.setText(summaryText(room.getNumber(), quote));
        } catch (RuntimeException e) {
            summaryLabel.setText(e.getMessage());
        }
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
