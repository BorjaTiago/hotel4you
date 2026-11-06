package pt.isep.hotel4you.gui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
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

    private static final NumberFormat PRICE_FORMAT =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-PT"));
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final HotelFacade facade;

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
        clientBox.setConverter(new LabelConverter<>(
                client -> client.getName() + " (" + client.getDocument() + ")"));
        typeBox.setConverter(new LabelConverter<>(type -> typeText(type.getName())));
        roomBox.setConverter(new LabelConverter<>(room -> "Quarto " + room.getNumber()
                + " (piso " + room.getFloorNumber() + ", " + typeText(room.getRoomTypeName()) + ")"));
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
        try {
            List<Client> clients = facade.searchClients(documentField.getText(), HotelApp.currentUser);
            clientBox.getItems().setAll(clients);
            if (clients.size() == 1) {
                clientBox.setValue(clients.get(0));
            } else if (clients.isEmpty()) {
                showMessage("Não há clientes com esse documento.", false);
            } else {
                showMessage("Escolha o cliente na lista.", false);
            }
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
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
        if (chosen != null) {
            typeBox.getItems().stream()
                    .filter(type -> type.getName().equalsIgnoreCase(chosen.getRoomTypeName()))
                    .findFirst()
                    .ifPresent(typeBox::setValue);
            startPicker.setValue(HotelApp.chosenStart);
            endPicker.setValue(HotelApp.chosenEnd);
            roomBox.getItems().setAll(chosen);
            roomBox.setValue(chosen);
        }
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
                    + DATE_FORMAT.format(saved.getEndDate()) + "." + priceSentence(room, saved), true);
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    /** The reservation is already saved here, so a failed quote only hides the price. */
    private String priceSentence(Room room, Reservation saved) {
        try {
            ReservationQuote quote = facade.quoteReservation(room, saved.getStartDate(),
                    saved.getEndDate(), HotelApp.currentUser);
            return " Preço base por noite: " + priceText(quote.pricePerNight()) + ".";
        } catch (RuntimeException e) {
            return "";
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
            summaryLabel.setText("Quarto " + room.getNumber() + ": " + quote.nights()
                    + (quote.nights() == 1 ? " noite" : " noites") + ". Preço base por noite: "
                    + priceText(quote.pricePerNight()) + ".");
        } catch (RuntimeException e) {
            summaryLabel.setText(e.getMessage());
        }
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

    /** Shows a value in a combo box with a fixed text. The user cannot type a value. */
    private static final class LabelConverter<T> extends StringConverter<T> {
        private final Function<T, String> text;

        private LabelConverter(Function<T, String> text) {
            this.text = text;
        }

        @Override
        public String toString(T value) {
            return value == null ? "" : text.apply(value);
        }

        @Override
        public T fromString(String value) {
            return null;
        }
    }
}
