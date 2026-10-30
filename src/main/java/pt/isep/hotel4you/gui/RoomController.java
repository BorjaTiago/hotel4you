package pt.isep.hotel4you.gui;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.service.HotelFacade;

/** Controller for room registration and the room list. */
public class RoomController {

    private final HotelFacade facade;
    private Map<Integer, Integer> floorLimits;

    @FXML
    private TextField numberField;

    @FXML
    private ComboBox<Integer> floorBox;

    @FXML
    private ComboBox<String> roomTypeBox;

    @FXML
    private Label messageLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, Integer> numberColumn;

    @FXML
    private TableColumn<Room, Integer> floorColumn;

    @FXML
    private TableColumn<Room, String> roomTypeColumn;

    /**
     * Creates the controller.
     *
     * @param facade the entry point for room operations
     */
    public RoomController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        floorLimits = facade.roomFloorLimits();
        floorBox.getItems().setAll(floorLimits.keySet().stream().sorted().toList());
        numberColumn.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().number()));
        floorColumn.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().floorNumber()));
        roomTypeColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().roomTypeName()));
        loadRoomTypes();
        loadRooms();
    }

    @FXML
    private void onSaveRoomClicked() {
        try {
            if (floorBox.getValue() == null || roomTypeBox.getValue() == null) {
                throw new IllegalArgumentException("Escolha o piso e o tipo de quarto.");
            }
            int number = readRoomNumber();
            facade.saveRoom(new Room(null, number, floorBox.getValue(), roomTypeBox.getValue()),
                    HotelApp.currentUser);
            showMessage("Quarto registado.", true);
            numberField.clear();
            loadRooms();
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    private int readRoomNumber() {
        String numberText = numberField.getText();
        if (numberText == null || numberText.isBlank()) {
            throw new IllegalArgumentException("Indique o número do quarto.");
        }
        try {
            return Integer.parseInt(numberText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Indique um número de quarto válido.");
        }
    }

    private void loadRoomTypes() {
        try {
            roomTypeBox.getItems().setAll(facade.listRoomTypes(HotelApp.currentUser));
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    private void loadRooms() {
        try {
            List<Room> rooms = facade.listRooms(HotelApp.currentUser);
            roomTable.getItems().setAll(rooms);
            updateSummary(rooms);
        } catch (RuntimeException e) {
            roomTable.getItems().clear();
            summaryLabel.setText("");
            showMessage(e.getMessage(), false);
        }
    }

    private void updateSummary(List<Room> rooms) {
        StringJoiner summary = new StringJoiner(" · ");
        for (int floor : floorBox.getItems()) {
            summary.add("Piso " + floor + ": " + countOnFloor(rooms, floor) + "/" + floorLimits.get(floor));
        }
        for (String type : roomTypeBox.getItems()) {
            summary.add(typeLabel(type) + ": " + countOfType(rooms, type));
        }
        summaryLabel.setText(summary.toString());
    }

    private static long countOnFloor(List<Room> rooms, int floorNumber) {
        return rooms.stream().filter(room -> room.floorNumber() == floorNumber).count();
    }

    private static long countOfType(List<Room> rooms, String roomTypeName) {
        return rooms.stream()
                .filter(room -> room.roomTypeName().equalsIgnoreCase(roomTypeName))
                .count();
    }

    private static String typeLabel(String roomTypeName) {
        return switch (roomTypeName.toLowerCase(Locale.ROOT)) {
            case "individual" -> "Individual";
            case "double" -> "Duplo";
            case "family" -> "Familiar";
            default -> roomTypeName;
        };
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message == null ? "O pedido não foi concluído." : message);
        messageLabel.setStyle(success ? "-fx-text-fill: #2e7d32;" : "-fx-text-fill: #b00020;");
    }
}
