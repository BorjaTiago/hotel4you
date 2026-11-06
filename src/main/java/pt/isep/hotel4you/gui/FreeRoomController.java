package pt.isep.hotel4you.gui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.function.Consumer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomSearchResult;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.service.HotelFacade;

/** Controller for the room step of the reservation screen: search by type and period, and choose a room. */
public class FreeRoomController {

    private static final NumberFormat PRICE_FORMAT =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-PT"));

    /**
     * The room that the receptionist chose and the searched period.
     *
     * @param room the chosen room
     * @param start the first night
     * @param end the check-out date
     */
    public record Choice(Room room, LocalDate start, LocalDate end) {
    }

    private final HotelFacade facade;
    private Consumer<Choice> onRoomChosen;
    private RoomType searchedType;
    private LocalDate searchedStart;
    private LocalDate searchedEnd;

    @FXML
    private ComboBox<RoomType> typeBox;

    @FXML
    private DatePicker startPicker;

    @FXML
    private DatePicker endPicker;

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, Integer> numberColumn;

    @FXML
    private TableColumn<Room, Integer> floorColumn;

    @FXML
    private TableColumn<Room, String> roomTypeColumn;

    @FXML
    private TableColumn<Room, String> priceColumn;

    @FXML
    private TableColumn<Room, String> totalColumn;

    @FXML
    private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the entry point for room operations
     */
    public FreeRoomController(HotelFacade facade) {
        this.facade = facade;
    }

    /**
     * Sets the action that runs when the receptionist chooses a room.
     * If the callback throws, this screen shows the error message.
     *
     * @param onRoomChosen the action, or null for none
     */
    public void setOnRoomChosen(Consumer<Choice> onRoomChosen) {
        this.onRoomChosen = onRoomChosen;
    }

    @FXML
    private void initialize() {
        numberColumn.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().getNumber()));
        floorColumn.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().getFloorNumber()));
        roomTypeColumn.setCellValueFactory(
                data -> new SimpleStringProperty(typeText(data.getValue().getRoomTypeName())));
        priceColumn.setCellValueFactory(
                data -> new SimpleStringProperty(priceText(searchedType.getBasePrice())));
        totalColumn.setCellValueFactory(data -> new SimpleStringProperty(
                priceText(searchedType.totalFor(searchedStart, searchedEnd))));
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
        try {
            typeBox.getItems().setAll(facade.listSearchRoomTypes(HotelApp.currentUser));
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onSearchClicked() {
        messageLabel.setText("");
        try {
            RoomSearchResult result = facade.searchRoomsWithMessage(startPicker.getValue(),
                    endPicker.getValue(), typeBox.getValue(), HotelApp.currentUser);
            searchedType = typeBox.getValue();
            searchedStart = startPicker.getValue();
            searchedEnd = endPicker.getValue();
            roomTable.getItems().setAll(result.rooms());
            showMessage(result.message(), false);
        } catch (RuntimeException e) {
            roomTable.getItems().clear();
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onChooseRoomClicked() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Escolha um quarto na tabela.", false);
            return;
        }
        if (onRoomChosen == null) {
            showMessage("Quarto " + selected.getNumber() + " escolhido para a reserva.", true);
            return;
        }
        try {
            onRoomChosen.accept(new Choice(selected, searchedStart, searchedEnd));
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
            return;
        }
        showMessage("Quarto " + selected.getNumber() + " escolhido para a reserva.", true);
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
