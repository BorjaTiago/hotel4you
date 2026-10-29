package pt.isep.hotel4you.gui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.RoomSummary;
import pt.isep.hotel4you.service.HotelFacade;

/** Controller for the read-only room list with the room type filter. */
public class RoomListController {

    private static final String ALL_TYPES = "";
    private static final NumberFormat PRICE_FORMAT =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-PT"));

    private final HotelFacade facade;

    @FXML
    private ComboBox<String> roomTypeBox;

    @FXML
    private Label messageLabel;

    @FXML
    private TableView<RoomSummary> roomTable;

    @FXML
    private TableColumn<RoomSummary, Integer> numberColumn;

    @FXML
    private TableColumn<RoomSummary, Integer> floorColumn;

    @FXML
    private TableColumn<RoomSummary, String> roomTypeColumn;

    @FXML
    private TableColumn<RoomSummary, String> basePriceColumn;

    /**
     * Creates the controller.
     *
     * @param facade the entry point for room operations
     */
    public RoomListController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        numberColumn.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().number()));
        floorColumn.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().floorNumber()));
        roomTypeColumn.setCellValueFactory(
                data -> new SimpleStringProperty(typeLabel(data.getValue().roomTypeName())));
        basePriceColumn.setCellValueFactory(
                data -> new SimpleStringProperty(priceLabel(data.getValue().basePrice())));
        roomTypeBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String roomTypeName) {
                return roomTypeName == null || roomTypeName.isEmpty() ? "Todos" : typeLabel(roomTypeName);
            }

            @Override
            public String fromString(String text) {
                return text;
            }
        });
        loadRoomTypes();
        loadRooms();
        roomTypeBox.setOnAction(event -> loadRooms());
    }

    private void loadRoomTypes() {
        List<String> choices = new ArrayList<>();
        choices.add(ALL_TYPES);
        try {
            choices.addAll(facade.listRoomTypes());
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
        roomTypeBox.getItems().setAll(choices);
        roomTypeBox.getSelectionModel().selectFirst();
    }

    private void loadRooms() {
        try {
            roomTable.getItems().setAll(facade.listRoomSummaries(roomTypeBox.getValue(), HotelApp.currentUser));
            messageLabel.setText("");
        } catch (RuntimeException e) {
            roomTable.getItems().clear();
            showError(e.getMessage());
        }
    }

    private static String typeLabel(String roomTypeName) {
        return switch (roomTypeName.toLowerCase(Locale.ROOT)) {
            case "single", "individual" -> "Individual";
            case "double", "duplo" -> "Duplo";
            case "family", "familiar" -> "Familiar";
            default -> roomTypeName;
        };
    }

    private static String priceLabel(BigDecimal basePrice) {
        return basePrice == null ? "—" : PRICE_FORMAT.format(basePrice);
    }

    private void showError(String message) {
        messageLabel.setText(message == null ? "O pedido não foi concluído." : message);
        messageLabel.setStyle("-fx-text-fill: #b00020;");
    }
}
